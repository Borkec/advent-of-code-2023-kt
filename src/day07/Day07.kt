package day07

import println
import readInput

const val cardOrder = "23456789TJQKA"
const val cardOrder2 = "J23456789TQKA"

fun main() {
    fun part1(input: List<String>): Int {
        return run(input, mapJ = false, cardOrdering = cardOrder)
    }

    fun part2(input: List<String>): Int {
        return run(input, mapJ = true, cardOrdering = cardOrder2)
    }

    val testInput = readInput("day07/input_test")
    check(part1(testInput) == 6440)

    val testInput2 = readInput("day07/input_test_2")
    check(part2(testInput2) == 5905)

    val input = readInput("day07/input")
    part1(input).println()
    part2(input).println()
}

fun run(input: List<String>, mapJ: Boolean, cardOrdering: String) =
        input
                .map { it.parse() }
                .sortedWith(
                        compareBy<Pair<String, Int>> { it.first.checkType(mapJ) }
                                .thenComparing(valuesComparator(cardOrdering))
                )
                .mapIndexed { index, el ->
                    (index + 1) * el.second
                }
                .sum()

fun String.parse() = Pair(this.substringBefore(' '), this.substringAfter(' ').toInt())

fun String.toCardValues(cardOrdering: String) = this.map { cardOrdering.indexOf(it) }

fun String.checkType(mapJ: Boolean = false) = this.countCards(mapJ).run {
    listOf(checkPair(), checkPairs(), check3(), checkFH(), check4(), check5())
            .indexOfLast { it } + 1
}

fun Map<Char, Int>.check5(): Boolean = this.containsValue(5)
fun Map<Char, Int>.check4(): Boolean = this.containsValue(4)
fun Map<Char, Int>.checkFH(): Boolean = this.containsValue(3) && this.containsValue(2)
fun Map<Char, Int>.check3(): Boolean = this.values.sorted() == listOf(1, 1, 3)
fun Map<Char, Int>.checkPairs(): Boolean = this.values.sorted() == listOf(1, 2, 2)
fun Map<Char, Int>.checkPair(): Boolean = this.values.sorted() == listOf(1, 1, 1, 2)
fun String.countCards(mapJ: Boolean): Map<Char, Int> = this
        .map {
            if (mapJ && it == 'J') {
                val nonJ = this.countCards(false)
                nonJ.keys.filter { it != 'J' }
                        .sortedBy { cardOrder2.indexOf(it) }
                        .reversed()
                        .maxByOrNull { nonJ[it]!! } ?: 'A'
            } else it
        }
        .joinToString("")
        .mapToCounts()

fun String.mapToCounts() = this.associateWith { card -> this.count { card == it } }

fun valuesComparator(cardOrdering: String) = Comparator { o1: Pair<String, Int>, o2: Pair<String, Int> ->
    val found = o1.first.toCardValues(cardOrdering)
            .zip(o2.first.toCardValues(cardOrdering))
            .firstOrNull { it.first != it.second } ?: return@Comparator 0
    found.first - found.second
}
