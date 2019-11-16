package com.islamassi.latestnews.dagger.module

import android.app.Application
import com.google.gson.GsonBuilder
import com.islamassi.latestnews.Constants.BASE_URL
import com.islamassi.latestnews.MyApp
import com.islamassi.latestnews.api.LiveDataCallAdapter
import com.islamassi.latestnews.api.LiveDataCallAdapterFactory
import com.islamassi.latestnews.api.Webservice
import com.islamassi.latestnews.usecase.ArticlesInteractor
import com.islamassi.latestnews.usecase.ArticlesUseCase

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

import javax.inject.Singleton
import java.util.concurrent.TimeUnit


/**
 *  Dagger Module for providing net related objects
 */

@Module
class NetModule {

    /**
     * provides a webservice object for calling API requests
     *
     * @param retrofitBuilder RetrofitBuilder for building the requests interface
     */
    @Provides
    @Singleton
    internal fun provideWebService(retrofitBuilder: Retrofit.Builder): Webservice {

        return retrofitBuilder
            .build()
            .create(Webservice::class.java)

    }

    /**
     * Provides RetrofitBuilder for building requests interfaces
     */
    @Provides
    @Singleton
    internal fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {

        val gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        val defaultRetrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())

        return defaultRetrofitBuilder
            .client(okHttpClient)
    }

    /**
     * provide OkHttpObject
     */
    @Provides
    @Singleton
    internal fun provideOkHttp(): OkHttpClient {

        val defaultHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        defaultHttpClient.addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )

        return defaultHttpClient.build()
    }

    /**
     * Provides ArticlesUseCase interface as ArticlesInteractor
     */
    @Provides
    @Singleton
    internal fun provideArticleUseCase(interactor: ArticlesInteractor):ArticlesUseCase{
        return interactor
    }
}
