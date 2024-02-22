plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.DAGGER_HILT)
    id(Plugins.KOTLIN_SERIALIZATION)
    id(Plugins.KOTLIN_SYMBOL_PROCESSING)
    kotlin(Plugins.KOTLIN_ANNOTATION_PROCESSING_TOOL)
    id(Plugins.KTORFIT)
    id("com.google.gms.google-services")
}
configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = Version.ktorfit
}
android {
    namespace = "com.abdts.petadoption"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.abdts.petadoption"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.abdts.petadoption.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    ksp {
        arg("compose-destinations.moduleName", "auth")
        arg("compose-destinations.mode", "destinations")
    }
    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(Deps.core)
    implementation(Deps.lifecycle_runtime)
    implementation(Deps.actvivty_compose)
    implementation(platform(Deps.compose_bom))
    implementation(Deps.compose_ui)
    implementation(Deps.compose_toolin_preview)
    implementation(Deps.compose_graphic)
    implementation(Deps.compose_material3)
    implementation(project(mapOf("path" to ":feature:auth:ui")))
    testImplementation(Deps.jUnit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Local unit tests
    testImplementation ("androidx.test:core:1.5.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation ("com.google.truth:truth:1.2.0")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation ("io.mockk:mockk:1.10.5")
    androidTestImplementation(project(":feature:auth:domain"))

    // For Robolectric tests.
    testImplementation ("com.google.dagger:hilt-android-testing:2.44")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // ...with Kotlin.
    kaptTest ("com.google.dagger:hilt-android-compiler:2.50")
    // For instrumented tests.
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.44")
    // ...with Kotlin.
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.50")


    implementation(Icons.icon_extended)

    //navigation
    implementation(Deps.compose_destination)
    ksp(Deps.compose_des_ksp)

    //view model
    implementation(ViewModel.viewmodel)
    //life cycle
    implementation(ViewModel.lifecycle_viewModel)

    // Serialization
    implementation(KotlinSerialization.serialization)

    //Room
    implementation(RoomDatabase.room_runtime)
    annotationProcessor(RoomDatabase.room_compiler)
    ksp(RoomDatabase.room_compiler_ksp)
    implementation (RoomDatabase.room_ktx)

    //DaggerHilt
    implementation(DaggerHilt.dagger_hilt)
    kapt(DaggerHilt.dagger_hilt_compiler)
    implementation(DaggerHilt.dagger_hilt_navigation)
//
//    ksp(DaggerHilt.dagger_compiler)
//    ksp(DaggerHilt.hilt_compiler)



    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofit_gson_converter)
    implementation(Retrofit.okhttp)
    implementation(Retrofit.okhttp_logging)

    //Ktor // Ktorfit
    implementation(Ktor.ktor_client_serialization)
    implementation(Ktor.ktor_serialization_json)
    implementation(Ktor.ktor_client_json)
    implementation(Ktor.ktor_client_logging)
    implementation(Ktor.ktor_client_content_negotiation)
    ksp(Ktor.ktorfit_ksp)
    implementation(Ktor.ktorfit_lib)

    //coli
    implementation(Coil.coil)

    //Data store
    implementation(DataStore.data_store)

    //firebase\
    implementation(platform(Firebase.firebase_bom))
    implementation ("com.google.firebase:firebase-dynamic-module-support:16.0.0-beta03")

    implementation(project(":core:network"))
    implementation(project(":feature:auth:ui"))
    implementation(project(":feature:home:ui"))
    implementation(project(":feature:profile:ui"))
    implementation(project(":feature:chat:ui"))

    implementation(project(":core:common"))
    implementation(project(":feature:home:domain"))
    implementation(project(":feature:profile:data"))
    implementation(project(":feature:home:data"))
    implementation(project(":core:database"))


    androidTestImplementation(project(":feature:auth:domain"))
    androidTestImplementation(project(":feature:profile:domain"))
    androidTestImplementation(project(":feature:home:domain"))


}