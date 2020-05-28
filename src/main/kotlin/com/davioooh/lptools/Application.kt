package com.davioooh.lptools

import com.davioooh.lptools.commands.ChaptersCmd
import com.davioooh.lptools.commands.ChaptersCmd.Convert
import com.davioooh.lptools.commands.ChaptersCmd.ListFiles
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class LPTools(val resolveManuscriptPathFun: ResolveManuscriptPathFun) : CliktCommand(name = APP_NAME, help = APP_HELP_MSG, epilog = APP_EPILOG_MSG) {
    private val bookFolder by option("-bf", "--book-folder", help = BOOK_FOLDER_OPT_HELP_MSG).path()
            .default(Path.of("."))
    private val config by findOrSetObject { Config() }

    override fun run() {
        if (resolveManuscriptPathFun(bookFolder) == null)
            throw UsageError("Invalid book path: cannot find manuscript folder in: $bookFolder")
        config.bookFolder = bookFolder
    }

    companion object {
        const val APP_NAME = "lptools"
        const val APP_HELP_MSG = "Command line tools for Leanpub authors."
        const val APP_EPILOG_MSG = "Run '$APP_NAME COMMAND --help' for more information on a command."
        const val BOOK_FOLDER_OPT_HELP_MSG = "The root folder of the book (containing the manuscript folder)"
    }

    data class Config(var bookFolder: Path? = null)

}

/**
 * Resolve the manuscript path, given the book root path.
 * Returns null if the manuscript folder does not exist.
 */
typealias ResolveManuscriptPathFun = (Path) -> Path?

fun main(args: Array<String>) =
        LPTools(::resolveManuscriptPathOrNull).subcommands(
                ChaptersCmd().subcommands(
                        ListFiles(::listAllChapterFiles),
                        Convert(
                                ::convertTxtChapterFilesToMd,
                                ::convertMdChapterFilesToTxt
                        )
                )
        ).main(args)