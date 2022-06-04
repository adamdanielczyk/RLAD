plugins {
    id("rlad.android.library")
    id("dagger.hilt.android.plugin")
}

dependencies {
    api(Libs.Coroutines.Test.test)
    api(Libs.Test.junit)
    api(Libs.Test.mockk)
    api(Libs.AndroidX.Test.runner)
    api(Libs.AndroidX.Test.core)
}