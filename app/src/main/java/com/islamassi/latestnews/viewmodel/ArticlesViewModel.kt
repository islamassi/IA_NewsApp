package com.islamassi.latestnews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.repo.ArticlesRepo
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(
    private val articlesRepo: ArticlesRepo
) : ViewModel() {

    fun loadNewsArticles():LiveData<Resource<List<Article>>>{
        return articlesRepo.getNewsArticles()
    }
}
