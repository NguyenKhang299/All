package com.photo.image.picture.tools.compressor.di

import android.content.Context
import android.os.Environment
import com.photo.image.picture.tools.compressor.data.repository.ImageRepositoryImpl
import com.photo.image.picture.tools.compressor.usecase.images.GetAllImagesUseCase
import com.photo.image.picture.tools.compressor.usecase.images.GetFolderImagesUseCase
import com.photo.image.picture.tools.compressor.usecase.images.GetImageByFolderUseCase
import com.photo.image.picture.tools.compressor.usecase.images.GetImageSave
import com.photo.image.picture.tools.compressor.usecase.images.ImageUseCase
import com.photo.image.picture.tools.compressor.usecase.images.SaveImagesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    @FileDirectory
    fun provideRootFilePath(): File {
        val fileRoot =
            File(Environment.getExternalStorageDirectory().path + "/Pictures/ImageTeras/")
        if (!fileRoot.exists()) {
            fileRoot.mkdirs()
        }
        return fileRoot
    }

    @Provides
    @Singleton
    fun providerImageRepo(@ApplicationContext context: Context, @FileDirectory directory: File) =
        ImageRepositoryImpl(context, directory)

    @Provides
    @Singleton
    fun provideImageUseCase(imageRepoImpl: ImageRepositoryImpl) = ImageUseCase(
        getAllImagesUseCase = GetAllImagesUseCase(imageRepoImpl),
        saveImagesUseCase = SaveImagesUseCase(imageRepoImpl),
        getFolderImagesUseCase = GetFolderImagesUseCase(imageRepoImpl),
        getImageByFolderUseCase = GetImageByFolderUseCase(imageRepoImpl),
        getImageSave = GetImageSave(imageRepoImpl)
    )
}