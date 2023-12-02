package day02

import println
import readInput

val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)
fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->

            val (gameIdx, game) = line.parseGame()

            val valid = game.all { draw ->
                draw.all { cubeSet ->
                    val colorNumberPair = cubeSet.trim()
                    val color = colorNumberPair.substringAfter(' ')
                    val cnt = colorNumberPair.substringBefore(' ').toInt()

                    limits[color]!! >= cnt
                }
            }
            if (valid) gameIdx else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val maxes = mutableMapOf("blue" to 0, "red" to 0, "green" to 0)

            val (_, game) = line.parseGame()

            game.forEach { draw ->
                draw.forEach { cubeSet ->
                    val colorNumberPair = cubeSet.trim()
                    val color = colorNumberPair.substringAfter(' ')
                    val cnt = colorNumberPair.substringBefore(' ').toInt()

                    maxes[color] = maxes[color]!!.coerceAtLeast(cnt)
                }
            }
            maxes.values.reduce(Math::multiplyExact)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/input_test")
    check(part1(testInput) == 8)

    val testInput2 = readInput("day02/input_test_2")
    check(part2(testInput2) == 2286)

    val input = readInput("day02/input")
    part1(input).println()
    part2(input).println()
}

private fun String.parseGame() = Pair(
        this.substringBefore(':')
                .substringAfter(' ')
                .toInt(),
        this.substringAfter(':')
                .split(';')
                .map { draw -> draw.split(',') }
)
