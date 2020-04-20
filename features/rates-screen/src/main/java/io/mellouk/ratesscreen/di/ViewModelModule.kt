package io.mellouk.ratesscreen.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.mellouk.common.di.ViewModelKey
import io.mellouk.ratesscreen.RateListViewModel


@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RateListViewModel::class)
    @RateListScope
    fun bindMainViewModel(rateListViewModel: RateListViewModel): ViewModel
}