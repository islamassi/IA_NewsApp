package com.islamassi.latestnews.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.islamassi.latestnews.ArticleListener
import com.islamassi.latestnews.R
import com.islamassi.latestnews.adapter.ArticlesAdapter
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.dagger.component.DaggerAppComponent
import com.islamassi.latestnews.databinding.ArticlesFragmentBinding
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.viewmodel.ArticlesViewModel
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import javax.inject.Inject


/**
 *  fragment for showing latest articles and searching for articles
 *
 */
class ArticlesFragment : Fragment(), ArticleListener {

    // used for creating ViewModel object
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    //  binding object
    private lateinit var binding:ArticlesFragmentBinding
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var articlesObserver : Observer<Resource<List<Article>>>
    private lateinit var searchView: SearchView
    private lateinit var viewModel: ArticlesViewModel

    companion object {
        fun newInstance() = ArticlesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerAppComponent.create().inject(this) // inject
        binding = DataBindingUtil.inflate(inflater, R.layout.articles_fragment, container, false)
        binding.lifecycleOwner = this
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        // get ViewModel object
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(ArticlesViewModel::class.java)
        articlesAdapter = ArticlesAdapter(mutableListOf(), this)
        binding.articlesRecyclerView.adapter = articlesAdapter

        binding.articlesRecyclerView.setOnScrollChangeListener(object : View.OnScrollChangeListener{
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                articlesAdapter.shouldAnimate = true
            }
        })
        articlesObserver = Observer {
            if (it is Resource.Success ){
                // articles resource successfully loaded from (database or network)
                it.data?.let { list -> handleNewArticleList(list) }
                binding.swipeToRefresh.isRefreshing = false
            }else if(it is Resource.Loading){
                // articles resource is being loaded there is data from database to show for now until the request finishes
                it.data?.let { list -> handleNewArticleList(list) }
            }else if (it is Resource.Error){
                // articles resource failed to load
                Snackbar.make(binding.root, getString(R.string.request_failed), Snackbar.LENGTH_LONG).show()
                binding.swipeToRefresh.isRefreshing = false
            }
        }

        val articlesExists:Boolean? = viewModel.articlesLiveData.value?.data?.isNullOrEmpty()?.not()
        if (articlesExists == true){
            // show articles if they exists
            viewModel.articlesLiveData.observe(this, articlesObserver)
        }else{
            // ask view model for articles if articles are null or empty
            refresh()
        }
        binding.swipeToRefresh.setOnRefreshListener {
            refresh()
            resetSearchView()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        searchView = ((menu.findItem(R.id.action_search)?.actionView)) as SearchView
        // initialize search view
        initSearchView()
        menu.findItem(R.id.action_refresh)?.setOnMenuItemClickListener {
            refresh()
            resetSearchView()
            return@setOnMenuItemClickListener true
        }
    }

    /**
     * initialize search view
     */
    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    /**
     * ask view model to search for an article
     */
    private fun search(query:String?){
        query?.trim().let {
            if (it.isNullOrEmpty())
                return
            binding.swipeToRefresh.isRefreshing = true
            removeObserver()
            viewModel.search(it)?.observe(this@ArticlesFragment, articlesObserver)
        }
    }

    /**
     * reload articles
     */
    private fun refresh(){
        binding.swipeToRefresh.isRefreshing = true
        removeObserver()
        viewModel.loadNewsArticles().observe(this, articlesObserver)
    }

    /**
     * reset search view to default state
     */
    private fun resetSearchView(){
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.isIconified = true
    }

    private fun removeObserver(){
        viewModel.articlesLiveData.removeObserver(articlesObserver)
    }

    private fun handleNewArticleList(list: List<Article>){
        if(list.isNullOrEmpty()){
            binding.emptyState.visibility = View.VISIBLE
            articlesAdapter.notifyChange(emptyList())
        }else{
            binding.emptyState.visibility = View.GONE
            articlesAdapter.notifyChange(list)
        }
    }

    override fun articleClicked(article: Article, image: ImageView, title:View, descrip: View, dateView:View) {
        viewModel.selectedArticle.value = article
        val detailsFragment
                = ArticleDetailsFragment.newInstance()

        fragmentManager
            ?.beginTransaction()
            ?.addSharedElement(image, image.transitionName)
            ?.addSharedElement(title, title.transitionName)
            ?.addSharedElement(descrip, descrip.transitionName)
            ?.addSharedElement(dateView, dateView.transitionName)
            ?.addToBackStack(null)
            ?.replace(R.id.container, detailsFragment, ArticleDetailsFragment::javaClass.name)
            ?.commit()

    }
}
