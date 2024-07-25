package com.stargazer.recipeapp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stargazer.recipeapp.dao.IngredientDAO
import com.stargazer.recipeapp.dao.IngredientRecipeDAO
import com.stargazer.recipeapp.dao.RecipeDAO
import com.stargazer.recipeapp.dao.StepDAO

@Database(
    entities = [Step::class, Ingredient::class, Recipe::class, IngredientRecipe::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecipeDAO(): RecipeDAO
    abstract fun getIngredientDAO(): IngredientDAO
    abstract fun getIngredientRecipeDAO(): IngredientRecipeDAO

    abstract fun getStepDAO(): StepDAO
}