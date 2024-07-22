package com.photo.image.picture.tools.compressor.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface BaseSchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun main(): Scheduler
}

@Singleton
class AppSchedulerProviderImpl @Inject constructor() : BaseSchedulerProvider {
    override fun computation(): Scheduler = Schedulers.computation()
    override fun io(): Scheduler = Schedulers.io()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}