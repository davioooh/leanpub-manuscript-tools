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

fun generateBookTxtFromChapters(bookRootPath: Path, chaptersFileNames: List<String>): File {
    val bookTxt = bookRootPath
            .resolve(MANUSCRIPT_FOLDER)
            .resolve("Book.txt")
            .toFile()

    if (bookTxt.exists()) bookTxt.delete()

    return bookTxt
            .apply { createNewFile() }
            .apply {
                printWriter().use { writer ->
                    chaptersFileNames.forEach { fileName ->
                        writer.println(fileName)
                    }
                }
            }
}