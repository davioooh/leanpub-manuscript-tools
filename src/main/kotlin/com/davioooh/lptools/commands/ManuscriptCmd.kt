package com.davioooh.lptools.commands

import com.davioooh.lptools.LPTools
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class ManuscriptCmd : NoOpCliktCommand(name = MANUSCRIPT_CMD_NAME) {
    override fun aliases(): Map<String, List<String>> = mapOf(
            "gn" to listOf(GENERATE_CMD_NAME)
    )

    class Generate(val listChapterFiles: ListChapterFilesFun, val generateBookTxt: GenerateBookTxtFun) :
            CliktCommand(name = GENERATE_CMD_NAME, help = CMD_HELP_MSG) {
        private val config by requireObject<LPTools.Config>()
        private val bookTxt by option("--book-txt", "-b", help = BOOK_TXT_OPT_HELP_MSG).flag()

        override fun run() {
            when {
                bookTxt -> {
                    // TODO what if I want to pass file names from console?
                    val bookRootFolder = config.bookFolder!!
                    val fileNames = listChapterFiles(bookRootFolder).map { it.name }
                    if (fileNames.isNotEmpty()) {
                        val bookTxt = generateBookTxt(bookRootFolder, fileNames)
                        echo("Generated: $bookTxt")
                    } else {
                        echo("Book.txt not generated: no chapter files found in manuscript folder.")
                    }
                }
                else -> {
                    throw UsageError("At least one option is required.")
                }
            }
        }

        companion object {
            const val CMD_HELP_MSG = "Generate artifacts from manuscript files."
            const val BOOK_TXT_OPT_HELP_MSG = "Generate Book.txt file."
        }
    }

    companion object {
        const val MANUSCRIPT_CMD_NAME = "manuscript"
        const val GENERATE_CMD_NAME = "generate"
    }

}