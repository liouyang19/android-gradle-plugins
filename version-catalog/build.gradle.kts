plugins {
    `version-catalog`
    `maven-publish`
}


catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

/**
 * 配置 Maven 发布设置
 * 定义发布构件的名称和来源组件
 */
publishing {
    publications {
        create<MavenPublication>("libs") {
            from(components["versionCatalog"])
            groupId = "com.github.liouyang19"
            artifactId = "android-gradle-plugins"
            artifactId = "version-catalog"
            version = "1.0.0"
            pom {
                name.set("Taisau project version catalog for Android")
                description.set("Taisau project version catalog for Android")
                url.set("https://github.com/liouyang19/android-gradle-plugins")
                packaging = "toml"
            }
        }
    }

    repositories {
//        maven {
//            val isSnapshot = version.toString().endsWith("SNAPSHOT")
//            // 注意：这里必须指定到具体的仓库名，不能只写到 /repository
//            url = if (isSnapshot) {
//                uri("http://maven.10000ss.cn/repository/maven-snapshots/")
//            } else {
//                uri("http://maven.10000ss.cn/repository/maven-releases/")
//            }
//            // 必须：允许非加密的 HTTP 协议发布
//            isAllowInsecureProtocol = true
//            // 提醒：如果发布报 401 错误，说明匿名权限仅限下载，你仍需在这里填入 credentials
//        }
        
//        maven {
//            //https://gitcode.com/api/v4/projects/[项目ID]/packages/maven
//            // 注意替换域名为你们私有 GitLab 的实际域名
//            url = uri("https://gitcode.com/api/v4/projects/ouyang_li%2Fandroid-gradle-plugins/packages/maven")
//            // 如果你的私有 GitLab 证书不是正规机构颁发的，建议加上这行防止 SSL 报错
//            isAllowInsecureProtocol = true
//            credentials {
//                // 固定字符串，必须是 "Private-Token"
//                // 而是使用你的真实用户名
//                username = providers.gradleProperty("GITCODE_USER").get()
//
//                // 使用个人访问令牌作为密码
//                password = providers.gradleProperty("GITCODE_USER").get()
//            }
//        }
    }
}

