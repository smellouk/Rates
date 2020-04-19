package io.mellouk.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel<State : BaseViewState> : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val liveData = MutableLiveData<State>()

    init {
        liveData.value = getInitialState()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun <T : Any> addObservable(
        source: Observable<T>,
        onNext: Next<T>,
        onError: Error
    ) {
        compositeDisposable.add(
            source
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    onNext.invoke(it)
                }, {
                    onError.invoke(it)
                })
        )
    }

    abstract fun getInitialState(): State
}

typealias Next<T> = (T) -> Unit
typealias Error = (Throwable?) -> Unit