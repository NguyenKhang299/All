package com.photo.imagecompressor.tools.di

import javax.inject.Qualifier



@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityContext

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class FileDirectory