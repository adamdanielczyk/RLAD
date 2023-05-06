# Remove when Retrofit is released with updated proguard rules
# https://github.com/square/retrofit/pull/3579
# https://github.com/square/retrofit/pull/3598
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
