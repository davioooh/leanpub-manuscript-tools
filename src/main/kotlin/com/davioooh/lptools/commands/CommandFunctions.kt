package com.davioooh.lptools.commands

import java.io.File
import java.nio.file.Path

/**
 * Resolve the manuscript path, given the book root path.
 * Returns null if the manuscript folder does not exist.
 */
typealias ResolveManuscriptPathFun = (Path) -> Path?

/**
 * List manuscript chapter files, given the book root path
 */
typealias ListChapterFilesFun = (Path) -> List<File>

/**
 * Rename manuscript chapter files from *.txt to *.md
 */
typealias TxtToMdFun = (Path) -> List<File>

/**
 * Rename manuscript chapter files from .md to .txt
 */
typealias MdToTxtFun = (Path) -> List<File>