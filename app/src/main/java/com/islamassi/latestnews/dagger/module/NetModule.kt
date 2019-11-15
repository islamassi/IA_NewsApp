package com.islamassi.latestnews.dagger.module

import com.google.gson.GsonBuilder
import com.islamassi.latestnews.Constants.BASE_URL
import com.islamassi.latestnews.api.Webservice

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

import javax.inject.Singleton
import java.util.concurrent.TimeUnit


/**
 * Created by islam on 25,May,2019
 *
 * provide net related objects
 */

@Module
class NetModule {

    @Provides
    @Singleton
    internal fun provideWebService(retrofitBuilder: Retrofit.Builder): Webservice {

        return retrofitBuilder
            .build()
            .create(Webservice::class.java)

    }


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

        return defaultRetrofitBuilder
            .client(okHttpClient)
    }


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


}
