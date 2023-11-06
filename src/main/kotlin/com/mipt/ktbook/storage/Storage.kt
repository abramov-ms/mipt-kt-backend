package com.mipt.ktbook.storage

import com.mipt.ktbook.model.Blogpost
import com.mipt.ktbook.model.User

interface Storage {
    suspend fun createPost(body: String, author: User): Blogpost
    suspend fun getPost(id: Long): Blogpost?
    suspend fun getPostRange(offset: Long, count: Long): List<Blogpost>
    suspend fun updatePost(id: Long, newBody: String): Boolean
    suspend fun deletePost(id: Long): Boolean

    suspend fun createUser(username: String, password: String, realName: String): User?
    suspend fun getUser(username: String): User?
}
