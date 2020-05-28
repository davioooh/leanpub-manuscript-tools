package com.davioooh.lptools

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"
const val CHAPTER_FILE_NAME_PREFIX = "ch"
const val TXT_EXT = "txt"
const val MD_EXT = "md"

fun listAllChapterFiles(bookRootPath: Path): List<File> =
        listChapterFilesWithExtension(bookRootPath, TXT_EXT)

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

// TODO split file fetching in a separate fun
fun convertTxtChapterFilesToMd(bookRootPath: Path): List<File> =
        listChapterFilesWithExtension(bookRootPath, TXT_EXT)
                .replaceExtensionWith(MD_EXT)

// TODO split file fetching in a separate fun
fun convertMdChapterFilesToTxt(bookRootPath: Path): List<File> =
        listChapterFilesWithExtension(bookRootPath, MD_EXT)
                .replaceExtensionWith(TXT_EXT)

private fun List<File>.replaceExtensionWith(newExtension: String): List<File> =
        this.map { file ->
            File(file.parentFile, "${file.nameWithoutExtension}.$newExtension")
                    .apply { file.renameTo(this) }
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