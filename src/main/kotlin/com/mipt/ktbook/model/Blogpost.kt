package com.mipt.ktbook.model

import kotlinx.serialization.Serializable

@Serializable
data class Blogpost(
    val id: Long,
    val body: String,
    val createdEpochSeconds: Long,
    val modifiedEpochSeconds: Long,
    val author: User
)