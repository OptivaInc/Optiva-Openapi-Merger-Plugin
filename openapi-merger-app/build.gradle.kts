plugins {
    kotlin("jvm")
    `maven-publish`
    signing
    id("io.kotest")
    id("org.jetbrains.dokka")
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(kotlin(module = "stdlib"))
    implementation(group = "io.swagger.parser.v3", name = "swagger-parser", version = "2.0.23")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.30")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    runtimeOnly("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    runtimeOnly("org.hibernate.validator:hibernate-validator-annotation-processor:8.0.2.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")
    runtimeOnly(group = "javax.el", name = "javax.el-api", version = "3.0.0")
    runtimeOnly(group = "org.glassfish.web", name = "javax.el", version = "2.2.6")

    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = "4.3.1")
    testImplementation(group = "io.kotest", name = "kotest-framework-engine-jvm", version = "4.3.1")
    testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.4.1")
    testImplementation(group = "io.mockk", name = "mockk", version = "1.12.2")

}

publishing {
    publications {
        create<MavenPublication>("openApiMergerApp") {
            from(components["java"])

            pom {
                name.set("Open API V3 Merger Application")
                description.set("A Kotlin app to merge open api v3 specification files")
                url.set("https://github.com/kpramesh2212/openapi-merger-plugin")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("rameshkp")
                        name.set("Ramesh KP")
                        email.set("kpramesh2212@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:kpramesh2212/openapi-merger-plugin.git")
                    developerConnection.set("scm:git:ssh://github.com:kpramesh2212/openapi-merger-plugin.git")
                    url.set("https://github.com/kpramesh2212/openapi-merger-plugin")
                }

            }

        }
    }
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



tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

/*signing {
    sign(publishing.publications["openApiMergerApp"])
}*/

val dokkaJavadoc by tasks.existing
val javadocJar by tasks.existing(Jar::class) {
    dependsOn(dokkaJavadoc)
    from("$buildDir/dokka/javadoc")
}