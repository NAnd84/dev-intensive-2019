package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        if (fullName.isNullOrEmpty())
            return null to null

        var parts: List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName.isNullOrEmpty()) firstName = null
        if (lastName.isNullOrEmpty()) lastName = null

        return firstName to lastName    //Pair(firstName, lastName)
    }


    fun toInitials(firstName: String?, lastName: String?): String? {
        var firstSymbol:String? = null

        if (firstName != null && firstName.isNotEmpty() && firstName[0] != ' ')
            firstSymbol = firstName[0].toUpperCase().toString()

        if (lastName.isNullOrEmpty() || lastName[0] == ' ') return firstSymbol
        if(firstSymbol == null) return lastName[0].toUpperCase().toString()

        return firstSymbol + lastName[0].toUpperCase().toString()
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var resultStr = ""


        for(char in payload) {
            val latChar = when(char) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "sh'"
                'ъ' -> ""
                'ы' -> "i"
                'ь' -> ""
                'э' -> "e"
                'ю' -> "yu"
                'я' -> "ya"

                'А' -> "A"
                'Б' -> "B"
                'В' -> "V"
                'Г' -> "G"
                'Д' -> "D"
                'Е' -> "E"
                'Ё' -> "E"
                'Ж' -> "Zh"
                'З' -> "Z"
                'И' -> "I"
                'Й' -> "I"
                'К' -> "K"
                'Л' -> "L"
                'М' -> "M"
                'Н' -> "N"
                'О' -> "O"
                'П' -> "P"
                'Р' -> "R"
                'С' -> "S"
                'Т' -> "T"
                'У' -> "U"
                'Ф' -> "F"
                'Х' -> "H"
                'Ц' -> "C"
                'Ч' -> "Ch"
                'Ш' -> "Sh"
                'Щ' -> "Sh'"
                'Ъ' -> ""
                'Ы' -> "I"
                'Ь' -> ""
                'Э' -> "E"
                'Ю' -> "Yu"
                'Я' -> "Ya"
                ' ' -> divider
                else -> char.toString()
            }
            resultStr += latChar
        }
        return resultStr
    }


}