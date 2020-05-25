package com.davioooh.leanpub

import com.davioooh.leanpub.commands.Chapters
import com.davioooh.leanpub.commands.Chapters.GetList
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands

class LPTools : NoOpCliktCommand()

fun main(args: Array<String>) =
        LPTools()
                .subcommands(Chapters().subcommands(GetList()))
                .main(args)