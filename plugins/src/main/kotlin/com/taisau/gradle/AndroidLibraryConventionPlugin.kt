package com.taisau.gradle

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.gradle.android.cache-fix")
                apply(AndroidLintConventionPlugin::class.java)
            }
         
            
            extensions.configure<LibraryExtension> {
                compileSdk {
                    version = release(Versions.COMPILE_SDK) {
                        minorApiLevel = 1
                    }
                }
                
                defaultConfig {
                    minSdk = Versions.MIN_SDK
                }
                
                buildFeatures {
                    buildConfig = true
                }
                
            }
            
//            dependencies {
//                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())
//                "testImplementation"(libs.findLibrary("kotlin.test").get())
//                "testImplementation"(libs.findLibrary("junit").get())
//                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
//            }
            
         
        }
    }
}
