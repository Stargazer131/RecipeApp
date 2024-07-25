package com.stargazer.recipeapp.repository

import com.stargazer.recipeapp.dao.StepDAO
import com.stargazer.recipeapp.model.Step
import javax.inject.Inject

class StepRepository @Inject constructor(private val stepDAO: StepDAO) {

    suspend fun insert(step: Step) {
        stepDAO.insert(step)
    }

    suspend fun insertAll(steps: List<Step>) {
        stepDAO.insertAll(steps)
    }

    suspend fun update(step: Step): Boolean {
        return stepDAO.update(step) > 0
    }

    suspend fun delete(step: Step): Boolean {
        return stepDAO.delete(step) > 0
    }

    suspend fun getAllForRecipe(recipeId: Long): List<Step> {
        return stepDAO.getAllForRecipe(recipeId)
    }

    suspend fun deleteAllByRecipe(recipeId: Long): Int {
        return stepDAO.deleteAllByRecipe(recipeId)
    }
}