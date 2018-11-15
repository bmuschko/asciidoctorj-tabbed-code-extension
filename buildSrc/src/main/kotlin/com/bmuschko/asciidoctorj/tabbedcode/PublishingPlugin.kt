package com.bmuschko.asciidoctorj.tabbedcode

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import java.text.SimpleDateFormat
import java.util.*

class PublishingPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyPublishingPlugin()
        configurePublishingExtension()
        configureBintrayExtension()
    }

    private
    fun Project.applyPublishingPlugin() {
        apply<MavenPublishPlugin>()
        apply<BintrayPlugin>()
    }

    private
    fun Project.configurePublishingExtension() {
        val sourcesJar: Jar by tasks.getting
        val javadocJar: Jar by tasks.getting

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(javadocJar)

                    pom {
                        name.set("AsciidoctorJ tabbed code extension")
                        description.set("An AsciidoctorJ extension for rendering code on multiple tabs.")
                        url.set("https://github.com/bmuschko/asciidoctorj-tabbed-code-extension")
                        inceptionYear.set("2018")

                        scm {
                            url.set("https://github.com/bmuschko/asciidoctorj-tabbed-code-extension")
                            connection.set("scm:https://bmuschko@github.com/bmuschko/asciidoctorj-tabbed-code-extension.git")
                            developerConnection.set("scm:git://github.com/bmuschko/asciidoctorj-tabbed-code-extension.git")
                        }

                        licenses {
                            license {
                                name.set("The Apache Software License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                distribution.set("repo")
                            }
                        }

                        developers {
                            developer {
                                id.set("bmuschko")
                                name.set("Benjamin Muschko")
                                url.set("https://github.com/bmuschko")
                            }
                        }
                    }
                }
            }
        }
    }

    private
    fun Project.configureBintrayExtension() {
        configure<BintrayExtension> {
            user = resolveProperty("BINTRAY_USER", "bintrayUser")
            key = resolveProperty("BINTRAY_KEY", "bintrayKey")
            setPublications("mavenJava")
            publish = true

            pkg(closureOf<BintrayExtension.PackageConfig> {
                repo = "maven"
                name = "asciidoctorj-tabbed-code-extension"
                desc = "An AsciidoctorJ extension for rendering code on multiple tabs."
                websiteUrl = "https://github.com/bmuschko/${project.name}"
                issueTrackerUrl = "https://github.com/bmuschko/${project.name}/issues"
                vcsUrl = "https://github.com/bmuschko/${project.name}.git"
                setLicenses("Apache-2.0")
                setLabels("asciidoc", "asciidoctor", "extension")
                publicDownloadNumbers = true
                githubRepo = "bmuschko/${project.name}"

                version(closureOf<BintrayExtension.VersionConfig> {
                    released = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(Date())
                    vcsTag = "v${project.version}"

                    gpg(closureOf<BintrayExtension.GpgConfig> {
                        sign = true
                        passphrase = resolveProperty("GPG_PASSPHRASE", "gpgPassphrase")
                    })
                })
            })
        }
    }

    private
    fun Project.resolveProperty(envVarKey: String, projectPropKey: String): String? {
        val propValue = System.getenv()[envVarKey]

        if(propValue != null) {
            return propValue
        }

        return findProperty(projectPropKey).toString()
    }
}