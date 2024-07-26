package com.stargazer.recipeapp.ui.activity


import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.ui.fragment.IngredientListFragment
import com.stargazer.recipeapp.ui.fragment.RecipeListFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var buttonSearch: ImageButton
    private lateinit var selectedView: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentContainer = findViewById(R.id.fragment_container)
        buttonAdd = findViewById(R.id.button_add)
        buttonSearch = findViewById(R.id.button_search)
        selectedView = intent.getStringExtra("selectedView") ?: "recipe"

        setupNavigation()
        setUpAddButton()
        setUpSearchButton()
    }

    private fun setUpAddButton() {
        buttonAdd.setOnClickListener {
            when (bottomNavigationView.selectedItemId) {
                R.id.menu_recipe -> {
                    val intent = Intent(this, EditRecipeActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_ingredient -> {
                    val intent = Intent(this, EditIngredientActivity::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
        }
    }

    private fun setUpSearchButton() {
        buttonSearch.setOnClickListener {
            when (bottomNavigationView.selectedItemId) {
                R.id.menu_recipe -> {
                    val intent = Intent(this, SearchRecipeActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_ingredient -> {
                    val intent = Intent(this, SearchIngredientActivity::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
        }
    }

    private fun setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_recipe -> {
                    loadFragment(RecipeListFragment.newInstance())
                    true
                }

                R.id.menu_ingredient -> {
                    loadFragment(IngredientListFragment.newInstance())
                    true
                }

                else -> false
            }
        }

        if (selectedView == "ingredient") {
            bottomNavigationView.selectedItemId = R.id.menu_ingredient
        } else {
            bottomNavigationView.selectedItemId = R.id.menu_recipe
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
