package com.taisau.gradle

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.gradle.android.cache-fix")
            }
            extensions.configure<ApplicationExtension>{
                compileSdk {
                    version = release(Versions.COMPILE_SDK) {
                        minorApiLevel = 1
                    }
                }
                defaultConfig {
                    minSdk = Versions.MIN_SDK
                    targetSdk = Versions.TARGET_SDK
                    versionName = target.getVersionNameFromTags()
                    versionCode = target.getVersionCodeFromTags()
                    
                }
                buildFeatures {
                    buildConfig = true
                }
                
                signingConfigs {
                    val properties = Properties().also  {
                        it.load(FileInputStream(rootProject.file("keystore/key.properties")))
                    }
                    create("release") {
                        storeFile = properties.getProperty("storeFile")?.let { file(it) }
                        storePassword = properties.getProperty("storePassword")
                        keyAlias =  properties.getProperty("keyAlias")
                        keyPassword = properties.getProperty("keyPassword")
                    }
                }
                
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        signingConfig = signingConfigs.getByName("release")
                        // The proguard files will be used to generate the release.
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            file("proguard-rules.pro")
                        )
               
                        buildConfigField("String", "VERSION_NAME", "\"${getVersionNameFromTags()}\"")
                        buildConfigField("String", "VERSION_CODE", "\"${getVersionCodeFromTags()}\"")
                    }
                    
                    maybeCreate("beta")
                    getByName("beta"){
                        initWith(buildTypes.getByName("release"))
                        matchingFallbacks += "release"
                        isDebuggable = true
                    }
                    
                    getByName("debug") {
                        isMinifyEnabled = false
                        isDebuggable = true
                        buildConfigField("String", "VERSION_NAME", "\"debug\"")
                        buildConfigField("String", "VERSION_CODE", "\"${getVersionCodeFromTags()}\"")
                    }
                }
            
            }
            
        }
    }
}
