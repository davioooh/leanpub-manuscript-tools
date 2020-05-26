package com.davioooh.leanpub

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.output.CliktConsole
import java.nio.file.Path

const val LINE_SEPARATOR = "\r\n"
const val TEST_BOOK_URL = "test/path"
val testBookPath: Path = Path.of(TEST_BOOK_URL)
val testManuscriptPath: Path = testBookPath.resolve(MANUSCRIPT_FOLDER)

object TestConsole : CliktConsole {
    val output = StringBuilder()

    override fun promptForLine(prompt: String, hideInput: Boolean): String? {
        println("Input : $prompt")
        return prompt
    }

    override fun print(text: String, error: Boolean) {
        output.append(text)
    }

    override val lineSeparator: String get() = LINE_SEPARATOR

    fun clear() {
        output.clear()
    }

}

class DummyCommand : NoOpCliktCommand(name = "fake-cmd")