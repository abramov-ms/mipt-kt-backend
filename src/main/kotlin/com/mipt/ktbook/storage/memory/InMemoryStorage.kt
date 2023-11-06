package com.mipt.ktbook.storage.memory

import com.mipt.ktbook.model.Blogpost
import com.mipt.ktbook.storage.Storage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Instant
import kotlin.math.min

class InMemoryStorage : Storage {
    private var monotonicId = 0L
    private val posts = mutableSetOf<Blogpost>()
    private val mutex = Mutex()

    override suspend fun createPost(body: String): Blogpost {
        mutex.withLock {
            val now = Instant.now().epochSecond
            val post = Blogpost(
                monotonicId, body, createdEpochSeconds = now, modifiedEpochSeconds = now
            )
            posts.add(post)
            ++monotonicId
            return post
        }
    }

    override suspend fun getPost(id: Long): Blogpost? {
        mutex.withLock {
            return posts.find { it.id == id }
        }
    }

    override suspend fun getPostRange(offset: Long, count: Long): List<Blogpost> {
        mutex.withLock {
            val list = posts.toList()
            val from = min(list.size, offset.toInt())
            val toInclusive = min(list.size, (offset + count).toInt()) - 1
            return list.slice(IntRange(from, toInclusive))
        }
    }

    override suspend fun updatePost(id: Long, newBody: String): Boolean {
        mutex.withLock {
            val post = posts.find { it.id == id }
            if (post == null) {
                return false
            }

            posts.remove(post)
            posts.add(
                Blogpost(
                    id,
                    newBody,
                    post.createdEpochSeconds,
                    modifiedEpochSeconds = Instant.now().epochSecond
                )
            )

            return true
        }
    }

    override suspend fun deletePost(id: Long): Boolean {
        mutex.withLock {
            return posts.removeIf { it.id == id }
        }
    }
}
