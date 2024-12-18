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
    mavenCentral()
     //maven {
         //url = uri("https://artifactory.tanzu.home/artifactory/gradle-dev-mirror/")
         //url = uri("http://10.0.189.49/artifactory/gradle-dev/")
       //  credentials {
        //    username = System.getenv("ARTIFACTORY_USER")
        //    password = System.getenv("ARTIFACTORY_PASSWORD")
        // }
      //   isAllowInsecureProtocol = true
    // }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.0")

    // Flyaway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    // AWS SDK
    implementation("software.amazon.awssdk:s3:2.20.37")

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
