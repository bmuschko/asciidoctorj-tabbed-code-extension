plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins {
        register("additional-artifacts-plugin") {
            id = "com.bmuschko.asciidoctorj.tabbedcode.additional-artifacts"
            implementationClass = "com.bmuschko.asciidoctorj.tabbedcode.AdditionalArtifactsPlugin"
        }
        register("publishing-plugin") {
            id = "com.bmuschko.asciidoctorj.tabbedcode.publishing"
            implementationClass = "com.bmuschko.asciidoctorj.tabbedcode.PublishingPlugin"
        }
    }
}