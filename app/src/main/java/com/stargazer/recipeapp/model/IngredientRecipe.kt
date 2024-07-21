package com.stargazer.recipeapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"]),
        ForeignKey(
            entity = Ingredient::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"]
        )
    ]
)
data class IngredientRecipe(
    var recipeId: Long,
    var ingredientId: Long,
    var quantity: Double,
    var unit: String
)

data class IngredientQuantity(
    @Embedded val ingredient: Ingredient,
    val quantity: Double,
    val unit: String
)

