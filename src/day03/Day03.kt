package day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var total = 0
        input.traverse(
                symbolSelector = Char::hasValidNeighbor,
                onSymbolFound = { _, number ->
                    total += number
                }
        )
        return total
    }

    fun part2(input: List<String>): Int {
        // maps x,y of * to neighbouring number
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>().withDefault { mutableListOf() }

        input.traverse(
                symbolSelector = Char::hasGearNeighbor,
                onSymbolFound = { symbol, num ->
                    gears.putIfAbsent(symbol, mutableListOf())
                    gears[symbol]?.add(num)
                }
        )

        return gears.values.filter { it.size == 2 }.sumOf { it.reduce(Math::multiplyExact) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/input_test")

    println(part1(testInput))
    check(part1(testInput) == 4361)

    val testInput2 = readInput("day03/input_test_2")
    println(part2(testInput2))
    check(part2(testInput2) == 467835)

    val input = readInput("day03/input")
    part1(input).println()
    part2(input).println()
}

fun List<String>.traverse(symbolSelector: Char.() -> Boolean, onSymbolFound: (Pair<Int, Int>, Int) -> Unit) =
        this.forEachIndexed { idx, row ->
            var check: Pair<Int, Int>? = null
            var num = ""
            row.forEachIndexed { symIdx, sym ->
                if (sym.isDigit()) {
                    num += sym
                    check = this.checkNeighbours(idx, symIdx, selector = symbolSelector) ?: check
                }

                if ((sym.isDigit().not() || symIdx == row.length - 1) && num != "") {
                    if (check != null) {
                        onSymbolFound(check!!, num.toInt())
                    }

                    num = ""
                    check = null
                }
            }

        }

/**
 * Returns the x,y index of first neighbour chosen by [selector]. Returns null if such neighbour doesn't exist
 */
fun List<String>.checkNeighbours(idx: Int, symIdx: Int, selector: Char.() -> Boolean): Pair<Int, Int>? =
        neighbors(idx, symIdx).run {
            getOrNull(
                    indexOfFirst { (x, y) ->
                        this@checkNeighbours.getOrNull(x)?.getOrNull(y)?.selector() ?: false
                    }
            )
        }


fun neighbors(x: Int, y: Int) = listOf(
        Pair(x - 1, y - 1),
        Pair(x - 1, y),
        Pair(x - 1, y + 1),
        Pair(x, y - 1),
        Pair(x, y + 1),
        Pair(x + 1, y - 1),
        Pair(x + 1, y),
        Pair(x + 1, y + 1),
)

fun Char.hasValidNeighbor() = this.isDigit().not() && this != '.'

fun Char.hasGearNeighbor() = this == '*'
