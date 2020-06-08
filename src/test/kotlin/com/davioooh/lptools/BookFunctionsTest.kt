package com.davioooh.lptools

import com.davioooh.lptools.commons.LINE_SEPARATOR
import com.davioooh.lptools.commons.TestBookFolder
import com.davioooh.lptools.commons.fakeMdFileNamesList
import com.davioooh.lptools.commons.fakeTxtFileNamesList
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BookFunctionsTest {
    private lateinit var testBook: TestBookFolder

    private val expected3TxtFiles = fakeTxtFileNamesList
    private val expected3MdFiles = fakeMdFileNamesList
    private val expectedBookTxt = expected3TxtFiles.joinToString(LINE_SEPARATOR, postfix = LINE_SEPARATOR)

    @BeforeAll
    fun setup(@TempDir testRootFolder: Path) {
        testBook = TestBookFolder(testRootFolder)
    }

    @AfterEach
    fun clearManuscriptFolder() {
        testBook.deleteManuscriptFolder()
    }


    /* List chapter files */

    @Test
    fun `should find 3 TXT chapter files in manuscript folder`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val txtChapters = listChapterFilesWithExtension(testBook.bookRootFolder, TXT_EXT)

        assertThat(txtChapters).hasSize(3)
        assertThat(txtChapters.map { it.name }).isEqualTo(expected3TxtFiles)
    }

    @Test
    fun `should find 3 MD chapter files in manuscript folder`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3, MD_EXT)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val mdChapters = listChapterFilesWithExtension(testBook.bookRootFolder, MD_EXT)

        assertThat(mdChapters).hasSize(3)
        assertThat(mdChapters.map { it.name }).isEqualTo(expected3MdFiles)
    }


    /* Generate Book.txt */

    @Test
    fun `should generate Book_txt with 3 chapter files`() {
        testBook.createManuscriptFolder()

        val bookTxt = generateBookTxtFromFileNames(testBook.bookRootFolder, expected3TxtFiles)

        assertThat(bookTxt.name).isEqualTo("Book.txt")
        assertThat(bookTxt.readText()).isEqualTo(expectedBookTxt)
    }


    /* Create new chapter */

    @Test
    fun `should create new chapter file given the chapter number`() {
        testBook.createManuscriptFolder()

        val newChapter = createNewChapterFile(testBook.bookRootFolder, 3)

        assertThat(newChapter.name).isEqualTo("ch3.txt")
        assertThat(newChapter.readText()).isEqualTo("{#ch-3}$LINE_SEPARATOR# New Chapter$LINE_SEPARATOR")
    }

    @Test
    fun `should create new chapter file given the chapter number and title`() {
        testBook.createManuscriptFolder()

        val newChapter = createNewChapterFile(testBook.bookRootFolder, 5, 1, "Chapter name")

        assertThat(newChapter.name).isEqualTo("ch05_chapter-name.txt")
        assertThat(newChapter.readText()).isEqualTo("{#ch-chapter-name}$LINE_SEPARATOR# Chapter name$LINE_SEPARATOR")
    }

    @Test
    fun `should throw exception when file already exists`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3)

        assertThatThrownBy {
            createNewChapterFile(testBook.bookRootFolder, 2, 1)
        }.isInstanceOf(IllegalStateException::class.java)
    }

}