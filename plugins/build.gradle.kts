plugins {
	`kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.spotless)
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
	compilerOptions {
		jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
	}
}

dependencies {
	compileOnly(libs.android.gradlePlugin)
	compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.licensee.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "taisau.android.application"
            implementationClass = "com.taisau.android.buildllogic.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "taisau.android.application.compose"
            implementationClass = "com.taisau.android.buildllogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "taisau.android.library"
            implementationClass = "com.taisau.android.buildllogic.AndroidLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "taisau.android.room"
            implementationClass = "com.taisau.android.buildllogic.RoomConventionPlugin"
        }
        register("androidHilt") {
            id = "taisau.android.hilt"
            implementationClass = "com.taisau.android.buildllogic.HiltConventionPlugin"
        }
        register("androidLint") {
            id = "taisau.android.lint"
            implementationClass = "com.taisau.android.buildllogic.LintConventionPlugin"
        }
        register("androidCompose") {
            id = "taisau.android.compose"
            implementationClass = "com.taisau.android.buildllogic.AndroidComposeConventionPlugin"
        }
    }
}

