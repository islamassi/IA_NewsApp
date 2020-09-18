package com.islamassi.latestnews.ui

import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.transition.TransitionInflater
import android.view.*
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.islamassi.latestnews.R
import com.islamassi.latestnews.dagger.component.DaggerAppComponent
import com.islamassi.latestnews.databinding.FragmentArticleDetailsBinding
import com.islamassi.latestnews.load
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.setTextGoneOnEmpty
import com.islamassi.latestnews.toDate
import com.islamassi.latestnews.viewmodel.ArticlesViewModel
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_article_details.*
import kotlinx.android.synthetic.main.layout_details.view.*
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
        animateText()
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
        return super.onOptionsItemSelected(item)
    }

    private fun bindData() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        viewModel.selectedArticle.value?.apply {
            binding.article = this
            binding.detailsContainer.title.setTextGoneOnEmpty(this.title)
            binding.detailsContainer.description.setTextGoneOnEmpty(this.description)
            val date = this.publishedAt?.toDate("yyyy-MM-dd'T'HH:mm:ssX")
            binding.detailsContainer.publish_date.text =
                DateUtils.getRelativeTimeSpanString(
                    date!!.time,
                    Date().time,
                    DateUtils.MINUTE_IN_MILLIS
                )
        }

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
        viewModel.selectedArticle.value?.apply {
            binding.articleImage.transitionName = this.title
            binding.articleImage.load(this.urlToImage, R.drawable.placeholder)
        }
    }

    private fun animateText() {
        var titleAnim = binding.detailsContainer.title
    }

    private fun bounceFabButton() {
        binding.fab.translationY = -400f
        binding.fab.animate().setInterpolator(BounceInterpolator()).translationYBy(400f).setDuration(1400)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ArticleDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ArticleDetailsFragment()
    }

    /**
     * Launches a Trusted Web Activity where navigations to non-validate domains will open
     * in a Custom Tab where the toolbar color has been customized.
     *
     * @param view the source of the event invoking this method.
     */
    fun launchWithCustomColors(url:String) {
        if (url.isEmpty())
            return
        val builder = TrustedWebActivityIntentBuilder(Uri.parse(url))
            .setToolbarColor(resources.getColor(R.color.colorPrimary))
        TwaLauncher(requireContext()).launch(builder, null, null)
    }
}