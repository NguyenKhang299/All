package com.photo.imagecompressor.tools.utils

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.Serializable
import java.util.Locale
import javax.inject.Inject


enum class LanguageEnum(val lang: String) : Serializable {
    ENGLISH("en"), VIETNAM("vi")
}

class LanguageConfig @Inject constructor(@ApplicationContext private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("language", AppCompatActivity.MODE_PRIVATE)


    fun setLocal(languageEnum: LanguageEnum) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.create(Locale.forLanguageTag(languageEnum.lang))
        )

        sharedPreferences.edit().apply {
            putString("lang", languageEnum.lang)
        }.apply()
    }


    fun getLocal(): LanguageEnum? {
        return when (sharedPreferences.getString("lang", null)) {
            LanguageEnum.VIETNAM.lang -> LanguageEnum.VIETNAM
            LanguageEnum.ENGLISH.lang -> LanguageEnum.ENGLISH
            else -> null
        }
    }
}