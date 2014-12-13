# Common
-dontwarn java.lang.invoke**
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe
-keepattributes Signature
-keepattributes *Annotation*

# Kotlin
-dontwarn kotlin.**
-keep class kotlin.jvm.**
-keep class kotlin.reflect.**

# Butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *; }

# Retrofit + OkHttp
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# RxJava
-keep class rx.** { *; }
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.welbits.cinesapp.model.** { *; }