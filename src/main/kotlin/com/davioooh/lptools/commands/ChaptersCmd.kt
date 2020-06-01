package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.davioooh.lptools.MD_EXT
import com.davioooh.lptools.TXT_EXT
import com.davioooh.lptools.replaceExtensionWith
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice

class ChaptersCmd : NoOpCliktCommand(name = CHAPTERS_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "lf" to listOf(LIST_FILE_CMD_NAME),
            "cf" to listOf(CONVERT_CMD_NAME)
    )

    class ListFiles(private val listChapterFiles: ListChapterFilesFun) :
            CliktCommand(name = LIST_FILE_CMD_NAME, help = CMD_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()

        override fun run() {
            listChapterFiles(config.bookFolder!!).map { it.name }.forEach { echo(it) }
        }

        companion object {
            const val CMD_HELP_MSG = "List all chapters in manuscript folder."
        }

    }

    class Convert(private val listChapterFilesWithExt: ListChapterFilesWithExtFun) :
            CliktCommand(name = CONVERT_CMD_NAME, help = CMD_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()
        private val to by option("--to", help = TO_OPT_HELP_MSG).choice(TXT_EXT, MD_EXT)
                .default("")

        override fun run() {
            val convertedFiles = when (to) {
                TXT_EXT -> {
                    val mdChapters = listChapterFilesWithExt(config.bookFolder!!, MD_EXT)
                    val txtRenamedChapters = mdChapters.replaceExtensionWith(TXT_EXT)
                    mdChapters.zip(txtRenamedChapters)
                }
                MD_EXT -> {
                    val txtChapters = listChapterFilesWithExt(config.bookFolder!!, TXT_EXT)
                    val mdRenamedChapters = txtChapters.replaceExtensionWith(MD_EXT)
                    txtChapters.zip(mdRenamedChapters)
                }
                else -> throw UsageError("Unsupported conversion format: $to")
            }
            echo("Converted ${convertedFiles.size} files.")
            convertedFiles.forEach { (from, to) ->
                echo("\t${from.name}\t=>\t${to.name}")
            }
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