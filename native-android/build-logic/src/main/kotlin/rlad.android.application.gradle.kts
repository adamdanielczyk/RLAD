import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("rlad.android.metro")
    id("rlad.renameartifacts")
}

extensions.configure<ApplicationExtension> {
    configureKotlinAndroid(this)
    configureCompose(this)
}

dependencies {
    shared()
}
