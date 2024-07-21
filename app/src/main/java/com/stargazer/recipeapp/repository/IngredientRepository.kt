package com.stargazer.recipeapp.repository

import androidx.lifecycle.LiveData
import com.stargazer.recipeapp.dao.IngredientDAO
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.model.IngredientQuantity
import javax.inject.Inject

class IngredientRepository @Inject constructor(private val ingredientDAO: IngredientDAO) {

    suspend fun insert(ingredient: Ingredient): Long {
        return ingredientDAO.insert(ingredient)
    }

    suspend fun update(ingredient: Ingredient): Boolean {
        return ingredientDAO.update(ingredient) > 0
    }

    suspend fun delete(ingredient: Ingredient): Boolean {
        return ingredientDAO.delete(ingredient) > 0
    }

    suspend fun getById(ingredientId: Long): Ingredient {
        return ingredientDAO.getById(ingredientId)
    }

    fun getAll(): LiveData<List<Ingredient>> {
        return ingredientDAO.getAll()
    }

    fun getAllWithQuantityByRecipeId(recipeId: Long): LiveData<List<IngredientQuantity>> {
        return ingredientDAO.getAllWithQuantityByRecipeId(recipeId)
    }
}
