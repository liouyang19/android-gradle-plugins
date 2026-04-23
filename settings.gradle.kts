pluginManagement {
    // 启用插件缓存
    repositories {
        // 优先使用本地缓存
        mavenLocal()
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven") }
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/huaweicloudsdk") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        // 其他必要仓库
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
        // 优先使用本地缓存
        mavenLocal()
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven") }
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/huaweicloudsdk") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        // 其他必要仓库
        maven { setUrl("https://www.jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Android-Gradle-Plugins"
include(":version-catalog")
include(":plugins")

