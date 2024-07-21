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
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _ingredient = MutableLiveData<Ingredient>()
    val ingredient: LiveData<Ingredient> get() = _ingredient
    val allIngredients = ingredientRepository.getAll()

    fun getIngredient(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ingredientRepository.getById(id)
            _ingredient.postValue(result)
        }
    }

    fun updateIngredientName(newName: String) {
        _ingredient.value?.let { currentIngredient ->
            val updatedIngredient = currentIngredient.copy(name = newName)
            _ingredient.postValue(updatedIngredient)
        }
    }

    fun updateIngredientDescription(newDescription: String) {
        _ingredient.value?.let { currentIngredient ->
            val updatedIngredient = currentIngredient.copy(description = newDescription)
            _ingredient.postValue(updatedIngredient)
        }
    }

    fun updateIngredientToDB() {
        _ingredient.value?.let { currentIngredient ->
            updateIngredient(currentIngredient)
        }
    }

    fun insertIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
        ingredientRepository.insert(ingredient)
    }

    fun updateIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
        ingredientRepository.update(ingredient)
    }

    fun deleteIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO) {
        ingredientRepository.delete(ingredient)
    }
}