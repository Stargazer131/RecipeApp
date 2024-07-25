package com.stargazer.recipeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.model.IngredientQuantity
import com.stargazer.recipeapp.utils.removeAtIndices
import com.stargazer.recipeapp.utils.showYesNoDialog

class IngredientQuantityRVAdapter(
    val context: Context,
    val ingredientChangeListener: OnIngredientChangeListener
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

        val imageLink = allIngredientQuantity[position].ingredient.imageLink
        if (imageLink == null) {
            holder.imageView.setImageResource(R.drawable.ingredient)
        } else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageLink))
        }

        holder.buttonDelete.setOnClickListener {
            showYesNoDialog(
                context,
                "Confirm delete",
                "Are you sure you want to delete this ingredient?"
            ) {
                ingredientChangeListener.onDeleteIngredientClick(position)
            }
        }

        holder.inputUnit.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                var unit = (view as? EditText)?.text?.toString()
                if (unit.isNullOrEmpty()) {
                    unit = "unit"
                }
                allIngredientQuantity[position].unit = unit
            }
        }

        holder.inputQuantity.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val quantity = (view as? EditText)?.text?.toString()?.toDoubleOrNull()
                allIngredientQuantity[position].quantity = quantity ?: 0.0
            }
        }

    }

    override fun getItemCount(): Int {
        return allIngredientQuantity.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<IngredientQuantity>) {
        val filterIndices: ArrayList<Int> = arrayListOf()
        for (i in newList.lastIndex downTo 0) {
            for (j in i - 1 downTo 0) {
                val ingredientA = newList[i]
                val ingredientB = newList[j]
                if (ingredientA.ingredient.id == ingredientB.ingredient.id) {
                    filterIndices.add(i)
                }
            }
        }

        allIngredientQuantity.clear()
        allIngredientQuantity.addAll(newList.removeAtIndices(filterIndices))
        notifyDataSetChanged()
    }

    fun getIngredientList(): List<IngredientQuantity> {
        return ArrayList(allIngredientQuantity)
    }

    private fun quantityToString(quantity: Double): String {
        return if (quantity % 1.0 == 0.0) {
            quantity.toInt().toString()
        } else {
            String.format("%.2f", quantity)
        }
    }
}

interface OnIngredientChangeListener {
    //    fun onUnitChanged(position: Int, unit: String)
//    fun onQuantityChanged(position: Int, quantity: Double)
    fun onDeleteIngredientClick(position: Int)
}
