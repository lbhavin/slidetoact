plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("android")
}

version = "0.0.2".plus(if (hasProperty("USE_SNAPSHOT")) "-SNAPSHOT" else "")
group = "com.ncorti"

android {
    compileSdk = 34
    namespace = "com.ncorti.slidetoact"

    defaultConfig {
        minSdk = 14
        vectorDrawables.useSupportLibrary = true
    }
    lint {
        abortOnError = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
}

publishing {
    repositories {
        maven {
            name = "nexus"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = "NEXUS_USERNAME".byProperty
                password = "NEXUS_PASSWORD".byProperty
            }
        }
        maven {
            name = "snapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            credentials {
                username = "NEXUS_USERNAME".byProperty
                password = "NEXUS_PASSWORD".byProperty
            }
        }
    }
    publications {
        register<MavenPublication>("release") {
            pom {
                name.set("slidetoact")
                description.set(
                    "A simple Slide to Unlock Material widget for Android, written in Kotlin",
                )
                url.set("https://github.com/lbhavin/slidetoact")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("lbhavin")
                        name.set("Bhavin Laiya")
                        email.set("laiya.bhavin@gmail.com")
                    }
                }
                scm {
                    connection.set("https://github.com/lbhavin/slidetoact.git")
                    developerConnection.set("https://github.com/lbhavin/slidetoact.git")
                    url.set("https://github.com/lbhavin/slidetoact")
                }
            }
            afterEvaluate {
                from(components["release"])
            }
        }

        val signingKey = "SIGNING_KEY".byProperty
        val signingPwd = "SIGNING_PWD".byProperty
        if (signingKey.isNullOrBlank() || signingPwd.isNullOrBlank()) {
            logger.info("Signing Disable as the PGP key was not found")
        } else {
            logger.info("GPG Key found - Signing enabled")
            signing {
                useInMemoryPgpKeys(signingKey, signingPwd)
                sign(publishing.publications["release"])
            }
        }
    }
}

ktlint {
    debug.set(false)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    filter {
        exclude("**/generated/**")
    }
}

val String.byProperty: String? get() = findProperty(this) as? String
