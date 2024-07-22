package com.photo.imagecompressor.tools.di

import android.content.Context
import android.os.Environment
import android.util.Log
import com.photo.imagecompressor.tools.usecase.images.GetAllImagesUseCase
import com.photo.imagecompressor.tools.usecase.images.GetFolderImagesUseCase
import com.photo.imagecompressor.tools.usecase.images.GetImageByFolderUseCase
import com.photo.imagecompressor.tools.usecase.images.GetImageSave
import com.photo.imagecompressor.tools.usecase.images.ImageUseCase
import com.photo.imagecompressor.tools.usecase.images.SaveImagesUseCase
import com.photo.imagecompressor.tools.data.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    @FileDirectory
    fun provideRootFilePath(): File {
        val fileRoot =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/ReduceImage/")
        if (!fileRoot.exists()) {
            fileRoot.mkdirs()
        }
        return fileRoot
    }

    @Provides
    @Singleton
    fun provideImageRepo(
        @ApplicationContext context: Context,
        @FileDirectory directory: File
    ): ImageRepositoryImpl =
        ImageRepositoryImpl(context, directory)

    @Provides
    @Singleton
    fun provideImageUseCase(imageRepoImpl: ImageRepositoryImpl): ImageUseCase =
        ImageUseCase(
            getAllImagesUseCase = GetAllImagesUseCase(imageRepoImpl),
            saveImagesUseCase = SaveImagesUseCase(imageRepoImpl),
            getFolderImagesUseCase = GetFolderImagesUseCase(imageRepoImpl),
            getImageByFolderUseCase = GetImageByFolderUseCase(imageRepoImpl),
            getImageSave = GetImageSave(imageRepoImpl)
        )
}
