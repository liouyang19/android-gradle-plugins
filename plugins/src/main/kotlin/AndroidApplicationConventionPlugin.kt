import com.taisau.android.buildllogic.configureLicensee
import com.taisau.android.gradle.configureAndroid
import com.taisau.android.gradle.configureAndroidLicensesTasks
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.gradle.android.cache-fix")
            }
            configureAndroid()
            configureLicensee()
            configureAndroidLicensesTasks()
        }
        
        
    }
}
