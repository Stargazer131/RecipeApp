package com.stargazer.recipeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.model.IngredientQuantity

@Dao
interface IngredientDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredient: Ingredient): Long

    @Update
    suspend fun update(ingredient: Ingredient): Int

    @Delete
    suspend fun delete(ingredient: Ingredient): Int

    @Query("SELECT * FROM Ingredient WHERE id = :ingredientId")
    suspend fun getById(ingredientId: Long): Ingredient?

    @Query("SELECT * FROM Ingredient")
    fun getAll(): LiveData<List<Ingredient>>

    @Transaction
    @Query(
        """
        SELECT Ingredient.*, IngredientRecipe.quantity, IngredientRecipe.unit 
        FROM Ingredient
        INNER JOIN IngredientRecipe ON Ingredient.id = IngredientRecipe.ingredientId
        WHERE IngredientRecipe.recipeId = :recipeId
        """
    )
    suspend fun getAllForRecipe(recipeId: Long): List<IngredientQuantity>
}