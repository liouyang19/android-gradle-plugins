import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Android Lint 约定插件
 * 
 * 该插件用于统一配置 Android 项目的 Lint 代码检查规则。根据项目类型（Application、Library 或纯 Lint 项目）
 * 自动应用相应的配置，确保所有 Android 项目具有一致的代码质量检查标准。
 * 
 * 支持的插件类型：
 * - com.android.application：Android 应用项目
 * - com.android.library：Android 库项目
 * - com.android.lint：纯 Lint 检查项目
 */
class AndroidLintConventionPlugin : Plugin<Project> {
    /**
     * 应用插件配置到目标项目
     * 
     * 根据项目已应用的 Android 插件类型，选择性地配置相应的 Lint 选项。
     * 如果项目既不是 application 也不是 library，则应用默认的 lint 插件并配置。
     * 
     * @param target 要应用插件配置的目标 Gradle 项目
     */
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    configure<ApplicationExtension> { lint(Lint::configure) }
                
                pluginManager.hasPlugin("com.android.library") ->
                    configure<LibraryExtension> { lint(Lint::configure) }
                
                else -> {
                    apply(plugin = "com.android.lint")
                    configure<Lint>(Lint::configure)
                }
            }
        }
    }
    
}

/**
 * 配置 Lint 代码检查选项
 * 
 * 设置统一的 Lint 检查规则：
 * - 启用 XML 格式报告
 * - 启用 SARIF 格式报告（用于静态分析结果交换）
 * - 检查依赖项中的问题
 * - 禁用 GradleDependency 检查（避免强制更新依赖版本）
 */
private fun Lint.configure() {
    xmlReport = true
    sarifReport = true
    checkDependencies = true
    disable += "GradleDependency"
}
