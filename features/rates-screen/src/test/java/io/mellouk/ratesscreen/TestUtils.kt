package io.mellouk.ratesscreen

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class RxImmediateSchedulerRule(private val scheduler: Scheduler = TestScheduler()) :
    TestRule {

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
                RxJavaPlugins.setIoSchedulerHandler { scheduler }
                RxJavaPlugins.setInitComputationSchedulerHandler { scheduler }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler }
                RxJavaPlugins.setInitSingleSchedulerHandler { scheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }

                try {
                    base?.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}