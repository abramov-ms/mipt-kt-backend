package com.mipt.ktbook

import com.mipt.ktbook.api.addAuthApi
import com.mipt.ktbook.api.addBlogpostApi
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.ktbook() {
    configureServer()
    addBlogpostApi()
    addAuthApi()
}
