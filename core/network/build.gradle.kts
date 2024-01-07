plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(Plugins.DAGGER_HILT)
    id(Plugins.KOTLIN_SERIALIZATION)
    id(Plugins.KOTLIN_SYMBOL_PROCESSING)
    kotlin(Plugins.KOTLIN_ANNOTATION_PROCESSING_TOOL)
    id(Plugins.KTORFIT)
}

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = Version.ktorfit
}

android {
    namespace = "com.core.network"
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
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(Deps.core)
    testImplementation(Deps.jUnit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //DaggerHilt
    implementation(DaggerHilt.dagger_hilt)
    kapt(DaggerHilt.dagger_hilt_compiler)
    implementation(DaggerHilt.dagger_hilt_navigation)
//    ksp(DaggerHilt.dagger_compiler)
//    ksp(DaggerHilt.hilt_compiler)
    // Serialization
    implementation(KotlinSerialization.serialization)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofit_gson_converter)
    implementation(Retrofit.okhttp)
    implementation(Retrofit.okhttp_logging)
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //Ktor // Ktorfit
    implementation(Ktor.ktor_client_serialization)
    implementation(Ktor.ktor_serialization_json)
    implementation(Ktor.ktor_client_json)
    implementation(Ktor.ktor_client_logging)
    implementation(Ktor.ktor_client_content_negotiation)
    ksp(Ktor.ktorfit_ksp)
    implementation(Ktor.ktorfit_lib)

    implementation(project(":core:common"))
}