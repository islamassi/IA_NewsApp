package com.islamassi.latestnews.ui

import android.animation.ObjectAnimator
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.islamassi.latestnews.*
import com.islamassi.latestnews.Constants.DESCRIPTION_PREFIX_TRANS
import com.islamassi.latestnews.Constants.TITLE_PREFIX_TRANS
import com.islamassi.latestnews.dagger.component.DaggerAppComponent
import com.islamassi.latestnews.databinding.FragmentArticleDetailsBinding
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.ui.custom.LiveNewsPopup
import com.islamassi.latestnews.ui.custom.ReadOptionsPopup
import com.islamassi.latestnews.viewmodel.ArticlesViewModel
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.layout_details.view.*
import kotlinx.android.synthetic.main.layout_live_news.*
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ArticleDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleDetailsFragment : Fragment() {
    private lateinit var binding:FragmentArticleDetailsBinding
    private var appBarExpanded:Boolean = true
    private lateinit var collapsedMenu: Menu
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticlesViewModel
    private lateinit var readOptionsPopup: ReadOptionsPopup
    private lateinit var livePopup: LiveNewsPopup

    companion object {
        @JvmStatic
        fun newInstance() =
            ArticleDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DaggerAppComponent.create().inject(this) // inject
        // Inflate the layout for this fragment
        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(ArticlesViewModel::class.java)
        binding.lifecycleOwner = this
        bindData()
        bounceFabButton()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.article_details_menu, menu)
        collapsedMenu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!appBarExpanded) {
            //collapsed
            collapsedMenu.add("browser")
                .setIcon(R.drawable.ic_baseline_open_in_browser_24).setOnMenuItemClickListener {
                    viewModel.selectedArticle.value?.url?.let {
                        launchWithCustomColors(it)
                    }
                    return@setOnMenuItemClickListener true
                }.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onPrepareOptionsMenu(collapsedMenu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.read_options -> {
                readOptionsPopup.show(binding.root)
                return true
            }
            R.id.live -> {
                showLivePopup()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindData() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        viewModel.selectedArticle.value?.apply {
            binding.article = this
            binding.detailsContainer.title.setTextGoneOnEmpty(this.title)
            binding.detailsContainer.description.setTextGoneOnEmpty(this.description)

            initTransition(this)
            animateDescription()
            initDate(publishedAt)
            binding.articleImage.load(this.urlToImage, R.drawable.placeholder)
            url?.apply {
                binding.fab.setOnClickListener { launchWithCustomColors(this) }
            }
            binding.toolbarLayout.title = this.author
        }
        initAppBar()
        readOptionsPopup = ReadOptionsPopup.newInstance(layoutInflater)
        livePopup = LiveNewsPopup.newInstance(layoutInflater)
    }

    private fun initTransition(article:Article) {
        binding.articleImage.transitionName = article.getImageTransition()
        binding.detailsContainer.title.transitionName = article.getTitleTransition()
        binding.detailsContainer.description.transitionName = article.getDescTransition()
        binding.detailsContainer.publish_date.transitionName = article.getDateTransition()
    }

    private fun animateDescription() {
        ObjectAnimator.ofFloat(binding.detailsContainer.more_descrip,
            "alpha",
            0f, 1f).apply {
            startDelay = 500
            duration = 300
            start()
        }
    }

    private fun initAppBar() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false
                    requireActivity().invalidateOptionsMenu()
                } else {
                    appBarExpanded = true
                    requireActivity().invalidateOptionsMenu()
                }
            }
        })
    }

    private fun initDate(publishedAt:String?) {
        val date = publishedAt?.toDate("yyyy-MM-dd'T'HH:mm:ssX")
        binding.detailsContainer.publish_date.text =
            DateUtils.getRelativeTimeSpanString(
                date!!.time,
                Date().time,
                DateUtils.MINUTE_IN_MILLIS
            )
    }

    override fun onPause() {
        super.onPause()
        livePopup.dismiss()
    }

    private fun showLivePopup(){
        livePopup.show(viewModel.selectedArticle.value?.title,0,resources.getDimension(R.dimen.live_offset).toInt(), binding.root)
    }

    private fun bounceFabButton() {
        val ty = resources.getDimension(R.dimen.fab_ty)
        binding.fab.translationY = -ty
        binding.fab.animate().setInterpolator(BounceInterpolator()).translationYBy(ty).setDuration(1400)
    }

    /**
     * Launches a Trusted Web Activity where navigations to non-validate domains will open
     * in a Custom Tab where the toolbar color has been customized.
     *
     * @param view the source of the event invoking this method.
     */
    private fun launchWithCustomColors(url:String) {
        if (url.isEmpty())
            return
        val builder = TrustedWebActivityIntentBuilder(Uri.parse(url))
            .setToolbarColor(resources.getColor(R.color.colorPrimary))
        TwaLauncher(requireContext()).launch(builder, null, null)
    }
}