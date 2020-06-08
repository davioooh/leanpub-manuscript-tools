package com.davioooh.lptools

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"

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
                            && file.hasNameWithChapterPrefix()
                            && file.hasExtension(extension)
                }?.toList()?.sortedBy { it.name } ?: listOf()

fun resolveManuscriptPathOrNull(bookRootPath: Path): Path? =
        bookRootPath.resolve(MANUSCRIPT_FOLDER)
                .let { if (Files.exists(it)) it else null }

fun resolveManuscriptPath(bookRootPath: Path): Path =
        resolveManuscriptPathOrNull(bookRootPath)
                ?: throw IllegalArgumentException("Invalid book path: cannot find manuscript folder.")


fun createNewChapterFile(bookRootPath: Path, number: Int, leadingZeros: Int = 0, title: String? = null): File {
    val normalizedChNum = number.normalizeChapterNumber(leadingZeros)
    val normalizedChTitle = title?.normalizeChapterTitle()
    val newChFileName = buildChapterFileName(normalizedChNum, normalizedChTitle)

    return resolveManuscriptPath(bookRootPath)
            .resolve(newChFileName)
            .toFile()
            .apply { createNewFile() }
            .apply {
                printWriter().use { writer ->
                    writer.println("{#ch-${normalizedChTitle ?: number}}")
                    writer.println("# ${title ?: "New Chapter"}")
                }
            }
}