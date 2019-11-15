package com.islamassi.latestnews.dagger.component

import com.islamassi.latestnews.dagger.module.NetModule
import com.islamassi.latestnews.dagger.module.ViewModelModule
import com.islamassi.latestnews.ui.ArticlesFragment
import com.islamassi.latestnews.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, NetModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ArticlesFragment)

}