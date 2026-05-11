package com.taisau.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import java.io.File
import java.nio.file.Files
import java.time.Year

class TaisauDokkaPlugin : Plugin<Project> {
	private val org = "taisau"
	
	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				apply("org.jetbrains.dokka")
			}
			
			val dokkaExtension = extensions.getByType<DokkaExtension>()
			dokkaExtension.apply {
				// Set the version.
				moduleVersion.set(getVersionNameFromTags())
				// Set the output directory for the documentation.
				// GitHub Pages are using "docs" directory.
				basePublicationsDirectory.set(rootDir.resolve("docs"))
				
				val icon = getResourceAsFile("logo-icon.svg")
				val styles = getResourceAsFile("logo-style.css")
				
				// Set the footer message.
				pluginsConfiguration.named("html", DokkaHtmlPluginParameters::class.java) {
					val year = Year.now().value
					footerMessage.set("Copyright © 2022 - $year $org. All Rights Reserved.")
					customAssets.from(icon.absolutePath)
					customStyleSheets.from(styles.absolutePath)
				}
			}
			
			tasks.register("verifyDokkaResources") {
				group = "verification"
				description = "Verify that Dokka resources (icon and styles) can be loaded successfully"
				
				doLast {
					try {
						val iconFile = getResourceAsFile("logo-icon.svg")
						logger.lifecycle("✓ Icon resource loaded successfully")
						logger.lifecycle("  Path: ${iconFile.absolutePath}")
						logger.lifecycle("  Exists: ${iconFile.exists()}")
						logger.lifecycle("  Size: ${if (iconFile.exists()) iconFile.readBytes().size else 0} bytes")
						
						val stylesFile = getResourceAsFile("logo-style.css")
						logger.lifecycle("✓ Styles resource loaded successfully")
						logger.lifecycle("  Path: ${stylesFile.absolutePath}")
						logger.lifecycle("  Exists: ${stylesFile.exists()}")
						logger.lifecycle("  Size: ${if (stylesFile.exists()) stylesFile.readBytes().size else 0} bytes")
						
						logger.lifecycle("\n✓ All Dokka resources verified successfully!")
					} catch (e: Exception) {
						logger.error("✗ Failed to verify Dokka resources: ${e.message}")
						throw e
					}
				}
			}
		}
	}
	
	private fun getResourceAsFile(resourceName: String): File {
		val resource = this::class.java.getResourceAsStream("/dokka/$resourceName")
			?: throw IllegalStateException("Resource not found: $resourceName")
		val tempDir = Files.createTempDirectory("dokka").toFile()
		tempDir.deleteOnExit()
		val tempFile = File(tempDir, resourceName)
		
		resource.use { input ->
			tempFile.outputStream().use { output ->
				input.copyTo(output)
			}
		}
		return tempFile
	}
	
}