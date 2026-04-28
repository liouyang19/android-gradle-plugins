package com.taisau.gradle
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * 配置 Spotless 代码格式化工具
 * 
 * 为项目统一应用 Spotless 代码格式化配置，确保团队代码风格一致性。
 * 支持 Kotlin、Gradle Kotlin DSL 等文件格式的自动化检查和修复。
 * 
 * 主要功能：
 * - 自动格式化 Kotlin 代码（使用 ktfmt）
 * - 统一 Gradle 构建脚本格式
 * - 管理导入语句顺序和未使用的导入
 * - 处理行尾空格和文件结尾换行
 * - 支持跨平台行结束符统一
 */
internal fun Project.configureSpotlessForAndroid() {
	configureSpotlessCommon()
	extensions.configure<SpotlessExtension> {
		format("xml") {
			target("src/**/*.xml")
			// Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
			licenseHeaderFile(rootDir.resolve("spotless/copyright.xml"), "(<[^!?])")
			endWithNewline()
		}
	}
}

internal fun Project.configureSpotlessForJvm() {
	configureSpotlessCommon()
}

internal fun Project.configureSpotlessForRootProject() {
	apply(plugin = "com.diffplug.spotless")
	extensions.configure<SpotlessExtension> {
		kotlin {
			target("build-logic/convention/src/**/*.kt")
			ktlint(libs.findVersion("ktlint").get().requiredVersion).editorConfigOverride(
				mapOf("android" to "true"),
			)
			licenseHeaderFile(rootDir.resolve("spotless/copyright.kt"))
			endWithNewline()
		}
		format("kts") {
			target("*.kts")
			target("build-logic/*.kts")
			target("build-logic/convention/*.kts")
			// Look for the first line that doesn't have a block comment (assumed to be the license)
			licenseHeaderFile(rootDir.resolve("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
			endWithNewline()
		}
	}
}

private fun Project.configureSpotlessCommon() {
	apply(plugin = "com.diffplug.spotless")
	extensions.configure<SpotlessExtension> {
		kotlin {
			target("src/**/*.kt")
			ktlint(libs.findVersion("ktlint").get().requiredVersion).editorConfigOverride(
				mapOf("android" to "true"),
			)
			licenseHeaderFile(rootDir.resolve("spotless/copyright.kt"))
			endWithNewline()
		}
		format("kts") {
			target("*.kts")
			// Look for the first line that doesn't have a block comment (assumed to be the license)
			licenseHeaderFile(rootDir.resolve("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
			endWithNewline()
		}
	}
}