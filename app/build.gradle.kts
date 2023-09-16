plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rg.headernotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rg.headernotes"
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    android.applicationVariants.all {
        this.apply {
            outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "HeaderNotes - $baseName - $versionName $versionCode.apk"
                output.outputFileName = outputFileName
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.preference:preference:1.2.1")

    //Navigation
    val navigationVersion = "2.7.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:navigationVersion")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-firestore:24.7.1")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.7.1")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation (platform("com.google.firebase:firebase-auth:21.1.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

    // Hilt
    val hiltVersion = "2.48"
    implementation ("com.google.dagger:hilt-android:$hiltVersion")
    kapt ("com.google.dagger:hilt-compiler:$hiltVersion")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    // Update Configuration
    implementation ("com.jakewharton:process-phoenix:2.0.0")
}

kapt {
    correctErrorTypes = true
}