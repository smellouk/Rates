package io.mellouk.ratesscreen.di

import dagger.Subcomponent
import io.mellouk.common.base.BaseComponentProvider
import io.mellouk.ratesscreen.RateListFragment

@RateListScope
@Subcomponent(modules = [ViewModelModule::class, PresentationModule::class])
interface RateListComponent {
    fun inject(fragment: RateListFragment)
}

interface RateListComponentProvider : BaseComponentProvider {
    fun getRateListComponent(): RateListComponent
}