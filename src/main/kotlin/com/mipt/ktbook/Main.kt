package com.mipt.ktbook

import com.mipt.ktbook.blogpost.api.addBlogpostApi
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        configureServer()
        addBlogpostApi()
    }.start(wait = true)
}
