plugins {
    id("com.android.application")
}

android {
    namespace = "tech.johnnn.ossave"
    compileSdk = 34

    defaultConfig {
        applicationId = "tech.johnnn.ossave"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")

    implementation("androidx.media3:media3-transformer:1.1.1")
    implementation("androidx.media3:media3-effect:1.1.1")
    implementation("androidx.media3:media3-common:1.1.1")

    //video operation like edit trim overlays filters
    implementation("com.arthenica:ffmpeg-kit-full-gpl:6.0.LTS")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}