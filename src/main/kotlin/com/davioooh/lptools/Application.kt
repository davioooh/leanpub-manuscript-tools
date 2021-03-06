package com.davioooh.lptools

import com.davioooh.lptools.commands.ChaptersCmd
import com.davioooh.lptools.commands.ChaptersCmd.*
import com.davioooh.lptools.commands.ListChapterFilesFun
import com.davioooh.lptools.commands.ManuscriptCmd
import com.davioooh.lptools.commands.ManuscriptCmd.Generate
import com.davioooh.lptools.commands.ResolveManuscriptPathFun
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import java.util.*

class LPTools(private val resolveManuscriptPath: ResolveManuscriptPathFun) :
        CliktCommand(name = APP_NAME, help = APP_HELP_MSG, epilog = APP_EPILOG_MSG, invokeWithoutSubcommand = true) {
    private val bookFolder by option("-bf", "--book-folder", help = BOOK_FOLDER_OPT_HELP_MSG).path()
            .default(Path.of("."))
    private val version by option().flag()
    private val config by findOrSetObject { Config() }

    override fun run() {
        if (currentContext.invokedSubcommand == null) {
            if (version) {
                val p = Properties()
                p.load(javaClass.classLoader.getResourceAsStream("version.properties"))
                echo("v${p["version"]}")
            } else {
                throw PrintHelpMessage(this)
            }
        } else {
            if (resolveManuscriptPath(bookFolder) == null)
                throw UsageError("Invalid book path: cannot find manuscript folder in: ${bookFolder.toAbsolutePath()}")
            config.bookFolder = bookFolder
        }
    }

    companion object {
        const val APP_NAME = "lptools"
        const val APP_HELP_MSG = "Command line tools for Leanpub authors."
        const val APP_EPILOG_MSG = "Run '$APP_NAME COMMAND --help' for more information on a command."
        const val BOOK_FOLDER_OPT_HELP_MSG = "The root folder of the book (containing the manuscript folder)"
    }

    data class Config(var bookFolder: Path? = null)

}

fun main(args: Array<String>) =
        LPTools(::resolveManuscriptPathOrNull)
                .subcommands(
                        chaptersCommand,
                        manuscriptCommand
                )
                .main(args)

val listTxtChapters: ListChapterFilesFun = { bookFolder -> listChapterFilesWithExtension(bookFolder, TXT_EXT) }

val chaptersCommand =
        ChaptersCmd().subcommands(
                ListFiles(listTxtChapters),
                Convert(::listChapterFilesWithExtension),
                CreateNew(::fetchChapterNumbers, ::createNewChapterFile)
        )

val manuscriptCommand =
        ManuscriptCmd().subcommands(
                Generate(listTxtChapters, ::generateBookTxtFromFileNames)
        )