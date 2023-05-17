fun main() {
    // write your code here
    val n = readln().toInt()
    val array = IntArray(n)
    for (i in array.indices) {
        array[i] = readln().toInt()
    }
    val m = readln().toInt()
    println( if (array.contains(m)) "YES" else "NO" )
}