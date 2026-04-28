package com.taisau.gradle
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaTarget

internal fun Project.configureKotlinAndroid(
	commonExtension: CommonExtension,
) {
	commonExtension.apply {
		compileOptions.apply {
			sourceCompatibility = Versions.JAVA_SOURCE_VERSION
			targetCompatibility = Versions.JAVA_TARGET_VERSION
		}
		
		configureKotlin<KotlinAndroidProjectExtension>()
	}
}

internal fun Project.configureKotlinJvm() {
	extensions.configure<JavaPluginExtension> {
		sourceCompatibility = Versions.JAVA_SOURCE_VERSION
		targetCompatibility = Versions.JAVA_TARGET_VERSION
	}
	
	configureKotlin<KotlinJvmProjectExtension>()
}

internal fun Project.configureKotlinKmp() {
	// KMP doesn't have a top-level JavaPluginExtension like a pure JVM project,
	// but we still want to ensure Java tasks (if withJava() is used) align versions.
	extensions.findByType<JavaPluginExtension>()?.apply {
		sourceCompatibility = Versions.JAVA_SOURCE_VERSION
		targetCompatibility = Versions.JAVA_TARGET_VERSION
	}
	
	configureKotlin<KotlinMultiplatformExtension>()
}


private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
	// Treat all Kotlin warnings as errors (disabled by default)
	// Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
	val warningsAsErrors: String? = project.findProperty("warningsAsErrors") as? String
	
	when (this) {
		is KotlinAndroidProjectExtension -> compilerOptions
		is KotlinJvmProjectExtension -> compilerOptions
		is KotlinMultiplatformExtension -> compilerOptions
		else -> TODO("Unsupported project extension $this ${T::class}")
	}.apply {
		allWarningsAsErrors.set(warningsAsErrors.toBoolean())
		
		languageVersion.set(Versions.KOTLIN_VERSION)
		apiVersion.set(Versions.KOTLIN_VERSION)
		optIn.add("kotlin.RequiresOptIn")
		optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
		optIn.add("kotlinx.coroutines.FlowPreview")
		// https://kotlinlang.org/docs/whatsnew23.html#explicit-backing-fields
		freeCompilerArgs.add("-Xexplicit-backing-fields")
		// https://kotlinlang.org/docs/whatsnew23.html#unused-return-value-checker
		freeCompilerArgs.add("-Xreturn-value-checker=full")
		
		if (this is KotlinJvmCompilerOptions) {
			jvmTarget.set(Versions.JVM_TARGET)
		}
		
		// In KMP, the top-level compilerOptions applies to all targets (iOS, JVM, etc).
		// However, some JVM-specific settings like jvmTarget only exist on JVM/Android targets.
		if (this is KotlinMultiplatformExtension) {
			targets.configureEach {
				if (this is KotlinWithJavaTarget<*, *>) {
					compilerOptions.jvmTarget.set(Versions.JVM_TARGET)
				}
			}
		}
	}
}