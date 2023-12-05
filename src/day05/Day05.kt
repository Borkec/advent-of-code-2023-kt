package day05

import println
import readInput

val maps = listOf(
        "seed-to-soil",
        "soil-to-fertilizer",
        "fertilizer-to-water",
        "water-to-light",
        "light-to-temperature",
        "temperature-to-humidity",
        "humidity-to-location"
)

fun main() {
    fun part1(input: List<String>): Int {

        val inputSeeds = input.parseSeeds()

        return maps
                .map { input.getMapValues(it) }
                .fold(inputSeeds) { acc, mapper ->
                    acc.mapAllWith(mapper)
                }.min().toInt()
    }

    fun part2(input: List<String>): Int {

        val inputSeeds = input.parseSeeds()
        val seeds = mutableListOf<Pair<Long, Long>>()

        for(i in inputSeeds.indices step 2) {
            seeds.add(Pair(inputSeeds[i], inputSeeds[i]+inputSeeds[i+1]))
        }

        return maps
            .map { input.getMapValues(it) }
            .fold(seeds.toList()) { acc, mapper ->
                acc.mapAllPairsWith(mapper)
            }.minOf { it.first }.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/input_test")
    check(part1(testInput) == 35)

    val testInput2 = readInput("day05/input_test_2")
    check(part2(testInput2) == 46)

    val input = readInput("day05/input")
    part1(input).println()
    part2(input).println()
}

fun List<String>.getMapValues(key: String) =
        this.subList(this.indexOf("$key map:") + 1, this.size).takeWhile(String::isNotBlank).map {
            val values = it.split(" ")
            values[1].toLong() to Pair(values[0].toLong(), values[2].toLong())
        }.sortedBy { it.first }.toMap()

fun List<String>.parseSeeds() = this[0].substringAfter(':').trim().split(' ').map { it.toLong() }

fun List<Long>.mapAllWith(map: Map<Long, Pair<Long, Long>>): List<Long> = this.map { it.mapWith(map) }

fun List<Pair<Long, Long>>.mapAllPairsWith(map: Map<Long, Pair<Long, Long>>): List<Pair<Long, Long>> = this.flatMap { it.mapPairsWith(map) }
fun Long.mapWith(map: Map<Long, Pair<Long, Long>>): Long {

    var lowerBound: Int = map.keys.toList().binarySearch { it - this }
    if (lowerBound < 0) {
        lowerBound = (lowerBound * -1) - 2
    }

    val key: Long = map.keys.toList().getOrNull(lowerBound) ?: return this

    return if ((key + map[key]!!.second) >= this) (this - key) + map[key]!!.first
    else this
}

fun Pair<Long, Long>.mapPairsWith(map: Map<Long, Pair<Long, Long>>): List<Pair<Long, Long>> {

    var lowerBoundFirst: Int = map.keys.toList().binarySearch { it - this.first }
    var lowerBoundSecond: Int = map.keys.toList().binarySearch { it - this.second }

    if (lowerBoundFirst < 0) {
        lowerBoundFirst = (lowerBoundFirst * -1) - 2
    }

    if (lowerBoundSecond < 0) {
        lowerBoundSecond = (lowerBoundSecond * -1) - 2
    }

    val acc = mutableListOf<Pair<Long, Long>>()

    var rem: Long = this.first
    for(i in lowerBoundFirst..lowerBoundSecond) {
        val input = map.keys.toList().getOrNull(i) ?: continue

        // this checks for same mappings inside boundary
        if(i > lowerBoundFirst && rem < input) {
            acc.add(Pair(rem, input-1))
            continue
        }

        val (output, delta) = map[input]!!

        // this checks for same mappings outside both boundaries
        if((input + delta) < this.first || input > this.second) {
            acc.add(this)
            continue
        }

        val found = Pair(
            output.coerceAtLeast(output + (this.first - input)),
            (output + delta).coerceAtMost(output + (this.second - input))
        )
        acc.add(found)
        rem = input + delta + 1
    }
    if(acc.isEmpty()) acc.add(this)
    return acc
}


/**
 * Copied and pasted from kotlin docs with comparison func that returns Long
 */
fun <T> List<T>.binarySearch(fromIndex: Int = 0, toIndex: Int = size, comparison: (T) -> Long): Int {
    rangeCheck(size, fromIndex, toIndex)

    var low = fromIndex
    var high = toIndex - 1

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = comparison(midVal)

        if (cmp < 0)
            low = mid + 1
        else if (cmp > 0)
            high = mid - 1
        else
            return mid // key found
    }
    return -(low + 1)  // key not found
}

private fun rangeCheck(size: Int, fromIndex: Int, toIndex: Int) {
    when {
        fromIndex > toIndex -> throw IllegalArgumentException("fromIndex ($fromIndex) is greater than toIndex ($toIndex).")
        fromIndex < 0 -> throw IndexOutOfBoundsException("fromIndex ($fromIndex) is less than zero.")
        toIndex > size -> throw IndexOutOfBoundsException("toIndex ($toIndex) is greater than size ($size).")
    }
}