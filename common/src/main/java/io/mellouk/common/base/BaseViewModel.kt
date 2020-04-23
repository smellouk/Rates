package io.mellouk.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
    ): Disposable = source
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe({
            onNext.invoke(it)
        }, {
            onError.invoke(it)
        }).apply {
            compositeDisposable.add(this)
            this
        }

    fun addCompletable(
        source: Completable,
        onError: Error? = null,
        onComplete: Complete? = null
    ) {
        source
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    onComplete?.invoke()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    onError?.invoke(e)
                }
            })
    }

    abstract fun getInitialState(): State
}

typealias Next<T> = (T) -> Unit
typealias Error = (Throwable?) -> Unit
typealias Complete = () -> Unit