package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { card ->
            val (winning, drawn) = card.parseGame()
            2.0.pow(drawn.count(winning::contains).toDouble()-1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val marks = MutableList(input.size) {0}

        input.forEachIndexed { idx, card ->

            val (winning, drawn) = card.parseGame()
            val found = drawn.count(winning::contains)
            for (i in idx+1 until idx+found+1) marks[i] += marks[idx] + 1
            marks[idx]
        }
        return marks.sum() + input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/input_test")
    check(part1(testInput) == 13)

    val testInput2 = readInput("day04/input_test_2")
    check(part2(testInput2) == 30)

    val input = readInput("day04/input")
    part1(input).println()
    part2(input).println()
}

fun String.parseGame() = Pair(
        this.substringAfter(':')
                .substringBefore('|')
                .trim()
                .split("\\s+".toRegex())
                .map {
                    it.trim().toInt()
                },
        this.substringAfter(':')
                .substringAfter('|')
                .trim()
                .split("\\s+".toRegex())
                .map { it.trim().toInt() }
)
