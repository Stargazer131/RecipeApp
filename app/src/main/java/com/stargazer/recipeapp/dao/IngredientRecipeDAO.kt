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
    suspend fun insert(ingredientRecipe: IngredientRecipe): Long

    @Update
    suspend fun update(ingredientRecipe: IngredientRecipe): Int

    @Delete
    suspend fun delete(ingredientRecipe: IngredientRecipe): Int

    @Query("SELECT * FROM IngredientRecipe")
    fun getAll(): LiveData<List<IngredientRecipe>>

//    @Query(
//        """
//        SELECT * FROM IngredientRecipe
//        WHERE IngredientRecipe.recipeId = :recipeId
//        order by ingredientId ASC
//        """
//    )
//    fun getAllByRecipeId(recipeId: Long): LiveData<List<IngredientRecipe>>
}