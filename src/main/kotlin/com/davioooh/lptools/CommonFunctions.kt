package com.davioooh.lptools

import java.io.File

fun List<File>.replaceExtensionWith(newExtension: String): List<File> =
        this.map { file ->
            File(file.parentFile, "${file.nameWithoutExtension}.$newExtension")
                    .apply { file.renameTo(this) }
        }

fun Int.normalizeChapterNumber(leadingZeros: Int = 0) = "0".repeat(leadingZeros) + this

fun String.normalizeChapterTitle() =
        this.split(Regex("\\W"))
                .filter { it.isNotEmpty() }
                .take(20)
                .joinToString(CHAPTER_FILE_NAME_SEPARATOR)
                .toLowerCase()