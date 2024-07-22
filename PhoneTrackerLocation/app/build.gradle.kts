import com.google.devtools.ksp.gradle.model.Ksp
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("io.github.islamkhsh.xdimen")
    id("com.google.gms.google-services")
}
// Load API keys from properties file
val apikeyPropsFile = file("apikey.properties")
val apikeyProps = Properties()
apikeyProps.load(apikeyPropsFile.inputStream())

android {
    namespace = "com.knd.duantotnghiep.phonetrackerlocation"
    compileSdk = 34
    android.buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "com.knd.duantotnghiep.phonetrackerlocation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        buildConfigField(
//            "String",
//            "GOOGLE_MAPS_API_KEY",
//            apikeyProps["googleMapsApiKey"].toString()
//        )
        buildConfigField("String", "GG_APP_OPEN", apikeyProps["GG_APP_OPEN"].toString())
        buildConfigField("String", "GG_BANNER", apikeyProps["GG_BANNER"].toString())
        buildConfigField("String", "GG_NATIVE", apikeyProps["GG_NATIVE"].toString())
        buildConfigField("String", "GG_FULL", apikeyProps["GG_FULL"].toString())
        buildConfigField("String", "GG_REWARDED", apikeyProps["GG_REWARDED"].toString())
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
        viewBinding = true
        dataBinding = true
    }

}
xdimen {
    deleteOldXdimen.set(true)
    designWidth.set(600)
    designDpi.set(mdpi())
    targetDevicesWidth.set(devicesInPortrait)
    dimensRange {
        minDimen.set(-10)
        maxDimen.set(500)
        step.set(0.5)
    }
    fontsRange {
        minDimen.set(10)
        maxDimen.set(60)
        step.set(1.0)
    }
}

dependencies {
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

//API video agora
    implementation ("io.agora.rtc:full-sdk:4.1.1")

    implementation ("com.makeramen:roundedimageview:2.3.0")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.intuit.sdp:sdp-android:1.1.0")
     //gg
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.android.volley:volley:1.2.1")

    //fb
    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")


    //country code
    implementation ("com.hbb20:ccp:2.5.0")

    //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")


    //circleindicator
    implementation ("me.relex:circleindicator:2.1.6")

    //Socket.IO
    implementation ("io.socket:socket.io-client:2.1.0")
 
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    //hilt-dagger
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.google.dagger:hilt-android:2.44")

    //Live-data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    //LoadImage

    implementation("com.squareup.picasso:picasso:2.71828")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.0")

    //Lottie
    implementation("com.airbnb.android:lottie:6.0.0")

    //GG map
    implementation("com.google.gms:google-services:4.3.15")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.maps.android:android-maps-utils:3.4.0")
    //Room Database
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
kapt {
    correctErrorTypes = true
}