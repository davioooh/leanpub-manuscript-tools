package com.davioooh.lptools.commands

import com.davioooh.lptools.*
import com.davioooh.lptools.commands.ChaptersCmd.*
import com.davioooh.lptools.commons.*
import com.github.ajalt.clikt.core.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class ChaptersCmdTest {

    private val lpToolsChaptersWithFakeCmd = lpToolsChaptersWith(FakeCommand)

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
    fun `should print nothing when manuscript folder is empty`() {
        lpToolsChaptersWith(listFilesCmd { listOf() })
                .parse(arrayOf("chapters", "lf"))

        assertThat(TestConsole.output).isEmpty()
    }

    @Test
    fun `should print out 3 chapter files`() {
        val expectedTxtFileNames = fakeTxtFileNames

        lpToolsChaptersWith(listFilesCmd { fakeTxtFiles })
                .parse(arrayOf("chapters", "lf"))

        assertThat(TestConsole.output).isEqualTo(expectedTxtFileNames)
    }

    private fun listFilesCmd(listChapterFiles: ListChapterFilesFun) =
            ListFiles(listChapterFiles).apply { context { console = TestConsole } }


    /* convert */

    @Test
    fun `no chapter file should be converted`() {
        lpToolsChaptersWith(convertCmd { _, _ -> listOf() })
                .parse(arrayOf("chapters", "cf", "--to=$TXT_EXT"))

        assertThat(TestConsole.output).contains("0 files")
        assertThat(TestConsole.output).doesNotContain("=>")
    }

    @Test
    fun `should convert 3 txt chapters to md`() {
        val mappingRegex = ".*\\.txt\t=>.*\\.md".toRegex()

        lpToolsChaptersWith(convertCmd { _, _ -> fakeTxtFiles })
                .parse(arrayOf("chapters", "cf", "--to=$MD_EXT"))

        assertThat(TestConsole.output).contains("3 files")
        assertThat(mappingRegex.findAll(TestConsole.output).asIterable()).hasSize(3)
    }

    @Test
    fun `should convert 3 md chapters to txt`() {
        val mappingRegex = ".*\\.md\t=>.*\\.txt".toRegex()

        lpToolsChaptersWith(convertCmd { _, _ -> fakeMdFiles })
                .parse(arrayOf("chapters", "cf", "--to=$TXT_EXT"))

        assertThat(TestConsole.output).contains("3 files")
        assertThat(mappingRegex.findAll(TestConsole.output).asIterable()).hasSize(3)
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

    private fun convertCmd(listChapterFilesWithExt: ListChapterFilesWithExtFun) =
            Convert(listChapterFilesWithExt).apply { context { console = TestConsole } }


    /* create new */

    @Test
    fun `when no chapter number is provided should set the new chapter number to _greatest chapter number + 1_`() {
        lpToolsChaptersWith(createNewCmd({ intArrayOf(1, 2) }))
                .parse(arrayOf("chapters", "new"))

        assertThat(TestConsole.output).contains("created", buildChapterFileName("03"))
    }

    @Test
    fun `when a chapter number is provided should create a new chapter with the given number (if not already used)`() {
        lpToolsChaptersWith(createNewCmd({ intArrayOf(1, 2) }))
                .parse(arrayOf("chapters", "new", "-n5"))

        assertThat(TestConsole.output).contains("created", buildChapterFileName("05"))
    }

    @Test
    fun `when a chapter number is provided should print out an error if the given number is already used`() {
        assertThatThrownBy {
            lpToolsChaptersWith(createNewCmd({ intArrayOf(1, 2) }))
                    .parse(arrayOf("chapters", "new", "-n2"))
        }.isInstanceOf(UsageError::class.java)
    }

    private fun createNewCmd(
            fetchExistingChapterNumbersFun: FetchExistingChapterNumbersFun,
            createNewChapterFun: CreateNewChapterFun = { _, n, z, _ -> File(buildChapterFileName(n.normalizeChapterNumber(z))) }
    ) = CreateNew(fetchExistingChapterNumbersFun, createNewChapterFun).apply { context { console = TestConsole } }

}