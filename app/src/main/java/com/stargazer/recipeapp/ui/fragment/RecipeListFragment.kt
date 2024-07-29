package com.stargazer.recipeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.adapter.RecipeRVAdapter
import com.stargazer.recipeapp.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecipeRVAdapter(requireContext())
        recyclerView.adapter = adapter

        viewModel.allRecipes.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipeListFragment()
    }
}