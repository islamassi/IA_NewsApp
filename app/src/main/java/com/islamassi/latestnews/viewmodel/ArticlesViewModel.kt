package com.islamassi.latestnews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.repo.ArticlesRepo
import com.islamassi.latestnews.usecase.ArticlesInteractor
import com.islamassi.latestnews.usecase.ArticlesUseCase
import javax.inject.Inject

/**
 * ViewModel for preparing and providing data for the ArticlesFragment
 *
 * @param articlesUseCase UseCase for preparing articles in the needed format and structure
 */
class ArticlesViewModel @Inject constructor(
    private val articlesUseCase: ArticlesUseCase
) : ViewModel() {

    var articlesLiveData: LiveData<Resource<List<Article>>> = MutableLiveData()

    /**
     * load new news articles
     */
    fun loadNewsArticles():LiveData<Resource<List<Article>>>{
        return loadNewsArticles(null)
    }

    /**
     * search for news article
     *
     * @param keyWord query for search
     */
    fun loadNewsArticles(keyWord: String?):LiveData<Resource<List<Article>>>{
        articlesLiveData = articlesUseCase.getArticles(keyWord)
        return articlesLiveData
    }

    fun search(keyWord : String?): LiveData<Resource<List<Article>>>?{
        return keyWord?.let {
            return loadNewsArticles(it)
        }
    }
}
