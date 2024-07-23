package com.stargazer.recipeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Ingredient(
    var name: String = "",
    var imageLink: String? = null,
    var description: String = ""
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}