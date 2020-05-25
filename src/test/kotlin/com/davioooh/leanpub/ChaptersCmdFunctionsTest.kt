package com.davioooh.leanpub

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ChaptersCmdFunctionsTest {
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
    fun `should find 3 chapter files in manuscript folder`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val chapters = listAllChapterFiles(testBook.bookRootFolder)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(expected3Files)
    }

    @Test
    fun `should generate Book_txt with 3 chapter files`() {
        testBook.createManuscriptFolder()

        val bookTxt = generateBookTxtFromFileNames(testBook.bookRootFolder, expected3Files)

        assertThat(bookTxt.readText()).isEqualTo(expectedBookTxt)
    }

    @Test
    fun `should convert TXT chapters to MD`() {
        testBook.createManuscriptFolder()
                .withFakeChapters(3)
                .withFakeBookTxt()
                .withFile("ch04_test.pdf")

        val mdChapters = convertTxtChapterFilesToMd(testBook.bookRootFolder)

        assertThat(mdChapters.map { it.name }).isEqualTo(expected3MdFiles)
    }

    @Test
    fun `should convert MD chapters to TXT`() {
        testBook.createManuscriptFolder().withFakeChapters(3, filesExt = MD_EXT)

        val txtChapters = convertMdChapterFilesToTxt(testBook.bookRootFolder)

        assertThat(txtChapters.map { it.name }).isEqualTo(expected3Files)
    }

}