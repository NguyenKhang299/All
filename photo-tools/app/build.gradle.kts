import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}
val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
android {
    namespace = "com.photo.image.picture.tools.compressor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.photo.image.picture.tools.compressor"
        minSdk = 24
        targetSdk = 34
        versionCode = 103
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GG_APP_OPEN", apikeyProperties["GG_APP_OPEN"] as String)
        buildConfigField("String", "GG_BANNER", apikeyProperties["GG_BANNER"] as String)
        buildConfigField("String", "GG_NATIVE", apikeyProperties["GG_NATIVE"] as String)
        buildConfigField("String", "GG_FULL", apikeyProperties["GG_FULL"] as String)
        buildConfigField("String", "GG_REWARDED", apikeyProperties["GG_REWARDED"] as String)

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    // Ads Helper
    implementation(files("libs/adshelper.aar"))
    // Google Play Services dependencies
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    // Multidex support
    implementation("androidx.multidex:multidex:2.0.1")

// Billing client
    implementation("com.android.billingclient:billing-ktx:6.2.0")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config-ktx:21.6.3")

    // RangeSeekBar
    implementation("com.github.Jay-Goo:RangeSeekBar:v3.0.0")
    //Edit Image
    implementation(libs.ucrop)
    //Animation
    implementation("com.airbnb.android:lottie:6.4.0")
    //Picaso
    implementation("com.squareup.picasso:picasso:2.71828")
    //Border Image
    implementation("com.makeramen:roundedimageview:2.3.0")

    // Reactive
    implementation("io.reactivex.rxjava3:rxjava:3.0.4")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    // Dagger
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Glide
    implementation(libs.glide)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.compiler)

    // NavHost
    val nav_version = "2.7.7"

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // ViewModel, LiveData'
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(libs.rxpermissions)
    implementation(libs.android.image.cropper)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}