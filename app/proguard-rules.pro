-keepclassmembers class com.google.firebase.perf.v1.PerfMetric { *; }
-keepclassmembers class com.google.firebase.perf.v1.TraceMetric { *; }
-keepclassmembernames class com.google.firebase.perf.v1.PerfSession { *; }
-keepclassmembernames class com.google.firebase.perf.v1.NetworkRequestMetric { *; }
-keepclassmembernames class com.google.firebase.perf.v1.ApplicationInfo { *; }
-keepclassmembernames class com.google.firebase.perf.v1.AndroidApplicationInfo { *; }
-keep interface com.kastik.apps.core.network.api.** { *; }


-assumenosideeffects class android.util.Log {
    public static int d(java.lang.String, java.lang.String);
    public static int v(java.lang.String, java.lang.String);
    public static int i(java.lang.String, java.lang.String);
    public static int w(java.lang.String, java.lang.String);
    public static int e(java.lang.String, java.lang.String);
    public static int wtf(java.lang.String, java.lang.String);
}