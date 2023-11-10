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

        //PokeKotlin Repository (wird nicht genutzt)
        //maven {
        //  url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "My Application"
include(":app")
