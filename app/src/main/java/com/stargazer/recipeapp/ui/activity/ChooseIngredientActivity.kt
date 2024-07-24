package com.stargazer.recipeapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.ChooseIngredientRVAdapter
import com.stargazer.recipeapp.utils.showToast
import com.stargazer.recipeapp.utils.showYesNoDialog
import com.stargazer.recipeapp.viewmodel.SearchIngredientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseIngredientActivity : AppCompatActivity() {
    private val viewModel: SearchIngredientViewModel by viewModels()
    private lateinit var inputKeyword: TextInputEditText
    private lateinit var buttonSearch: ImageButton
    private lateinit var buttonChoose: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChooseIngredientRVAdapter
    private var recipeId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_ingredient)

        inputKeyword = findViewById(R.id.input_keyword)
        buttonSearch = findViewById(R.id.button_search)
        buttonChoose = findViewById(R.id.button_choose)
        recipeId = intent.getLongExtra("recipeId", -1)

        setUpRecyclerView()
        setUpChooseButton()
    }

    private fun setUpRecyclerView() {
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChooseIngredientRVAdapter(this)
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.ingredientList.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        viewModel.setSearchIngredientData(recipeId)
    }

    private fun setUpChooseButton() {
        buttonChoose.setOnClickListener {
            showYesNoDialog(this, "Confirm choose", "You want to select these ingredients?") {
                val ingredientList = adapter.getSelectedIngredients()
                if (ingredientList.isEmpty()) {
                    showToast(this, "You haven't chose any ingredient")
                } else {
                    val resultIntent = Intent()
                    resultIntent.putExtra("ingredientList", ingredientList)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}