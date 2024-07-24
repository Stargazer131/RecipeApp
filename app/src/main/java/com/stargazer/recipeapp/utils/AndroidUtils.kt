package com.stargazer.recipeapp.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showYesNoDialog(context: Context, title: String, message: String, onYes: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    // Set positive button (Yes)
    builder.setPositiveButton("Yes") { dialog, _ ->
        onYes()
        dialog.dismiss()
    }

    // Set negative button (No) - optional (you can omit this if you only want Yes/OK)
    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
    }

    builder.create().show()
}

fun <T> List<T>.removeAtIndices(indices: List<Int>): List<T> {
    val indexSet = indices.toSet()
    return this.filterIndexed { index, _ -> index !in indexSet }
}

data class MutablePair<A, B>(
    var first: A,
    var second: B
)