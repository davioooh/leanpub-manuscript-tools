package com.davioooh.lptools

import com.davioooh.lptools.commands.ChaptersCmd
import com.davioooh.lptools.commands.ChaptersCmd.*
import com.davioooh.lptools.commands.ListChapterFilesFun
import com.davioooh.lptools.commands.ManuscriptCmd
import com.davioooh.lptools.commands.ManuscriptCmd.Generate
import com.davioooh.lptools.commands.ResolveManuscriptPathFun
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class LPTools(private val resolveManuscriptPath: ResolveManuscriptPathFun) : CliktCommand(name = APP_NAME, help = APP_HELP_MSG, epilog = APP_EPILOG_MSG) {
    private val bookFolder by option("-bf", "--book-folder", help = BOOK_FOLDER_OPT_HELP_MSG).path()
            .default(Path.of("."))
    private val config by findOrSetObject { Config() }

    override fun run() {
        if (resolveManuscriptPath(bookFolder) == null)
            echo("Error: Invalid book path: cannot find manuscript folder in: $bookFolder", err = true)
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

val listTxtChapters: ListChapterFilesFun = { bookFolder -> listChapterFilesWithExtension(bookFolder, TXT_EXT) }

fun main(args: Array<String>) =
        LPTools(::resolveManuscriptPathOrNull).subcommands(
                ChaptersCmd().subcommands(
                        ListFiles(listTxtChapters),
                        Convert(::listChapterFilesWithExtension),
                        CreateNew(::fetchChapterNumbers, ::createNewChapterFile)
                ),
                ManuscriptCmd().subcommands(
                        Generate(listTxtChapters, ::generateBookTxtFromFileNames)
                )
        ).main(args)