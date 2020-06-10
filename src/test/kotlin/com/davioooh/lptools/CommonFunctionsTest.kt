package com.davioooh.lptools

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommonFunctionsTest {

    @Test
    fun `should rename txt files to md`() {
        val fakeTxtFiles = listOf(File("ch01.txt"), File("ch02.txt"), File("ch03.txt"))

        val mdFiles = fakeTxtFiles.replaceExtensionWith(MD_EXT)

        assertThat(mdFiles.map { it.name }).allMatch { it.endsWith(MD_EXT) }
    }

    @ParameterizedTest
    @MethodSource("numberNormalizationTestPairs")
    fun `should normalize chapter number correctly`(testPair: Pair<Pair<Int, Int>, String>) {
        val normalizedNumber = testPair.first.first.normalizeChapterNumber(testPair.first.second)

        assertThat(normalizedNumber).isEqualTo(testPair.second)
    }

    private fun numberNormalizationTestPairs() = Stream.of(
            (1 to 0) to "1",
            (2 to 2) to "002",
            (14 to 1) to "014",
            (400 to 3) to "000400"
    )

    @ParameterizedTest
    @MethodSource("titleNormalizationTestPairs")
    fun `should normalize chapter title correctly`(testPair: Pair<String, String>) {
        val normalizedTitle = testPair.first.normalizeChapterTitle()

        assertThat(normalizedTitle).isEqualTo(testPair.second)
    }

    private fun titleNormalizationTestPairs() = Stream.of(
            "This is my favorite chapter!" to "this-is-my-favorite-chapter",
            "Wow! What a nice chapter!" to "wow-what-a-nice-chapter",
            "This is a chapter called -= 10=-..." to "this-is-a-chapter-called-10",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas vel diam at libero sodales venenatis. " +
                    "Vivamus risus nibh, porta varius pharetra sed, bibendum sed risus. Donec quis dui et est finibus elementum. " +
                    "Maecenas non tellus turpis. Sed fringilla risus libero, non euismod diam fermentum ut." to "lorem-ipsum-dolor-sit-amet-consectetur-adipiscing-elit-maecenas-vel-diam-at-libero-sodales-venenatis-vivamus-risus-nibh-porta-varius"
    )


    /* Chapter numbers */

    @Test
    fun `should return false when a chapter with the given number already exists`() {
        assertThat(intArrayOf(1, 2, 3).isChapterNumberAvailable(2)).isFalse()
    }

    @Test
    fun `should return true when no chapter with the given number exists`() {
        assertThat(intArrayOf(1, 2).isChapterNumberAvailable(5)).isTrue()
    }

    @Test
    fun `should return 1 when no chapter exists in manuscript folder`() {
        assertThat(intArrayOf().getNextAvailableChapterNumber()).isEqualTo(1)
    }

    @Test
    fun `should return 5 when chapter 4 already exists`() {
        assertThat(intArrayOf(1,3,4).getNextAvailableChapterNumber()).isEqualTo(5)
    }

}