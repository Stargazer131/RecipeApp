package com.stargazer.recipeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stargazer.recipeapp.model.Recipe

@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe): Int

    @Delete
    suspend fun delete(recipe: Recipe): Int

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    fun getById(recipeId: Long): Recipe

    @Query("SELECT * FROM Recipe")
    fun getAll(): LiveData<List<Recipe>>
}
