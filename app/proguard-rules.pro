# Remove when Retrofit uses OkHttp 5 which contains these rules (if ever?)
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Remove when Retrofit is released with updated proguard rules
# https://github.com/square/retrofit/pull/3579
# https://github.com/square/retrofit/pull/3598
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
