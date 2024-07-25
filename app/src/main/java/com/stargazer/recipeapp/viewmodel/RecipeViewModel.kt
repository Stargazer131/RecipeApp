package com.stargazer.recipeapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.model.IngredientQuantity
import com.stargazer.recipeapp.model.IngredientRecipe
import com.stargazer.recipeapp.model.Recipe
import com.stargazer.recipeapp.model.Step
import com.stargazer.recipeapp.repository.IngredientRecipeRepository
import com.stargazer.recipeapp.repository.IngredientRepository
import com.stargazer.recipeapp.repository.RecipeRepository
import com.stargazer.recipeapp.repository.StepRepository
import com.stargazer.recipeapp.utils.ImageStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientRecipeRepository: IngredientRecipeRepository,
    private val stepRepository: StepRepository,
    private val imageStorageManager: ImageStorageManager
) : ViewModel() {

    private var imageUri: Uri? = null
    val allRecipes = recipeRepository.getAll()
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> get() = _recipe
    private val _ingredientQuantityList = MutableLiveData<List<IngredientQuantity>>()
    val ingredientQuantityList: LiveData<List<IngredientQuantity>> get() = _ingredientQuantityList
    private val _stepList = MutableLiveData<List<Step>>()
    val stepList: LiveData<List<Step>> get() = _stepList

    ///////////////////////////////////////////////////////
    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    fun setRecipeData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeResult = recipeRepository.getById(id) ?: Recipe()
            _recipe.postValue(recipeResult)

            val ingredientsResult = ingredientRepository.getAllForRecipe(id)
            _ingredientQuantityList.postValue(ingredientsResult)

            val stepResult = stepRepository.getAllForRecipe(id)
            _stepList.postValue(stepResult)
        }
    }

    fun insertRecipeToDB() = viewModelScope.launch(Dispatchers.IO) {
        val recipeValue = withContext(Dispatchers.Main) { _recipe.value }
        val ingredientQuantityValue =
            withContext(Dispatchers.Main) { _ingredientQuantityList.value }
        val stepsValue = withContext(Dispatchers.Main) { _stepList.value }

        recipeValue?.let { item ->
            // add recipe
            val recipeResult = recipeRepository.insert(recipeValue)
            if (recipeResult == -1L) {
                _insertResult.postValue(false)
            } else {
                ///
                val fileName = "recipe_${recipeResult}.jpg"
                val imageLink = imageStorageManager.saveImageToInternalStorage(imageUri, fileName)
                if (imageLink != null) {
                    recipeRepository.updateImageLink(recipeResult, imageLink)
                    val tempRecipe = item.copy(imageLink = imageLink)
                    _recipe.postValue(tempRecipe)
                }
                ///

                // add ingredients
                ingredientQuantityValue?.let {
                    val ingredientRecipeList = ingredientQuantityValue.map {
                        IngredientRecipe(recipeResult, it.ingredient.id, it.quantity, it.unit)
                    }
                    ingredientRecipeRepository.insertAll(ingredientRecipeList)
                }

                // add steps
                stepsValue?.let { list ->
                    val tempList = ArrayList(list)
                    tempList.forEach { it.recipeId = recipeResult }
                    stepRepository.insertAll(tempList)
                }

                _insertResult.postValue(true)
            }
        }
    }

    fun updateRecipeToDB() = viewModelScope.launch(Dispatchers.IO) {
        val recipeValue = withContext(Dispatchers.Main) { _recipe.value }
        val ingredientQuantityValue =
            withContext(Dispatchers.Main) { _ingredientQuantityList.value }
        val stepsValue = withContext(Dispatchers.Main) { _stepList.value }

        recipeValue?.let { item ->
            // update recipe
            val recipeResult = recipeRepository.update(item)
            if (!recipeResult) {
                _updateResult.postValue(false)
            } else {
                ///
                val fileName = "recipe_${item.id}.jpg"
                val imageLink = imageStorageManager.saveImageToInternalStorage(imageUri, fileName)
                if (imageLink != null) {
                    recipeRepository.updateImageLink(item.id, imageLink)
                    val tempRecipe = item.copy(imageLink = imageLink)
                    tempRecipe.id = item.id
                    _recipe.postValue(tempRecipe)
                }
                ///

                // update ingredients
                ingredientQuantityValue?.let {
                    // delete all old ingredients
                    val deleteResult = ingredientRecipeRepository.deleteAllByRecipe(recipeValue.id)
                    val ingredientRecipeList = ingredientQuantityValue.map {
                        IngredientRecipe(recipeValue.id, it.ingredient.id, it.quantity, it.unit)
                    }
                    ingredientRecipeRepository.insertAll(ingredientRecipeList)
                }

                // update steps
                stepsValue?.let {
                    val deleteResult = stepRepository.deleteAllByRecipe(recipeValue.id)
                    stepRepository.insertAll(it)
                }

                _updateResult.postValue(true)
            }
        }
    }

    fun deleteRecipeFromDB() = viewModelScope.launch(Dispatchers.IO) {
        val recipeValue = withContext(Dispatchers.Main) { _recipe.value }

        recipeValue?.let {
            val deleteRecipeResult = recipeRepository.delete(it)
            val deleteImageResult = imageStorageManager.deleteImage(it.imageLink)
            _deleteResult.postValue(deleteRecipeResult)
        }
    }

    fun syncIngredientListFromRV(newList: List<IngredientQuantity>) {
        _ingredientQuantityList.value = newList
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
        name: String?, description: String?, favorite: Boolean?,
        youtubeLink: String?, imageLink: String?
    ) {
        _recipe.value?.let { currentRecipe ->
            val oldId = currentRecipe.id
            val updatedRecipe = currentRecipe.copy(
                name = name ?: currentRecipe.name,
                description = description ?: currentRecipe.description,
                favorite = favorite ?: currentRecipe.favorite,
                youtubeLink = youtubeLink ?: currentRecipe.youtubeLink,
                imageLink = imageLink ?: currentRecipe.imageLink
            )

            updatedRecipe.id = oldId
            _recipe.value = updatedRecipe
        }
    }

    fun updateImageLink(chosenImageUri: Uri?) {
        imageUri = chosenImageUri
    }

    fun addStepData(recipeId: Long) {
        _stepList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            val description = "Step ${tempList.size + 1}"
            val newStep = Step(description, tempList.size + 1, recipeId)
            tempList.add(newStep)
            _stepList.value = tempList
        }
    }

    fun deleteStepData(position: Int) {
        _stepList.value?.let { currentList ->
            val tempList = ArrayList(currentList)
            tempList.removeAt(position)
            for (index in 1..tempList.size) {
                tempList[index - 1].order = index
            }
            _stepList.value = tempList
        }
    }
}