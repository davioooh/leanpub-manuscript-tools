package com.davioooh

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest {

    private lateinit var manuscriptTestPath: Path

    @BeforeAll
    fun setup(@TempDir testRootFolder: Path) {
        manuscriptTestPath = createManuscriptTestFolder(testRootFolder)
        createTestChapter(manuscriptTestPath, 1)
        createTestChapter(manuscriptTestPath, 2)
        createTestChapter(manuscriptTestPath, 3)
    }

    @Test
    fun `should find 3 chapters in manuscript folder`() {
        val chapters = listAllChapters(manuscriptTestPath.toString())

        assertThat(chapters).hasSize(3)
        assertThat(chapters.map { it.name })
                .isEqualTo(listOf(
                        "ch01.txt",
                        "ch02.txt",
                        "ch03.txt"
                ))
    }

}