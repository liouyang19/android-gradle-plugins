import com.android.build.api.dsl.LibraryExtension
import com.taisau.android.buildllogic.libs
import com.taisau.android.gradle.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				apply(AndroidLibraryConventionPlugin::class.java)
				apply(AndroidKotlinConventionPlugin::class.java)
				apply("org.jetbrains.kotlin.plugin.compose")
			}
			
			val extension = extensions.getByType<LibraryExtension>()
			configureAndroidCompose(extension)
			
			dependencies {
				// Add Material 3 Compose
				add("implementation", libs.findLibrary("androidx.compose.material3").get())
			}
		}
	}
}