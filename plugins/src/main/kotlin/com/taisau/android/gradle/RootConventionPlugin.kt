package com.taisau.android.buildllogic
import org.gradle.api.Plugin
import org.gradle.api.Project


class RootConventionPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			configureSpotless()
		}
	}
}