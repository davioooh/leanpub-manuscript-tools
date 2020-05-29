package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.davioooh.lptools.MD_EXT
import com.davioooh.lptools.TXT_EXT
import com.davioooh.lptools.commands.ChaptersCmd.Convert
import com.davioooh.lptools.commands.ChaptersCmd.ListFiles
import com.davioooh.lptools.commons.*
import com.github.ajalt.clikt.core.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChaptersCmdTest {

    private val lpToolsChaptersWithFakeCmd = lpToolsChaptersWith(FakeCommand)
    private val lpToolsChaptersWithListFilesCmd = lpToolsChaptersWith(
            ListFiles { fakeTxtFiles }.apply { context { console = TestConsole } }
    )

    private fun lpToolsChaptersWith(chaptersSubCmd: CliktCommand) =
            LPTools { TEST_MANUSCRIPT_PATH }
                    .subcommands(ChaptersCmd()
                            .subcommands(chaptersSubCmd)
                    )


    @BeforeEach
    fun setup() {
        TestConsole.clear()
    }

    @Test
    fun `running with no child command should print help`() {
        assertThatThrownBy {
            lpToolsChaptersWithFakeCmd.parse(arrayOf("chapters"))
        }.isInstanceOf(PrintHelpMessage::class.java)
    }


    /* list-files */

    @Test
    fun `should print out 3 chapter files`() {
        val expectedTxtFileNames = fakeTxtFileNames
        lpToolsChaptersWithListFilesCmd.parse(arrayOf("chapters", "lf"))
        assertThat(TestConsole.output.toString()).isEqualTo(expectedTxtFileNames)
    }


    /* convert */

    @Test
    fun `no chapter file should be converted`() {
        lpToolsChaptersWith(convertCmd { _, _ -> listOf() })
                .parse(arrayOf("chapters", "cf", "--to=$TXT_EXT"))
        assertThat(TestConsole.output.toString()).contains("0 files")
        assertThat(TestConsole.output.toString()).doesNotContain("=>")
    }

    @Test
    fun `should convert 3 txt chapters to md`() {
        lpToolsChaptersWith(convertCmd { _, _ -> fakeTxtFiles })
                .parse(arrayOf("chapters", "cf", "--to=$MD_EXT"))
        assertThat(TestConsole.output.toString()).contains("3 files")
        assertThat(TestConsole.output.toString()).containsPattern(".*\\.txt\t=>.*\\.md")
    }

    @Test
    fun `should convert 3 md chapters to txt`() {
        lpToolsChaptersWith(convertCmd { _, _ -> fakeMdFiles })
                .parse(arrayOf("chapters", "cf", "--to=$TXT_EXT"))
        assertThat(TestConsole.output.toString()).contains("3 files")
        assertThat(TestConsole.output.toString()).containsPattern(".*\\.md\t=>.*\\.txt")
    }

    @Test
    fun `should print out error when no conversion format is provided`() {
        assertThatThrownBy {
            lpToolsChaptersWith(convertCmd { _, _ -> listOf() })
                    .parse(arrayOf("chapters", "cf", "--to="))
        }.isInstanceOf(UsageError::class.java)
    }

    @Test
    fun `should print out error when the provided conversion format is not supported`() {
        assertThatThrownBy {
            lpToolsChaptersWith(convertCmd { _, _ -> listOf() })
                    .parse(arrayOf("chapters", "cf", "--to=foo"))
        }.isInstanceOf(UsageError::class.java)
    }

    private fun convertCmd(listChapterFilesWithExtFun: ListChapterFilesWithExtFun) =
            Convert(listChapterFilesWithExtFun).apply { context { console = TestConsole } }

}