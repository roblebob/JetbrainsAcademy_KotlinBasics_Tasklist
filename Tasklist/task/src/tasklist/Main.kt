package tasklist

import kotlinx.datetime.*


fun main() {
    // write your code here

    var line = ""
    val tasks = mutableListOf<Task>()
    outer@ while (true) {
        println("Input an action (add, print, edit, delete, end):")
        line = readln().trim()

        when (line.lowercase()) {
            "end" -> break
            "add" -> {
                val task = Task()
                addPriority(task)
                addDate(task)
                addLines(task)
                tasks.add(task)
            }
            "edit" -> editTask(tasks)
            "print" -> printTasks(tasks)
            "delete" -> {
                tasks.removeAt( selectIndex(tasks))
                println("The task is deleted")
            }
            else -> println("The input action is invalid")
        }
    }
    println("Tasklist exiting!")
}






data class Task (
    var priority: Char = 'N',
    val lines: MutableList<String> = mutableListOf(),
    var date: String = "2023-01-01",
    var time: String = "00:00",
)
{
    val dueTag : Char
        get() {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val due = LocalDateTime.parse (date + "T" + time).date
            return when (today.daysUntil(due)) {
                in Int.MIN_VALUE..-1 -> 'I'
                0 -> 'T'
                else -> 'O'
            }
        }
}

fun selectIndex(tasks: MutableList<Task>) : Int {
    while (true) {
        println("Input the task number (1-${tasks.size}):")
        val index = readln().trim().toIntOrNull()
        if (index == null || index !in 1..tasks.size) {
            println("Invalid task number")
            continue
        }
        return index - 1
    }
}




fun editTask(tasks: MutableList<Task>) {
    while (true) {
        val task = tasks[ selectIndex(tasks)]
        while (true) {
            println("Input the field name (priority, date, lines, time):")
            when (readln().trim().lowercase()) {
                "priority" -> addPriority(task)
                "date" -> addDate(task)
                "lines" -> addLines(task)
                "time" -> addTime(task)
                else -> {
                    println("The input field is invalid")
                    continue
                }
            }
            break
        }
        break
    }
}



fun addPriority(task: Task) {
    while (true) {
        println("Input the task priority (C, H, N, L):")
        val string = readln().trim()
        if (string.lowercase() in listOf("c", "h", "n", "l")) {
            task.priority = string[0].uppercaseChar()
            break;
        }
    }
}

fun addDate(task: Task) {
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        val dataStrings = readln().trim().split("-")
        val year = dataStrings[0].toInt()
        val month = dataStrings[1].toInt()
        val day = dataStrings[2].toInt()
        val date = try {
            LocalDate(  year, month, day)
        } catch (e: Exception) {
            println("The input date is invalid")
            continue
        }
        task.date = date.toString()
        break
    }
}



fun addTime(task: Task)  {
    while (true) {
        println("Input the time (hh:mm):")
        val timeStrings = readln().trim().split(":")
        val time = try {
            LocalDateTime.parse (task.date + "T" + timeStrings[0] + ":" + timeStrings[1] + ":00")
        } catch (e: Exception) {
            println("The input time is invalid")
            continue
        }
        task.time = "${time.hour}:${time.minute}"
        break
    }
}








fun addLines(task: Task) {
    val lines = mutableListOf<String>()

    val condition = { l: List<String> -> l.isEmpty() || l.all { it.isBlank() } }
    while (condition(lines)) {

        println("Input a new task (enter a blank line to end):")
        val input = readln()
        if (input.isBlank() && lines.isEmpty()) {
            println("The task is blank")
            continue
        }
        lines.add(input)
    }
    task.lines.addAll(lines)
}






fun printTasks(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        tasks.forEachIndexed { index, task ->
            run {
                val indexStr = "${index + 1}  ".subSequence(0, 3)
                println("$indexStr${task.date} ${task.time} ${task.priority} ${task.dueTag}")
                task.lines.forEach { line -> println("   $line") }
                println()
            }
        }
    }
}

