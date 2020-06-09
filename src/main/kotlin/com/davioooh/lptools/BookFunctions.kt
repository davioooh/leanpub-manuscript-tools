package com.davioooh.lptools

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

const val MANUSCRIPT_FOLDER = "manuscript"

// TODO improvement: transform all the functions in Path extension fun?

fun generateBookTxtFromFileNames(bookRootPath: Path, chaptersFileNames: List<String>): File {
    val bookTxt = resolveManuscriptPath(bookRootPath)
            .resolve("Book.txt")
            .toFile()

    if (bookTxt.exists()) bookTxt.delete()

    return bookTxt.createAndWriteLines(*chaptersFileNames.toTypedArray())
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


fun createNewChapterFile(bookRootPath: Path, chNumber: Int, chNumberLeadingZeros: Int = 0, chTitle: String? = null): File {
    val normalizedChNum = chNumber.normalizeChapterNumber(chNumberLeadingZeros)
    val normalizedChTitle = chTitle?.normalizeChapterTitle()
    val newChFileName = buildChapterFileName(normalizedChNum, normalizedChTitle)

    return resolveManuscriptPath(bookRootPath)
            .resolve(newChFileName)
            .toFile()
            .createAndWriteLines(
                    "{#ch-${normalizedChTitle ?: chNumber}}",
                    "# ${chTitle ?: "New Chapter"}"
            )
}

fun isChapterNumberAvailable(bookRootPath: Path, chNumber: Int): Boolean =
        chNumber !in fetchChapterNumbers(bookRootPath)

fun getNextAvailableChapterNumber(bookRootPath: Path): Int =
        fetchChapterNumbers(bookRootPath).max()?.plus(1) ?: 1

fun fetchChapterNumbers(bookRootPath: Path): IntArray {
    val chFiles =
            listChapterFilesWithExtension(bookRootPath, TXT_EXT) +
                    listChapterFilesWithExtension(bookRootPath, MD_EXT)

    return chFiles
            .map { it.nameWithoutExtension }
            .map { it.removePrefix(CHAPTER_FILE_NAME_PREFIX) }
            .map { it.substringBefore(CHAPTER_FILE_NUM_SEPARATOR) }
            .map { it.toInt() }
            .sorted()
            .toIntArray()
}