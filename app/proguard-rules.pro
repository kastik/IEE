-repackageclasses

-keepclassmembers class com.google.firebase.perf.v1.* { *; }

-keepclassmembers class com.google.protobuf.** { *; }
-keepclassmembers class com.kastik.apps.core.datastore.proto.* { *; }
-keep interface com.kastik.apps.core.network.api.** { *; }

-dontwarn com.google.android.gms.common.annotation.NoNullnessRewrite

-assumenosideeffects class android.util.Log {
    public static int d(java.lang.String, java.lang.String);
}