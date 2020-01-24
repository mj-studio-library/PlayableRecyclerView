plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Version.compileSdk)
    buildToolsVersion(Version.buildTools)
    defaultConfig {
        applicationId = Constant.appId
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.compileSdk)
        versionCode = Version.versionCode
        versionName = Version.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    dataBinding.isEnabled = true


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = true
        isWarningsAsErrors = true
    }
}

dependencies {
    implementation(project(":playablerecyclerview"))
//    implementation("com.github.mym0404:PlayableRecyclerView:1.0-alpha01-20200124")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.17")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    implementation("com.google.android.material:material:1.2.0-alpha03")

    implementation("de.hdodenhof:circleimageview:3.0.2")

    implementation("com.victor:lib:1.0.4")

    implementation("com.google.android.exoplayer:exoplayer-core:2.11.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.11.1")

}
