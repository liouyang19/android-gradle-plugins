# AGENTS.md - Android Gradle Plugins

## Project Overview

This is an Android Gradle Plugins repository containing custom Gradle convention plugins for Android projects. The project manages Android dependency versions centrally via a version catalog.

### Modules

- `plugins/` - Custom Gradle convention plugins (Kotlin DSL)
- `version-catalog/` - Version catalog for publishing
- `sample/` - Sample Android app demonstrating plugin usage

---

## Build Commands

### Gradle Wrapper

```bash
./gradlew          # Unix/Linux
./gradlew.bat      # Windows
```

### Build & Test

| Command | Description |
|---------|-------------|
| `./gradlew build` | Full build of all projects |
| `./gradlew plugins:build` | Build plugins module |
| `./gradlew plugins:test` | Run plugins tests |
| `./gradlew plugins:check` | Run all checks for plugins |
| `./gradlew plugins:assemble` | Assemble plugins outputs |

### Sample App Commands

| Command | Description |
|---------|-------------|
| `./gradlew sample:assemble` | Assemble sample app |
| `./gradlew sample:test` | Run all sample unit tests |
| `./gradlew sample:testDebugUnitTest` | Run debug unit tests |
| `./gradlew sample:testReleaseUnitTest` | Run release unit tests |
| `./gradlew sample:assembleDebugUnitTest` | Assemble debug unit tests |
| `./gradlew sample:assembleReleaseUnitTest` | Assemble release unit tests |

### Running a Single Test

```bash
# Single test class
./gradlew plugins:test --tests "TestClassName"

# Single test method
./gradlew plugins:test --tests "TestClassName.testMethod"

# Sample app single test
./gradlew sample:testDebugUnitTest --tests "TestClassName.testMethod"
```

### Lint Commands

| Command | Description |
|---------|-------------|
| `./gradlew sample:lint` | Run lint on default variant |
| `./gradlew sample:lintDebug` | Run lint on debug variant |
| `./gradlew sample:lintRelease` | Run lint on release variant |
| `./gradlew sample:lintFix` | Auto-fix lint issues |
| `./gradlew sample:lintFixDebug` | Auto-fix debug lint issues |
| `./gradlew sample:lintFixRelease` | Auto-fix release lint issues |
| `./gradlew sample:updateLintBaseline` | Update lint baseline |

### Other Useful Commands

```bash
./gradlew clean                    # Clean all build directories
./gradlew dependencies             # Show all dependencies
./gradlew sample:androidDependencies  # Show Android dependencies
./gradlew sample:signingReport     # Show signing info
./gradlew sample:sourceSets        # Show source sets
./gradlew projects                 # Show project structure
```

---

## Code Style Guidelines

### Language & Version

- **Gradle**: Kotlin DSL (`.gradle.kts` files)
- **Plugin Code**: Kotlin
- **Java Version**: Java 21 for plugins, Java 11 for sample apps
- **Kotlin Version**: 2.3.10
- **AGP Version**: 8.13.2

### Project Structure

```
plugins/src/main/kotlin/com/taisau/android/buildllogic/
├── AndroidApplicationConventionPlugin.kt
├── AndroidApplicationComposeConventionPlugin.kt
├── AndroidLibraryConventionPlugin.kt
├── AndroidComposeConventionPlugin.kt
├── AndroidLintConventionPlugin.kt
├── AndroidRoomConventionPlugin.kt
├── HiltConventionPlugin.kt
└── KotlinAndroid.kt
```

### Conventions

#### Plugin Naming

- Convention plugins follow pattern: `AndroidXxxConventionPlugin`
- Custom plugin IDs follow pattern: `taisau.android.xxx`

#### Kotlin Conventions

- Use `internal` for non-public API functions
- Use `with()` scope for clean Gradle configuration
- Use `named()` for accessing version catalog entries

```kotlin
// Good example
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    commonExtension.apply { ... }
}
```

#### Imports

```kotlin
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
```

### Version Management

- All dependency versions MUST be defined in `gradle/libs.versions.toml`
- Access versions via version catalog, not hardcoded

```kotlin
// Good - use version catalog
val targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()

// Bad - hardcoded version
val targetSdk = 36
```

### Error Handling

- Use `with(target)` scope for applying plugins to projects
- Use `when` with `pluginManager.hasPlugin()` for conditional configuration
- Fail fast with clear error messages for misconfiguration

### Formatting

- 4-space indentation (Kotlin standard)
- Follow existing code style in the repository
- Keep Gradle configuration blocks properly indented

---

## Dependency Management

### Version Catalog Structure

The version catalog (`gradle/libs.versions.toml`) contains:

- `[versions]` - Version numbers
- `[libraries]` - Library definitions
- `[plugins]` - Plugin definitions

### Adding New Dependencies

1. Add version to `[versions]` section
2. Add library to `[libraries]` section
3. Use `alias()` in build files:

```kotlin
plugins {
    alias(libs.plugins.android.application)
}
```

---

## Repository Configuration

### Mirrors

The project uses Chinese mirror repositories for faster downloads:

- Huawei Cloud: `https://mirrors.huaweicloud.com/repository/maven`
- Aliyun: `https://maven.aliyun.com/repository/*`
- Tencent Cloud: `https://mirrors.cloud.tencent.com`

### Cache

Maven local is prioritized for plugin caching.

---

## Testing Convention Plugins

When testing convention plugins, ensure:

1. Use `gradle.properties` or `local.properties` for local settings
2. Test against the `sample` app to verify plugin behavior
3. Run `./gradlew plugins:check` before committing

---

## Additional Notes

- This is a Gradle plugin project, not a typical Android app
- The `plugins` module uses `kotlin-dsl` and `java-gradle-plugin`
- Custom plugins are registered in `plugins/build.gradle.kts`
- No existing lint rules files or code style configs in repository
