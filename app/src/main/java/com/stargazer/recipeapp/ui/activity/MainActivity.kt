package com.stargazer.recipeapp.ui.activity


import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentContainer = findViewById(R.id.fragment_container)
        buttonAdd = findViewById(R.id.button_add)

        setupNavigation()
        setUpButton()
    }

    private fun setUpButton() {
        buttonAdd.setOnClickListener {
            when (bottomNavigationView.selectedItemId) {
                R.id.menu_recipe -> {

                }

                R.id.menu_ingredient -> {
                    val intent = Intent(this, EditIngredientActivity::class.java)
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

        bottomNavigationView.selectedItemId = R.id.menu_recipe
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
