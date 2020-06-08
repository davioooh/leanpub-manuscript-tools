package com.davioooh.lptools

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"
const val CHAPTER_FILE_NAME_PREFIX = "ch"
const val CHAPTER_FILE_NUM_SEPARATOR = '_'
const val CHAPTER_FILE_NAME_SEPARATOR = '-'
const val TXT_EXT = "txt"
const val MD_EXT = "md"

fun generateBookTxtFromFileNames(bookRootPath: Path, chaptersFileNames: List<String>): File {
    val bookTxt = resolveManuscriptPath(bookRootPath)
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

fun listChapterFilesWithExtension(bookRootPath: Path, extension: String): List<File> =
        resolveManuscriptPath(bookRootPath)
                .toFile()
                .listFiles { file ->
                    file.isFile
                            && file.nameWithoutExtension.startsWith(CHAPTER_FILE_NAME_PREFIX, true)
                            && file.extension.toLowerCase() == extension
                }?.toList()?.sortedBy { it.name } ?: listOf()

fun resolveManuscriptPathOrNull(bookRootPath: Path): Path? =
        bookRootPath.resolve(MANUSCRIPT_FOLDER)
                .let { if (Files.exists(it)) it else null }

fun resolveManuscriptPath(bookRootPath: Path): Path =
        resolveManuscriptPathOrNull(bookRootPath)
                ?: throw IllegalArgumentException("Invalid book path: cannot find manuscript folder.")


fun createNewChapterFile(number: Int, title: String? = null): File { // TODO implement leadingZeros
    val formattedNum = number.toString()
    val formattedTitle =
            title?.toLowerCase()?.replace(Regex("\\W"), CHAPTER_FILE_NAME_SEPARATOR.toString())
                    ?.take(200)
                    ?.trim(CHAPTER_FILE_NAME_SEPARATOR)

    return File(
            "$CHAPTER_FILE_NAME_PREFIX$formattedNum" +
                    (if (formattedTitle != null) "${CHAPTER_FILE_NUM_SEPARATOR}$formattedTitle" else "") +
                    ".$TXT_EXT"
    )
}