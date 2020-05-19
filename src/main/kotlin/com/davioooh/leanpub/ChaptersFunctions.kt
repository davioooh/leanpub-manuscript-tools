package com.davioooh.leanpub

import java.io.File
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"
const val TXT_EXT = "txt"
const val MD_EXT = "md"
const val CHAPTER_PREFIX = "ch"

fun listAllChapters(bookRootPath: Path): List<File> =
    listChaptersWithExtension(bookRootPath, TXT_EXT)

fun generateBookTxtFromChapters(bookRootPath: Path, chaptersFileNames: List<String>): File {
    val bookTxt = manuscriptPath(bookRootPath)
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

fun convertTxtChaptersToMd(bookRootPath: Path): List<File> =
    listChaptersWithExtension(bookRootPath, TXT_EXT)
        .map { txtFile ->
            txtFile.withExtension(MD_EXT)
                .apply { txtFile.renameTo(this) }
        }

fun convertMdChaptersToTxt(bookRootPath: Path): List<File> =
    listChaptersWithExtension(bookRootPath, MD_EXT)
        .map { mdFile ->
            mdFile.withExtension(TXT_EXT)
                .apply { mdFile.renameTo(this) }
        }

private fun manuscriptPath(bookRootPath: Path) =
    bookRootPath.resolve(MANUSCRIPT_FOLDER)

private fun listChaptersWithExtension(bookRootPath: Path, extension: String) =
    manuscriptPath(bookRootPath)
        .toFile()
        .listFiles { file ->
            file.isFile
                    && file.nameWithoutExtension.startsWith(CHAPTER_PREFIX, true)
                    && file.extension.toLowerCase() == extension
        }?.toList()?.sortedBy { it.name } ?: listOf()

private fun File.withExtension(targetExtension: String) =
    File(this.parentFile, "${this.nameWithoutExtension}.$targetExtension")