plugins {
    `java-library`
    id("com.bmuschko.asciidoctorj.tabbedcode.additional-artifacts")
    id("com.bmuschko.asciidoctorj.tabbedcode.publishing")
}

group = "com.bmuschko"
version = "0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    compileOnly("org.asciidoctor:asciidoctorj:1.6.0-RC.1")
    testImplementation("org.asciidoctor:asciidoctorj:1.6.0-RC.1")
    val junitJupiterVersion = "5.3.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}