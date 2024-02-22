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
}

rootProject.name = "Pet Adoption"
include(":app")
include(":core:network")
include(":core:common")
include(":feature:auth:data")
include(":feature:auth:domain")
include(":feature:auth:ui")
include(":feature:home:data")
include(":feature:home:ui")
include(":feature:home:domain")
include(":feature:profile:ui")
include(":core:database")
include(":feature:profile:data")
include(":feature:profile:domain")
include(":feature:chat:domain")
include(":feature:chat:ui")
