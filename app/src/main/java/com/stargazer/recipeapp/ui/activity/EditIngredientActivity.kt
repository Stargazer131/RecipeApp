package com.stargazer.recipeapp.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.utils.showToast
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
                inputDescription.setText(item.description ?: "")
            }
        }

        if (ingredientId == -1L) {
            viewModel.insertResult.observe(this) { item ->
                item?.let {
                    if (item != -1L) {
                        showToast(this, "Insert successfully")
                    } else {
                        showToast(this, "Insert fail")
                    }
                }
            }
        } else {
            viewModel.updateResult.observe(this) { item ->
                item?.let {
                    if (item) {
                        showToast(this, "Update successfully")
                    } else {
                        showToast(this, "Update fail")
                    }
                }
            }
        }
    }

    private fun setUpButton() {
        if (ingredientId == -1L) {
            buttonDelete.isEnabled = false
        }

        buttonSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        if (!checkValidData()) {
            return
        }

        val name = inputName.text.toString()
        val description = inputDescription.text.toString()

        if (ingredientId == -1L) {
            viewModel.updateIngredientData(name, description, null)
            viewModel.insertIngredientToDB()
        } else {
            viewModel.updateIngredientData(name, description, null)
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