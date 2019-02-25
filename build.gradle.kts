import com.google.protobuf.gradle.*
import edu.wpi.first.toolchain.NativePlatforms

val ROBOT_MAIN_CLASS = "frc.team852.Main"
val grpcVersion = "1.18.0"
val protobufVersion = "3.5.1"
val protocVersion = "3.5.1-1"

plugins {
    java
    idea
    application
    `kotlin-dsl`
    id("edu.wpi.first.GradleRIO") version "2019.3.1"
    id("com.google.protobuf") version "0.8.8"
}

repositories {
    maven {
        url = uri("https://maven-central.storage-download.googleapis.com/repos/central/data/")
    }
    mavenLocal()
}

deploy {
    targets {
        roboRIO {
            setProject(rootProject)
            val name = "roborio"
            val team = frc.getTeamOrDefault(852)
        }
    }
    artifacts {
        frcJavaArtifact {
            targets.add(jaci.gradle.deploy.target.RemoteTarget("roborio", rootProject))
        }

        fileTreeArtifact("frcStaticFileDeploy", closureOf<Any> {
            val files = fileTree("src/main/deploy")
            targets.add(jaci.gradle.deploy.target.RemoteTarget("roborio", rootProject))
            val directory = "/home/lvuser/deploy"
        })


    }
}

dependencies {
    // TODO maybe filter out OpenCV and other crap
    wpi.deps.wpilib().forEach { implementation(it) }
    wpi.deps.vendor.java().forEach { implementation(it) }
    // Get in the right format b/c jni seems to return something that the kotlin DSL doesn't like
    // nativeZip(wpi.deps.vendor.jni(wpi.platforms.roborio))
    nativeZip(wpi.deps.vendor.jni(NativePlatforms.roborio).map {
        it.split("@")[0].split(":").slice(IntRange(0, 2))
    }[0].joinToString(separator = ":"))
    // nativeDesktopZip(wpi.deps.vendor.jni(wpi.platforms.desktop))
    nativeDesktopZip(wpi.deps.vendor.jni(NativePlatforms.desktop).map {
        it.split("@")[0].split(":").slice(IntRange(0, 2))
    }[0].joinToString(separator = ":"))

    implementation("edu.wpi.first.shuffleboard:api:1.3.1")
    implementation("org.openjfx:javafx-base:11.0.1")

    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")

    if (JavaVersion.current().isJava9Compatible) {
        compileOnly("javax.annotation:javax.annotation-api:1.2")
    }

    implementation("org.eclipse.jetty:jetty-server:9.4.14.v20181114")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.14.v20181114")

    implementation("io.prometheus:client:0.0.10")
    implementation("io.prometheus:simpleclient:0.6.0")
    implementation("io.prometheus:simpleclient_hotspot:0.6.0")
    implementation("io.prometheus:simpleclient_httpserver:0.6.0")
    implementation("io.prometheus:simpleclient_servlet:0.6.0")

    testImplementation("io.grpc:grpc-testing:${grpcVersion}")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:1.9.5")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${protocVersion}" }
    plugins {
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}" }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins { id("grpc") }
        }
    }
}


tasks.jar {
    configurations.compile.get().map { if (it.isDirectory) it else zipTree(it) }
    manifest(edu.wpi.first.gradlerio.GradleRIOPlugin.javaManifest(ROBOT_MAIN_CLASS))
}

tasks.withType<Wrapper> {
    gradleVersion = "5.2.1"
}

tasks.startScripts {
    this.mainClassName = "frc.team852.lib.grpc.CVDataServer"
    this.applicationName = "grpc-server"
    this.outputDir = File(project.buildDir, "tmp")
}
fun String.runCommand(workingDir: File) {
    ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(60, TimeUnit.MINUTES)
}
// TODO make these non ad-hoc tasks
tasks.create("python-stubs") {
    group = "ARC852"
    description = "Generate python gRPC stubs"
    doLast {
        // Lets us not care about catching exceptions
        val dir = project.rootDir
        println(project.rootDir)
        "mkdir -p ./build/generated/source/python".runCommand(dir)
        "touch ./build/generated/source/python/__init__.py".runCommand(dir)
        "python3 -m grpc_tools.protoc -I. --python_out=./build/generated/source/python --grpc_python_out=./build/generated/source/python --proto_path=./src/main/proto CVData.proto".runCommand(dir)
    }
}

tasks.create("java-server") {
    group = "ARC852"
    description = "Run standalone java gRPC server"
    doLast {
        "build/install/${project.name}/bin/grpc-server".runCommand(project.rootDir)
    }
}

tasks.create("python-server") {
    group = "ARC852"
    description = "Run the python gRPC server impl"
    doLast {
        "python3 src/main/python/greeter_server.py".runCommand(project.rootDir)
    }
}

tasks.create("python-client") {
    group = "ARC852"
    description = "Run python gRPC client"
    doLast {
        "python3 src/main/python/visionClient.py".runCommand(project.rootDir)
    }
}

tasks.create("arc852") {
    group = "ARC852"
    description = "Use pip to install team python libraries ** Currently broken (no sudo) **"
    doLast {
        "sudo -H pip3 install --upgrade arc852-robotics --extra-index-url https://pypi.fury.io/pambrose/".runCommand(project.rootDir)
    }
}
