package com.tearas.myapplication.di

import android.app.Application
import android.os.Environment
import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.usecase.media.DeleteMedia
import com.tearas.myapplication.usecase.media.GetFilesRecoverSaved
import com.tearas.myapplication.usecase.media.GetMediaFromFile
import com.tearas.myapplication.usecase.media.MediaUseCases
import com.tearas.myapplication.usecase.media.RecoveryMedia
import com.tearas.myapplication.usecase.media.ScanAllAudio
import com.tearas.myapplication.usecase.media.ScanAllDocument
import com.tearas.myapplication.usecase.media.ScanAllIVideo
import com.tearas.myapplication.usecase.media.ScanAllImage
import com.tearas.myapplication.usecase.media.ScanFilesDuplicated
import com.tearas.myapplication.usecase.media.SweepFilesJunk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DirectorySave

@InstallIn(SingletonComponent::class)
@Module
object ModuleApp {


    @Provides
    fun provideMediaUseCases(mediaRepositoryImpl: MediaRepositoryImpl) = MediaUseCases(
        scanAllImage = ScanAllImage(mediaRepositoryImpl),
        scanAllDocument = ScanAllDocument(mediaRepositoryImpl),
        scanAllIVideo = ScanAllIVideo(mediaRepositoryImpl),
        scanAllAudio = ScanAllAudio(mediaRepositoryImpl),
        getMediaFromFile = GetMediaFromFile(mediaRepositoryImpl),
        scanFilesDuplicated = ScanFilesDuplicated(mediaRepositoryImpl),
        deleteMedia = DeleteMedia(mediaRepositoryImpl),
        recoveryMedia = RecoveryMedia(mediaRepositoryImpl),
        getFilesRecoverSaved = GetFilesRecoverSaved(mediaRepositoryImpl),
        sweepFilesJunk = SweepFilesJunk(mediaRepositoryImpl)
    )

    @DirectorySave
    @Provides
    fun provideDirectorySave(): String {
        val file = File(Environment.getExternalStorageDirectory().path + "/RecoveryFiles")
        if (!file.exists()) file.mkdirs()
        return file.path
    }

    @Provides
    fun provideMediaRepository(application: Application, @DirectorySave path: String) =
        MediaRepositoryImpl(application, path)

}