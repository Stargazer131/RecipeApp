package com.stargazer.recipeapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.repository.IngredientRecipeRepository
import com.stargazer.recipeapp.repository.IngredientRepository
import com.stargazer.recipeapp.utils.ImageStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val ingredientRecipeRepository: IngredientRecipeRepository,
    private val imageStorageManager: ImageStorageManager
) : ViewModel() {

    private var imageUri: Uri? = null
    val allIngredients = ingredientRepository.getAll()
    private val _ingredient = MutableLiveData<Ingredient>()
    val ingredient: LiveData<Ingredient> get() = _ingredient

    ///////////////////////////////////////////////////////
    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    fun setIngredientData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ingredientRepository.getById(id) ?: Ingredient()
            _ingredient.postValue(result)
        }
    }

    fun updateIngredientData(name: String?, description: String?, imageLink: String?) {
        _ingredient.value?.let { currentIngredient ->
            val oldId = currentIngredient.id
            val updatedIngredient = currentIngredient.copy(
                name = name ?: currentIngredient.name,
                description = description ?: currentIngredient.description,
                imageLink = imageLink ?: currentIngredient.imageLink
            )

            updatedIngredient.id = oldId
            _ingredient.value = updatedIngredient
        }
    }

    fun insertIngredientToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
            ingredientValue?.let {
                val result = ingredientRepository.insert(it)
                if (result != -1L) {
                    ///
                    val fileName = "ingredient_${result}.jpg"
                    val imageLink =
                        imageStorageManager.saveImageToInternalStorage(imageUri, fileName)
                    if (imageLink != null) {
                        ingredientRepository.updateImageLink(result, imageLink)
                        val tempIngredient = it.copy(imageLink = imageLink)
                        _ingredient.postValue(tempIngredient)
                    }
                    ///
                }
                _insertResult.postValue(result != -1L)
            }
        }
    }

    fun updateIngredientToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
            ingredientValue?.let {
                val result = ingredientRepository.update(it)
                _updateResult.postValue(result)

                ///
                val fileName = "ingredient_${it.id}.jpg"
                val imageLink = imageStorageManager.saveImageToInternalStorage(imageUri, fileName)
                if (imageLink != null) {
                    ingredientRepository.updateImageLink(it.id, imageLink)
                    val tempIngredient = it.copy(imageLink = imageLink)
                    tempIngredient.id = it.id
                    _ingredient.postValue(tempIngredient)
                }
                ///
            }
        }
    }

    fun deleteIngredientFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
            ingredientValue?.let {
                val result = ingredientRepository.delete(it)
                val deleteImageResult = imageStorageManager.deleteImage(it.imageLink)
                _deleteResult.postValue(result)
            }
        }
    }

    fun updateImageLink(chosenImageUri: Uri?) {
        imageUri = chosenImageUri
    }
}