plugins {
	`kotlin-dsl`
    `java-gradle-plugin`
     alias(libs.plugins.spotless)
    alias(libs.plugins.android.lint)
}


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
    compileOnly(libs.licensee.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    lintChecks(libs.androidx.lint.gradle)
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
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("android.application.compose") {
            id = "com.taisau.android.plugin.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("android.library") {
            id = "com.taisau.android.plugin.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        
        register("android.library.compose") {
            id = "com.taisau.android.plugin.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("android.feature") {
            id = "com.taisau.android.plugin.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        
        register("android.hilt") {
            id = "com.taisau.android.plugin.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        
        register("android.test"){
            id = "com.taisau.android.plugin.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        
        register("android.room") {
            id = "com.taisau.android.plugin.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
      
        register("android.lint") {
            id = "com.taisau.android.plugin.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
     
        
        register("jvm.kotlin"){
            id = "com.taisau.jvm.plugin.kotlin"
            displayName = "Kotlin plugin for JVM projects"
            implementationClass = "JvmKotlinConventionPlugin"
        }
        register("kmp.kotlin") {
            id = "com.taisau.kmp.plugin.kotlin"
            displayName = "Kotlin plugin for KMP projects"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        
        register("cmp.compose") {
            id = "com.taisau.cmp.plugin.compose"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        
        register("dokka") {
            id = "com.taisau.plugin.dokka"
            displayName = "Taisau Dokka plugin"
            implementationClass = "TaisauDokkaPlugin"
        }
        register("root") {
            id = "com.taisau.plugin.root"
            implementationClass = "RootConventionPlugin"
        }
    }
}

