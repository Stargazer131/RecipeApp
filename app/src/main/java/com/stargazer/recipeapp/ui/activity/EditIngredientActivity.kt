package com.stargazer.recipeapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.utils.showToast
import com.stargazer.recipeapp.utils.showYesNoDialog
import com.stargazer.recipeapp.viewmodel.IngredientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditIngredientActivity : AppCompatActivity() {
    private val viewModel: IngredientViewModel by viewModels()
    private lateinit var imageView: ImageView
    private lateinit var inputName: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonSave: Button
    private var ingredientId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ingredient)

        ingredientId = intent.getLongExtra("ingredientId", -1)
        imageView = findViewById(R.id.image_view)
        inputName = findViewById(R.id.input_name)
        inputDescription = findViewById(R.id.input_description)
        buttonDelete = findViewById(R.id.button_delete)
        buttonSave = findViewById(R.id.button_save)

        setUpButton()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.setIngredientData(ingredientId)

        viewModel.ingredient.observe(this) { item ->
            item?.let {
                inputName.setText(item.name)
                inputDescription.setText(item.description)
            }
        }

        if (ingredientId == -1L) {
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

    private fun setUpButton() {
        if (ingredientId == -1L) {
            buttonDelete.isEnabled = false
        }

        buttonDelete.setOnClickListener {
            showYesNoDialog(this, "Confirm Delete", "Are you sure you want to delete this item?") {
                viewModel.deleteIngredientFromDB()
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
        viewModel.updateIngredientData(name, description, null)

        if (ingredientId == -1L) {
            viewModel.insertIngredientToDB()
        } else {
            viewModel.updateIngredientToDB()
        }
    }

    private fun checkValidData(): Boolean {
        val name = inputName.text.toString()

        if (name.isEmpty()) {
            inputName.error = "Empty field!"
            return false
        }

        return true
    }
}