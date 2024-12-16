package main

import utils.*
import java.io.File


fun build_run_jar(projectName: String?, ktfiles: List<String>?) {
    if (projectName == null || ktfiles == null) {
        printlnc("Error: projectName or ktfiles is null", red)
        return
    }
    
    printlnc("Project Name: $projectName", cyan)
    printlnc("Running Task: Compile", yellow)

    ktfiles.forEach { printlnc(it, green) }

    runCommand("kotlinc ${ktfiles.joinToString(" ")} -include-runtime -d .zeta/$projectName.jar -verbose", true)

    if (File(".zeta/$projectName.jar").exists()) {
        printlnc("Compiled successfully", green)
        printlnc("Running Task: Run", yellow)
        runCommand("kotlin .zeta/$projectName.jar", true)
        printlnc("Ran successfully", green)
    } else {
        printlnc("Compilation failed", red)
    }
}
fun build_native_image(projectName: String?, ktfiles: List<String>?) {
    if (projectName == null || ktfiles == null) {
        printlnc("Error: projectName or ktfiles is null", red)
        return
    }
    
    printlnc("Project Name: $projectName", cyan)
    printlnc("Running Task: Compile", yellow)

    ktfiles.forEach { printlnc(it, green) }


    runCommand("kotlinc ${ktfiles.joinToString(" ")} -include-runtime -d .zeta/$projectName.jar -verbose", true)

    if (File(".zeta/$projectName.jar").exists()) {
        printlnc("Compiled successfully", green)
        printlnc("Running Task: Compile to image.", yellow)
        runCommand("native-image --no-server -jar .zeta/$projectName.jar", true)
        if (File("$projectName").exists()) {
            printlnc("Compiled to image successfully", green)
        } else {
            printlnc("Failed to compile to image", red)
        }
    } else {
        printlnc("Compilation failed", red)
    }
}

fun init() {
    println("What should the project be called?")
    val projectName = readln()
    mkdir(".zeta/")
    mkdir("src")

    val configFile = File("zeta.cfg").apply {
        if (createNewFile()) {
            println("File zeta.cfg created successfully.")
            printWriter().use { out ->
                out.println("projectName = $projectName")
                out.println("ktfiles = [src/Main.kt]")
            }
        } else {
            println("File zeta.cfg already exists.")
        }
    }

    val ktfile = File("src/Main.kt").apply {
        if (createNewFile()) {
            println("File Main.kt created successfully.")
            printWriter().use { out ->
                out.println("fun main() {")
                out.println("    println(\"Hello, World!\")")
                out.println("}")
            }
        } else {
            println("File src/Main.kt already exists.")
        }
    }
}

fun main(args: Array<String>) {
    var configfile_warning = false
    // Create an instance of ConfigLoader with the path to your configuration file
    val configLoader = ConfigLoader("zeta.cfg") // Make sure to call loadConfig to populate the variables

    // Check if the configuration was loaded successfully
    val projectName = configLoader.getVariable("projectName")
    val ktfiles = configLoader.getList("ktfiles")

    if (projectName == null || ktfiles == null) {
        configfile_warning = true
        printlnc("Warning: Failed to load configuration file", yellow)
    }

    if (args.isNotEmpty() && args[0] == "run") {
        build_run_jar(projectName, ktfiles)
    } else if (args.isNotEmpty() && args[0] == "init") {
        init() 
    } else if (args.isNotEmpty() && args[0] == "native") {
        build_native_image(projectName, ktfiles)
    } else {
        printlnc("Error: Invalid argument", red)
        help()
    }
}