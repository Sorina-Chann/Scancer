plugins {
    id("com.android.application")

    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.scancer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.scancer"
        minSdk = 28
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.cast.framework)
    testImplementation(libs.junit)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-database:21.0.0")

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation ("com.makeramen:roundedimageview:2.3.0")

    implementation ("com.google.firebase:firebase-auth:23.2.1")
    implementation ("androidx.credentials:credentials:1.5.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation ("com.github.dhaval2404:imagepicker:2.1")

    implementation ("com.tbuonomo:dotsindicator:4.3")
   // implementation ("com.tbuonomo.viewpagerdotsindicator:dotsindicator:4.3")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}