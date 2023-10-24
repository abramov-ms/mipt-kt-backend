package com.mipt.ktbook.blogpost.storage

import com.mipt.ktbook.blogpost.model.Blogpost

interface Storage {
    suspend fun createPost(body: String): Blogpost
    suspend fun getPost(id: Long): Blogpost?
    suspend fun getPostRange(offset: Long, count: Long): List<Blogpost>
    suspend fun updatePost(id: Long, newBody: String): Boolean
    suspend fun deletePost(id: Long): Boolean
}
