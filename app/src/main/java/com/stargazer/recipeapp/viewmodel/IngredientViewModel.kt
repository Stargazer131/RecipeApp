package com.stargazer.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.repository.IngredientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    val allIngredients = ingredientRepository.getAll()
    private val _ingredient = MutableLiveData<Ingredient>()
    val ingredient: LiveData<Ingredient> get() = _ingredient

    ///
    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> get() = _insertResult

    ////
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult

    ////
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    fun setIngredientData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ingredientRepository.getById(id) ?: Ingredient("", null, null)
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

    fun insertIngredientToDB() = viewModelScope.launch(Dispatchers.IO) {
        val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
        ingredientValue?.let {
            val result = ingredientRepository.insert(it)
            _insertResult.postValue(result)
        }
    }

    fun updateIngredientToDB() = viewModelScope.launch(Dispatchers.IO) {
        val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
        ingredientValue?.let {
            val result = ingredientRepository.update(it)
            _updateResult.postValue(result)
        }
    }

    fun deleteIngredientFromDB() = viewModelScope.launch(Dispatchers.IO) {
        val ingredientValue = withContext(Dispatchers.Main) { _ingredient.value }
        ingredientValue?.let {
            val result = ingredientRepository.delete(it)
            _deleteResult.postValue(result)
        }
    }

//    fun insertIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
//        ingredientRepository.insert(ingredient)
//    }
//
//    fun updateIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
//        ingredientRepository.update(ingredient)
//    }
//
//    fun deleteIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
//        ingredientRepository.delete(ingredient)
//    }
}