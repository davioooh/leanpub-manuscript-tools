package com.davioooh.leanpub.commands

import com.davioooh.leanpub.LINE_SEPARATOR
import com.davioooh.leanpub.LPTools
import com.davioooh.leanpub.TestConsole
import com.davioooh.leanpub.testManuscriptPath
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class ChaptersCmdTest {

    private val fakeFiles = listOf(File("ch01.txt"), File("ch02.txt"), File("ch03.txt"))
    private val expectedFileNames = fakeFiles.joinToString(LINE_SEPARATOR, postfix = LINE_SEPARATOR)

    private val lpTools = LPTools { testManuscriptPath }
            .subcommands(ChaptersCmd()
                    .subcommands(ChaptersCmd.ListFiles { fakeFiles }.apply { context { console = TestConsole } }))

    @BeforeEach
    fun setup() {
        TestConsole.clear()
    }

    @Test
    fun `running with no child command should print help`() {
        assertThatThrownBy {
            lpTools.parse(arrayOf("chapters"))
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `should print out 3 chapter files`() {
        lpTools.parse(arrayOf("chapters", "lf"))
        assertThat(TestConsole.output.toString()).isEqualTo(expectedFileNames)
    }

}