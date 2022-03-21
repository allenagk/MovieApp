package jp.ml.movies.data.local

import androidx.room.TypeConverter

/**
 * TypeConverter class to store string list to database
 */
class StringListConverter {
    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }
}