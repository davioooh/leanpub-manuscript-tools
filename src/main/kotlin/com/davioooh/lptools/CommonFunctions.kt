package com.davioooh.lptools

import java.io.File

fun List<File>.replaceExtensionWith(newExtension: String): List<File> =
        this.map { file ->
            File(file.parentFile, "${file.nameWithoutExtension}.$newExtension")
                    .apply { file.renameTo(this) }
        }