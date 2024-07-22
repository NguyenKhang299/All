plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id ("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
//    id("com.google.gms.google-services")
}

android {
    namespace = "com.knd.duantotnghiep.testsocket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.knd.duantotnghiep.testsocket"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    }
}

dependencies {
    //Live-data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation ("com.google.code.gson:gson:2.10")
    //Socket.IO
    implementation ("io.socket:socket.io-client:2.1.0")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    //hilt-dagger
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.8")

}
kapt {
    correctErrorTypes = true
}