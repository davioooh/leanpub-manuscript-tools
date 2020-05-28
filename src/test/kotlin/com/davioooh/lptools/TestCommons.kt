package com.davioooh.lptools

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.output.CliktConsole
import java.nio.file.Path

val LINE_SEPARATOR: String = System.lineSeparator()
const val TEST_BOOK_URL = "test/path"
val TEST_BOOK_PATH: Path = Path.of(TEST_BOOK_URL)
val TEST_MANUSCRIPT_PATH: Path = TEST_BOOK_PATH.resolve(MANUSCRIPT_FOLDER)

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