package com.davioooh.leanpub

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class LPTools : NoOpCliktCommand()

class Chapters : NoOpCliktCommand(){
    override fun aliases(): Map<String, List<String>> = mapOf(
            "l" to listOf("list")
    )
}

class ChList : CliktCommand(name = "list", help = "List all chapters in manuscript folder") {
    private val bookFolder by option(
            "-bf", "--book-folder",
            help = "The root folder of the book (containing the manuscript/ folder)").path()
            .default(Path.of("."))

    override fun run() {
        listAllChapters(bookFolder).map { it.name }.forEach { echo(it) }
    }
}

fun main(args: Array<String>) =
        LPTools()
                .subcommands(Chapters().subcommands(ChList()))
                .main(args)