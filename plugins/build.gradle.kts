plugins {
	`kotlin-dsl`
    `maven-publish`
    `version-catalog`
    `java-gradle-plugin`
    alias(libs.plugins.spotless)
    alias(libs.plugins.android.lint)
}

apply(from = "../gradle/git-tag-version.gradle.kts")

val versionNameFromTags: String by extra


java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

dependencies {
	compileOnly(libs.android.gradlePlugin)
	compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)

    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    lintChecks(libs.androidx.lint.gradle)
    
    implementation(libs.licensee.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}



gradlePlugin {
    plugins {
        register("android.application") {
            id = "com.taisau.android.plugin.android.application"
            displayName = "Standalone Application configuration"
            implementationClass = "com.taisau.gradle.AndroidApplicationConventionPlugin"
        }
        register("android.application.compose") {
            id = "com.taisau.android.plugin.android.application.compose"
            implementationClass = "com.taisau.gradle.AndroidApplicationComposeConventionPlugin"
        }
        register("android.library") {
            id = "com.taisau.android.plugin.library"
            implementationClass = "com.taisau.gradle.AndroidLibraryConventionPlugin"
        }
        
        register("android.library.compose") {
            id = "com.taisau.android.plugin.library.compose"
            implementationClass = "com.taisau.gradle.AndroidLibraryComposeConventionPlugin"
        }
        register("android.feature") {
            id = "com.taisau.android.plugin.feature"
            implementationClass = "com.taisau.gradle.AndroidFeatureConventionPlugin"
        }
        
        register("android.hilt") {
            id = "com.taisau.android.plugin.hilt"
            implementationClass = "com.taisau.gradle.AndroidHiltConventionPlugin"
        }
   
        
        register("android.room") {
            id = "com.taisau.android.plugin.room"
            implementationClass = "com.taisau.gradle.AndroidRoomConventionPlugin"
        }
      
        register("android.lint") {
            id = "com.taisau.android.plugin.lint"
            implementationClass = "com.taisau.gradle.AndroidLintConventionPlugin"
        }
        
        register("android.kotlin") {
            id = "com.taisau.android.plugin.kotlin"
            implementationClass = "com.taisau.gradle.AndroidKotlinConventionPlugin"
        }
        
        register("jvm.kotlin"){
            id = "com.taisau.jvm.plugin.kotlin"
            displayName = "Kotlin plugin for JVM projects"
            implementationClass = "com.taisau.gradle.JvmKotlinConventionPlugin"
        }
        register("kmp.kotlin") {
            id = "com.taisau.kmp.plugin.kotlin"
            displayName = "Kotlin plugin for KMP projects"
            implementationClass = "com.taisau.gradle.KotlinMultiplatformConventionPlugin"
        }
        
        register("cmp.compose") {
            id = "com.taisau.cmp.plugin.compose"
            implementationClass = "com.taisau.gradle.ComposeMultiplatformConventionPlugin"
        }
        
        register("dokka") {
            id = "com.taisau.plugin.dokka"
            displayName = "Taisau Dokka plugin"
            implementationClass = "com.taisau.gradle.TaisauDokkaPlugin"
        }
        register("root") {
            id = "com.taisau.plugin.root"
            implementationClass = "com.taisau.gradle.RootConventionPlugin"
        }
    }
}


publishing {
    publications {
        // 为每个插件创建单独的发布（JitPack 需要）
        withType<MavenPublication>().configureEach {
            groupId = "com.taisau.android.plugin"
            version = versionNameFromTags
            if (name == "pluginMaven") {
                artifactId = "application.compose"
            }
            pom {
                name.set("Taisau Android Gradle Plugins")
                description.set("Collection of Android Gradle convention plugins")
                url.set("https://github.com/liouyang19/android-gradle-plugins")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
}

