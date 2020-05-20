package com.davioooh.leanpub

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

    private val expected3Chapters = listOf(
        "ch01.txt",
        "ch02.txt",
        "ch03.txt"
    )

    private val expected3MdChapters = listOf(
        "ch01.md",
        "ch02.md",
        "ch03.md"
    )

    private val expectedChaptersIndex = StringBuilder()
        .apply { expected3Chapters.map { this.append("$it\r\n") } }
        .toString()

    @BeforeAll
    fun setup(@TempDir testRootFolder: Path) {
        testBook = TestBookFolder(testRootFolder)
    }

    @AfterEach
    fun clearManuscriptFolder() {
        testBook.deleteManuscriptFolder()
    }

    @Test
    fun `should find 3 chapters in manuscript folder`() {
        testBook.createManuscriptFolder().withFakeChapters(3)

        val chapters = listAllChapters(testBook.bookRootFolder)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(expected3Chapters)
    }

    @Test
    fun `should generate Book-txt with 3 chapters`() {
        val bookTxt = generateBookTxtFromChapters(testBook.bookRootFolder, expected3Chapters)

        assertThat(bookTxt.readText()).isEqualTo(expectedChaptersIndex + "fdsfdsfds")
    }

    @Test
    fun `should convert TXT chapters to MD`() {
        testBook.createManuscriptFolder().withFakeChapters(3)

        val mdChapters = convertTxtChaptersToMd(testBook.bookRootFolder)

        assertThat(mdChapters.map { it.name }).isEqualTo(expected3MdChapters)
    }

    @Test
    fun `should convert MD chapters to TXT`() {
        testBook.createManuscriptFolder().withFakeChapters(3, chaptersExt = MD_EXT)

        val txtChapters = convertMdChaptersToTxt(testBook.bookRootFolder)

        assertThat(txtChapters.map { it.name }).isEqualTo(expected3Chapters)
    }

}