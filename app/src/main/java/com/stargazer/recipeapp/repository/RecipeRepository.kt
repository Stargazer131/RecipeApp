package com.stargazer.recipeapp.repository

import androidx.lifecycle.LiveData
import com.stargazer.recipeapp.dao.RecipeDAO
import com.stargazer.recipeapp.model.Recipe
import java.util.Date
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val recipeDAO: RecipeDAO) {
    suspend fun insert(recipe: Recipe): Long {
        return recipeDAO.insert(recipe)
    }

    suspend fun delete(recipe: Recipe): Boolean {
        return recipeDAO.delete(recipe) > 0
    }

    suspend fun update(recipe: Recipe): Boolean {
        recipe.updatedTimestamp = Date()
        return recipeDAO.update(recipe) > 0
    }

    suspend fun updateImageLink(recipeId: Long, imageLink: String): Boolean {
        return recipeDAO.updateImageLink(recipeId, imageLink) > 0
    }

    fun getAll(): LiveData<List<Recipe>> {
        return recipeDAO.getAll()
    }

    suspend fun getById(recipeId: Long): Recipe? {
        return recipeDAO.getById(recipeId)
    }

    suspend fun getAllByName(keyword: String): List<Recipe> {
        return recipeDAO.getAllByName(keyword)
    }

    suspend fun getAllByDateRange(startDate: Date, endDate: Date): List<Recipe> {
        return recipeDAO.getAllByDateRange(startDate, endDate)
    }
}
