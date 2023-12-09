package day08

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {

        val (commands, mapping) = input.parseInput()
        var current = "AAA"
        var cnt = 0
        while (current != "ZZZ") {
            for (sym in commands) {
                current = when (sym) {
                    'L' -> mapping[current]!!.first
                    'R' -> mapping[current]!!.second
                    else -> throw IllegalStateException()
                }
                cnt++
            }
        }

        return cnt
    }

    fun part2(input: List<String>): Long {
        val (commands, mapping) = input.parseInput()
        var current = mapping.keys.filter { it.endsWith('A') }
        var final = 1L
        for (c in current) {
            var start = c
            var cnt = 0L
            while (start.endsWith('Z').not()) {
                for (sym in commands) {
                    start = when (sym) {
                        'L' -> mapping[start]!!.first
                        'R' -> mapping[start]!!.second
                        else -> throw IllegalStateException()
                    }
                    cnt++
                }
            }
            println(cnt)
            final = lcm(final, cnt)
        }
        return final
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/input_test")
    check(part1(testInput) == 6)

    val testInput2 = readInput("day08/input_test_2")
    check(part2(testInput2) == 6L)

    val input = readInput("day08/input")
    part1(input).println()
    part2(input).println()
}

fun List<String>.parseInput() = Pair(this[0], this.subList(2, size).associate {
    it.split('=').let {
        it[0].trim() to Pair(it[1].subSequence(2, 5).toString(), it[1].subSequence(7, 10).toString())
    }
}
)

fun lcm(n1: Long, n2: Long): Long {
    var gcd = 1

    var i = 1
    while (i <= n1 && i <= n2) {
        if (n1 % i == 0L && n2 % i == 0L)
            gcd = i
        ++i
    }

    return n1 * n2 / gcd
}