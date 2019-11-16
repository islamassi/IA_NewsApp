package com.islamassi.latestnews.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.islamassi.latestnews.model.Article

/**
 * RoomDatabase
 */
@Database(entities = [Article::class], version = 1)
abstract class NewsDatabase: RoomDatabase(){
    abstract fun articleDao():ArticleDao
}