package day11

import println
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {

        val expanded = expand(input)
        val locs = mutableListOf<Pair<Int, Int>>()
        for(i in expanded.indices) {
            for(j in expanded[i].indices) {
                if(expanded[i][j] == '#') locs.add(Pair(i, j))
            }
        }

        var sum = 0
        for(i in locs.indices) {
            for(j in i+1 until locs.size) {
                val dist = abs(locs[i].first - locs[j].first) + abs(locs[i].second - locs[j].second)
                sum += dist
            }
        }
        return sum
    }

    fun part2(input: List<String>, duplic: Long = 1000000L): Long {
        val rows = countRows(input)
        val columns = countColumns(input)

        val locs = mutableListOf<Pair<Int, Int>>()
        for(i in input.indices) {
            for(j in input[i].indices) {
                if(input[i][j] == '#') locs.add(Pair(i, j))
            }
        }

        var sum = 0L
        for(i in locs.indices) {
            for(j in i+1 until locs.size) {
                val els = rows.indexOfFirst { max(locs[j].first, locs[i].first) < it } - rows.indexOfFirst { min(locs[j].first, locs[i].first) < it }
                val ers = columns.indexOfFirst { max(locs[j].second, locs[i].second) < it } - columns.indexOfFirst { min(locs[j].second, locs[i].second) < it }
                val dist = abs(locs[i].first - locs[j].first) + els*(duplic-1) + abs(locs[i].second - locs[j].second) + ers*(duplic-1)
                sum += dist
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/input_test")
    check(part1(testInput) == 374)

    val testInput2 = readInput("day11/input_test_2")
    check(part2(testInput2, duplic=100L) == 8410L)

    val input = readInput("day11/input")
    part1(input).println()
    part2(input).println()
}

fun expand(input: List<String>): List<String> {
    var expanded = input.toMutableList()
    var delta = 0
    input.forEachIndexed { idx, row ->
        if (row.filterNot { it == '.' }.isEmpty()) {
            expanded.add(idx+delta, row)
            delta++
        }
    }
    delta = 0
    for(j in 0 until input[0].length) {
        val col = input.map { it[j] }.joinToString("")
        if (col.filterNot { it == '.' }.isEmpty()) {
            expanded = expanded.map {
                it.substring(0, j+delta) + "." + it.substring(j+delta)
            }.toMutableList()

            delta++
        }
    }
    return expanded
}

fun countRows(input: List<String>): List<Int> {
    var delta = mutableListOf<Int>()
    input.forEachIndexed { idx, row ->
        if (row.filterNot { it == '.' }.isEmpty()) {
            delta.add(idx)
        }
    }

    delta.add(Int.MAX_VALUE)
    return delta
}

fun countColumns(input: List<String>): List<Int> {
    var delta = mutableListOf<Int>()
    for(j in 0 until input[0].length) {
        val col = input.map { it[j] }.joinToString("")
        if (col.filterNot { it == '.' }.isEmpty()) {
            delta.add(j)
        }
    }
    delta.add(Int.MAX_VALUE)
    return delta
}
