plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id(Plugins.DAGGER_HILT)
    id(Plugins.KOTLIN_SYMBOL_PROCESSING)
    kotlin(Plugins.KOTLIN_ANNOTATION_PROCESSING_TOOL)
}

android {
    namespace = "com.home.domain"
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

    implementation(Deps.core)
    testImplementation(Deps.jUnit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Room
    implementation(RoomDatabase.room_runtime)
    annotationProcessor(RoomDatabase.room_compiler)
    ksp(RoomDatabase.room_compiler_ksp)
    implementation (RoomDatabase.room_ktx)

    //DaggerHilt
    implementation(DaggerHilt.dagger_hilt)
    kapt(DaggerHilt.dagger_hilt_compiler)
    implementation(DaggerHilt.dagger_hilt_navigation)

    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofit_gson_converter)
    implementation(Retrofit.okhttp)
    implementation(Retrofit.okhttp_logging)
}