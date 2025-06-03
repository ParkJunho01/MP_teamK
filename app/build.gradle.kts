plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tageatproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tageatproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "NAVER_CLIENT_ID", "\"86zf15o9kd\"")
            buildConfigField ("String", "NAVER_CLIENT_SECRET", "\"3m4eVIbSZ6j9Mks2FOheh9xrnDNJR5JF70MDXxTV\"")
        }
        release {
            buildConfigField("String", "NAVER_CLIENT_ID", "86zf15o9kd")
            buildConfigField ("String", "NAVER_CLIENT_SECRET", "3m4eVIbSZ6j9Mks2FOheh9xrnDNJR5JF70MDXxTV")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-common-ktx:20.4.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


    implementation ("com.naver.maps:map-sdk:3.21.0") // naver map 의존성 추가
    implementation("com.google.android.gms:play-services-location:21.0.1") // 위치추적 의존성 추가

    implementation("com.google.android.material:material:1.11.0") // BottomNavigationView 라이브러리 추가



    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}