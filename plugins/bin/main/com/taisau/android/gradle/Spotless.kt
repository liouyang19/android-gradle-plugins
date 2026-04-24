package com.taisau.android.buildllogic

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import org.gradle.api.Project
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
fun Project.configureSpotless() {
	with(pluginManager) {
		apply("com.diffplug.spotless")
	}
	
	spotless {
		val ktlintVersion = libs.findLibrary("ktlint").get().get().version
		kotlin {
			target("src/**/*.kt")
			ktlint(ktlintVersion)
		}
		
		kotlinGradle {
			target("*.kts")
			ktlint(ktlintVersion)
		}
		
	}
	
	
//    pluginManager.apply("com.diffplug.spotless")
//
//    extensions.configure<SpotlessExtension> {
//        kotlin {
//            target("**/*.kt")
//            targetExclude("**/build/**/*.kt")
//            ktfmt()
//            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
//        }
//
//        format("kts") {
//            target("**/*.kts")
//            targetExclude("**/build/**/*.kts")
//            trimTrailingWhitespace()
//            indentWithSpaces()
//            endWithNewline()
//        }
//
//        lineEndings = LineEnding.PLATFORM_NATIVE
//    }
}

private fun Project.spotless(action: SpotlessExtension.() -> Unit) = extensions.configure<SpotlessExtension>(action)