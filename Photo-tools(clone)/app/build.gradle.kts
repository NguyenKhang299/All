plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
//    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.photo.imagecompressor.tools"
    compileSdk = 34
    androidResources {
        generateLocaleConfig = true //add this for  locale
    }
    defaultConfig {
        applicationId = "com.photo.imagecompressor.tools"
        minSdk = 24
        targetSdk = 34
        versionCode = 107
        versionName = "1.0.7"
        resourceConfigurations.plus(listOf("vi","en"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
    }
}

dependencies {
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
    implementation(libs.firebase.config)
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
    implementation("me.relex:circleindicator:2.1.6")

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
}// Allow references to generated code
kapt {
    correctErrorTypes = true
}