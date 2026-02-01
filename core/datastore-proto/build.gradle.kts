plugins {
    alias(libs.plugins.kastik.jvm.library)
    alias(libs.plugins.protobuf)
}

group = "com.kastik.apps.core.datastore.proto"

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                named("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    api(libs.protobuf.kotlin.lite)
}
