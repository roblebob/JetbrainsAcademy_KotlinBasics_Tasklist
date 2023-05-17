package tasklist

fun main() {
    // write your code here

    var line = "";
    val tasks = mutableListOf<MutableList<String>>()
    while (true) {
        println("Input an action (add, print, end):")
        line = readln();
        line = line.trim()
        when (line.lowercase()) {
            "end" -> break
            "add" -> {
                println("Input a new task (enter a blank line to end):")
                val taskLines = mutableListOf<String>()
                while (true) {
                    line = readln()
                    line = line.trim()
                    if (line.isBlank()) {
                        break
                    }
                    taskLines.add(line)
                }

                if (taskLines.isEmpty()) {
                    println("The task is blank")
                    continue
                }

                tasks.add(taskLines)
            }
            "print" -> {
                if (tasks.isEmpty()) {
                    println("No tasks have been input")
                } else {
                    tasks.forEachIndexed { index, taskLines ->
                        run {
                            taskLines.forEachIndexed { i, s ->
                                println(if (i != 0) "   $s" else "${(index + 1).toString().padEnd(3, ' ')}$s")
                            }
                            println()
                        }
                    }
                }
            }
            else -> println("The input action is invalid")
        }
    }
    println("Tasklist exiting!")
}

