import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pharmacy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pharmacy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            android.buildFeatures.buildConfig = true
            val p = Properties()
            p.load(project.rootProject.file("app/local.properties").reader())
            val yourKey: String = p.getProperty("API_KEY")
            buildConfigField("String", "API_KEY", "\"$yourKey\"")
        }
        release {
            android.buildFeatures.buildConfig = true
            val p = Properties()
            p.load(project.rootProject.file("app/local.properties").reader())
            val yourKey: String = p.getProperty("API_KEY")
            buildConfigField("String", "API_KEY", "\"$yourKey\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.material.v1120)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.rxjava.v318)
    implementation(libs.rxandroid)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.gson)
    implementation(libs.maps.mobile.v461full)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}