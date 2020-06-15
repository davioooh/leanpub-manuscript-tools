package com.davioooh.lptools

import com.davioooh.lptools.LPTools.Config
import com.davioooh.lptools.commons.*
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.core.subcommands
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class LPToolsTest {

    private val lpTools = LPTools { TEST_MANUSCRIPT_PATH }.subcommands(FakeCommand)
    private val lpToolsWithInvalidBookFolder = LPTools { null }.subcommands(FakeCommand)


    @BeforeEach
    fun setup() {
        TestConsole.clear()
    }

    @Test
    fun `running with no child command or option should print help`() {
        assertThatThrownBy {
            lpTools.parse(arrayOf())
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `running with no child command should print help -- also when book path is not valid`() {
        assertThatThrownBy {
            lpToolsWithInvalidBookFolder.parse(arrayOf())
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `default book folder should be set in command context`() {
        lpTools.parse(arrayOf(FAKE_CMD_NAME))

        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(Path.of("."))
    }

    @Test
    fun `provided book folder should be set in command context`() {
        lpTools.parse(arrayOf("-bf=$TEST_BOOK_URL", FAKE_CMD_NAME))

        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(TEST_BOOK_PATH)
    }

    @Test
    fun `should print out error if book folder is not valid and a child cmd is invoked`() {
        assertThatThrownBy {
            lpToolsWithInvalidBookFolder.parse(arrayOf("-bf=$TEST_BOOK_URL", FAKE_CMD_NAME))
        }.isInstanceOf(UsageError::class.java)
    }

}