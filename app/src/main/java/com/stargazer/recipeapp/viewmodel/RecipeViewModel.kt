package com.stargazer.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.model.IngredientQuantity
import com.stargazer.recipeapp.model.Recipe
import com.stargazer.recipeapp.repository.IngredientRecipeRepository
import com.stargazer.recipeapp.repository.IngredientRepository
import com.stargazer.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientRecipeRepository: IngredientRecipeRepository
) : ViewModel() {

    val allRecipes = recipeRepository.getAll()
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> get() = _recipe
    private val _ingredientQuantityList = MutableLiveData<List<IngredientQuantity>>()
    val ingredientQuantityList: LiveData<List<IngredientQuantity>> get() = _ingredientQuantityList

    ///
    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> get() = _insertResult

    ////
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult

    ////
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    fun setRecipeData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeResult = recipeRepository.getById(id) ?: Recipe()
            _recipe.postValue(recipeResult)

            val ingredientsResult = ingredientRepository.getAllForRecipe(id)
            _ingredientQuantityList.postValue(ingredientsResult)
        }
    }

    fun syncIngredientListFromRV(newList: List<IngredientQuantity>) {
        _ingredientQuantityList.value = newList
    }

    fun updateIngredientQuantity(position: Int, quantity: Double) {
        _ingredientQuantityList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            tempList[position].quantity = quantity
            _ingredientQuantityList.value = tempList
        }
    }

    fun updateIngredientUnit(position: Int, unit: String) {
        _ingredientQuantityList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            tempList[position].unit = unit
            _ingredientQuantityList.value = tempList
        }
    }

    fun deleteIngredientData(position: Int) {
        _ingredientQuantityList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            tempList.removeAt(position)
            _ingredientQuantityList.value = tempList
        }
    }


    fun addIngredientData(ingredients: List<Ingredient>, recipeId: Long) {
        _ingredientQuantityList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            ingredients.forEach {
                tempList.add(IngredientQuantity(it, 0.0, "unit", recipeId))
            }

            _ingredientQuantityList.value = tempList
        }
    }

    fun updateRecipeData(
        name: String?, description: String?, instructions: String?,
        favorite: Boolean?, youtubeLink: String?, imageLink: String?
    ) {
        _recipe.value?.let { currentRecipe ->
            val oldId = currentRecipe.id
            val updatedRecipe = currentRecipe.copy(
                name = name ?: currentRecipe.name,
                description = description ?: currentRecipe.description,
                instructions = instructions ?: currentRecipe.instructions,
                favorite = favorite ?: currentRecipe.favorite,
                youtubeLink = youtubeLink ?: currentRecipe.youtubeLink,
                imageLink = imageLink ?: currentRecipe.imageLink
            )

            updatedRecipe.id = oldId
            _recipe.value = updatedRecipe
        }
    }
}