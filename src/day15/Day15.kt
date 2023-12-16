package day15

import println
import readInput

data class Lens(val label: String, val focal: Int)

fun main() {
    fun part1(input: List<String>): Int {
        return input[0].split(',').sumOf(String::hash)
    }

    fun part2(input: List<String>): Int {

        val map = HashMap<Int, MutableList<Lens>>().withDefault { mutableListOf() }

        input[0].split(',').forEach {
            if(it.contains('=')) {
                val label = it.substringBefore('=')
                val focal = it.substringAfter('=').toInt()

                if(map.containsKey(label.hash()).not()) map[label.hash()] = mutableListOf()
                val check = map[label.hash()]?.indexOfFirst { it.label == label }
                if(check == null || check == -1) {
                    map[label.hash()]?.add(Lens(label, focal))
                } else {
                    map[label.hash()]?.removeAt(check)
                    map[label.hash()]?.add(check, Lens(label, focal))
                }
            }
            if(it.contains('-')) {
                val label = it.substringBefore('-')
                if(map.containsKey(label.hash()).not()) map[label.hash()] = mutableListOf()

                val check = map[label.hash()]?.indexOfFirst { it.label == label }
                if(check != null && check != -1) {
                    map[label.hash()]?.removeAt(check)
                }
            }
            //println("Processing: $it current: $map")
        }
        return map.map {
            val boxNumber = it.key
            it.value.foldIndexed(0) { index, acc, lens ->
                acc + lens.focal * (boxNumber + 1) * (index+1)
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day15/input_test")
    check(part1(testInput) == 1320)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("day15/input_test")
    check(part2(testInput2) == 145)

    val input = readInput("day15/input")
    part1(input).println()
    part2(input).println()
}

fun String.hash(): Int {
    return fold(0) { acc, c ->
        (acc + c.code) * 17 % 256
    }
}
