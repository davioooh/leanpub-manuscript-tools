package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class ManuscriptCmd : NoOpCliktCommand(name = MANUSCRIPT_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "gn" to listOf(GENERATE_CMD_NAME)
    )

    class Generate(val listChapterFilesFun: ListChapterFilesFun, val generateBookTxtFun: GenerateBookTxtFun) :
            CliktCommand(name = GENERATE_CMD_NAME, help = GENERATE_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()
        private val bookTxt by option("--book-txt", "-b", help = BOOK_TXT_OPT_HELP_MSG).flag()

        override fun run() {
            if (bookTxt) {
                // TODO what if I want to pass file names from console?
                val bookRootFolder = config.bookFolder!!
                val fileNames = listChapterFilesFun(bookRootFolder).map { it.name }
                val bookTxt = generateBookTxtFun(bookRootFolder, fileNames)
                echo("Generated: " + bookTxt.relativeTo(bookRootFolder.toFile()))
            }
        }
    }

    companion object {
        const val MANUSCRIPT_CMD_NAME = "manuscript"
        const val GENERATE_CMD_NAME = "generate"
        const val GENERATE_HELP_MSG = "" // TODO some helpful message...
        const val BOOK_TXT_OPT_HELP_MSG = "Generate Book.txt file."
    }

}