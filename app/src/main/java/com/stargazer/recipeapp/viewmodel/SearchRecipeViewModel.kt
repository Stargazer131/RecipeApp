package com.stargazer.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.recipeapp.model.Recipe
import com.stargazer.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipeList = MutableLiveData<List<Recipe>>()
    val recipeList: LiveData<List<Recipe>> get() = _recipeList

    /**
     * Set the data for MutableLiveData
     */
    fun setSearchRecipesData(
        keywordName: String,
        sortCriteria: String,
        favorite: String,
        reverseOrder: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = recipeRepository.getAllByName(keywordName)
            when (favorite) {
                "Yes" -> result = result.filter { it.favorite }
                "No" -> result = result.filter { !it.favorite }
                else -> {}
            }

            if (reverseOrder) {
                result = result.reversed()
            }

            _recipeList.postValue(result)
        }
    }

    /**
     * Set the data for MutableLiveData
     */
    fun setSearchRecipesData(
        startDay: Date,
        endDay: Date,
        sortCriteria: String,
        favorite: String,
        reverseOrder: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = recipeRepository.getAllByDateRange(startDay, endDay)
            when (favorite) {
                "Yes" -> result = result.filter { it.favorite }
                "No" -> result = result.filter { !it.favorite }
                else -> {}
            }
            if (reverseOrder) {
                result = result.reversed()
            }

            _recipeList.postValue(result)
        }
    }
}