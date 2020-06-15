package com.davioooh.lptools

import com.davioooh.lptools.LPTools.Config
import com.davioooh.lptools.commands.ResolveManuscriptPathFun
import com.davioooh.lptools.commons.*
import com.github.ajalt.clikt.core.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class LPToolsTest {

    @BeforeEach
    fun setup() {
        TestConsole.clear()
    }

    @Test
    fun `running with no child command or option should print help`() {
        assertThatThrownBy {
            lpToolsWith(FakeCommand).parse(arrayOf())
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `running with --version option should print version number`() {
        lpToolsWith(FakeCommand).parse(arrayOf("--version"))

        assertThat(TestConsole.output).containsPattern("^v.*$")
    }

    @Test
    fun `running with no child command should print help -- also when book path is not valid`() {
        assertThatThrownBy {
            lpToolsWith(FakeCommand) { null }.parse(arrayOf())
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `default book folder should be set in command context`() {
        val lpTools = lpToolsWith(FakeCommand)

        lpTools.parse(arrayOf(FAKE_CMD_NAME))

        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(Path.of("."))
    }

    @Test
    fun `provided book folder should be set in command context`() {
        val lpTools = lpToolsWith(FakeCommand)

        lpTools.parse(arrayOf("-bf=$TEST_BOOK_URL", FAKE_CMD_NAME))

        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(TEST_BOOK_PATH)
    }

    @Test
    fun `should print out error if book folder is not valid and a child cmd is invoked`() {
        assertThatThrownBy {
            lpToolsWith(FakeCommand) { null }.parse(arrayOf("-bf=$TEST_BOOK_URL", FAKE_CMD_NAME))
        }.isInstanceOf(UsageError::class.java)
    }

    private fun lpToolsWith(
            subCmd: CliktCommand,
            resolveManuscriptPath: ResolveManuscriptPathFun = { TEST_MANUSCRIPT_PATH }
    ) = LPTools(resolveManuscriptPath)
            .apply { context { console = TestConsole } }
            .subcommands(subCmd)

}