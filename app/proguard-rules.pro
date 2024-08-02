# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.internal.** { *; }
-keep class com.google.gson.reflect.** { *; }
-keep class com.google.gson.stream.** { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep class com.squareup.moshi.JsonClass { *; }
-keep class com.squareup.moshi.Json { *; }

# Your data classes and API interfaces
# Keep all classes and fields in the package that are used for Retrofit API calls
-keep class com.plcoding.weatherapp.data.remote.model.** { *; }
-keep class com.plcoding.weatherapp.data.remote.api.** { *; }

# Keep all classes used by Moshi or Gson for serialization/deserialization
-keep class com.plcoding.weatherapp.data.model.** { *; }
