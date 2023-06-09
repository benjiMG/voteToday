package com.example.votetoday.Common

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveBitmapToFile(imageBitmap: ImageBitmap?): File? {
    val bitmap = imageBitmap?.asAndroidBitmap() ?: return null

    // Crea un archivo en el directorio de imágenes externo
    val fileName = "imagen.png"
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val imageFile = File(dir, fileName)

    // Crea un FileOutputStream para escribir el bitmap en el archivo
    var outputStream: FileOutputStream? = null
    return try {
        outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        imageFile
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}