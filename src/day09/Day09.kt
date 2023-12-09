package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return run(input, reversed = false)
    }

    fun part2(input: List<String>): Int {
        return run(input, reversed = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/input_test")
    check(part1(testInput) == 114)

    val testInput2 = readInput("day09/input_test")
    check(part2(testInput2) == 2)

    val input = readInput("day09/input")
    part1(input).println()
    part2(input).println()
}

fun run(input: List<String>, reversed: Boolean) =
        input.parse().sumOf {
            var list = if(reversed) it.reversed() else it
            var sum = 0
            while (list.filterNot { it == 0 }.isNotEmpty()) {
                sum += list.last()
                list = List(list.size - 1) { idx ->
                    list[idx + 1] - list[idx]
                }
            }
            sum
        }

fun List<String>.parse() = map { it.split(' ').map(String::toInt) }
