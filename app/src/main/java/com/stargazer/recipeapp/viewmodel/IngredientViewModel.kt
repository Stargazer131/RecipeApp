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

    private val _ingredient = MutableLiveData(Ingredient("", null, null))
    val ingredient: LiveData<Ingredient> get() = _ingredient
    val allIngredients = ingredientRepository.getAll()

    fun setIngredientData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ingredientRepository.getById(id)
            _ingredient.postValue(result)
        }
    }

    fun updateIngredientData(name: String?, description: String?, imageLink: String?) {
        _ingredient.value?.let { currentIngredient ->
            val updatedIngredient = currentIngredient.copy(
                name = name ?: currentIngredient.name,
                description = description ?: currentIngredient.description,
                imageLink = imageLink ?: currentIngredient.imageLink
            )
            _ingredient.value = updatedIngredient
        }
    }

    fun insertIngredientToDB() = viewModelScope.launch(Dispatchers.IO) {
        ingredient.value?.let { ingredientRepository.insert(it) }
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