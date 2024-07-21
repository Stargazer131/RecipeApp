package com.stargazer.recipeapp.ui.activity


import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.ui.fragment.IngredientListFragment
import com.stargazer.recipeapp.ui.fragment.RecipeListFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentContainer = findViewById(R.id.fragment_container)

        setupNavigation()
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
