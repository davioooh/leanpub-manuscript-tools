package com.davioooh.lptools

import com.davioooh.lptools.commons.LINE_SEPARATOR
import com.davioooh.lptools.commons.TestBookFolder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ChaptersFunctionsTest {
    private lateinit var testBook: TestBookFolder

    private val expected3Files = listOf(
            "ch01.txt",
            "ch02.txt",
            "ch03.txt"
    )

    private val expected3MdFiles = listOf(
            "ch01.md",
            "ch02.md",
            "ch03.md"
    )

    private val expectedBookTxt = expected3Files.joinToString(LINE_SEPARATOR, postfix = LINE_SEPARATOR)

    @BeforeAll
    fun setup(@TempDir testRootFolder: Path) {
        testBook = TestBookFolder(testRootFolder)
    }

    @AfterEach
    fun clearManuscriptFolder() {
        testBook.deleteManuscriptFolder()
    }

    @Test
    fun `should find 3 TXT chapter files in manuscript folder`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val chapters = listChapterFilesWithExtension(testBook.bookRootFolder, TXT_EXT)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(expected3Files)
    }

    @Test
    fun `should find 3 MD chapter files in manuscript folder`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3, MD_EXT)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val chapters = listChapterFilesWithExtension(testBook.bookRootFolder, TXT_EXT)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(expected3Files)
    }

    @Test
    fun `should generate Book_txt with 3 chapter files`() {
        testBook.createManuscriptFolder()

        val bookTxt = generateBookTxtFromFileNames(testBook.bookRootFolder, expected3Files)

        assertThat(bookTxt.readText()).isEqualTo(expectedBookTxt)
    }

}