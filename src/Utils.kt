// Utils.kt
package utils

import java.io.File
// function that makes a directory
fun mkdir(path: String) {
    val directory = File(path)
    // Create the directory and any necessary parent directories
    if (!directory.exists()) {
        directory.mkdirs()
    }
}
fun printc(message: String, colorCode: String) {
    // ANSI escape codes for text colors
    val reset = "\u001B[0m"
    val coloredMessage = "\u001B[$colorCode$message$reset" // Corrected line
    print(coloredMessage)
}

fun printlnc(message: String, colorCode: String) {
    // ANSI escape codes for text colors
    val reset = "\u001B[0m"
    val coloredMessage = "\u001B[$colorCode$message$reset" // Corrected line
    println(coloredMessage)
}

// funcion runs a command in bash
fun runCommand(command: String, stdout: Boolean = false) {
    val process = Runtime.getRuntime().exec(command)
    val output = process.inputStream.bufferedReader()
    if (stdout) {
        output.useLines { lines ->
            lines.forEach { println(it) }
        }
    }
    process.waitFor()
}

class ConfigLoader(private val filePath: String) {
    private val variables = mutableMapOf<String, String>()
    private val lists = mutableMapOf<String, List<String>>()

    init {
        if (File(filePath).exists()) {
            loadConfig()
        }
    }

    private fun loadConfig() {
        File(filePath).forEachLine { line ->
            if (line.startsWith("#") || line.isBlank()) return@forEachLine // Skip comments and empty lines
            val parts = line.split("=")
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                if (value.startsWith("[") && value.endsWith("]")) {
                    // It's a list
                    lists[key] = value.substring(1, value.length - 1).split(",").map { it.trim() }
                } else {
                    // It's a variable
                    variables[key] = value
                }
            }
        }
    }

    fun getVariable(key: String): String? = variables[key]

    fun getList(key: String): List<String>? = lists[key]
}

const val red = "\u001B[31m"
const val blue = "\u001B[34m"
const val green = "\u001B[32m"
const val yellow = "\u001B[33m"
const val white = "\u001B[37m"
const val black = "\u001B[30m"
const val magenta = "\u001B[35m"
const val cyan = "\u001B[36m"