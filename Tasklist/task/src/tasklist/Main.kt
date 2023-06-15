package tasklist

import kotlinx.datetime.*


fun main() {
    // write your code here

    var line = ""
    val tasks = mutableListOf<Task>()
    while (true) {
        println("Input an action (add, print, end):")
        line = readln().trim()

        when (line.lowercase()) {
            "end" -> break
            "add" -> {
                val task = Task()

                // priority
                while (true) {
                    println("Input the task priority (C, H, N, L):")
                    val string = readln().trim()
                    if (string.lowercase() in listOf("c", "h", "n", "l")) {
                        task.priority = string[0].uppercaseChar()
                        break;
                    }
                }

                // date
                while (true) {
                    println("Input the date (yyyy-mm-dd):")
                    val strings = readln().trim().split("-")
                    val date = try {
                        LocalDate(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
                    } catch (e: Exception) {
                        println("The input date is invalid")
                        continue
                    }
                    task.date = date.toString()
                    break
                }

                // time
                while (true) {
                    println("Input the time (hh:mm):")
                    val strings = readln().trim().split(":")
                    val time = try {
                        LocalTime(hour=strings[0].toInt(), minute=strings[1].toInt())
                    } catch (e: Exception) {
                        println("The input time is invalid")
                        continue
                    }
                    task.time = time.toString()
                    break
                }

                // lines
                println("Input a new task (enter a blank line to end):")
                while (true) {
                    line = readln()
                    line = line.trim()
                    if (line.isBlank()) {
                        break
                    }
                    task.lines.add(line)
                }

                if (task.lines.isEmpty()) {
                    println("The task is blank")
                    continue
                }

                tasks.add(task)
            }
            "print" -> {
                if (tasks.isEmpty()) {
                    println("No tasks have been input")
                } else {
                    tasks.forEachIndexed { index, task -> run {
                            val indexStr = "${index + 1}  ".subSequence(0, 3)
                            println("$indexStr${task.date} ${task.time} ${task.priority}")
                            task.lines.forEach { line -> println("   $line") }
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

class Task {
    var priority: Char = 'N'
    var date: String = ""
    var time: String = ""
    val lines: MutableList<String> = mutableListOf()
}