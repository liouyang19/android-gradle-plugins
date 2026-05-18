val isJitPack = System.getenv("JITPACK") != null

pluginManagement {
    repositories {
        mavenLocal()
        if (!isJitPack) {
            // 国内镜像（仅本地开发用，JitPack 环境不可达）
            maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
            maven { setUrl("https://maven.aliyun.com/repository/central") }
            maven { setUrl("https://maven.aliyun.com/repository/google") }
        }
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
        if (!isJitPack) {
            // 国内镜像（仅本地开发用，JitPack 环境不可达）
            maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
            maven { setUrl("https://maven.aliyun.com/repository/central") }
            maven { setUrl("https://maven.aliyun.com/repository/google") }
        }
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

