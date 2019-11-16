package com.islamassi.latestnews.ui

import android.app.SearchManager
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.islamassi.latestnews.viewmodel.ArticlesViewModel
import com.islamassi.latestnews.R
import com.islamassi.latestnews.adapter.ArticlesAdapter
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.dagger.component.DaggerAppComponent
import com.islamassi.latestnews.databinding.ArticlesFragmentBinding
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import javax.inject.Inject

class ArticlesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding:ArticlesFragmentBinding
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var articlesObserver : Observer<Resource<List<Article>>>
    private lateinit var searchView: SearchView
    companion object {
        fun newInstance() = ArticlesFragment()
    }

    private lateinit var viewModel: ArticlesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerAppComponent.create().inject(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.articles_fragment, container, false)
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticlesViewModel::class.java)
        articlesAdapter = ArticlesAdapter(mutableListOf())
        binding.articlesRecyclerView.adapter = articlesAdapter
        articlesObserver = Observer {
            if (it is Resource.Success ){
                it.data?.let { list -> articlesAdapter.notifyChange(list) }
                binding.swipeToRefresh.isRefreshing = false
            }else if(it is Resource.Loading){
                it.data?.let { list -> articlesAdapter.notifyChange(list) }
            }else if (it is Resource.Error){
                Snackbar.make(binding.root, getString(R.string.request_failed), Snackbar.LENGTH_LONG).show()
                binding.swipeToRefresh.isRefreshing = false
            }
        }

        val articlesExists:Boolean? = viewModel.articlesLiveData.value?.data?.isNullOrEmpty()?.not()
        if (articlesExists == true){
            viewModel.articlesLiveData.observe(this, articlesObserver)
        }else{
            refresh()
        }
        binding.swipeToRefresh.setOnRefreshListener {
            refresh()
            resetSearchView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        searchView = ((menu.findItem(R.id.action_search)?.actionView)) as SearchView
        initSearchView()
        menu.findItem(R.id.action_refresh)?.setOnMenuItemClickListener {
            refresh()
            resetSearchView()
            return@setOnMenuItemClickListener true
        }
    }

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

    private fun search(query:String?){
        query?.trim().let {
            binding.swipeToRefresh.isRefreshing = true
            viewModel.search(it)?.observe(this@ArticlesFragment, articlesObserver)
        }
    }

    private fun refresh(){
        binding.swipeToRefresh.isRefreshing = true
        viewModel.loadNewsArticles().observe(this, articlesObserver)
    }

    private fun resetSearchView(){
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.isIconified = true
    }

    fun onBackPressed():Boolean{
        if (!searchView.query.isNullOrEmpty()) {
            resetSearchView()
            return true;
        }
        return false
    }
}
