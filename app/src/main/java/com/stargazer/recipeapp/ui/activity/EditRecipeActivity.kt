package com.stargazer.recipeapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.IngredientRVAdapter
import com.stargazer.recipeapp.viewmodel.IngredientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeActivity : AppCompatActivity() {
    private val viewModel: IngredientViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = IngredientRVAdapter(this)
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.allIngredients.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }
    }
}