package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import java.io.File
import java.nio.file.Path

class ChaptersCmd : NoOpCliktCommand(name = CHAPTERS_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "lf" to listOf(LIST_FILE_CMD_NAME)
    )

    class ListFiles(private val listFilesFun: ListChapterFilesFun) :
            CliktCommand(name = LIST_FILE_CMD_NAME, help = CMD_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()

        override fun run() {
            listFilesFun(config.bookFolder!!).map { it.name }.forEach { echo(it) }
        }

        companion object {
            const val CMD_HELP_MSG = "List all chapters in manuscript folder."
        }

    }

    class Convert(private val conversionMappings: Map<String, (Path) -> List<File>>) :
            CliktCommand(name = CONVERT_CMD_NAME, help = CMD_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()
        private val to by option("--to", help = TO_OPT_HELP_MSG).choice("txt", "md")
                .default("")

        override fun run() {
            val conversionFun = conversionMappings[to]
                    ?: throw CliktError("Error: Unsupported conversion format: $to")
            val convertedFiles = conversionFun(config.bookFolder!!)
            echo("Converted ${convertedFiles.size} files.")
        }

        companion object {
            const val CMD_HELP_MSG = "Convert manuscript files to selected format."
            const val TO_OPT_HELP_MSG = "The format to convert to."
        }

    }

    companion object {
        const val CHAPTERS_CMD_NAME = "chapters"
        const val LIST_FILE_CMD_NAME = "list-files"
        const val CONVERT_CMD_NAME = "convert"

    }

}

/**
 * List manuscript chapter files, given the book root path
 */
typealias ListChapterFilesFun = (Path) -> List<File>