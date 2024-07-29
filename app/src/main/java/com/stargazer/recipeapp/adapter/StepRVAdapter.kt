package com.stargazer.recipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.stargazer.recipeapp.R
import com.stargazer.recipeapp.model.Step
import com.stargazer.recipeapp.utils.showYesNoDialog

class StepRVAdapter(
    val context: Context,
    private val stepChangeListener: OnStepChangeListener
) :
    RecyclerView.Adapter<StepRVAdapter.ViewHolder>() {

    private val allSteps = ArrayList<Step>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputStep: TextInputEditText = itemView.findViewById(R.id.input_step)
        val inputLayoutStep: TextInputLayout = itemView.findViewById(R.id.input_layout_step)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.step_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.inputStep.setText(allSteps[position].description)
        holder.inputLayoutStep.hint = "Step ${allSteps[position].order}"

        holder.buttonDelete.setOnClickListener {
            showYesNoDialog(
                context,
                "Confirm delete",
                "Are you sure you want to delete this step?"
            ) {
                stepChangeListener.onDeleteStepClick(position)
            }
        }

        // Update data if user click off the edit text (counted as a change)
        holder.inputStep.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                var step = (view as? EditText)?.text?.toString()
                if (step.isNullOrEmpty()) {
                    step = "Step ${allSteps[position].order}"
                }

                allSteps[position].description = step
            }
        }

    }

    override fun getItemCount(): Int {
        return allSteps.size
    }

    /**
     * This function pass a reference for every object in newList
     * to update the data, so beware that the change made to data in adapter may be reflected
     * to outside data
     */
    fun updateList(newList: List<Step>) {
        allSteps.clear()
        allSteps.addAll(newList)
        notifyDataSetChanged()
    }
}

interface OnStepChangeListener {
    fun onDeleteStepClick(position: Int)
}

