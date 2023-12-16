import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun List<String>.to2DCharArray() = List(this.size) { MutableList(this[0].length) { Char.MIN_VALUE } }.also {
    for(i in indices) {
        for(j in 0 ..< this[0].length) {
            it[i][j] = this[i][j]
        }
    }
}

fun List<List<Char>>.rotate() = List(this[0].size) { MutableList(this.size) { Char.MIN_VALUE } }.also {
    for(i in indices) {
        for(j in 0 ..< this[0].size) {
            it[j][this.size-i-1] = this[i][j]
        }
    }
}

fun List<List<Char>>.prettyPrint() = also { map { it.joinToString("").println() } }