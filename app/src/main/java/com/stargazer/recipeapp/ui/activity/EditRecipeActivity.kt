package com.stargazer.recipeapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.IngredientQuantityRVAdapter
import com.stargazer.recipeapp.adapter.OnIngredientChangeListener
import com.stargazer.recipeapp.adapter.OnStepChangeListener
import com.stargazer.recipeapp.adapter.StepRVAdapter
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.utils.showToast
import com.stargazer.recipeapp.utils.showYesNoDialog
import com.stargazer.recipeapp.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeActivity : AppCompatActivity(), OnIngredientChangeListener, OnStepChangeListener {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var imageView: ImageView
    private lateinit var inputName: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputYoutubeLink: TextInputEditText
    private lateinit var buttonAddIngredient: FloatingActionButton
    private lateinit var buttonAddStep: FloatingActionButton
    private lateinit var buttonYoutube: FloatingActionButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonSave: Button
    private lateinit var checkBoxFavorite: CheckBox

    private lateinit var recyclerViewIngredient: RecyclerView
    private lateinit var adapterIngredient: IngredientQuantityRVAdapter
    private lateinit var recyclerViewStep: RecyclerView
    private lateinit var adapterStep: StepRVAdapter
    private var chosenImageUri: Uri? = null
    private var recipeId: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        recipeId = intent.getLongExtra("recipeId", -1)
        imageView = findViewById(R.id.image_view)
        inputName = findViewById(R.id.input_name)
        inputDescription = findViewById(R.id.input_description)
        inputYoutubeLink = findViewById(R.id.input_youtube_link)
        buttonAddIngredient = findViewById(R.id.button_add_ingredient)
        buttonAddStep = findViewById(R.id.button_add_step)
        buttonYoutube = findViewById(R.id.button_youtube)
        checkBoxFavorite = findViewById(R.id.check_box_favorite)
        buttonDelete = findViewById(R.id.button_delete)
        buttonSave = findViewById(R.id.button_save)

        viewModel.setRecipeData(recipeId)
        setUpChooseIngredientLauncher()
        setUpObserverAndRV()
        setUpButton()
        setUpCRUDObserver()
        setUpImagePicker()
    }

    private fun setUpImagePicker() {
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    chosenImageUri = fileUri
                    imageView.setImageURI(fileUri)
                }
            }

        imageView.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(2048) //Final image size
                .maxResultSize(1080, 1080)  //Final image resolution
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }


    private fun setUpObserverAndRV() {
        recyclerViewIngredient = findViewById(R.id.recycler_view_ingredient)
        recyclerViewIngredient.layoutManager = LinearLayoutManager(this)
        adapterIngredient = IngredientQuantityRVAdapter(this, this)
        recyclerViewIngredient.adapter = adapterIngredient

        viewModel.ingredientQuantityList.observe(this) { list ->
            list?.let {
                adapterIngredient.updateList(it)
            }
        }

        ///////////////////

        recyclerViewStep = findViewById(R.id.recycler_view_step)
        recyclerViewStep.layoutManager = LinearLayoutManager(this)
        adapterStep = StepRVAdapter(this, this)
        recyclerViewStep.adapter = adapterStep

        viewModel.stepList.observe(this) { list ->
            list?.let {
                adapterStep.updateList(it)
            }
        }

        ///////////////////

        viewModel.recipe.observe(this) { item ->
            item?.let {
                inputName.setText(item.name)
                inputDescription.setText(item.description)
                checkBoxFavorite.isChecked = item.favorite
                inputYoutubeLink.setText(item.youtubeLink)
                if (item.imageLink == null) {
                    imageView.setImageResource(R.drawable.recipe)
                } else {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(item.imageLink))
                }
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
                        intent.putExtra("selectedView", "recipe")
                        startActivity(intent)
                    } else {
                        showToast(this, "Delete fail")
                    }
                }
            }
        }
    }

    private fun setUpChooseIngredientLauncher() {
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

        buttonAddStep.setOnClickListener {
            viewModel.addStepData(recipeId)
        }

        buttonYoutube.setOnClickListener {
            try {
                val youtubeUrl = inputYoutubeLink.text.toString()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                startActivity(intent)
            } catch (_: Exception) {
                showToast(this, "Invalid URL")
            }
        }
    }

    private fun saveData() {
        if (!checkValidData()) {
            return
        }

        val name = inputName.text.toString().trim()
        val description = inputDescription.text.toString().trim()
        val youtubeLink = inputYoutubeLink.text.toString().trim()
        val favorite = checkBoxFavorite.isChecked

        viewModel.updateRecipeData(name, description, favorite, youtubeLink, null)
        viewModel.updateImageLink(chosenImageUri)
        clearAllFocus()

        if (recipeId == -1L) {
            viewModel.insertRecipeToDB()
        } else {
            viewModel.updateRecipeToDB()
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

    private fun clearAllFocus() {
        for (i in 0 until recyclerViewStep.childCount) {
            val child = recyclerViewStep.getChildAt(i)
            val inputStep = child.findViewById<TextInputEditText>(R.id.input_step)
            if (inputStep != null && inputStep.hasFocus()) {
                inputStep.clearFocus()
                break
            }
        }

        for (i in 0 until recyclerViewIngredient.childCount) {
            val child = recyclerViewIngredient.getChildAt(i)
            val inputQuantity = child.findViewById<TextInputEditText>(R.id.input_quantity)
            val inputUnit = child.findViewById<TextInputEditText>(R.id.input_unit)

            if (inputQuantity != null && inputQuantity.hasFocus()) {
                inputQuantity.clearFocus()
                break
            }

            if (inputUnit != null && inputUnit.hasFocus()) {
                inputUnit.clearFocus()
                break
            }
        }
    }

    override fun onDeleteIngredientClick(position: Int) {
        clearAllFocus()
        viewModel.deleteIngredientData(position)
    }

    override fun onDeleteStepClick(position: Int) {
        clearAllFocus()
        viewModel.deleteStepData(position)
    }
}