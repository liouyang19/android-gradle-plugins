package com.taisau.android.gradle
import org.gradle.api.Project
import java.time.ZoneId
import java.time.ZonedDateTime
private const val DEFAULT_CODE = 1
private const val DEFAULT_NAME = "0.0.0"

/**
 * This method returns the version code based on the current date (YYMM) and number of git revisions.
 * The format is YYMMxxxxx where x is a number of git revisions.
 */
fun Project.getVersionCodeFromTags(): Int {
	val numberOfCommits: Int
	try {
		numberOfCommits = providers
			.exec { commandLine("git", "rev-list", "--count", "HEAD") }
			.standardOutput
			.asText.get().trim()
			.toInt()
	} catch (e: Exception) {
		e.printStackTrace()
		return DEFAULT_CODE
	}
	val now = ZonedDateTime.now(ZoneId.of("UTC"))
	val year = now.year % 100
	val month = String.format("%02d", now.monthValue)
	val day = String.format("%02d", now.dayOfMonth)
	val revisions = String.format("%02d", numberOfCommits % 100)
	val version = "$year$month$day$revisions".toInt()
	return version
}

/**
 * This method returns the version name from the latest git tag.
 */
fun Project.getVersionNameFromTags(): String {
	val latestTag: String
	try {
		latestTag = providers
			.exec { commandLine("git", "describe", "--tags", "--abbrev=0") }
			.standardOutput
			.asText.get().trim()
	} catch (e: Exception) {
		e.printStackTrace()
		return DEFAULT_NAME
	}
	// Version may have % to add additional information
	return latestTag.split("%")[0]
}