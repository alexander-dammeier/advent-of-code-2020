package de.dammeier.advent

/**
 * Splits the input in groups of minimum one element.
 * The element to split on is the first element
 * in all groups except the first one. There it can be the first element
 * if the list begins with an element to split on.
 * @param predicate determines where to split
 */
fun <T> Iterable<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    val x = this.take(1) + this.drop(1).takeWhile { predicate(it).not() }
    val rest = this.drop(x.size)
    return if (rest.isEmpty())
        listOf(x)
    else
        listOf(x) + rest.split(predicate)
}

@ExperimentalUnsignedTypes
fun ULong.toBinaryString(): String {
    val length = ULong.MAX_VALUE.toString(2).length
    val str = this.toString(2)
    val toAdd = length - str.length
    return (0 until toAdd).fold("") {acc, _ -> acc+"0" }+str
}