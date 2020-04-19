package io.mellouk.main.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.mellouk.common.di.ViewModelKey
import io.mellouk.main.di.MainScope
import io.mellouk.main.MainViewModel

@Module
interface MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(io.mellouk.main.MainViewModel::class)
    @MainScope
    fun bindMainViewModel(mainViewModel: io.mellouk.main.MainViewModel): ViewModel
}