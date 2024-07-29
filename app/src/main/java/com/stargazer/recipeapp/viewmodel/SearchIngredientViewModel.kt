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
class SearchIngredientViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _ingredientList = MutableLiveData<List<Ingredient>>()
    val ingredientList: LiveData<List<Ingredient>> get() = _ingredientList

    /**
     * Set the data for MutableLiveData
     */
    fun setIngredientsData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ingredientRepository.getAllNotInRecipe(id)
            _ingredientList.postValue(result)
        }
    }

    /**
     * Set the data for MutableLiveData, only ingredients that isn't related to the recipe
     */
    fun setSearchIngredientsData(keyword: String, recipeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val tempList = ingredientRepository.getAllForRecipe(recipeId)
            val ingredientList = tempList.map { it.ingredient }
            val result = ArrayList(ingredientRepository.getAllByName(keyword))
            result.removeAll(ingredientList.toSet())
            _ingredientList.postValue(result)
        }
    }

    /**
     * Set the data for MutableLiveData
     */
    fun setSearchIngredientsData(keywordName: String, reverseOrder: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = ingredientRepository.getAllByName(keywordName)
            if (reverseOrder) {
                result = result.reversed()
            }

            _ingredientList.postValue(result)
        }
    }
}