import Version.accompanistVersion
import Version.coilVersion
import Version.compose_des_version
import Version.dagger_hilt_Version
import Version.kotlin_compiler_version
import Version.lifecycleVersion
import Version.okhttpVersion
import Version.retrofitVersion
import Version.roomVersion

object Version {
    const val core = "1.12.0"
    const val lifecycleVersion = "2.6.2"
    const val roomVersion = "2.5.2"
    const val coilVersion = "2.4.0"
    const val retrofitVersion = "2.9.0"
    const val okhttpVersion = "5.0.0"
    const val kotlin_compiler_version = "1.5.1"
    const val dagger_hilt_Version = "2.50"
    const val compose_des_version = "1.9.42-beta"
    const val ktor_version = "2.3.1"
    const val ktorfit = "1.6.0"
    const val accompanistVersion = "0.35.0-alpha"

}

object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val DAGGER_HILT = "com.google.dagger.hilt.android"
    const val KOTLIN_SERIALIZATION = "kotlinx-serialization"
    const val KOTLIN_SYMBOL_PROCESSING = "com.google.devtools.ksp"
    const val KOTLIN_ANNOTATION_PROCESSING_TOOL = "kapt"
    const val KTORFIT = "de.jensklingenberg.ktorfit"
}


object Deps {
    const val core = "androidx.core:core-ktx:${Version.core}"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2"
    const val actvivty_compose = "androidx.activity:activity-compose:1.8.1"
    const val compose_bom = "androidx.compose:compose-bom:2023.10.01"
    const val compose_ui = "androidx.compose.ui:ui"
    const val compose_graphic = "androidx.compose.ui:ui-graphics"
    const val compose_toolin_preview = "androidx.compose.ui:ui-tooling-preview"
    const val compose_material3 = "androidx.compose.material3:material3-android:1.2.0-rc01"
    const val constraintsLayout_compose = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    const val jUnit4 = "junit:junit:4.13.2"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2"

    //Compose destination
    const val compose_destination =
        "io.github.raamcosta.compose-destinations:core:$compose_des_version"
    const val compose_des_ksp = "io.github.raamcosta.compose-destinations:ksp:$compose_des_version"
}

object ViewModel {
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
    const val lifecycle_viewModel = "androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion"
}

object Coil {
    const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val retrofit_gson_converter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion-alpha.2"
    const val okhttp_logging = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion-alpha.2"
}

object Ktor{

    const val ktor_client_serialization = "io.ktor:ktor-client-serialization:${Version.ktor_version}"
    const val ktor_serialization_json = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor_version}"
    const val ktor_client_json = "io.ktor:ktor-client-json-jvm:${Version.ktor_version}"
    const val ktor_client_content_negotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor_version}"
    const val ktor_client_logging ="io.ktor:ktor-client-logging-jvm:${Version.ktor_version}"
    //"ch.qos.logback:logback-classic:1.2.3")
    const val ktorfit_ksp = "de.jensklingenberg.ktorfit:ktorfit-ksp:${Version.ktorfit}"
    const val ktorfit_lib = "de.jensklingenberg.ktorfit:ktorfit-lib:${Version.ktorfit}"
}

object RoomDatabase {
    const val room_runtime = "androidx.room:room-runtime:$roomVersion"
    const val room_compiler = "androidx.room:room-compiler:$roomVersion"
    const val room_compiler_ksp = "androidx.room:room-compiler:$roomVersion"
    const val room_ktx = "androidx.room:room-ktx:$roomVersion"
}

object KotlinSerialization {
    const val serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_compiler_version"
}

object DaggerHilt {
    const val dagger_hilt = "com.google.dagger:hilt-android:$dagger_hilt_Version"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-android-compiler:$dagger_hilt_Version"
    const val dagger_hilt_navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"

    const val dagger_compiler =
        "com.google.dagger:dagger-compiler:$dagger_hilt_Version" // Dagger compiler
    const val hilt_compiler =
        "com.google.dagger:hilt-compiler:$dagger_hilt_Version"   // Hilt compiler
}

object Icons {
    const val icon_extended =
        "androidx.compose.material:material-icons-extended:$kotlin_compiler_version"
}

object DataStore {
    const val data_store = "androidx.datastore:datastore-preferences:1.0.0"
}

object Accompanist {
    const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$accompanistVersion"
    const val permission = "com.google.accompanist:accompanist-permissions:$accompanistVersion"
    const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:$accompanistVersion"

}

object Firebase {
    const val firebase_bom = "com.google.firebase:firebase-bom:32.7.1"
    const val firbase_firestore = "com.google.firebase:firebase-firestore"
    const val firebase_storage = "com.google.firebase:firebase-storage"
}
