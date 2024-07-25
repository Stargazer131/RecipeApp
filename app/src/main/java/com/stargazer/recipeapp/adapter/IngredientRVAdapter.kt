package com.stargazer.recipeapp.adapter

import android.annotation.SuppressLint
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
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.ui.activity.EditIngredientActivity

class IngredientRVAdapter(
    val context: Context
) :
    RecyclerView.Adapter<IngredientRVAdapter.ViewHolder>() {

    private val allIngredients = ArrayList<Ingredient>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentView: CardView = itemView.findViewById(R.id.parent_layout)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textName: TextView = itemView.findViewById(R.id.text_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.ingredient_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = allIngredients[position].name
        holder.parentView.setOnClickListener {
            val intent = Intent(context, EditIngredientActivity::class.java)
            intent.putExtra("ingredientId", allIngredients[position].id)
            context.startActivity(intent)
        }

        val imageLink = allIngredients[position].imageLink
        if (imageLink == null) {
            holder.imageView.setImageResource(R.drawable.ingredient)
        } else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageLink))
        }
    }

    override fun getItemCount(): Int {
        return allIngredients.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Ingredient>) {
        allIngredients.clear()
        allIngredients.addAll(newList)
        notifyDataSetChanged()
    }
}