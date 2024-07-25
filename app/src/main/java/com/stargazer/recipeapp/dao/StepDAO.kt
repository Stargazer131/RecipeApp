package com.stargazer.recipeapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stargazer.recipeapp.model.Step


@Dao
interface StepDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(step: Step)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(steps: List<Step>)

    @Update
    suspend fun update(step: Step): Int

    @Delete
    suspend fun delete(step: Step): Int

    @Query("SELECT * FROM Step WHERE recipeId = :recipeId ORDER BY `order` ASC")
    suspend fun getAllForRecipe(recipeId: Long): List<Step>

    @Query("DELETE FROM Step WHERE recipeId = :recipeId")
    suspend fun deleteAllByRecipe(recipeId: Long): Int
}