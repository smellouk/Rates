package io.mellouk.main.di

import dagger.Subcomponent
import io.mellouk.common.base.BaseComponentProvider
import io.mellouk.main.MainActivity

@MainScope
@Subcomponent(modules = [MainViewModelModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}

interface MainComponentProvider : BaseComponentProvider{
    fun getMainComponent(): MainComponent
}