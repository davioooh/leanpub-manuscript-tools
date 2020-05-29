package com.davioooh.lptools

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class CommonFunctionsTest {

    @Test
    fun `should rename txt files to md`() {
        val fakeTxtFiles = listOf(File("ch01.txt"), File("ch02.txt"), File("ch03.txt"))

        val mdFiles = fakeTxtFiles.replaceExtensionWith(MD_EXT)

        Assertions.assertThat(mdFiles.map { it.name }).allMatch { it.endsWith(MD_EXT) }
    }

}