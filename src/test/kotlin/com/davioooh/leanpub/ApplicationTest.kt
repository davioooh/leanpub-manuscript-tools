package com.davioooh.leanpub

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
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
    }

    @BeforeEach
    fun createManuscriptFolder() {
        testManuscriptPath = createManuscriptTestFolder(testBookRootPath)
    }

    @AfterEach
    fun clearManuscriptFolder() {
        testManuscriptPath.toFile().deleteRecursively()
    }

    @Test
    fun `should find 3 chapters in manuscript folder`() {
        setupFakeChapters(testManuscriptPath)

        val chapters = listAllChapters(testBookRootPath)

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name }).isEqualTo(test3Chapters)
    }

    @Test
    fun `should generate Book-txt with 3 chapters`() {
        setupFakeChapters(testManuscriptPath)

        val bookTxt = generateBookTxtFromChapters(testBookRootPath, test3Chapters)

        assertThat(bookTxt.readText()).isEqualTo(chaptersIndex)
    }

    @Test
    fun `should convert TXT chapters to MD`() {
        setupFakeChapters(testManuscriptPath)

        val mdChapters = convertTxtChaptersToMd(testBookRootPath)

        assertThat(mdChapters.map { it.name }).isEqualTo(test3MdChapters)
    }

    @Test
    fun `should convert MD chapters to TXT`() {
        setupFakeChapters(testManuscriptPath, chaptersExt = MD_EXT)

        val txtChapters = convertMdChaptersToTxt(testBookRootPath)

        assertThat(txtChapters.map { it.name }).isEqualTo(test3Chapters)
    }

}