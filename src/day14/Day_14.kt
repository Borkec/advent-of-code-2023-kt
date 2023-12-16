package day14

import prettyPrint
import println
import readInput
import rotate
import to2DCharArray

fun main() {
    fun part1(input: List<String>): Int {
        return input.to2DCharArray().count()
    }

    fun part2(input: List<String>): Int {
        val field = input.to2DCharArray()


        var new = field.rotate().rotate().rotate()

        for (i in 0..<10000) {
            new = new.toEast().rotate().toEast().rotate().toEast().rotate().toEast().rotate()
            //new.rotate().prettyPrint()


            new.rotate().let {
                it.mapIndexed { idx, row ->
                    row.count { it == 'O' } * (it.size - idx)
                }.sum()
            }.also { println(" $i $it") }
            println()
        } // todo calculate loop modulo 1000000000

        return 64
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/input_test")
    println(part1(testInput))
    check(part1(testInput) == 136)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("day14/input_test")
    check(part2(testInput2) == 64)

    val input = readInput("day14/input")
    part1(input).println()
    part2(input).println()
}

fun List<MutableList<Char>>.toEast() = this.also {
    for (i in indices) {
        var cnt = 0
        var lastIdx = 0
        for (j in this[0].indices) {
            if (this[i][j] == '.' || this[i][j] == 'O') cnt++
            if (this[i][j] == '#') {
                this[i].subList(lastIdx, j).sortDescending()
                lastIdx = j + 1
            }
        }

        this[i].subList(lastIdx, this[i].size).sortDescending()
    }
}

fun List<List<Char>>.count(): Int = this.let {
    this.prettyPrint()
    var final = 0
    for (j in 0..<this[0].size) {
        var cnt = 0
        var mark = this.size
        for (i in this.indices) {
            if (this[i][j] == '#') {
                if (mark != -1) {
                    //println("($i $j): $mark, $cnt, ${(mark + (mark-cnt+1)) * (mark - (mark-cnt+1) + 1) / 2}")
                    final += (mark + (mark - cnt + 1)) * (mark - (mark - cnt + 1) + 1) / 2
                }
                mark = this.size - i - 1
                cnt = 0
            }
            if (this[i][j] == 'O') cnt++
        }
        if (mark != -1) {
            //println("(end of $j): $mark, $cnt, ${(mark + (mark-cnt+1)) * (mark - (mark-cnt+1) + 1) / 2}")
            final += (mark + (mark - cnt + 1)) * (mark - (mark - cnt + 1) + 1) / 2
        }

    }
    return final
}
