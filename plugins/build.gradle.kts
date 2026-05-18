plugins {
	`kotlin-dsl`
    `maven-publish`
    `version-catalog`
    `java-gradle-plugin`
    alias(libs.plugins.spotless)
    alias(libs.plugins.android.lint)
}

apply(from = "../gradle/git-tag-version.gradle.kts")

val versionNameFromTags = extra["versionNameFromTags"] as String
version = versionNameFromTags

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
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
    implementation(libs.dokka.gradlePlugin)
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
    isAutomatedPublishing = true
    plugins {
        register("android.application") {
            id = "com.taisau.android.plugin.application"
            displayName = "Standalone Application configuration"
            implementationClass = "com.taisau.gradle.AndroidApplicationConventionPlugin"
        }
        register("android.application.compose") {
            id = "com.taisau.android.plugin.application.compose"
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
            id = "com.taisau.kotlin.plugin.kmp"
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
        // 对所有自动生成的发布（包括 Marker 和 Main）进行统一校准
        withType<MavenPublication>().configureEach {
            // 统一 Version，确保 Marker 指向的版本与主 Jar 一致
            version = project.version.toString()
            
            // 如果是主 Jar 发布 (java-gradle-plugin 默认创建的叫 pluginMaven)
            if (name == "pluginMaven") {
                groupId = "com.taisau.android.plugin"
                artifactId = "taisau-convention-plugins" // 所有的插件逻辑其实都在这一个 Jar 里
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

