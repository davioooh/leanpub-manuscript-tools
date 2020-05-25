package com.davioooh.leanpub.commands

import com.davioooh.leanpub.LPTools
import com.davioooh.leanpub.ListFilesFun
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.output.CliktConsole

class ChaptersCmd : NoOpCliktCommand(name = CHAPTERS_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "lf" to listOf(LIST_FILE_CMD_NAME)
    )

    class ListFiles(customConsole: CliktConsole? = null, val listFilesFun: ListFilesFun) :
            CliktCommand(name = LIST_FILE_CMD_NAME, help = LIST_FILES_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()

        init {
            if (customConsole != null) context { this.console = customConsole }
        }

        override fun run() {
            listFilesFun(config.bookFolder!!).map { it.name }.forEach { echo(it) }
        }
    }

    companion object {
        const val CHAPTERS_CMD_NAME = "chapters"
        const val LIST_FILE_CMD_NAME = "list-files"
        const val LIST_FILES_HELP_MSG = "List all chapters in manuscript folder"
    }

}