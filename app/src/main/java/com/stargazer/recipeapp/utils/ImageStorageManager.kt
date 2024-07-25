package com.stargazer.recipeapp.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class ImageStorageManager(private val context: Context) {

    private val storageDir: File = context.filesDir

    fun saveImageToInternalStorage(uri: Uri?, filename: String): String? {
        if (uri == null) {
            return null
        }

        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(storageDir, filename)

        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    fun deleteFile(filePath: String?): Boolean {
        filePath ?: return false
        val file = File(filePath)
        return file.delete()
    }
}
