package com.davioooh.leanpub

import java.io.File
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"
const val TXT_EXT = "txt"
const val CHAPTER_PREFIX = "ch"

fun listAllChapters(bookRootPath: Path): List<File> =
        bookRootPath
                .resolve(MANUSCRIPT_FOLDER)
                .toFile()
                .listFiles { file ->
                    file.isFile
                            && file.nameWithoutExtension.startsWith(CHAPTER_PREFIX, true)
                            && file.extension.toLowerCase() == TXT_EXT
                }?.toList()?.sortedBy { it.name } ?: listOf()