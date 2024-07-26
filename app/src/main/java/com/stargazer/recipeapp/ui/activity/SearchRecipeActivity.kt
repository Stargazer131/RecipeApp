package com.stargazer.recipeapp.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.RecipeRVAdapter
import com.stargazer.recipeapp.utils.showToast
import com.stargazer.recipeapp.utils.stringToDate
import com.stargazer.recipeapp.viewmodel.SearchRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class SearchRecipeActivity : AppCompatActivity() {
    private val viewModel: SearchRecipeViewModel by viewModels()
    private lateinit var inputKeyword: TextInputEditText
    private lateinit var buttonSearch: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerSearchCriteria: Spinner
    private lateinit var spinnerSortCriteria: Spinner
    private lateinit var switchButton: MaterialSwitch
    private lateinit var adapter: RecipeRVAdapter
    private lateinit var inputStartDay: TextInputEditText
    private lateinit var inputEndDay: TextInputEditText
    private lateinit var inputLayoutStartDay: TextInputLayout
    private lateinit var inputLayoutEndDay: TextInputLayout
    private lateinit var inputLayoutKeyword: TextInputLayout
    private lateinit var spinnerFavorite: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        inputKeyword = findViewById(R.id.input_keyword)
        buttonSearch = findViewById(R.id.button_search)
        spinnerSearchCriteria = findViewById(R.id.spinner_search_criteria)
        spinnerSortCriteria = findViewById(R.id.spinner_sort_criteria)
        switchButton = findViewById(R.id.switch_button)
        inputStartDay = findViewById(R.id.input_start_day)
        inputEndDay = findViewById(R.id.input_end_day)
        inputLayoutStartDay = findViewById(R.id.input_layout_start_day)
        inputLayoutEndDay = findViewById(R.id.input_layout_end_day)
        inputLayoutKeyword = findViewById(R.id.input_layout_keyword)
        spinnerFavorite = findViewById(R.id.spinner_favorite)

        changeUIBasedOnCriteria()
        setUpRecyclerView()
        setUpSearch()
        setUpDateInput()
    }

    private fun setUpDateInput() {
        inputStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    inputStartDay.setText(selectedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        inputEndDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    inputEndDay.setText(selectedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }
    }

    private fun changeUIBasedOnCriteria() {
        spinnerSearchCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Name" -> {
                        inputLayoutStartDay.visibility = View.INVISIBLE
                        inputLayoutStartDay.hint = ""
                        inputLayoutEndDay.visibility = View.INVISIBLE
                        inputLayoutEndDay.hint = ""
                        inputLayoutKeyword.visibility = View.VISIBLE
                        inputLayoutKeyword.hint = "Keyword"
                    }

                    "Updated Time" -> {
                        inputLayoutStartDay.visibility = View.VISIBLE
                        inputLayoutStartDay.hint = "Start day"
                        inputLayoutEndDay.visibility = View.VISIBLE
                        inputLayoutEndDay.hint = "End day"
                        inputLayoutKeyword.visibility = View.INVISIBLE
                        inputLayoutKeyword.hint = ""
                    }

                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerSearchCriteria.setSelection(0)
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeRVAdapter(this)
        recyclerView.adapter = adapter

        viewModel.recipeList.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun setUpSearch() {
        buttonSearch.setOnClickListener {
            val sortCriteria = spinnerSortCriteria.selectedItem.toString()
            val descendingOrder = switchButton.isChecked
            val favorite = spinnerFavorite.selectedItem.toString()
            when (spinnerSearchCriteria.selectedItem.toString()) {
                "Name" -> {
                    val keyword = inputKeyword.text.toString()
                    viewModel.setSearchRecipesData(
                        keyword,
                        sortCriteria,
                        favorite,
                        descendingOrder
                    )
                }

                "Updated Time" -> {
                    searchByDate(sortCriteria, favorite, descendingOrder)
                }

                else -> {}
            }
        }
    }

    private fun searchByDate(sortCriteria: String, favorite: String, reverseOrder: Boolean) {
        val startDate = stringToDate(inputStartDay.text.toString()) ?: Date(0)
        val endDate = stringToDate(inputEndDay.text.toString()) ?: Date(Long.MAX_VALUE)

        if (startDate > endDate) {
            showToast(this, "Start date can't be larger than End date")
            return
        }

        viewModel.setSearchRecipesData(startDate, endDate, sortCriteria, favorite, reverseOrder)
    }
}