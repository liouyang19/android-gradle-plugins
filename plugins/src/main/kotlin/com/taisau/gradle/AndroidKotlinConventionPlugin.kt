package com.taisau.gradle

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidKotlinConventionPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				apply("org.jetbrains.kotlin.android")
			}

			pluginManager.withPlugin("com.android.application") {
				extensions.configure<ApplicationExtension> {
					configureKotlinAndroid(this)
				}
			}
			
			pluginManager.withPlugin("com.android.library") {
				extensions.configure<LibraryExtension> {
					configureKotlinAndroid(this)
				}
			}
		}
	}



}