package com.davioooh.lptools.commons

import com.github.ajalt.clikt.output.CliktConsole

object TestConsole : CliktConsole {
    private val outputSB = StringBuilder()
    val output: String
        get() = outputSB.toString()

    override fun promptForLine(prompt: String, hideInput: Boolean): String? {
        println("Input : $prompt")
        return prompt
    }

    override fun print(text: String, error: Boolean) {
        outputSB.append(text)
    }

    override val lineSeparator: String
        get() = LINE_SEPARATOR

    fun clear() = outputSB.clear()

}