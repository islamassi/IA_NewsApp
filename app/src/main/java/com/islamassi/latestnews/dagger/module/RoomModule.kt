package com.islamassi.latestnews.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room
import com.islamassi.latestnews.MyApp
import com.islamassi.latestnews.db.ArticleDao
import com.islamassi.latestnews.db.NewsDatabase


@Module
class RoomModule {

    @Provides
    @Singleton
    internal fun provideNewsDatabase(context: Context): NewsDatabase{
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java, "news-db"
        ).build()
    }

    @Provides
    @Singleton
    internal fun provideArticleDao(database: NewsDatabase): ArticleDao{
        return database.articleDao()
    }


    @Provides
    @Singleton
    internal fun providesContext(): Context {
        return MyApp.instance
    }
}