package com.sample.buildsrc

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.6.3"

    const val moshi = "com.squareup.moshi:moshi-kotlin:1.9.2"
    const val glide = "com.github.bumptech.glide:glide:4.11.0"

    object Google {
        const val material = "com.google.android.material:material:1.1.0"
    }

    object Kotlin {
        private const val version = "1.3.72"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Coroutines {
        private const val version = "1.3.5"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

        object Test {
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val activity = "androidx.activity:activity-ktx:1.1.0"
        const val paging = "androidx.paging:paging-runtime-ktx:2.1.2"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val core = "androidx.core:core-ktx:1.2.0"

        object Test {
            private const val version = "1.2.0"
            const val runner = "androidx.test:runner:$version"
            const val core = "androidx.test:core:$version"
        }

        object Arch {
            private const val version = "2.1.0"
            const val coreRuntime = "androidx.arch.core:core-runtime:2.1.0"

            object Test {
                const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
            }
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        }

        object Room {
            private const val version = "2.2.5"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val room = "androidx.room:room-ktx:$version"
        }
    }

    object Dagger {
        private const val version = "2.27"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
    }

    object Retrofit {
        private const val version = "2.8.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Test {
        const val junit = "junit:junit:4.13"
        const val mockk = "io.mockk:mockk:1.10.0"
    }
}
