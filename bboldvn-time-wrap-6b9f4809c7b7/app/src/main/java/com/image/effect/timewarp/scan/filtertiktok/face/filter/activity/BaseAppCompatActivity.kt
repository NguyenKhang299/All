package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Pref
import com.orhanobut.hawk.Hawk
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*


open class BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // update locale
        if (Hawk.contains(Pref.SelectedLanguage)) {
            changeLocale(Hawk.get(Pref.SelectedLanguage, "en"))
        }
    }

    private fun changeLocale(locale: String) {
        val conf = resources.configuration
        val myLocale = Locale(locale)
        conf.setLocale(myLocale)
        conf.setLayoutDirection(myLocale)
        resources.updateConfiguration(conf, resources.displayMetrics)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}