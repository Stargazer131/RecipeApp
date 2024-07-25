package com.stargazer.recipeapp.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["recipeId", "order"],
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Step(
    var description: String,
    var order: Int,
    var recipeId: Long
)
