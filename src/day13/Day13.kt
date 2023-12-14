package day13

import println
import readInput
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {

        val sliced = mutableListOf<MutableList<MutableList<Char>>>()
        val newList = mutableListOf<MutableList<Char>>()
        for (lineIdx in input.indices) {
            if (input[lineIdx].isEmpty() || lineIdx == input.size - 1) {
                sliced.add(newList.toMutableList())
                newList.clear()
            } else {
                newList.add(input[lineIdx].toCharArray().toMutableList())
            }
        }

        var final = 0
        for (block in sliced) {

            val cols = process(block)
            val rows = process(block.transposed())

            final += cols
            final += rows * 100
        }

        return final
    }

    fun part2(input: List<String>): Int {
        val sliced = mutableListOf<List<List<Char>>>()
        val newList = mutableListOf<List<Char>>()
        for (lineIdx in input.indices) {
            if(lineIdx == input.size-1) {
                newList.add(input[lineIdx].toCharArray().toList())
                sliced.add(newList.toList())
            }
            if (input[lineIdx].isEmpty()) {
                sliced.add(newList.toList())
                newList.clear()
            } else {
                newList.add(input[lineIdx].toCharArray().toList())
            }
        }

        var final = 0
        for (block in sliced) {
            val fixedCols = process(block)
            val fixedRows = process(block.transposed())

            var colRem = -1
            var rowRem = -1
            for(i in block.indices) {
                for(j in block[0].indices) {
                    val newBlock = block.map { it.toMutableList() }.also {
                        it[i][j] = if(it[i][j] == '#') '.' else '#'
                    }

                    val cols = process(newBlock, fixedCols)
                    val rows = process(newBlock.transposed(), fixedRows)

                    if(colRem != cols && cols != fixedCols && cols != 0) {
                        colRem = cols
                        final += cols
                    }
                    if(rowRem != rows && rows != fixedRows && rows != 0) {
                        rowRem = rows
                        final += rows * 100
                    }
                }
            }
        }
        return final
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/input_test")
    check(part1(testInput) == 405)

    val testInput2 = readInput("day13/input_test")
    check(part2(testInput2) == 400)

    val input = readInput("day13/input")
    part1(input).println()
    part2(input).println()
}

fun process(block: List<List<Char>>, ignoreCol: Int = -1): Int {
    var temp = 0
    for (j in 1..<block[0].size) {
        if(ignoreCol != -1 && j == ignoreCol) continue
        var cnt = 0
        for (i in block.indices) {
            val side1 = block[i].subList(0, j).reversed()

            val side2 = block[i].subList(j, block[i].size)
            val check = side1
                    .zip(side2)
                    .count { (first, second) -> first == second } == min(side1.size, side2.size)
            if (check) cnt++
            else break
        }
        if (cnt == block.size) {
            temp = j
        }
    }
    return temp
}

fun List<List<Char>>.transposed(): List<List<Char>> =
        MutableList(get(0).size) { MutableList(size) { Char.MIN_VALUE } }
                .also {
                    for (i in indices) {
                        for (j in 0..<get(0).size) {
                            it[j][i] = this[i][j]
                        }
                    }
                }
