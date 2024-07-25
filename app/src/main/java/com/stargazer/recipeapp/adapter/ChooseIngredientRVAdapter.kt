package com.stargazer.recipeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.model.Ingredient
import com.stargazer.recipeapp.utils.MutablePair

class ChooseIngredientRVAdapter(
    val context: Context
) :
    RecyclerView.Adapter<ChooseIngredientRVAdapter.ViewHolder>() {

    private val allIngredients = ArrayList<MutablePair<Ingredient, Boolean>>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentView: CardView = itemView.findViewById(R.id.parent_layout)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val checkBox: CheckBox = itemView.findViewById(R.id.check_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.choose_ingredient_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = allIngredients[position].first.name
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            allIngredients[position].second = isChecked
        }

        val imageLink = allIngredients[position].first.imageLink
        if (imageLink == null) {
            holder.imageView.setImageResource(R.drawable.ingredient)
        } else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageLink))
        }
    }

    override fun getItemCount(): Int {
        return allIngredients.size
    }

    fun getSelectedIngredients(): ArrayList<Ingredient> {
        return allIngredients.filter { it.second }.map { it.first } as ArrayList<Ingredient>
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Ingredient>) {
        allIngredients.clear()
        newList.forEach {
            allIngredients.add(MutablePair(it, false))
        }
        notifyDataSetChanged()
    }
}
