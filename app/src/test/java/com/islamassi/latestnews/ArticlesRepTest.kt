package com.islamassi.latestnews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.api.Webservice
import com.islamassi.latestnews.db.ArticleDao
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.model.NewsArticlesResponse
import com.islamassi.latestnews.repo.ArticlesRepo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response

@RunWith(JUnit4::class)
class ArticlesRepTest {
    private val articleDao = mock(ArticleDao::class.java)
    private val webservice = mock(Webservice::class.java)
    private val repo = ArticlesRepo(webservice, articleDao)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadFromDb() {
        repo.getNewsArticles("")
        verify(articleDao).getArticles()
    }

    @Test
    fun serviceCallResponse() {
        val responseLiveData = MutableLiveData<ApiResponse<NewsArticlesResponse>>()
        repo.getNewsArticles("")
        val apiResponse : ApiResponse<NewsArticlesResponse> = ApiResponse(
            Response.success(NewsArticlesResponse("200",2,
            emptyList()))
        )
        responseLiveData.value = apiResponse
        val observer = mock<Observer<ApiResponse<NewsArticlesResponse>>>()
        responseLiveData.observeForever(observer)
        `when`(webservice!!.getArticles(anyString(), anyString())).thenReturn(responseLiveData)
        verify(observer).onChanged(apiResponse)
    }

    @Test
    fun repoResponse() {
        val responseLiveData = MutableLiveData<Resource<List<Article>>>()
        repo.getNewsArticles("")
        val repoResponse : Resource<List<Article>> = Resource.Success(emptyList())
        responseLiveData.value = repoResponse
        val observer = mock<Observer<Resource<List<Article>>>>()
        responseLiveData.observeForever(observer)
        `when`(repo!!.getNewsArticles("")).thenReturn(responseLiveData)
        verify(observer).onChanged(repoResponse)
    }

    inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)

}