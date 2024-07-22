package com.image.effect.timewarp.scan.filtertiktok.face.filter

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class Const {
    companion object {
        const val DevId = "2Hero+Mobi"
        const val PolicyLink = "https://sites.google.com/view/timewarp-policy"

//        const val AdmobOpenAdId = "ca-app-pub-3940256099942544/3419835294"
        const val AdmobOpenAdId = "ca-app-pub-9674055550946724/7002884558"
        const val MaxBannerId = "478eff333356dafb"
        const val MaxNativeId = "db24cd404d4ee057"
        const val MaxFullId = "e308f7a4bf48c3fc"
        const val MaxRewardedId = "ba69b3d93857d82c"
        const val MaxOpenAdId = "f655423f4d644f1a"

        const val IABBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkUGB0rv264ldQNN+pRY/dgzxUjRlebVjXsm4ezVpi4YBoVwjiDouVrhj7ms+bBZyLHxq11uJknJLufqx4mdusaDVhuk/iMsea2CfeDKuTQnQC9o8Q8+l98+mSAMWQBWWRwHYS5QwSdC0Q8tv9zSlC//enR75InybTdDuR8oet9vDrhr7JFTYZYD3eARMjJpgy6IOyb0qg9sGzQ32UEBfuub/Ku0tKHTM6/Wie+q+WEfkJwvbquPEImGLk1cG844ngPo6jK8dfDcGqwZ1HK7Yq6DbVOMq0488jlIzeyYRWmkUt0HDF3omuO45kjBQp/vrQ0hAy9+NsPKKmJTkv9O6xQIDAQAB"

        const val PackageTiktok = "com.ss.android.ugc.trill"
        const val PackageFacebook = "com.facebook.katana"
        const val PackageInstagram = "com.instagram.android"

        fun getSupportLocales(): ArrayList<Locale> {
            return ArrayList<Locale>().apply {
                add(Locale("en"))
                add(Locale("hi"))
                add(Locale("in"))
                add(Locale("pt"))
                add(Locale("th"))
                add(Locale("vi"))
            }
        }
    }
}

class Pref {
    companion object {
        const val InstallFd = "pref_install_fd"
        const val LastActiveFd = "pref_last_active_fd"
        const val ContinuousActiveDay = "pref_continuous_active_day"
        const val FilterUseCount = "pref_filter_use_count"
        const val SelectedLanguage = "pref_selected_language"
        const val AppRated = "pref_app_rated"
    }
}

class Events {
    companion object {
        const val TAG = "[Events]"
        fun log(event: String, bundle: Bundle? = null) {
            Firebase.analytics.logEvent(event, bundle)
            Log.e(TAG, "event: $event")
        }

        const val Active3dayContinuous = "use_app_3d_cont"
        const val UseFilter10times = "use_filter_10times"
        const val ShareApp = "share_app"
        const val Feedback = "feedback"
    }
}
