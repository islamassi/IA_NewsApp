package com.islamassi.latestnews.repo

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.Constants
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.api.Webservice
import com.islamassi.latestnews.db.ArticleDao
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.model.NewsArticlesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepo @Inject constructor(
    private val webservice: Webservice,
    private val articleDao: ArticleDao
) {

    fun getNewsArticles(keyWord: String?): LiveData<Resource<List<Article>>> {
        return object : NetworkBoundResource<List<Article>, NewsArticlesResponse>() {
            override fun saveCallResult(item: NewsArticlesResponse) {
                item.articles?.let {
                    articleDao.replaceAll(it)
                }
            }
            override fun shouldFetch(data: List<Article>?): Boolean = true

            override fun loadFromDb(): LiveData<List<Article>> = articleDao.getArticles()

            override fun createCall(): LiveData<ApiResponse<NewsArticlesResponse>> =
                webservice.getArticles(Constants.API_KEY, keyWord)

        }.asLiveData()
    }
}