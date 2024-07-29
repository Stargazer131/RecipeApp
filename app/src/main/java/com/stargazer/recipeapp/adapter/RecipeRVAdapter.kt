package com.stargazer.recipeapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.model.Recipe
import com.stargazer.recipeapp.ui.activity.EditRecipeActivity

class RecipeRVAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecipeRVAdapter.ViewHolder>() {

    private val allRecipes = ArrayList<Recipe>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentView: CardView = itemView.findViewById(R.id.parent_layout)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textName: TextView = itemView.findViewById(R.id.text_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recipe_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = allRecipes[position].name
        holder.parentView.setOnClickListener {
            val intent = Intent(context, EditRecipeActivity::class.java)
            intent.putExtra("recipeId", allRecipes[position].id)
            context.startActivity(intent)
        }

        val imageLink = allRecipes[position].imageLink
        if (imageLink == null) {
            holder.imageView.setImageResource(R.drawable.recipe)
        } else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageLink))
        }
    }

    override fun getItemCount(): Int {
        return allRecipes.size
    }

    /**
     * This function pass a reference for every object in newList
     * to update the data, so beware that the change made to data in adapter may be reflected
     * to outside data
     */
    fun updateList(newList: List<Recipe>) {
        allRecipes.clear()
        allRecipes.addAll(newList)
        notifyDataSetChanged()
    }
}