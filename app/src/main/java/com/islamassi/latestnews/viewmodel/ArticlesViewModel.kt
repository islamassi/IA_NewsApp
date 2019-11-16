package com.islamassi.latestnews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.repo.ArticlesRepo
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(
    private val articlesRepo: ArticlesRepo
) : ViewModel() {

    var articlesLiveData: LiveData<Resource<List<Article>>> = MutableLiveData()

    fun loadNewsArticles():LiveData<Resource<List<Article>>>{
        return loadNewsArticles(null)
    }

    fun loadNewsArticles(keyWord: String?):LiveData<Resource<List<Article>>>{
        articlesLiveData = articlesRepo.getNewsArticles(keyWord)
        return articlesLiveData
    }

    fun search(keyWord : String?): LiveData<Resource<List<Article>>>?{
        return keyWord?.let {
            return loadNewsArticles(it)
        }
    }
}
