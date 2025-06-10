plugins {
    `java-gradle-plugin`
    kotlin("jvm")
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
    id("org.jetbrains.dokka")
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(kotlin(module = "stdlib"))
    implementation(project(":openapi-merger-app"))
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    runtimeOnly("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    runtimeOnly("org.hibernate.validator:hibernate-validator-annotation-processor:8.0.2.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")

    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = "5.9.1")
    testImplementation(group = "io.kotest", name = "kotest-framework-engine-jvm", version = "5.9.1")
    testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.4.1")
}

pluginBundle {
    website = "https://github.com/kpramesh2212/openapi-merger-plugin"
    vcsUrl = "https://github.com/kpramesh2212/openapi-merger-plugin.git"
    tags = listOf("openapi-3.0", "merger")
}

gradlePlugin {
    plugins {
        create("openapi-merger-gradle-plugin") {
            id = "com.rameshkp.openapi-merger-gradle-plugin"
            displayName = "OpenAPI 3 merger gradle plugin"
            description = "A gradle plugin to merge multiple openapi files"
            implementationClass = "com.rameshkp.openapi.merger.gradle.plugin.OpenApiMergerGradlePlugin"
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
val dokkaJavadoc by tasks.existing
val javadocJar by tasks.existing(Jar::class) {
    dependsOn(dokkaJavadoc)
    from("$buildDir/dokka/javadoc")
}
project.afterEvaluate {
    val publishPluginJavaDocsJar by tasks.existing(Jar::class) {
        dependsOn(dokkaJavadoc)
        from("$buildDir/dokka/javadoc")
    }
}

fun setPomDetails(mavenPublication: MavenPublication) {
     mavenPublication.pom {
        name.set("Open API V3 Merger gradle plugin")
        description.set("A gradle plugin to merge open api v3 specification files")
        url.set("https://github.com/kpramesh2212/openapi-merger-plugin")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("EvgeniiVol")
                name.set("Evgenii Volokhonskii")
                email.set("evgenii.volokhonskii@optiva.com")
            }
        }
        scm {
            connection.set("git@github.com:kpramesh2212/openapi-merger-plugin.git")
            developerConnection.set("git@github.com:kpramesh2212/openapi-merger-plugin.git")
            url.set("https://github.com/kpramesh2212/openapi-merger-plugin")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "Artifactory"
            url = uri("https://artifactory.labs.optiva.com/artifactory/plugins-snapshot-local")
            credentials {
                username = project.findProperty("artifactoryUser") as String?
                password = project.findProperty("artifactoryPassword") as String?
            }
        }
    }
}