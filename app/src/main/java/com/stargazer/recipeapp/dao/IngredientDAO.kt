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

    @Query("UPDATE Ingredient SET imageLink = :imageLink WHERE id = :ingredientId")
    suspend fun updateImageLink(ingredientId: Long, imageLink: String): Int

    @Delete
    suspend fun delete(ingredient: Ingredient): Int

    @Query("SELECT * FROM Ingredient WHERE id = :ingredientId")
    suspend fun getById(ingredientId: Long): Ingredient?

    @Query("SELECT * FROM Ingredient")
    fun getAll(): LiveData<List<Ingredient>>

    @Transaction
    @Query(
        """
        SELECT Ingredient.*, IngredientRecipe.quantity, IngredientRecipe.unit, IngredientRecipe.recipeId
        FROM Ingredient
        INNER JOIN IngredientRecipe ON Ingredient.id = IngredientRecipe.ingredientId
        WHERE IngredientRecipe.recipeId = :recipeId
        """
    )
    suspend fun getAllForRecipe(recipeId: Long): List<IngredientQuantity>

    @Transaction
    @Query(
        """
        SELECT * FROM Ingredient 
        WHERE id NOT IN (
            SELECT ingredientId 
            FROM IngredientRecipe 
            WHERE recipeId = :recipeId
        )
    """
    )
    suspend fun getAllNotInRecipe(recipeId: Long): List<Ingredient>

    @Query("SELECT * FROM Ingredient WHERE name LIKE '%' || :keyword || '%' ORDER BY name ASC")
    suspend fun getAllByName(keyword: String): List<Ingredient>
}