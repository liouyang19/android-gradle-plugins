# AGENTS.md - Android Gradle Plugins

## Project Overview

Custom Gradle convention plugins for Android/KMP/Compose Multiplatform projects. Dependency versions are managed centrally via a version catalog (`gradle/libs.versions.toml`), published separately via the `version-catalog` module.

### Modules

- `plugins/` - Custom Gradle convention plugins (Kotlin, 25 source files)
- `version-catalog/` - Publishes the version catalog for external consumption
- `build.gradle.kts` - Root project (minimal, just enables version-catalog publishing)

---

## Build & Test Commands

### Gradle Wrapper

```bash
./gradlew          # Unix/Linux
./gradlew.bat      # Windows
```

### Common Commands

| Command | Description |
|---------|-------------|
| `./gradlew plugins:build` | Build plugins module |
| `./gradlew plugins:test` | Run all plugins tests |
| `./gradlew plugins:check` | Run all checks (test + lint) |
| `./gradlew plugins:assemble` | Assemble plugins outputs |
| `./gradlew plugins:publishToMavenLocal` | Publish plugins to local Maven |

### Running a Single Test

```bash
# Single test class
./gradlew plugins:test --tests "TestClassName"

# Single test method
./gradlew plugins:test --tests "TestClassName.testMethod"
```

### Lint & Code Quality

| Command | Description |
|---------|-------------|
| `./gradlew spotlessApply` | Auto-format Kotlin/kts code (ktlint) |
| `./gradlew spotlessCheck` | Check formatting without applying |
| `./gradlew plugins:check` | Run all checks including Spotless |

### Other Commands

```bash
./gradlew clean                    # Clean all build directories
./gradlew projects                 # Show project structure
./gradlew dependencies             # Show all dependencies
./gradlew version-catalog:publishToMavenLocal  # Publish version catalog
```

---

## Code Style Guidelines

### Language & Version

- **Gradle**: Kotlin DSL (`.gradle.kts` files)
- **Plugin Code**: Kotlin
- **Java Version**: JDK 21 (toolchain auto-provisioned via `gradle-daemon-jvm.properties`)
- **Kotlin Version**: 2.3.20
- **AGP Version**: 9.1.0
- **Min SDK**: 26, **Target/Compile SDK**: 36

### Project Structure

```
plugins/src/main/kotlin/com/taisau/gradle/
├── AndroidApplicationConventionPlugin.kt
├── AndroidApplicationComposeConventionPlugin.kt
├── AndroidFeatureConventionPlugin.kt
├── AndroidHiltConventionPlugin.kt
├── AndroidKotlinConventionPlugin.kt
├── AndroidLibraryComposeConventionPlugin.kt
├── AndroidLibraryConventionPlugin.kt
├── AndroidLicensesHandler.kt
├── AndroidLintConventionPlugin.kt
├── AndroidRoomConventionPlugin.kt
├── AssetCopyTask.kt
├── Compose.kt
├── ComposeMultiplatformConventionPlugin.kt
├── GitVersion.kt
├── IosLicensesHandler.kt
├── Java.kt
├── JvmKotlinConventionPlugin.kt
├── Kotlin.kt
├── KotlinMultiplatformConventionPlugin.kt
├── Licensee.kt
├── RootConventionPlugin.kt
├── Spotless.kt
├── TaisauDokkaPlugin.kt
├── VersionCatalog.kt
└── Versions.kt
```

### Plugin Naming

```kotlin
// ID pattern: com.taisau.{android|jvm|kmp|cmp}.plugin.{name}
// Implementation: com.taisau.gradle.XxxPlugin

register("android.application") {
    id = "com.taisau.android.plugin.android.application"
    implementationClass = "com.taisau.gradle.AndroidApplicationConventionPlugin"
}
```

Registered plugins: `android.application`, `android.application.compose`, `android.library`, `android.library.compose`, `android.feature`, `android.hilt`, `android.room`, `android.lint`, `android.kotlin`, `jvm.kotlin`, `kmp.kotlin`, `cmp.compose`, `dokka`, `root`.

### Code Conventions

- **Package**: `com.taisau.gradle`
- Use `internal` for non-public API utility functions (e.g., `configureKotlinAndroid`, `configureSpotlessForAndroid`)
- Use `with(target)` scope functions for applying plugins to projects
- Use `extensions.configure<T> { }` for configuring Gradle extensions
- Use `extensions.getByType<T>()` for retrieving extensions
- Access version catalog via the `libs` extension property:

```kotlin
// VersionCatalog.kt defines:
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Usage:
dependencies {
    add("implementation", libs.findLibrary("androidx-compose-material3").get())
}
```

- Use `compileOnly` for Gradle plugin dependencies (AGP, Kotlin GP, KSP, Hilt, Room, Compose)
- Use `pluginManager.apply("plugin.id")` or `apply(XxxPlugin::class.java)` to apply plugins
- Use `pluginManager.hasPlugin("...")` in `when` blocks for conditional configuration
- Version constants defined in `Versions.kt` object (COMPILE_SDK, MIN_SDK, KOTLIN_VERSION, JVM_TARGET, etc.)
- Git-based versioning via `getVersionCodeFromTags()` / `getVersionNameFromTags()` extension functions

### Imports

```kotlin
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.dependencies
```

### Documentation

- KDoc comments used for public API, utility functions, and plugin classes
- Comments are bilingual (Chinese descriptions for business logic, English for technical config)
- Private extension functions often defined at bottom of file (e.g., `private fun Project.java(...)`)

### Spotless / Formatting

- Spotless with ktlint enforces code style (configured in `Spotless.kt`)
- Copyright headers from `spotless/copyright.kt`, `spotless/copyright.kts`, `spotless/copyright.xml`
- Run `./gradlew spotlessApply` to auto-format before committing

### Error Handling

- Fail fast with `?: throw IllegalStateException(...)` for missing resources
- Use `runCatching { }.getOrNull()` for operations that may fail
- Print stack traces with `e.printStackTrace()` in catch blocks for Git operations
- Validates plugins strictly: `enableStricterValidation = true`, `failOnWarning = true`

---

## Dependency Management

### Version Catalog

All dependency versions in `gradle/libs.versions.toml`:

```
[versions]    # Version numbers
[libraries]   # Library GAV coordinates
[plugins]     # Plugin coordinates
```

### Adding New Dependencies

1. Add version to `[versions]` section
2. Add library to `[libraries]` section
3. Access via `libs.findLibrary("name").get()` in convention plugins

---

## Repository Configuration

### Mirrors

Configured in `settings.gradle.kts`:
- Huawei Cloud: `https://mirrors.huaweicloud.com/repository/maven`
- Aliyun: `https://maven.aliyun.com/repository/*`
- Google, Maven Central, JitPack, Gradle Plugin Portal

### Cache

Maven local is prioritized for plugin caching.

---

## Publishing

- Plugins published via `maven-publish` and `java-gradle-plugin` (group: `com.taisau.android.plugin`)
- Version catalog published with group `com.github.liouyang19`, artifact `version-catalog`
- Version derived from git tags via `gradle/git-tag-version.gradle.kts`
- JitPack CI: `./gradlew publishToMavenLocal -x test` (configured in `jitpack.yml`)
