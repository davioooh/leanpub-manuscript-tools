package com.davioooh.lptools.commons

import com.davioooh.lptools.MANUSCRIPT_FOLDER
import com.github.ajalt.clikt.core.NoOpCliktCommand
import java.io.File
import java.nio.file.Path

val LINE_SEPARATOR: String = System.lineSeparator()

val fakeTxtFiles = listOf(File("ch01.txt"), File("ch02.txt"), File("ch03.txt"))
val fakeMdFiles = listOf(File("ch01.md"), File("ch02.md"), File("ch03.md"))
val fakeTxtFileNamesList = fakeTxtFiles.map { it.name }
val fakeMdFileNamesList = fakeMdFiles.map { it.name }
val fakeTxtFileNames = fakeTxtFileNamesList.joinToString(LINE_SEPARATOR, postfix = LINE_SEPARATOR)
val fakeMdFileNames = fakeMdFileNamesList.joinToString(LINE_SEPARATOR, postfix = LINE_SEPARATOR)

const val TEST_BOOK_URL = "test/path"
val TEST_BOOK_PATH: Path = Path.of(TEST_BOOK_URL)
val TEST_MANUSCRIPT_PATH: Path = TEST_BOOK_PATH.resolve(MANUSCRIPT_FOLDER)

const val FAKE_CMD_NAME = "fake-cmd"

object FakeCommand : NoOpCliktCommand(name = FAKE_CMD_NAME)