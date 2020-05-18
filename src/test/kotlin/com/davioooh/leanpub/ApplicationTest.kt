package com.davioooh.leanpub

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest {

    private lateinit var testBookRootPath: Path
    private lateinit var testManuscriptPath: Path

    private val test3Chapters = listOf(
            "ch01.txt",
            "ch02.txt",
            "ch03.txt"
    )

    private val test3MdChapters = listOf(
        "ch01.md",
        "ch02.md",
        "ch03.md"
    )

    private val chaptersIndex = StringBuilder()
            .apply { test3Chapters.map { this.append("$it\r\n") } }
            .toString()


    @BeforeAll
    fun setup(@TempDir testRootFolder: Path) {
        testBookRootPath = testRootFolder
        testManuscriptPath = createManuscriptTestFolder(testRootFolder)
        createTestChapter(testManuscriptPath, 1)
        createTestChapter(testManuscriptPath, 2)
        createTestChapter(testManuscriptPath, 3)
    }

    @Test
    fun `should find 3 chapters in manuscript folder`() {
        val chapters = listAllChapters(testBookRootPath)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(test3Chapters)
    }

    @Test
    fun `should generate Book-txt with 3 chapters`() {
        val bookTxt = generateBookTxtFromChapters(testBookRootPath, test3Chapters)
        assertThat(bookTxt.readText()).isEqualTo(chaptersIndex)
    }

    @Test
    fun `should convert TXT chapters to MD`() {
        val mdChapters = convertTxtChaptersToMd(testBookRootPath)
        assertThat(mdChapters.map { it.name }).isEqualTo(test3MdChapters)
    }

}