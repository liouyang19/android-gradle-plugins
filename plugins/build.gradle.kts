plugins {
	`kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
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
    
    implementation(libs.android.cacheFix.gradlePlugin)
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
            print(libs.plugins.taisau.android.application)
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
    repositories {
        mavenLocal() // 方便本地调试
    }
    // 统一配置所有自动生成的发布任务
    publications.withType<MavenPublication>().configureEach {
        groupId = "com.github.liouyang19" // 如果发到 JitPack，建议用这个
        version = versionNameFromTags
    }
}

