
job("Build, run tests, publish") {
    container(displayName = "Run publish script", image = "maven:3-openjdk-17-slim") {
        // url of a Space Packages repository
        env["REPOSITORY_URL"] = "https://maven.pkg.jetbrains.space/reformed/p/rc/reformed-repo"

        shellScript {
            content = """
                echo Build and run tests...
                mvn clean install
                echo Publish artifacts...
                mvn deploy -s settings.xml \
                    -DrepositoryUrl=${'$'}REPOSITORY_URL \
                    -DspaceUsername=${'$'}JB_SPACE_CLIENT_ID \
                    -DspacePassword=${'$'}JB_SPACE_CLIENT_SECRET
            """
        }
    }
}