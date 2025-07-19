buildscript{
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.10.1") // Example Gradle version
        classpath("com.google.gms:google-services:4.4.3") // Google services plugin
    }

}
plugins {
    id("com.android.application") version "8.10.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply true



}