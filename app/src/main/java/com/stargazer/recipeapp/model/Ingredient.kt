package com.stargazer.recipeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    var name: String,
    var imageLink: String?,
    var description: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}