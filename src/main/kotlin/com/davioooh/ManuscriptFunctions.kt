package com.davioooh

import java.io.File

const val MANUSCRIPT_FOLDER = "manuscript"
const val TXT_EXT = "txt"
const val CHAPTER_PREFIX = "ch"

fun listAllChapters(path: String): List<File> =
        File(path)
                .listFiles { file ->
                    file.isFile
                            && file.nameWithoutExtension.startsWith(CHAPTER_PREFIX, true)
                            && file.extension.toLowerCase() == TXT_EXT
                }?.toList()?.sortedBy { it.name } ?: listOf()