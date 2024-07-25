package com.stargazer.recipeapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.stargazer.recipeapp.dao.IngredientDAO
import com.stargazer.recipeapp.dao.IngredientRecipeDAO
import com.stargazer.recipeapp.dao.RecipeDAO
import com.stargazer.recipeapp.model.AppDatabase
import com.stargazer.recipeapp.repository.IngredientRecipeRepository
import com.stargazer.recipeapp.repository.IngredientRepository
import com.stargazer.recipeapp.repository.RecipeRepository
import com.stargazer.recipeapp.utils.ImageStorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideImageStorageManager(@ApplicationContext context: Context): ImageStorageManager {
        return ImageStorageManager(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "recipe_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRecipeDAO(db: AppDatabase): RecipeDAO {
        return db.getRecipeDAO()
    }

    @Provides
    fun provideRecipeRepository(recipeDAO: RecipeDAO): RecipeRepository {
        return RecipeRepository(recipeDAO)
    }

    @Provides
    fun provideIngredientDAO(db: AppDatabase): IngredientDAO {
        return db.getIngredientDAO()
    }

    @Provides
    fun provideIngredientRepository(ingredientDAO: IngredientDAO): IngredientRepository {
        return IngredientRepository(ingredientDAO)
    }

    @Provides
    fun provideIngredientRecipeDAO(db: AppDatabase): IngredientRecipeDAO {
        return db.getIngredientRecipeDAO()
    }

    @Provides
    fun provideIngredientRecipeRepository(ingredientRecipeDAO: IngredientRecipeDAO): IngredientRecipeRepository {
        return IngredientRecipeRepository(ingredientRecipeDAO)
    }

}
