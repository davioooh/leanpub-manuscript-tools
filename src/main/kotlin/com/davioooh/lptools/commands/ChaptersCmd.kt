package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.requireObject
import java.io.File
import java.nio.file.Path

class ChaptersCmd : NoOpCliktCommand(name = CHAPTERS_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "lf" to listOf(LIST_FILE_CMD_NAME)
    )

    class ListFiles(val listFilesFun: ListChapterFilesFun) :
            CliktCommand(name = LIST_FILE_CMD_NAME, help = LIST_FILES_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()

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

/**
 * List manuscript chapter files, given the book root path
 */
typealias ListChapterFilesFun = (Path) -> List<File>