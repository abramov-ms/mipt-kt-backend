package com.mipt.ktbook.model

data class Blogpost(
    val id: Long,
    val body: String,
    val createdEpochSeconds: Long,
    val modifiedEpochSeconds: Long,
    val author: User
)