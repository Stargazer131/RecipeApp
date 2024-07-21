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
import com.stargazer.recipeapp.adapter.IngredientRVAdapter
import com.stargazer.recipeapp.viewmodel.IngredientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientListFragment : Fragment() {
    private val viewModel: IngredientViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredient_list, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = IngredientRVAdapter(requireContext())
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.allIngredients.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = IngredientListFragment()
    }
}
