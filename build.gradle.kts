plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.graalvm.buildtools.native") version "0.10.2"
    id("java")
    id("maven-publish")
}

group = "com.vmware.tap.accelerators"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

springBoot {
	buildInfo()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

repositories {
     maven {
         url = uri("https://artifactory.tanzu.home/artifactory/gradle-dev/")
         credentials {
            username = System.getenv("ARTIFACTORY_USER")
            password = System.getenv("ARTIFACTORY_PASSWORD")
         }
     }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.5")

    // Observability support
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.13.6")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}
