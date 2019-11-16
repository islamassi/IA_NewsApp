package com.islamassi.latestnews.dagger.component

import com.islamassi.latestnews.dagger.module.NetModule
import com.islamassi.latestnews.dagger.module.RoomModule
import com.islamassi.latestnews.dagger.module.ViewModelModule
import com.islamassi.latestnews.ui.ArticlesFragment
import com.islamassi.latestnews.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger App component
 */
@Singleton
@Component(modules = [ViewModelModule::class, NetModule::class, RoomModule::class])
interface AppComponent {
    /**
     * injects properties for MainActivity
     */
    fun inject(activity: MainActivity)

    /**
     * injects properties for ArticlesFragment
     */
    fun inject(fragment: ArticlesFragment)
}