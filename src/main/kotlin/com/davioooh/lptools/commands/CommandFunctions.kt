package com.davioooh.lptools.commands

import java.io.File
import java.nio.file.Path

/**
 * Resolve the manuscript path, given the book root path.
 * Returns null if the manuscript folder does not exist.
 */
typealias ResolveManuscriptPathFun = (bookFolder: Path) -> Path?

/**
 * List manuscript chapter files, given the book root path
 */
typealias ListChapterFilesFun = (bookFolder: Path) -> List<File>

/**
 * List manuscript chapter files having the given extension
 */
typealias ListChapterFilesWithExtFun = (bookFolder: Path, ext: String) -> List<File>

/**
 * Rename manuscript chapter files from *.ext to *.newExt
 */
typealias ReplaceExtFun = (chapterFiles: List<File>, newExt: String) -> List<File>

