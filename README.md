# silver-octo-couscous

## usage 
  - clone repo
  - Open a terminal (or command prompt) in your project directory and run the command:
    ```sh
    ./gradlew build
    ```
      If you are using Windows, replace ./gradlew with gradlew.bat.
      This command will build the project and create a JAR file in the build/libs directory of your project.
  - add code below to your build.gradle.kts:
    ```kotlin
      tasks.withType<KotlinCompile> {
      compilerOptions {
          freeCompilerArgs.add("-Xplugin=${project.rootDir}/build/libs/comp_plugin-1.0-SNAPSHOT.jar")
      }
    }  
    ```
