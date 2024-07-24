package com.stargazer.recipeapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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
import com.stargazer.recipeapp.utils.showToast
import com.stargazer.recipeapp.utils.showYesNoDialog
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
    private lateinit var checkBoxFavorite: CheckBox
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
        checkBoxFavorite = findViewById(R.id.check_box_favorite)
        buttonDelete = findViewById(R.id.button_delete)
        buttonSave = findViewById(R.id.button_save)

        viewModel.setRecipeData(recipeId)
        setUpLauncher()
        setUpObserverAndRV()
        setUpButton()
        setUpCRUDObserver()
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

        viewModel.recipe.observe(this) { item ->
            item?.let {
                inputName.setText(item.name)
                inputDescription.setText(item.description)
                inputInstructions.setText(item.instructions)
                checkBoxFavorite.isChecked = item.favorite
                inputYoutubeLink.setText(item.youtubeLink)
            }
        }
    }

    private fun setUpCRUDObserver() {
        if (recipeId == -1L) {
            viewModel.insertResult.observe(this) { item ->
                item?.let {
                    if (it) {
                        showToast(this, "Insert successfully")
                    } else {
                        showToast(this, "Insert fail")
                    }
                }
            }
        } else {
            viewModel.updateResult.observe(this) { item ->
                item?.let {
                    if (it) {
                        showToast(this, "Update successfully")
                    } else {
                        showToast(this, "Update fail")
                    }
                }
            }

            viewModel.deleteResult.observe(this) { item ->
                item?.let {
                    if (it) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        showToast(this, "Delete fail")
                    }
                }
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

    private fun setUpButton() {
        if (recipeId == -1L) {
            buttonDelete.isEnabled = false
        }

        buttonDelete.setOnClickListener {
            showYesNoDialog(this, "Confirm Delete", "Are you sure you want to delete this item?") {
                viewModel.deleteRecipeFromDB()
            }
        }

        buttonSave.setOnClickListener {
            showYesNoDialog(this, "Confirm Save", "Are you sure you want to save the change?") {
                saveData()
            }
        }
    }

    private fun saveData() {
        if (!checkValidData()) {
            return
        }

        val name = inputName.text.toString()
        val description = inputDescription.text.toString()
        val instructions = inputInstructions.text.toString()
        val youtubeLink = inputYoutubeLink.text.toString()
        val favorite = checkBoxFavorite.isChecked

        viewModel.updateRecipeData(name, description, instructions, favorite, youtubeLink, null)
        if (recipeId == -1L) {
            viewModel.insertRecipeToDB()
        } else {
            viewModel.updateRecipeToDB()
        }
    }

    private fun checkValidData(): Boolean {
        val name = inputName.text.toString()
        val instructions = inputInstructions.text.toString()

        if (name.isEmpty()) {
            inputName.error = "Empty field!"
            return false
        }

        if (instructions.isEmpty()) {
            inputInstructions.error = "Empty field!"
            return false
        }

        return true
    }

    override fun onDeleteIconClick(position: Int) {
        viewModel.deleteIngredientData(position)
    }
}