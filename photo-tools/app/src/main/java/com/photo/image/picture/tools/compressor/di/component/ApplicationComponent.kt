package com.photo.image.picture.tools.compressor.di.component

import com.photo.image.picture.tools.compressor.di.ApplicationModule
import com.photo.image.picture.tools.compressor.di.FileDirectory
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