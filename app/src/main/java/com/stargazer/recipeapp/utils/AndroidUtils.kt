package com.stargazer.recipeapp.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.stargazer.recipeapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(context: Context, message: String) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.toast_layout, null)
    val textView: TextView = layout.findViewById(R.id.toast_message)
    textView.text = message

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}


fun showYesNoDialog(context: Context, title: String, message: String, onYes: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton("Yes") { dialog, _ ->
        onYes()
        dialog.dismiss()
    }

    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
    }

    builder.create().show()
}

fun <T> List<T>.removeAtIndices(indices: List<Int>): List<T> {
    val indexSet = indices.toSet()
    return this.filterIndexed { index, _ -> index !in indexSet }
}

fun dateToString(date: Date, format: String = "dd/MM/yyyy"): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(date)
}

fun stringToDate(dateString: String, format: String = "dd/MM/yyyy"): Date? {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return try {
        formatter.parse(dateString)
    } catch (e: Exception) {
        null
    }
}

data class MutablePair<A, B>(
    var first: A,
    var second: B
)