package com.mipt.ktbook.api

import com.mipt.ktbook.api.model.CreateRequest
import com.mipt.ktbook.api.model.CreateResponse
import com.mipt.ktbook.api.model.EditRequest
import com.mipt.ktbook.model.User
import com.mipt.ktbook.storage.Storage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

private const val MAX_POST_LENGTH = 500
private const val POSTS_PER_PAGE = 50L

fun PipelineContext<Unit, ApplicationCall>.getPathParameter(name: String): String? {
    return call.parameters[name]
}

suspend fun PipelineContext<Unit, ApplicationCall>.getPrincipalUser(storage: Storage): User {
    val principal = call.principal<JWTPrincipal>()!!
    val username = principal.payload.getClaim("username").asString()
    return storage.getUser(username)!!
}

fun Application.addBlogpostApi() {
    routing {
        val storage by inject<Storage>()

        get("/posts/{id}") {
            val id = getPathParameter("id")?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Bad id format")
                return@get
            }

            val post = storage.getPost(id)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound, "Post not found")
                return@get
            }

            call.respond(post)
        }

        get("/posts/pages/{num}") {
            val pageNo = getPathParameter("num")?.toLongOrNull()
            if (pageNo == null) {
                call.respond(HttpStatusCode.BadRequest, "Bad page no. format")
                return@get
            }

            val offset = pageNo * POSTS_PER_PAGE
            call.respond(storage.getPostRange(offset, POSTS_PER_PAGE))
        }

        authenticate("auth-jwt") {
            put("/posts") {
                val request = call.receive<CreateRequest>()
                if (request.postBody.length > MAX_POST_LENGTH) {
                    call.respond(HttpStatusCode.BadRequest, "Post is too long")
                    return@put
                }

                val post = storage.createPost(request.postBody, author = getPrincipalUser(storage))
                call.respond(CreateResponse(post.id))
            }

            patch("/posts/{id}") {
                val id = getPathParameter("id")?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Bad id format")
                    return@patch
                }

                val post = storage.getPost(id)
                if (post == null) {
                    call.respond(HttpStatusCode.BadRequest, "No such post")
                    return@patch
                }

                if (post.author != getPrincipalUser(storage)) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@patch
                }

                val request = call.receive<EditRequest>()
                assert(storage.updatePost(id, request.newBody))
                call.respond(HttpStatusCode.OK)
            }

            delete("/posts/{id}") {
                val id = getPathParameter("id")?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Bad id format")
                    return@delete
                }

                val post = storage.getPost(id)
                if (post == null) {
                    call.respond(HttpStatusCode.BadRequest, "No such post")
                    return@delete
                }

                if (post.author != getPrincipalUser(storage)) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }

                assert(storage.deletePost(id))
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}
