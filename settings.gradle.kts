import org.gradle.internal.os.OperatingSystem

pluginManagement{
    repositories{
        mavenLocal()
        gradlePluginPortal()
        val frcYear: String = "2019"
        val frcHome: File
        if(OperatingSystem.current().isWindows) {
            var publicFolder: String? = System.getenv("PUBLIC")
            if (publicFolder == null)
                publicFolder = "C:\\Users\\Public"
            frcHome = File(publicFolder, "frc${frcYear}")
        } else {
            val userFolder = System.getProperty("user.home")
            frcHome = File(userFolder, "frc${frcYear}")
        }
        val frcHomeMaven = File(frcHome, "maven")
        maven{
            name = "frcHome"
            url = uri(frcHomeMaven.path)
        }
    }
}