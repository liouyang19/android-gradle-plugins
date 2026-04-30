plugins {
    `version-catalog`
    `maven-publish`
}

apply(from = "../gradle/git-tag-version.gradle.kts")
val versionNameFromTags: String by extra
group = "com.github.liouyang19"
version = versionNameFromTags

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

/**
 * 配置 Maven 发布设置
 * 定义发布构件的名称和来源组件
 */
publishing {
    publications {
        create<MavenPublication>("libs") {
            from(components["versionCatalog"])
            groupId = group.toString()
            artifactId = "version-catalog"
            version = project.version.toString()
            pom {
                name.set("Taisau project version catalog for Android")
                description.set("Taisau project version catalog for Android")
                url.set("https://github.com/liouyang19/android-gradle-plugins")
                packaging = "toml"
                
                licenses {
                    license {
                        name.set("BSD-3-Clause")
                        url.set("http://opensource.org/licenses/BSD-3-Clause")
                        distribution.set("repo")
                    }
                }
                
                scm {
                    url.set("https://github.com/liouyang19/android-gradle-plugins")
                    connection.set("scm:git@github.com:liouyang19/Android-Gradle-Plugins.git")
                    developerConnection.set("scm:git@github.com:liouyang19/Android-Gradle-Plugins.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "mavenLocal"
            url = uri("${System.getProperty("user.home")}/.m2/repository")
        }
    }
}

tasks.register("install") {
    dependsOn("publishToMavenLocal")
}

