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


    /*
    fun transliteration(payload: String, divider: String = " "): String {
        TODO()
    }
*/
    fun toInitials(firstName: String?, lastName: String?): String? {
        var initials: String? = null
        if (firstName != null && firstName.isNotEmpty() && firstName[0] != ' ')
            initials = firstName[0].toUpperCase().toString()

        if (lastName.isNullOrEmpty() || lastName[0] == ' ') return initials
        initials = lastName[0].toUpperCase().toString()

        return initials
    }


}