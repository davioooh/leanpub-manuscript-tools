package com.davioooh.lptools

import java.io.File

const val CHAPTER_FILE_NAME_PREFIX = "ch"
const val CHAPTER_FILE_NUM_SEPARATOR = "_"
const val CHAPTER_FILE_NAME_SEPARATOR = "-"
const val TXT_EXT = "txt"
const val MD_EXT = "md"

fun File.hasNameWithChapterPrefix() = this.nameWithoutExtension.startsWith(CHAPTER_FILE_NAME_PREFIX, true)
fun File.hasExtension(ext: String) = this.extension.toLowerCase() == ext

fun File.createAndWriteLines(vararg lines: String): File {
    if (createNewFile()) {
        printWriter().use { writer ->
            lines.forEach { writer.println(it) }
        }
        return this
    } else {
        throw IllegalStateException("Cannot create $this, file already exists.")
    }
}

fun List<File>.replaceExtensionWith(newExtension: String): List<File> =
        this.map { file ->
            File(file.parentFile, "${file.nameWithoutExtension}.$newExtension")
                    .apply { file.renameTo(this) }
        }

fun Int.normalizeChapterNumber(leadingZeros: Int = 0) = "0".repeat(leadingZeros) + this

fun String.normalizeChapterTitle(): String =
        this.split(Regex("\\W"))
                .filter { it.isNotEmpty() }
                .take(20)
                .joinToString(CHAPTER_FILE_NAME_SEPARATOR)
                .toLowerCase()

fun buildChapterFileName(normalizedChNum: String, normalizedChTitle: String? = null, fileExtension: String = TXT_EXT): String =
        "$CHAPTER_FILE_NAME_PREFIX$normalizedChNum" +
                (if (normalizedChTitle != null) "${CHAPTER_FILE_NUM_SEPARATOR}$normalizedChTitle" else "") +
                ".$fileExtension"