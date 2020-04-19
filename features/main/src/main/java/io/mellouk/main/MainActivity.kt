package io.mellouk.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.mellouk.common.base.BaseActivity
import io.mellouk.main.ViewState.RateList
import io.mellouk.main.di.MainComponentProvider

class MainActivity : BaseActivity<MainComponentProvider, ViewState, MainViewModel>(
    R.layout.activity_main
) {
    private val navController: NavController by lazy {
        Navigation.findNavController(
            this, R.id.navMain
        )
    }

    override fun getViewModelClass() = MainViewModel::class.java

    override fun inject() {
        componentProvider.getMainComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        navController.setGraph(R.navigation.main_nav, savedInstanceState)
    }

    override fun renderViewState(state: ViewState) {
        when (state) {
            is RateList -> navigateTo(state.destination)
        }
    }

    private fun navigateTo(@IdRes destination: Int, bundle: Bundle? = null) {
        navController.navigate(destination, bundle)
    }
}
