package com.davioooh.leanpub.commands

import com.davioooh.leanpub.listAllChapters
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class Chapters : NoOpCliktCommand() {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "ls" to listOf("list")
    )

    class GetList : CliktCommand(name = "list", help = GET_LIST_HELP_MSG) {

        private val bookFolder by option("-bf", "--book-folder", help = BOOK_FOLDER_OPT_HELP_MSG).path()
                .default(Path.of("."))

        override fun run() {
            listAllChapters(bookFolder).map { it.name }.forEach { echo(it) }
        }
    }

    companion object {
        const val GET_LIST_HELP_MSG = "List all chapters in manuscript folder"
        const val BOOK_FOLDER_OPT_HELP_MSG = "The root folder of the book (containing the manuscript/ folder)"
    }

}