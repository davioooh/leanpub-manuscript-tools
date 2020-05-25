package com.davioooh.leanpub

import java.io.File
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"
const val CHAPTER_FILE_NAME_PREFIX = "ch"
const val TXT_EXT = "txt"
const val MD_EXT = "md"

fun listAllChapterFiles(bookRootPath: Path): List<File> =
        listChapterFilesWithExtension(bookRootPath, TXT_EXT)

fun generateBookTxtFromFileNames(bookRootPath: Path, chaptersFileNames: List<String>): File {
    val bookTxt = bookRootPath.resolve(MANUSCRIPT_FOLDER)
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
        listChapterFilesWithExtension(bookRootPath, TXT_EXT)
                .map { txtFile ->
                    txtFile.withExtension(MD_EXT)
                            .apply { txtFile.renameTo(this) }
                }

fun convertMdChaptersToTxt(bookRootPath: Path): List<File> =
        listChapterFilesWithExtension(bookRootPath, MD_EXT)
                .map { mdFile ->
                    mdFile.withExtension(TXT_EXT)
                            .apply { mdFile.renameTo(this) }
                }

fun listChapterFilesWithExtension(bookRootPath: Path, extension: String): List<File> =
        bookRootPath.resolve(MANUSCRIPT_FOLDER)
                .toFile()
                .listFiles { file ->
                    file.isFile
                            && file.nameWithoutExtension.startsWith(CHAPTER_FILE_NAME_PREFIX, true)
                            && file.extension.toLowerCase() == extension
                }?.toList()?.sortedBy { it.name } ?: listOf()

private fun File.withExtension(targetExtension: String): File =
        File(this.parentFile, "${this.nameWithoutExtension}.$targetExtension")