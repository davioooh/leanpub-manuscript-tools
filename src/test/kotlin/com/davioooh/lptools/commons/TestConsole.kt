package com.davioooh.lptools.commons

import com.github.ajalt.clikt.output.CliktConsole

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