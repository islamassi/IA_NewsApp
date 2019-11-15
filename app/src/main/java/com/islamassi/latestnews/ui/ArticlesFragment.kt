package com.islamassi.latestnews.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.islamassi.latestnews.viewmodel.ArticlesViewModel
import com.islamassi.latestnews.R
import com.islamassi.latestnews.dagger.component.DaggerAppComponent
import com.islamassi.latestnews.databinding.ArticlesFragmentBinding
import com.islamassi.latestnews.viewmodel.ViewModelFactory
import javax.inject.Inject

class ArticlesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding:ArticlesFragmentBinding

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticlesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
