package com.taisau.gradle

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

class AndroidKotlinConventionPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			extensions.findByType<LibraryExtension>()?.apply {
				configureKotlinAndroid(this)
			}
			extensions.findByType<ApplicationExtension>()?.apply {
				configureKotlinAndroid(this)
			}
		}
	}
}