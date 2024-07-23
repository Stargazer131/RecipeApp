package com.stargazer.recipeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity
data class Recipe(
    var name: String = "",
    var description: String = "",
    var instructions: String = "",
    var favorite: Boolean = false,
    var updatedTimestamp: Date = Date(),
    var youtubeLink: String = "",
    var imageLink: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

