package com.mipt.ktbook.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String,
    val realName: String,
    val createdEpochSeconds: Long
)
