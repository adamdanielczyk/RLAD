package com.rlad.buildsrc

@Suppress("MayBeConstant")
object Libs {
    val androidGradlePlugin = "com.android.tools.build:gradle:7.1.3"

    val moshi = "com.squareup.moshi:moshi-kotlin:1.13.0"

    object Coil {
        private val version = "2.0.0-rc03"
        val compose = "io.coil-kt:coil-compose:$version"
        val gif = "io.coil-kt:coil-gif:$version"
    }

    object Google {
        val material = "com.google.android.material:material:1.5.0"
    }

    object Kotlin {
        private val version = "1.6.10"
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Coroutines {
        private val version = "1.6.1"
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

        object Test {
            val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        val navigation = "androidx.navigation:navigation-compose:2.4.2"

        object Activity {
            private val version = "1.4.0"
            val activity = "androidx.activity:activity-ktx:$version"
            val compose = "androidx.activity:activity-compose:$version"
        }

        object Test {
            private val version = "1.4.0"
            val runner = "androidx.test:runner:$version"
            val core = "androidx.test:core:$version"
        }

        object Arch {
            private val version = "2.1.0"
            val coreRuntime = "androidx.arch.core:core-runtime:$version"

            object Test {
                val coreTesting = "androidx.arch.core:core-testing:$version"
            }
        }

        object Lifecycle {
            private val version = "2.4.1"
            val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"

            object ViewModel {
                val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
                val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
            }
        }

        object Room {
            private val version = "2.4.2"
            val runtime = "androidx.room:room-runtime:$version"
            val compiler = "androidx.room:room-compiler:$version"
            val room = "androidx.room:room-ktx:$version"
            val paging = "androidx.room:room-paging:$version"
        }

        object Compose {
            val version = "1.1.1"
            val ui = "androidx.compose.ui:ui:$version"
            val tooling = "androidx.compose.ui:ui-tooling:$version"
            val foundation = "androidx.compose.foundation:foundation:$version"
            val material = "androidx.compose.material:material:$version"
            val materialIcons = "androidx.compose.material:material-icons-extended:$version"
        }

        object Paging {
            val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
            val compose = "androidx.paging:paging-compose:1.0.0-alpha14"
        }
    }

    object Accompanist {
        private val version = "0.24.7-alpha"
        val systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object Hilt {
        private val version = "2.41"
        val hilt = "com.google.dagger:hilt-android:$version"
        val compiler = "com.google.dagger:hilt-compiler:$version"
        val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Retrofit {
        private val version = "2.9.0"
        val retrofit = "com.squareup.retrofit2:retrofit:$version"
        val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Test {
        val junit = "junit:junit:4.13.2"
        val mockk = "io.mockk:mockk:1.12.3"
    }
}
