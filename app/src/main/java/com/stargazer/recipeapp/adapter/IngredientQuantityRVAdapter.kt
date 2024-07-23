package com.stargazer.recipeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.model.IngredientQuantity

class IngredientQuantityRVAdapter(
    val context: Context,
    val clickDeleteInterface: ClickDeleteInterface
) :
    RecyclerView.Adapter<IngredientQuantityRVAdapter.ViewHolder>() {

    private val allIngredientQuantity = ArrayList<IngredientQuantity>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentView: CardView = itemView.findViewById(R.id.parent_layout)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val inputQuantity: TextInputEditText = itemView.findViewById(R.id.input_quantity)
        val inputUnit: TextInputEditText = itemView.findViewById(R.id.input_unit)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.ingredient_quantity_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = allIngredientQuantity[position].ingredient.name
        holder.inputQuantity.setText(quantityToString(allIngredientQuantity[position].quantity))
        holder.inputUnit.setText(allIngredientQuantity[position].unit)

        holder.buttonDelete.setOnClickListener {
            clickDeleteInterface.onDeleteIconClick(position)
        }
    }

    override fun getItemCount(): Int {
        return allIngredientQuantity.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<IngredientQuantity>) {
        allIngredientQuantity.clear()
        allIngredientQuantity.addAll(newList)
        notifyDataSetChanged()
    }

    private fun quantityToString(quantity: Double): String {
        return if (quantity % 1.0 == 0.0) {
            quantity.toInt().toString()
        } else {
            String.format("%.2f", quantity)
        }
    }
}

interface ClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(position: Int)
}