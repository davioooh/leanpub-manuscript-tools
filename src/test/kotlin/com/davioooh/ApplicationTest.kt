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

    private fun createManuscriptTestFolder(bookRootFolder: Path): Path {
        val manuscriptPath = bookRootFolder.resolve(MANUSCRIPT_FOLDER)
        manuscriptPath.toFile().mkdir()
        return manuscriptPath
    }

    private fun createTestChapter(manuscriptFolder: Path, chapterNum: Int) {

        val testCh = manuscriptFolder.resolve(testFileNameFrom(chapterNum)).toFile()
        testCh.createNewFile()
        testCh.printWriter().use { out ->
            out.println("# Chapter $chapterNum")
            out.println()
            out.println("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis mi nec risus sollicitudin finibus. Maecenas nec gravida mauris. Maecenas sit amet quam ac tellus placerat molestie. Proin vestibulum sapien feugiat, vulputate tortor eget, rhoncus eros. Nam non elit eget mauris porttitor sollicitudin. Aenean ullamcorper, magna at pharetra pulvinar, sapien magna pulvinar tortor, ut dignissim purus nulla ac purus. Morbi in risus sit amet ipsum sodales feugiat sit amet in arcu. Integer at feugiat sem, quis tincidunt risus. Vestibulum tincidunt sem et dignissim molestie. Curabitur fringilla magna sit amet vehicula bibendum. Phasellus blandit auctor justo, at aliquet magna mollis ac. Curabitur posuere pharetra tristique. Vestibulum tristique mi id velit gravida condimentum. Donec aliquam congue quam, a luctus leo pharetra a. Aliquam faucibus sollicitudin massa, ut interdum velit commodo id. Duis eros ipsum, faucibus vitae dapibus at, ornare in nisi.")
            out.println()
            out.println("Nullam tellus neque, consequat quis eleifend a, dignissim dignissim ipsum. Ut placerat dignissim interdum. Nam dignissim sagittis interdum. Cras tortor erat, volutpat ut sagittis in, tincidunt eget mauris. Quisque a felis sed urna sollicitudin ornare commodo eu libero. Morbi luctus tristique ante nec pulvinar. Mauris interdum ante et velit convallis volutpat. Sed rutrum lacus et ligula maximus ultrices. Nulla quis dignissim metus, ac euismod leo. Sed auctor lorem et eleifend ultricies. Nullam suscipit tellus eget molestie lobortis. Morbi viverra ipsum sit amet molestie eleifend.")
        }
    }

    // TODO optimize file name creation
    private fun testFileNameFrom(chapterNum: Int) =
            "${CHAPTER_PREFIX}0$chapterNum.$TXT_EXT"

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