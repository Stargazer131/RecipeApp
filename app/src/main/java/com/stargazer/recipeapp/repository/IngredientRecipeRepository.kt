package com.stargazer.recipeapp.repository

import androidx.lifecycle.LiveData
import com.stargazer.recipeapp.dao.IngredientRecipeDAO
import com.stargazer.recipeapp.model.IngredientRecipe
import javax.inject.Inject

class IngredientRecipeRepository @Inject constructor(private val ingredientRecipeDAO: IngredientRecipeDAO) {

    suspend fun insert(ingredientRecipe: IngredientRecipe) {
        ingredientRecipeDAO.insert(ingredientRecipe)
    }

    suspend fun insertAll(ingredientRecipes: List<IngredientRecipe>) {
        return ingredientRecipeDAO.insertAll(ingredientRecipes)
    }

    suspend fun update(ingredientRecipe: IngredientRecipe): Boolean {
        return ingredientRecipeDAO.update(ingredientRecipe) > 0
    }

    suspend fun delete(ingredientRecipe: IngredientRecipe): Boolean {
        return ingredientRecipeDAO.delete(ingredientRecipe) > 0
    }

    fun getAll(): LiveData<List<IngredientRecipe>> {
        return ingredientRecipeDAO.getAll()
    }

    suspend fun deleteAllByRecipe(recipeId: Long): Int {
        return ingredientRecipeDAO.deleteAllByRecipe(recipeId)
    }

    suspend fun deleteAllByIngredient(ingredientId: Long): Int {
        return ingredientRecipeDAO.deleteAllByIngredient(ingredientId)
    }
}
