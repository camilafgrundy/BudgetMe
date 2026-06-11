plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}