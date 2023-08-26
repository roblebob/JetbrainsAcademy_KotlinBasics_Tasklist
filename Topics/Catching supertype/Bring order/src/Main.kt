fun main() {
    val input = readln()
    try {
        println(100 / input.toInt())
    } catch (e: ArithmeticException) {
        println("You cannot divide by zero!")
    } catch (e: NumberFormatException) {
        println("You did not enter a valid integer!")
    } catch (e: Exception) {
        println("An unknown error has occurred!")
    }
}