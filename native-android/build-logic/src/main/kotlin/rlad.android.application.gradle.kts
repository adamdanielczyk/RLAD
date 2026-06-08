import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")
    id("dev.zacsweers.metro")
    id("org.jetbrains.kotlin.plugin.compose")
    id("rlad.renameartifacts")
}

extensions.configure<ApplicationExtension> {
    configureKotlinAndroid(this)
    configureCompose(this)
}

dependencies {
    shared()
}
