package com.example.victor_ai.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Утилиты для работы с изображениями: resize, compress, base64
 */
object ImageUtils {
    private const val TAG = "ImageUtils"
    private const val MAX_SIZE = 4096 // Максимальный размер для точности
    private const val PNG_QUALITY = 100 // PNG без потерь

    /**
     * Класс для хранения прикрепленного изображения
     */
    data class ImageAttachment(
        val uri: Uri,
        val base64: String,
        val thumbnail: Bitmap? = null
    )

    /**
     * Конвертирует URI изображения в base64 PNG
     * - Читает EXIF и корректирует ориентацию
     * - Resize до MAX_SIZE (4096px) с сохранением пропорций
     * - Compress в PNG без потерь
     * - Конвертирует в base64
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val bitmap = loadAndProcessBitmap(context, uri) ?: return null
            bitmapToBase64(bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка конвертации URI в base64", e)
            null
        }
    }

    /**
     * Создает ImageAttachment из URI
     */
    fun createImageAttachment(context: Context, uri: Uri): ImageAttachment? {
        return try {
            val bitmap = loadAndProcessBitmap(context, uri) ?: return null
            val base64 = bitmapToBase64(bitmap)
            val thumbnail = createThumbnail(bitmap, 120) // Превью 120px

            ImageAttachment(
                uri = uri,
                base64 = base64,
                thumbnail = thumbnail
            )
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка создания ImageAttachment", e)
            null
        }
    }

    /**
     * Загружает Bitmap из URI с обработкой EXIF и resize
     */
    private fun loadAndProcessBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                ?: return null

            // Декодируем изображение
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap == null) {
                Log.e(TAG, "Не удалось декодировать изображение")
                return null
            }

            // Корректируем ориентацию по EXIF
            bitmap = fixOrientation(context, uri, bitmap)

            // Resize до MAX_SIZE с сохранением пропорций
            bitmap = resizeBitmap(bitmap, MAX_SIZE)

            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки bitmap", e)
            null
        }
    }

    /**
     * Корректирует ориентацию изображения по EXIF данным
     */
    private fun fixOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exif = ExifInterface(inputStream)
            inputStream.close()

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                else -> return bitmap
            }

            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            Log.w(TAG, "Не удалось прочитать EXIF, используем оригинал", e)
            bitmap
        }
    }

    /**
     * Resize bitmap с сохранением пропорций
     * Максимальная сторона = maxSize
     */
    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Если уже меньше maxSize - возвращаем как есть
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            // Горизонтальное изображение
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            // Вертикальное или квадратное
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }

        Log.d(TAG, "Resize: ${width}x${height} -> ${newWidth}x${newHeight}")
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Создает thumbnail (миниатюру) для превью
     */
    private fun createThumbnail(bitmap: Bitmap, size: Int): Bitmap {
        return resizeBitmap(bitmap, size)
    }

    /**
     * Конвертирует Bitmap в base64 PNG
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, outputStream)
        val byteArray = outputStream.toByteArray()

        val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)

        // Логируем размер для отладки
        val sizeKb = byteArray.size / 1024
        Log.d(TAG, "Размер PNG: ${sizeKb}KB, base64: ${base64.length} символов")

        return base64
    }
}
