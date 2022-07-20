val kotlinWrappersVersion = "18.2.0-pre.357"
val emotionWrappersVersion = "11.9.3-pre.357"
val kotlinxVersion = "0.7.5"

plugins {
    id("org.springframework.boot") version "2.7.1" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
//    war
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.spring") version "1.6.21" apply false
    application
}

group = "me.farme"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm("backend") {
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")

        compilations.all {
            kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js("frontend", LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                outputFileName = "main.js"
                outputPath = File(buildDir, "processedResources/backend/main/static")
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val backendMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxVersion")
                implementation("org.springframework.boot:spring-boot-starter-web")


            }
        }
        val backendTest by getting{
            dependencies{
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val frontendMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$kotlinWrappersVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$kotlinWrappersVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:$emotionWrappersVersion")
            }
        }
        val frontendTest by getting
    }
}

application {
    mainClass.set("me.farme.application.ServerAppKt")
}

tasks.named<Copy>("backendProcessResources") {
    val frontendBrowserDistribution = tasks.named("frontendBrowserDistribution")
    from(frontendBrowserDistribution){

    }
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("backendJar"))
    classpath(tasks.named<Jar>("backendJar"))
}