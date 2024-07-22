package com.photo.imagecompressor.tools

import android.app.Application
import android.content.res.Configuration
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.photo.imagecompressor.tools.data.RemoteHelper
import com.photo.imagecompressor.tools.utils.LanguageConfig
import com.photo.imagecompressor.tools.utils.LanguageEnum
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var languageConfig: LanguageConfig
    override fun onCreate() {
        super.onCreate()
        RemoteHelper.fetchAndActivate(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val local = languageConfig.getLocal()
         if (local == null) {
            languageConfig.setLocal(LanguageEnum.VIETNAM)
        } else {
            languageConfig.setLocal(local)
        }
    }
}