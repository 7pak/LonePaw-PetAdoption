plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(Plugins.DAGGER_HILT)
    id(Plugins.KOTLIN_SYMBOL_PROCESSING)
    kotlin(Plugins.KOTLIN_ANNOTATION_PROCESSING_TOOL)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.core.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testImplementation(Deps.jUnit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //view model
    implementation(ViewModel.viewmodel)
    //life cycle
    implementation(ViewModel.lifecycle_viewModel)

    //DaggerHilt
    implementation(DaggerHilt.dagger_hilt)
    kapt(DaggerHilt.dagger_hilt_compiler)
    implementation(DaggerHilt.dagger_hilt_navigation)

    //Data store
    implementation(DataStore.data_store)

    //firebase
    implementation(platform(Firebase.firebase_bom))
    implementation(Firebase.firbase_firestore)
    implementation(Firebase.firebase_storage)

    //Room
    implementation(RoomDatabase.room_runtime)
    annotationProcessor(RoomDatabase.room_compiler)
    ksp(RoomDatabase.room_compiler_ksp)
    implementation (RoomDatabase.room_ktx)
}