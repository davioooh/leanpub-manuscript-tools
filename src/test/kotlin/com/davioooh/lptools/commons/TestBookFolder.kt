package com.davioooh.lptools.commons

import com.davioooh.lptools.MANUSCRIPT_FOLDER
import com.davioooh.lptools.TXT_EXT
import com.davioooh.lptools.buildChapterFileName
import com.davioooh.lptools.createAndWriteLines
import java.io.File
import java.nio.file.Path

// TODO improvement: convert to object?
class TestBookFolder(val bookRootFolder: Path) {
    private val manuscriptFolder = bookRootFolder.resolve(MANUSCRIPT_FOLDER)

    fun createManuscriptFolder(): TestBookFolder {
        manuscriptFolder.apply { toFile().mkdir() }
        return TestBookFolder(bookRootFolder)
    }

    fun withFakeChapters(numberOfChapters: Int, filesExt: String = TXT_EXT): TestBookFolder {
        repeat(numberOfChapters) {
            createFakeChapterFile(it + 1, filesExt)
        }
        return this
    }

    fun withFakeBookTxt(): TestBookFolder = withFile("Book.txt")

    fun withFile(fileName: String): TestBookFolder {
        manuscriptFolder.resolve(fileName)
                .toFile()
                .createAndWriteLines()
        return this
    }

    fun deleteManuscriptFolder(): TestBookFolder {
        manuscriptFolder.toFile().deleteRecursively()
        return this
    }

    private fun createFakeChapterFile(chapterNum: Int, fileExt: String): File =
            manuscriptFolder.resolve(buildChapterFileName("0$chapterNum", fileExtension = fileExt))
                    .toFile()
                    .createAndWriteLines(
                            "# Chapter $chapterNum$LINE_SEPARATOR",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis mi nec risus sollicitudin finibus. " +
                                    "Maecenas nec gravida mauris. Maecenas sit amet quam ac tellus placerat molestie. Proin vestibulum sapien feugiat, vulputate tortor eget, rhoncus eros. " +
                                    "Nam non elit eget mauris porttitor sollicitudin. Aenean ullamcorper, magna at pharetra pulvinar, sapien magna pulvinar tortor, ut dignissim purus nulla ac purus. " +
                                    "Morbi in risus sit amet ipsum sodales feugiat sit amet in arcu. Integer at feugiat sem, quis tincidunt risus. Vestibulum tincidunt sem et dignissim molestie. " +
                                    "Curabitur fringilla magna sit amet vehicula bibendum. Phasellus blandit auctor justo, at aliquet magna mollis ac. Curabitur posuere pharetra tristique. " +
                                    "Vestibulum tristique mi id velit gravida condimentum. Donec aliquam congue quam, a luctus leo pharetra a. Aliquam faucibus sollicitudin massa, ut interdum velit commodo id. " +
                                    "Duis eros ipsum, faucibus vitae dapibus at, ornare in nisi.$LINE_SEPARATOR",
                            "Nullam tellus neque, consequat quis eleifend a, dignissim dignissim ipsum. Ut placerat dignissim interdum. Nam dignissim sagittis interdum. Cras tortor erat, volutpat ut sagittis in, tincidunt eget mauris. " +
                                    "Quisque a felis sed urna sollicitudin ornare commodo eu libero. Morbi luctus tristique ante nec pulvinar. Mauris interdum ante et velit convallis volutpat. " +
                                    "Sed rutrum lacus et ligula maximus ultrices. Nulla quis dignissim metus, ac euismod leo. Sed auctor lorem et eleifend ultricies. Nullam suscipit tellus eget molestie lobortis. " +
                                    "Morbi viverra ipsum sit amet molestie eleifend."
                    )

}