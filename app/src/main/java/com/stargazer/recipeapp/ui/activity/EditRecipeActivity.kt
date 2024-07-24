package com.stargazer.recipeapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.IngredientQuantityRVAdapter
import com.stargazer.recipeapp.adapter.OnIngredientChangeListener
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeActivity : AppCompatActivity(), OnIngredientChangeListener {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var imageView: ImageView
    private lateinit var inputName: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputInstructions: TextInputEditText
    private lateinit var inputYoutubeLink: TextInputEditText
    private lateinit var buttonAddIngredient: FloatingActionButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonSave: Button
    private var recipeId: Long = 0L
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientQuantityRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        recipeId = intent.getLongExtra("recipeId", -1)
        imageView = findViewById(R.id.image_view)
        inputName = findViewById(R.id.input_name)
        inputDescription = findViewById(R.id.input_description)
        inputInstructions = findViewById(R.id.input_instruction)
        inputYoutubeLink = findViewById(R.id.input_youtube_link)
        buttonAddIngredient = findViewById(R.id.button_add)
        buttonDelete = findViewById(R.id.button_delete)
        buttonSave = findViewById(R.id.button_save)

        viewModel.setRecipeData(recipeId)
        setUpLauncher()
        setUpObserverAndRV()
    }

    private fun setUpObserverAndRV() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = IngredientQuantityRVAdapter(this, this)
        recyclerView.adapter = adapter

        viewModel.ingredientQuantityList.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun setUpLauncher() {
        val chooseIngredientLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val returnedResult =
                    data?.getSerializableExtra("ingredientList") as? ArrayList<Ingredient>
                viewModel.addIngredientData(returnedResult ?: ArrayList(), recipeId)
            }
        }

        buttonAddIngredient.setOnClickListener {
            viewModel.syncIngredientListFromRV(adapter.getIngredientList())
            val intent = Intent(this, ChooseIngredientActivity::class.java)
            intent.putExtra("recipeId", recipeId)
            chooseIngredientLauncher.launch(intent)
        }
    }

    override fun onDeleteIconClick(position: Int) {
        viewModel.deleteIngredientData(position)
    }
}