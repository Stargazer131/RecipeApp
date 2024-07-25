package com.stargazer.recipeapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ingredient::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IngredientRecipe(
    var recipeId: Long,
    var ingredientId: Long,
    var quantity: Double = 0.0,
    var unit: String = "unit"
)

data class IngredientQuantity(
    @Embedded val ingredient: Ingredient,
    var quantity: Double,
    var unit: String,
    val recipeId: Long
)

