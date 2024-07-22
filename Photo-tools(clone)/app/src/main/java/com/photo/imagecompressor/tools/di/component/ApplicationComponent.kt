package com.photo.imagecompressor.tools.di.component

import com.photo.imagecompressor.tools.di.ApplicationModule
import com.photo.imagecompressor.tools.di.FileDirectory
 import dagger.Component
 import java.io.File
import javax.inject.Singleton
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    //
//    fun inject(app: MyApplication)
//
//    fun getApplication(): Application



    @FileDirectory
    fun getFileRootPath(): File
}