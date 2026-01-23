package com.kastik.apps.core.data.utils

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface Base64ImageExtractor {
    suspend fun process(html: String): String
}


internal class Base64ImageExtractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : Base64ImageExtractor {
    override suspend fun process(html: String): String = withContext(Dispatchers.Default) {
        val regex =
            "<img.+?src=\"(data:image/(.+?);base64,)(.+?)\".*?>".toRegex(RegexOption.DOT_MATCHES_ALL)
        val matches = regex.findAll(html).toList()

        if (matches.isEmpty()) return@withContext html

        val replacements = matches.map { match ->
            async {
                val base64Prefix = match.groupValues[1]
                val extension = match.groupValues[2]
                val base64Image = match.groupValues[3]

                val cleanBase64 = base64Image.replace("\\s".toRegex(), "")
                val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

                val filename = generateFilename(imageBytes, extension)

                val file = saveFile(filename, imageBytes)

                if (file != null) {
                    val localUri = "file://${file.absolutePath}"
                    base64Prefix + base64Image to localUri
                } else {
                    null
                }
            }
        }.awaitAll().filterNotNull()

        var currentHtml = html
        replacements.forEach { (target, replacement) ->
            currentHtml = currentHtml.replace(target, replacement)
        }

        currentHtml
    }

    private suspend fun generateFilename(imageBytes: ByteArray, extension: String): String =
        withContext(Dispatchers.Default) {
            val messageDigest = java.security.MessageDigest.getInstance("SHA-256")
            val hashBytes = messageDigest.digest(imageBytes)
            val hexString = hashBytes.joinToString("") { "%02x".format(it) }
            return@withContext "img_${hexString}.$extension"
        }

    private suspend fun saveFile(filename: String, bytes: ByteArray): File? =
        withContext(Dispatchers.IO) {
            try {
                val dir = File("${context.cacheDir}/announcementImage/")
                if (!dir.exists()) dir.mkdirs()

                val file = File(dir, filename)
                if (file.exists()) return@withContext file

                FileOutputStream(file).use { it.write(bytes) }
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}