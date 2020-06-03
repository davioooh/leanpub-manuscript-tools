package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.davioooh.lptools.commons.FakeCommand
import com.davioooh.lptools.commons.TEST_MANUSCRIPT_PATH
import com.davioooh.lptools.commons.TestConsole
import com.davioooh.lptools.commons.fakeTxtFiles
import com.github.ajalt.clikt.core.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class ManuscriptCmdTest {

    private val lpToolsManuscriptWithFakeCmd = lpToolsManuscriptWith(FakeCommand)

    private fun lpToolsManuscriptWith(manuscriptSubCmd: CliktCommand) =
            LPTools { TEST_MANUSCRIPT_PATH }
                    .subcommands(ManuscriptCmd()
                            .subcommands(manuscriptSubCmd)
                    )


    @BeforeEach
    fun setup() {
        TestConsole.clear()
    }

    @Test
    fun `running with no child command should print help`() {
        assertThatThrownBy {
            lpToolsManuscriptWithFakeCmd.parse(arrayOf("manuscript"))
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `should print out error when no generation option is provided`() {
        assertThatThrownBy {
            lpToolsManuscriptWith(generateCmd({ listOf() }))
                    .parse(arrayOf("manuscript", "gn"))
        }.isInstanceOf(UsageError::class.java)
    }

    @Test
    fun `should not generate Book_txt when manuscript folder is empty`() {
        lpToolsManuscriptWith(generateCmd({ listOf() }))
                .parse(arrayOf("manuscript", "gn", "-b"))

        assertThat(TestConsole.output.toString()).contains("Book.txt not generated")
    }

    @Test
    fun `should generate Book_txt when manuscript folder is not empty`() {
        lpToolsManuscriptWith(generateCmd({ fakeTxtFiles }))
                .parse(arrayOf("manuscript", "gn", "-b"))

        assertThat(TestConsole.output.toString()).contains("Generated:")
    }

    private fun generateCmd(
            listChapterFiles: ListChapterFilesFun,
            generateBookTxt: GenerateBookTxtFun = { _, _ -> File("Book.txt") }
    ) = ManuscriptCmd.Generate(listChapterFiles, generateBookTxt).apply { context { console = TestConsole } }

}