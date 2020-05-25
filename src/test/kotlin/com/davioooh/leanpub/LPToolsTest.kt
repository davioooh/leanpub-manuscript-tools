package com.davioooh.leanpub

import com.davioooh.leanpub.commands.Chapters
import com.github.ajalt.clikt.core.subcommands
import org.junit.jupiter.api.Test

internal class LPToolsTest {
    @Test
    fun test() {
        LPTools().subcommands(Chapters()).parse(arrayOf("chapters", "-bf=test\\path"))
    }
}