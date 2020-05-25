package com.davioooh.leanpub.commands

import com.davioooh.leanpub.LPTools
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class ChaptersTest {

    private val fakeFiles = listOf(File("ch01.txt"), File("ch02.txt"), File("ch03.txt"))
    private val lpTools = LPTools()
            .subcommands(Chapters().subcommands(Chapters.ListFiles { fakeFiles }))

    @Test
    fun `running with no child command should print help`() {
        Assertions.assertThatThrownBy {
            lpTools.parse(arrayOf("chapters"))
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `should print out 3 files`() {
        lpTools.parse(arrayOf("chapters", "lf"))
    }


}