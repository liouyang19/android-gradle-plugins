val versionCode = runCatching {
	val proc = ProcessBuilder("git", "tag", "--list")
		.redirectErrorStream(true)
		.start()
	val output = proc.inputStream.bufferedReader().readText()
	proc.waitFor()
	2 + output.lines().count { it.isNotEmpty() }
}.getOrDefault(-1)

val versionName = runCatching {
	val proc = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
		.redirectErrorStream(true)
		.start()
	if (proc.waitFor() != 0) return@runCatching "local"
	proc.inputStream.bufferedReader().readText().trim().split("%")[0]
}.getOrDefault("local")

extra["versionCodeFromTags"] = versionCode
extra["versionNameFromTags"] = versionName
