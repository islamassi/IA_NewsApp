package com.islamassi.latestnews.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import javax.inject.Inject

class ArticlesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding:ArticlesFragmentBinding
    private lateinit var articlesAdapter: ArticlesAdapter
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticlesViewModel::class.java)
        articlesAdapter = ArticlesAdapter(mutableListOf())
        binding.articlesRecyclerView.adapter = articlesAdapter
        viewModel.loadNewsArticles().observe(this, Observer {
            if (it is Resource.Success || it is Resource.Loading){
                it.data?.let { list -> articlesAdapter.notifyChange(list) }
            }else if (it is Resource.Error){
                Snackbar.make(binding.root, "Request failed", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
