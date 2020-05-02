plugins {
    `java-library`
    id("com.bmuschko.asciidoctorj.tabbedcode.additional-artifacts")
    id("com.bmuschko.asciidoctorj.tabbedcode.publishing")
}

group = "com.bmuschko"
version = "0.3"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    compileOnly("org.asciidoctor:asciidoctorj:2.2.0")
    testImplementation("org.asciidoctor:asciidoctorj:2.2.0")
    testImplementation("org.jsoup:jsoup:1.11.3")
    val junitJupiterVersion = "5.3.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit-pioneer:junit-pioneer:0.3.0")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
