package com.sample.buildsrc

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.3"

    const val moshi = "com.squareup.moshi:moshi-kotlin:1.13.0"
    const val glide = "com.github.bumptech.glide:glide:4.13.1"

    object Google {
        const val material = "com.google.android.material:material:1.5.0"
    }

    object Kotlin {
        private const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Coroutines {
        private const val version = "1.6.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

        object Test {
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val activity = "androidx.activity:activity-ktx:1.4.0"
        const val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.3"
        const val core = "androidx.core:core-ktx:1.7.0"

        object Test {
            private const val version = "1.4.0"
            const val runner = "androidx.test:runner:$version"
            const val core = "androidx.test:core:$version"
        }

        object Arch {
            private const val version = "2.1.0"
            const val coreRuntime = "androidx.arch.core:core-runtime:$version"

            object Test {
                const val coreTesting = "androidx.arch.core:core-testing:$version"
            }
        }

        object Lifecycle {
            private const val version = "2.4.1"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.4.2"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val room = "androidx.room:room-ktx:$version"
            const val paging = "androidx.room:room-paging:$version"
        }
    }

    object Dagger {
        private const val version = "2.41"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val mockk = "io.mockk:mockk:1.12.3"
    }
}
