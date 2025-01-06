pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    dependencyResolutionManagement {
        repositories {
            google()
            mavenCentral()
            maven("https://jitpack.io") // Add JitPack repository
        }
    }

}

rootProject.name = "Midterm Rachad Souaiby"
include(":app")
