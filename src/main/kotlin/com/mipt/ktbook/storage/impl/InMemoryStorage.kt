package com.mipt.ktbook.storage.impl

import com.mipt.ktbook.model.Blogpost
import com.mipt.ktbook.model.User
import com.mipt.ktbook.storage.Storage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import kotlin.math.min

class InMemoryStorage : Storage {
    private var monotonicId = 0L
    private val posts = mutableSetOf<Blogpost>()
    private val users = mutableSetOf<User>()
    private val mutex = Mutex()

    override suspend fun createPost(body: String, author: User): Blogpost {
        mutex.withLock {
            val now = Instant.now().epochSecond
            val post = Blogpost(
                monotonicId, body, createdEpochSeconds = now, modifiedEpochSeconds = now, author
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

    override suspend fun updatePost(post: Blogpost, newBody: String) {
        mutex.withLock {
            posts.remove(post)
            posts.add(
                Blogpost(
                    post.id,
                    newBody,
                    post.createdEpochSeconds,
                    modifiedEpochSeconds = Instant.now().epochSecond,
                    post.author
                )
            )
        }
    }

    override suspend fun deletePost(post: Blogpost) {
        mutex.withLock {
            posts.remove(post)
        }
    }

    override suspend fun createUser(username: String, password: String, realName: String): User? {
        mutex.withLock {
            if (users.find { it.username == username } != null) {
                return null
            }

            val user = User(
                username,
                BCrypt.hashpw(password, BCrypt.gensalt()),
                realName,
                Instant.now().epochSecond
            );
            users.add(user)
            return user
        }
    }

    override suspend fun getUser(username: String): User? {
        mutex.withLock {
            return users.find { it.username == username }
        }
    }
}
