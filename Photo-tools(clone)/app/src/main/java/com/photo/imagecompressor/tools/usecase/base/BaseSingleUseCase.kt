package com.photo.imagecompressor.tools.usecase.base
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

abstract class BaseSingleUseCase<UseCaseInput, UseCaseOutput : Any>(
    private val executionThread: Scheduler,
    private val postExecutionThread: Scheduler
) : BaseUseCase() {

    protected abstract fun create(input: UseCaseInput): Single<UseCaseOutput>

    fun execute(input: UseCaseInput): Single<UseCaseOutput> {
        return create(input)
            .doOnError(this::doOnError)
            .subscribeOn(executionThread)
            .observeOn(postExecutionThread)
    }
}