package com.mipt.ktbook.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mipt.ktbook.api.model.LoginRequest
import com.mipt.ktbook.api.model.SignupRequest
import com.mipt.ktbook.storage.Storage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.addAuthApi() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()

    routing {
        val storage by inject<Storage>()

        post("/user/signup") {
            val request = call.receive<SignupRequest>()
            val user = storage.createUser(request.username, request.password, request.realName)
            if (user == null) {
                call.respond(HttpStatusCode.Forbidden, "User already exists")
                return@post
            }

            call.respond(HttpStatusCode.OK)
        }

        post("/user/login") {
            val request = call.receive<LoginRequest>()
            val user = storage.getUser(request.username)
            if (user == null || user.password != request.password) {
                call.respond(HttpStatusCode.Forbidden, "Wrong username or password")
                return@post
            }

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60_000_000))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token))
        }
    }
}
