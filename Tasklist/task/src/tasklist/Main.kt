package tasklist

import kotlinx.datetime.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

fun main() {
    // reading json file if it exits
    val jsonFile = File("tasklist.json")
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(List::class.java, Task::class.java)
    val adapter = moshi.adapter<List<Task?>>(type)

    val tasks : MutableList<Task> = if (jsonFile.exists()) {

        val json = jsonFile.readText()
        adapter.fromJson(json)?.filterNotNull()?.toMutableList() ?: mutableListOf()
    } else {
        mutableListOf()
    }



    var line: String

    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        line = readln().trim()

        when (line.lowercase()) {
            "end" -> break
            "add" -> {
                val task = Task()
                addPriority(task)
                addDate(task)
                addTime(task)
                addLines(task)
                tasks.add(task)
            }
            "edit" -> editTask(tasks)
            "print" -> printTasks(tasks)
            "delete" -> {
                if (tasks.isEmpty()) {
                    println("No tasks have been input")
                    continue
                }
                printTasks(tasks)
                tasks.removeAt(selectIndex(tasks))
                println("The task is deleted")
            }
            else -> println("The input action is invalid")
        }
    }
    println("Tasklist exiting!")
    jsonFile.writeText(adapter.toJson(tasks))
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
            val due = LocalDateTime.parse ("${date}T${time}:00").date
            return when (due.daysUntil(today)) {
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
        if (tasks.isEmpty()) {
            println("No tasks have been input")
            return
        }
        printTasks(tasks)
        val task = tasks[ selectIndex(tasks)]
        while (true) {
            println("Input a field to edit (priority, date, time, task):")
            when (readln().trim().lowercase()) {
                "priority" -> addPriority(task)
                "date" -> addDate(task)
                "time" -> addTime(task)
                "task" -> addLines(task)
                else -> {
                    println("Invalid field")
                    continue
                }
            }
            break
        }
        break
    }
    println("The task is changed")
}



fun addPriority(task: Task) {
    while (true) {
        println("Input the task priority (C, H, N, L):")
        val string = readln().trim()
        if (string.lowercase() in listOf("c", "h", "n", "l")) {
            task.priority = string[0].uppercaseChar()
            break
        }
    }
}

fun addDate(task: Task) {
    while (true) {
        println("Input the date (yyyy-mm-dd):")

        val input = readln().trim()
        if (!"""\d{4}-\d+-\d+$""".toRegex().matches(input)) {

            println("The input date is invalid")
            continue

            /*var s = Clock.System.now().toString()
            s = s.substring(0, s.indexOf("T"))
            task.time = s
            break*/
        }
        val dataStrings = input.split("-")
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
        val input = readln()

        if (!"""\d{1,2}:\d{1,2}""".toRegex().matches(input)) {
            println("The input time is invalid")
            continue
        }
        val timeStrings = input
            .trim()
            .split(":")
//            .stream()
//            .map(String::trim)
//            .collect(java.util.stream.Collectors.toList())


        val timeStampString = task.date + "T" + (if (timeStrings[0].length == 1) "0" else "") + timeStrings[0] + ":" + (if (timeStrings[1].length == 1) "0" else "") + timeStrings[1] + ":00"
        val time = try {
            LocalDateTime.parse (timeStampString)
        } catch (e: Exception) {
            println("The input time is invalid")
            continue
        }
        task.time = "${String.format("%2s", time.hour.toString()).replace(' ', '0')}:${String.format("%2s", time.minute.toString()).replace(' ', '0')}"
        break
    }
}








fun addLines(task: Task) {
    val lines = mutableListOf<String>()
    println("Input a new task (enter a blank line to end):")
    do {
        val input = readln()
        if (input.isBlank() && lines.isEmpty()) {
            println("The task is blank")
            break
        }
        lines.add(input)
    } while (lines.isEmpty() || lines.last().isNotBlank())
    task.lines.clear()
    task.lines.addAll(lines)
}






fun printTasks(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
        return
    }


    var output ="+----+------------+-------+---+---+--------------------------------------------+\n" +
            "| N  |    Date    | Time  | P | D |                   Task                     |\n" +
            "+----+------------+-------+---+---+--------------------------------------------+\n"





    tasks.forEachIndexed { index, task ->
        run {
            val indexStr = "${index + 1} ".subSequence(0, 2)

            val newLines = mutableListOf<String>()
            task.lines.forEach { s ->
                var string = s
                while (string.length > 44) {
                    newLines.add(string.substring(0, 44))
                    string = string.substring(44)
                }
                newLines.add(string)
            }
            task.lines.clear()
            task.lines.addAll(newLines)




            output += "| $indexStr | ${task.date} | ${task.time} | ${printPriority( task)} | ${printDueTag( task)} |${printLine(task.lines.first())}|\n"
            task.lines.drop(1).dropLast(1). forEach { line ->
                output += "|    |            |       |   |   |${printLine(line)}|\n"
            }
            output += "+----+------------+-------+---+---+--------------------------------------------+\n"
        }
    }




    print(output)
}



fun printLine(s: String): String {
    return (s + " ".repeat(44)).substring(0, 44)
}

fun printDueTag(task: Task): String {
    return when (task.dueTag) {
        'I' -> "\u001B[102m \u001B[0m"
        'T' -> "\u001B[103m \u001B[0m"
        'O' -> "\u001B[101m \u001B[0m"
        else -> "?"
    }
}

fun printPriority(task: Task): String {
    return when (task.priority) {
        'C' -> "\u001B[101m \u001B[0m"
        'H' -> "\u001B[103m \u001B[0m"
        'N' -> "\u001B[102m \u001B[0m"
        'L' -> "\u001B[104m \u001B[0m"
        else -> "?"
    }
}
