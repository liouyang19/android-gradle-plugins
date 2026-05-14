fun gitVersionCodeProvider() = providers.provider {
	runCatching {
		val result = providers.exec {
			commandLine("git", "tag", "--list")
			isIgnoreExitValue = true
		}.standardOutput.asText.get()
		2 + result.split("\n").count { it.isNotEmpty() }
	}.getOrDefault(-1)
}

fun gitVersionNameProvider() = providers.provider {
	runCatching {
		val result = providers.exec {
			commandLine("git", "describe", "--tags", "--abbrev=0")
		}
		if (result.result.get().exitValue != 0) return@provider "local"
		result.standardOutput.asText.get().trim().split("%")[0]
	}.getOrDefault("local")
}

extra["versionCodeFromTags"] = gitVersionCodeProvider()
extra["versionNameFromTags"] = gitVersionNameProvider()
