package com.taisau.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				apply(AndroidLibraryComposeConventionPlugin::class.java)
				apply(AndroidHiltConventionPlugin::class.java)
			}
			
			dependencies {
				add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
				// The above library depends on the following libraries, but to keep them in the
				// newest version, we add them here as well.
				add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
				add("implementation", libs.findLibrary("androidx.lifecycle.viewModel.compose").get())
			}
		}
	}
}