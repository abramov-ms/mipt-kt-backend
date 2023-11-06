package com.mipt.ktbook.blogpost.model

import kotlinx.serialization.Serializable

@Serializable
data class Blogpost(
    val id: Long,
    val body: String,
    val createdEpochSeconds: Long,
    val modifiedEpochSeconds: Long
)