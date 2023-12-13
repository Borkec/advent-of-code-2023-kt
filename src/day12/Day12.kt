package day12

import println
import readInput
import kotlin.math.pow

val bio = mutableMapOf<Pair<String, List<Int>>, Long>()

fun main() {
    fun part1(input: List<String>): Long {

        var final = 0L
        for (i in input.indices) {
            val str = input[i].substringBefore(' ')
            val cnts = input[i].substringAfter(' ').split(",").map(String::toInt)

            bio.clear()
            val returns = rek(cnts,  str)
            final += returns
        }
        return final
    }

    fun part2(input: List<String>): Long {
        var final = 0L
        for (i in input.indices) {
            val str = input[i].substringBefore(' ')
            val cnts = input[i].substringAfter(' ').split(",").map(String::toInt)

            val duplicated = "$str?$str?$str?$str?$str"
            val duplicatedCnt = cnts.plus(cnts).plus(cnts).plus(cnts).plus(cnts)

            bio.clear()
            val returns = rek(duplicatedCnt,  duplicated)
            final += returns
        }
        return final
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/input_test")
    check(part1(testInput) == 21L)

    val testInput2 = readInput("day12/input_test")
    check(part2(testInput2) == 525152L)

    val input = readInput("day12/input")
    part1(input).println()
    part2(input).println()
}

fun rek(ranges: List<Int>, str: String, rangeIndex: Int = 0, startIndex: Int = 0, countHash: Int = 0): Long {
    if (startIndex > str.length + 1) return 0
    if (rangeIndex == ranges.size) {
        return if (countHash == str.count { it == '#' }) 1 else 0
    }
    val loc = Pair(str.subSequence(startIndex, str.length).toString(), ranges.subList(rangeIndex, ranges.size))
    if (bio.containsKey(loc)) return bio[loc]!!

    val summ = ranges.subList(rangeIndex, ranges.size).sum()
    var counter = 0L
    for (k in startIndex..<str.length - summ + 1) {
        if (str[k] == '.') continue

        val sub = str.subSequence(k, k + ranges[rangeIndex])
        val hash = countHash + sub.count { it == '#' }
        val canFill = sub.filterNot { it == '#' || it == '?' }.isEmpty()
        val isComplete = ((k + ranges[rangeIndex]) == str.length || (str[k + ranges[rangeIndex]] != '#'))

        if (canFill && isComplete) {
            counter += rek(ranges, str, rangeIndex + 1,k + ranges[rangeIndex] + 1,  hash)
        }
        if (str[k] == '#') break
    }
    bio[loc] = counter
    return counter
}
