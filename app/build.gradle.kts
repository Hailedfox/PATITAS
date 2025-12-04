plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}


android {
    namespace = "com.example.catalogo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.catalogo"
        minSdk = 21
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11


    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Supabase Kotlin (versión más reciente)
    implementation(platform("io.github.jan-tennert.supabase:bom:2.4.0"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")

    // Kotlinx Serialization (Usando solo la versión 1.7.3)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // OKHttp (Usando solo la versión 4.12.0)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Ktor (Si no se usa Ktor, se pueden eliminar estas dos)
    implementation("io.ktor:ktor-client-okhttp:2.3.5")
    implementation("io.ktor:ktor-client-core:2.3.5")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.javax.inject)
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.foundation:foundation:1.7.0")
    implementation("androidx.compose.ui:ui-text:1.7.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.runtime:runtime:1.7.0")

    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.navigation.compose)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}