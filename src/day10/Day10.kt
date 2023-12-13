package day10

import println
import readInput
import java.lang.IndexOutOfBoundsException
import kotlin.math.max
import kotlin.math.min

fun generateBio(sizeX: Int, sizeY: Int): List<MutableList<Boolean>> = List(sizeX) { MutableList(sizeY) { false } }

val maxs = 0
fun main() {
    fun part1(input: List<String>): Int {
        val locS = input.findS()
        val bio = generateBio(input.size, input[0].length)

        val history = mutableListOf<Pair<Pair<Int, Int>, Boolean>>()
        val deque = ArrayDeque<Pair<Pair<Int, Int>, Int>>()
        return bfs(input, locS, deque, history, bio)
    }

    fun part2(input: List<String>): Int {
        val locS = input.findS()
        val bio = generateBio(input.size, input[0].length)

        for (out in input) println(out)
        println()


        val history = ArrayDeque<Pair<Pair<Int, Int>, Boolean>>()
        val deque = ArrayDeque<Pair<Pair<Int, Int>, Int>>()

        dfs(input, locS, deque, history, bio)
        val reversed = history.map { Pair(it.first, !it.second) }

        val map = inflate(input, history)

        val k = bio
                .mapIndexed { i, it ->
                    it.mapIndexed { j, it -> if (it) input[i][j] else '.' }
                            .mapIndexed { j, it ->
                                if (map[i][j] == 1) '+' else if (it == '.') '.' else 'x'
                            }
                            .joinToString("")
                }


        for(one in k) { println(one) }
        // println()
        // println(inflate(input, reversed))
        return min(inflate(input, history).sumOf { it.sum() }, inflate(input, reversed).sumOf { it.sum() })
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("day10/input_test")
    //check(part1(testInput) == 4)

    //val testInput2 = readInput("day10/input_test_2")
    // check(part1(testInput2) == 8)

    //val testInput = readInput("day10/input_test_2_1")
    //check(part2(testInput) == 5)

    //val testInput2 = readInput("day10/input_test_2_2")
    //check(part2(testInput2) == 4)

    //val testInput3 = readInput("day10/input_test_2_3")
    //check(part2(testInput3) == 8)

    val testInput4 = readInput("day10/input_test_2_4")
    println(part2(testInput4))
    //check(part2(testInput4) == 10)

    val input = readInput("day10/input")
    // part1(input).println()
    part2(input).println()
}

val map: Map<Char, List<Pair<Int, Int>>> = mapOf(
        'S' to listOf(Pair(0, -1), Pair(+1, 0), Pair(-1, 0), Pair(0, +1)),
        '-' to listOf(Pair(0, -1), Pair(0, +1)),
        '|' to listOf(Pair(-1, 0), Pair(+1, 0)),
        '7' to listOf(Pair(0, -1), Pair(+1, 0)),
        'L' to listOf(Pair(0, +1), Pair(-1, 0)),
        'J' to listOf(Pair(0, -1), Pair(-1, 0)),
        'F' to listOf(Pair(0, +1), Pair(+1, 0)),
)

val starters = listOf(listOf('-', 'L', 'F'), listOf('|', 'L', 'J'), listOf('|', '7', 'F'), listOf('-', '7', 'J')) //left, down, up, right,
val startersMarkers = mapOf(
        Pair(0, 1) to '-',
        Pair(1, 2) to 'L',
        Pair(2, 3) to '|',
        Pair(0, 2) to 'J',
        Pair(0, 3) to '7',
        Pair(1, 3) to 'F'
)

fun bfs(field: List<String>, start: Pair<Int, Int>, deque: ArrayDeque<Pair<Pair<Int, Int>, Int>>, history: MutableList<Pair<Pair<Int, Int>, Boolean>>, bio: List<MutableList<Boolean>>): Int {

    var out = 0
    deque.addLast(Pair(start, 0))
    history.add(Pair(start, true))
    while (deque.isNotEmpty()) {
        val (current, cnt) = deque.removeFirst()
        val (x, y) = current
        bio[x][y] = true

        println("Current $x $y: ${field[x][y]}, numMoves: $cnt")
        out = max(out, cnt)

        val moves = map[field[x][y]] ?: continue
        for (moveIdx in moves.indices) {
            val neigh = Pair(x + moves[moveIdx].first, y + moves[moveIdx].second)
            if (neigh.first < 0 || neigh.second < 0 || neigh.first >= field.size || neigh.second >= field[0].length) continue
            if (field[x][y] == 'S') {
                if (starters[moveIdx].contains(field[neigh.first][neigh.second]).not()) continue
            }
            if (bio[neigh.first][neigh.second]) continue
            deque.addLast(Pair(neigh, cnt + 1))
        }
        println("deque: $deque history: $history")
    }
    return out
}

fun dfs(field: List<String>, start: Pair<Int, Int>, deque: ArrayDeque<Pair<Pair<Int, Int>, Int>>, history: ArrayDeque<Pair<Pair<Int, Int>, Boolean>>, bio: List<MutableList<Boolean>>): Int {
    var out = 0
    deque.addFirst(Pair(start, 0))
    history.add(Pair(start, true))
    while (deque.isNotEmpty()) {
        val (current, cnt) = deque.removeFirst()
        val (x, y) = current

        if (bio[x][y]) continue
        bio[x][y] = true

        //println("Current $x $y: ${field[x][y]}, numMoves: $cnt")
        out = max(out, cnt)

        val moves = map[field[x][y]] ?: continue
        for (moveIdx in moves.indices) {
            val neigh = Pair(x + moves[moveIdx].first, y + moves[moveIdx].second)
            if (neigh.first < 0 || neigh.second < 0 || neigh.first >= field.size || neigh.second >= field[0].length) continue
            if (bio[neigh.first][neigh.second]) continue
            if (field[x][y] == 'S') {
                if (starters[moveIdx].contains(field[neigh.first][neigh.second]).not()) continue
            }
            val outer = when (field[neigh.first][neigh.second]) {
                '-' -> neigh.second == (current.second + 1)
                '|' -> neigh.first == (current.first + 1)
                'F' -> neigh.second == (current.second - 1)
                '7' -> neigh.first == (current.first - 1)
                'J' -> neigh.second == (current.second + 1)
                'L' -> neigh.first == (current.first + 1)
                else -> false
            }
            history.addLast(Pair(neigh, outer))

            deque.addFirst(Pair(neigh, cnt + 1))
        }
        //println("deque: $deque history: $history")
    }
    history.removeAt(1)
    return out
}

fun inflate(field: List<String>, history: List<Pair<Pair<Int, Int>, Boolean>>): List<List<Int>> {

    //println(history)
    val newField = MutableList(field.size) { MutableList(field[0].length) { 0 } }

    for ((pos, outer) in history) {
        when (field[pos.first][pos.second]) {
            'F' -> {
                if (outer.not()) {
                    newField.historyHas(history, pos.first - 1, pos.second - 1)
                    newField.historyHas(history, pos.first, pos.second - 1)
                    newField.historyHas(history, pos.first - 1, pos.second)
                } else {
                    newField.historyHas(history, pos.first + 1, pos.second + 1)
                }
            }

            'J' -> {
                if (outer.not()) {
                    newField.historyHas(history, pos.first + 1, pos.second + 1)
                    newField.historyHas(history, pos.first + 1, pos.second)
                    newField.historyHas(history, pos.first, pos.second + 1)
                } else {
                    newField.historyHas(history, pos.first - 1, pos.second - 1)
                }
            }

            'L' -> {
                if (outer.not()) {
                    newField.historyHas(history, pos.first + 1, pos.second - 1)
                    newField.historyHas(history, pos.first + 1, pos.second)
                    newField.historyHas(history, pos.first, pos.second - 1)
                } else {
                    newField.historyHas(history, pos.first - 1, pos.second + 1)
                }
            }

            '7' -> {
                if (outer.not()) {
                    newField.historyHas(history, pos.first - 1, pos.second + 1)
                    newField.historyHas(history, pos.first - 1, pos.second)
                    newField.historyHas(history, pos.first, pos.second + 1)
                } else {
                    newField.historyHas(history, pos.first + 1, pos.second - 1)
                }
            }

            '|' -> {
                if (outer) {
                    newField.historyHas(history, pos.first, pos.second + 1)
                } else {
                    newField.historyHas(history, pos.first, pos.second - 1)
                }
            }

            '-' -> {
                if (outer) {
                    newField.historyHas(history, pos.first - 1, pos.second)
                } else {
                    newField.historyHas(history, pos.first + 1, pos.second)
                }
            }
        }
        //println("$pos: ${field[pos.first][pos.second]} $outer")
        //for(out in newField) println(out)

        //println()
    }
    //for(out in newField) println(out)
    //println()
    return newField
}

fun MutableList<MutableList<Int>>.historyHas(history: List<Pair<Pair<Int, Int>, Boolean>>, x: Int, y: Int) {
    if (x < 0 || y < 0 || x >= size || y >= this[0].size) return
    if (history.find { it.first == Pair(x, y) } == null)
        this[x][y] = 1 else this[x][y] = 0
}

fun List<String>.findS() = Pair(this.indexOfFirst { it.contains('S') }, this.find { it.contains('S') }!!.indexOf('S'))
