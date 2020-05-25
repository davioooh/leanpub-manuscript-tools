package com.davioooh.leanpub

import com.davioooh.leanpub.LPTools.Config
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class LPToolsTest {

    private val lpTools = LPTools().subcommands(DummyCommand())

    @Test
    fun `running with not child command should print help`() {
        assertThatThrownBy {
            lpTools.parse(arrayOf())
        }.isInstanceOf(PrintHelpMessage::class.java)
    }

    @Test
    fun `default book folder should be set in command context`() {
        lpTools.parse(arrayOf("fake-cmd"))
        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(Path.of("."))
    }

    @Test
    fun `provided book folder should be set in command context`() {
        lpTools.parse(arrayOf("-bf=test/path", "fake-cmd"))
        val config = lpTools.currentContext.findObject<Config>()!!
        assertThat(config.bookFolder).isEqualTo(Path.of("test/path"))
    }

    class DummyCommand : NoOpCliktCommand(name = "fake-cmd")

}