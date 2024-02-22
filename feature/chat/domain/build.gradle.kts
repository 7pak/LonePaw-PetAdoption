plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(Plugins.DAGGER_HILT)
    kotlin(Plugins.KOTLIN_ANNOTATION_PROCESSING_TOOL)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.feature.chat.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //DaggerHilt
    implementation(DaggerHilt.dagger_hilt)
    kapt(DaggerHilt.dagger_hilt_compiler)
    implementation(DaggerHilt.dagger_hilt_navigation)

    implementation(project(":core:network"))
    implementation(project(":core:common"))

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofit_gson_converter)
    implementation(Retrofit.okhttp)
    implementation(Retrofit.okhttp_logging)

    //firebase
    implementation(platform(Firebase.firebase_bom))
    implementation(Firebase.firbase_firestore)
    implementation(Firebase.firebase_storage)
}