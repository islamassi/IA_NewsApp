package com.islamassi.latestnews.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.islamassi.latestnews.model.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articleList: List<Article>)
}