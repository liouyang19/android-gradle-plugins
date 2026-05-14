package com.taisau.gradle
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaTarget


internal fun Project.configureKotlinAndroid(
	commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
	commonExtension.apply {
		compileOptions {
			sourceCompatibility = Versions.JAVA_SOURCE_VERSION
			targetCompatibility = Versions.JAVA_TARGET_VERSION
		}
	}

	extensions.configure<KotlinAndroidProjectExtension> {
		compilerOptions {
			jvmTarget.set(Versions.JVM_TARGET)
			freeCompilerArgs.addAll(
				"-Xexplicit-backing-fields",
				"-Xreturn-value-checker=full"
			)
			optIn.addAll(
				"kotlin.RequiresOptIn",
				"kotlinx.coroutines.ExperimentalCoroutinesApi",
				"kotlinx.coroutines.FlowPreview"
			)
		}
	}
}

internal fun Project.configureKotlinJvm() {
	extensions.configure<JavaPluginExtension> {
		sourceCompatibility = Versions.JAVA_SOURCE_VERSION
		targetCompatibility = Versions.JAVA_TARGET_VERSION
	}

	extensions.configure<KotlinJvmProjectExtension> {
		compilerOptions {
			languageVersion.set(Versions.KOTLIN_VERSION)
			apiVersion.set(Versions.KOTLIN_VERSION)
			jvmTarget.set(Versions.JVM_TARGET)
			freeCompilerArgs.addAll("-Xexplicit-backing-fields", "-Xreturn-value-checker=full")
		}
	}
}

internal fun Project.configureKotlinKmp() {
	extensions.findByType<JavaPluginExtension>()?.apply {
		sourceCompatibility = Versions.JAVA_SOURCE_VERSION
		targetCompatibility = Versions.JAVA_TARGET_VERSION
	}

	extensions.configure<KotlinMultiplatformExtension> {
		compilerOptions {
			languageVersion.set(Versions.KOTLIN_VERSION)
			apiVersion.set(Versions.KOTLIN_VERSION)
			freeCompilerArgs.addAll("-Xexplicit-backing-fields", "-Xreturn-value-checker=full")
		}

		targets.configureEach {
			if (this is KotlinWithJavaTarget<*, *>) {
				compilations.configureEach {
					compileTaskProvider.configure {
						compilerOptions {
							freeCompilerArgs.add("-jvm-target=${Versions.JVM_TARGET.target}")
						}
					}
				}
			}
		}
	}
}