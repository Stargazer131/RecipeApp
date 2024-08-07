package com.stargazer.recipeapp.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.IngredientRVAdapter
import com.stargazer.recipeapp.viewmodel.SearchIngredientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchIngredientActivity : AppCompatActivity() {
    private val viewModel: SearchIngredientViewModel by viewModels()
    private lateinit var inputKeyword: TextInputEditText
    private lateinit var buttonSearch: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerSearchCriteria: Spinner
    private lateinit var spinnerSortCriteria: Spinner
    private lateinit var switchButton: MaterialSwitch
    private lateinit var adapter: IngredientRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredient)

        inputKeyword = findViewById(R.id.input_keyword)
        buttonSearch = findViewById(R.id.button_search)
        spinnerSearchCriteria = findViewById(R.id.spinner_search_criteria)
        spinnerSortCriteria = findViewById(R.id.spinner_sort_criteria)
        switchButton = findViewById(R.id.switch_button)

        setUpRecyclerView()
        setUpSearch()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = IngredientRVAdapter(this)
        recyclerView.adapter = adapter

        viewModel.ingredientList.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun setUpSearch() {
        buttonSearch.setOnClickListener {
            val keyword = inputKeyword.text.toString().trim()
            val searchCriteria = spinnerSearchCriteria.selectedItem.toString()
            val sortCriteria = spinnerSortCriteria.selectedItem.toString()
            val descendingOrder = switchButton.isChecked
            viewModel.setSearchIngredientsData(keyword, descendingOrder)
        }
    }
}