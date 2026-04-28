package com.taisau.gradle

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        
        with(target) {
            with(pluginManager) {
                apply(AndroidApplicationConventionPlugin::class.java)
                apply(AndroidKotlinConventionPlugin::class.java)
                apply("org.jetbrains.kotlin.plugin.compose")
            }
          
            
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            
            dependencies {
                //material3
                add("implementation", libs.findLibrary("androidx.compose.material3").get())
            }
        }
    }
}
