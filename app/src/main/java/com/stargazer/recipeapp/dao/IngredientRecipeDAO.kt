package com.stargazer.recipeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stargazer.recipeapp.model.IngredientRecipe

@Dao
interface IngredientRecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredientRecipe: IngredientRecipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(ingredientRecipes: List<IngredientRecipe>)

    @Update
    suspend fun update(ingredientRecipe: IngredientRecipe): Int

    @Delete
    suspend fun delete(ingredientRecipe: IngredientRecipe): Int

    @Query("DELETE FROM IngredientRecipe WHERE recipeId = :recipeId")
    suspend fun deleteAllByRecipe(recipeId: Long): Int

    @Query("DELETE FROM IngredientRecipe WHERE ingredientId = :ingredientId")
    suspend fun deleteAllByIngredient(ingredientId: Long): Int

    @Query("SELECT * FROM IngredientRecipe")
    fun getAll(): LiveData<List<IngredientRecipe>>
}