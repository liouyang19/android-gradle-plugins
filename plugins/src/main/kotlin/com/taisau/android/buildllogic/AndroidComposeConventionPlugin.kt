package com.taisau.android.buildllogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.run {
                findByName("android") as? com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>
            }

            extension?.let {
                configureAndroidCompose(it)
            }
        }
    }
}
