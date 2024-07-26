package com.stargazer.recipeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stargazer.recipeapp.model.Recipe
import java.util.Date

@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe): Int

    @Query("UPDATE Recipe SET imageLink = :imageLink WHERE id = :recipeId")
    suspend fun updateImageLink(recipeId: Long, imageLink: String): Int

    @Delete
    suspend fun delete(recipe: Recipe): Int

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getById(recipeId: Long): Recipe?

    @Query("SELECT * FROM Recipe")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE name LIKE '%' || :keyword || '%' ORDER BY name ASC")
    suspend fun getAllByName(keyword: String): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE updatedTimestamp BETWEEN :startDate AND :endDate ORDER BY updatedTimestamp ASC")
    suspend fun getAllByDateRange(startDate: Date, endDate: Date): List<Recipe>
}
