val values: Map<String, String> = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
)

val possibleValues: Set<String> = values.keys + setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            "${it.first(Char::isDigit)}${it.last(Char::isDigit)}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            (
                "${it.findAnyOf(possibleValues)!!.second.convertTxtToInt()}" +
                "${it.findLastAnyOf(possibleValues)!!.second.convertTxtToInt()}"
            ).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(part1(testInput))
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()


    val testInput2 = readInput("Day01_test_pt2")
    println(part2(testInput2))
    check(part2(testInput2) == 281)
    part2(input).println()
}

fun String.convertTxtToInt() = if (values.containsKey(this)) values[this]!! else this.toInt()
