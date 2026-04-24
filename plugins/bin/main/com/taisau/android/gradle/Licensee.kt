package com.taisau.android.buildllogic

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.UnusedAction
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * 配置 Licensee 许可证检查工具
 * 
 * 为项目应用并配置 Licensee 插件，用于检查和验证项目依赖的开源许可证。
 * 该函数会允许特定的开源许可证类型和自定义许可证 URL，确保项目使用的依赖符合法律要求。
 * 
 * 允许的许可证类型：
 * - Apache-2.0：Apache License 2.0
 * - MIT：MIT License
 * - BSD-2-Clause：BSD 2-Clause "Simplified" License
 * - BSD-3-Clause：BSD 3-Clause "New" or "Revised" License
 * 
 * 允许的自定义许可证 URL：
 * - Android SDK 许可协议
 * - Open Source Licenses (MIT)
 * - moko-permissions 项目许可证
 * - MaterialKolor 项目许可证
 * - Amazon Program Materials License
 */
fun Project.configureLicensee() {
	with(pluginManager) {
		apply("app.cash.licensee")
	}
	
	configure<LicenseeExtension> {
		allow("Apache-2.0")
		allow("MIT")
		allow("BSD-2-Clause")
		allow("BSD-3-Clause")
		allowUrl("https://developer.android.com/studio/terms.html")
		allowUrl("https://opensource.org/licenses/MIT")
		allowUrl("https://github.com/icerockdev/moko-permissions/blob/master/LICENSE.md")
		allowUrl("https://github.com/jordond/materialkolor/blob/master/LICENSE")
		allowUrl("https://developer.amazon.com/support/legal/pml")
		
		unusedAction(UnusedAction.IGNORE)
	}
}