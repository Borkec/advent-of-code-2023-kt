package day06

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {

        val time = input[0].parseInput()
        val distance = input[1].parseInput()

        return time.zip(distance).map(::findSmallest).reduce(Math::multiplyExact)
    }

    fun part2(input: List<String>): Long {
        val t = input[0].parseInputInt()
        val d = input[1].parseInputInt()

        return findSmallest(t to d)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/input_test")
    check(part1(testInput) == 288L)

    val testInput2 = readInput("day06/input_test_2")
    check(part2(testInput2) == 71503L)

    val input = readInput("day06/input")
    part1(input).println()
    part2(input).println()
}

fun String.parseInput() = this.substringAfter(':').trim().split("\\s+".toRegex()).map { it.trim().toLong() }

fun String.parseInputInt() = this.substringAfter(':').trim().replace("\\s+".toRegex(), "").toLong()

fun findSmallest(p: Pair<Long, Long>): Long {
    val (time, distance) = p
    var maxi = 0L
    for(i in 0 until time) {
        if((time-i) * i > distance) maxi++
    }
    return maxi
}