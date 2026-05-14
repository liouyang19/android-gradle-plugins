pluginManagement {
    repositories {
        mavenLocal()
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven { setUrl("https://www.jitpack.io") }
        mavenCentral()
    }
}

rootProject.name = "Android-Gradle-Plugins"
include(":version-catalog")
include(":plugins")

