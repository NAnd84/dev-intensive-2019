package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {

    if (this.length < length) return this

    var truncatedText = this.take(length).dropLastWhile { it == ' ' }   //.trimEnd()

    if (this.takeLast(this.length - length).isNotBlank()) truncatedText += "..."

    return truncatedText
}


fun String.stripHtml(): String = this
    .replace(Regex("<.*?>|&#\\d+?|\\w+?;"), "")
    .replace(Regex("\\s{2,}"), " ")
