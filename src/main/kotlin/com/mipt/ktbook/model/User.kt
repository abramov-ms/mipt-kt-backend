package com.mipt.ktbook.model

data class User(
    val username: String,
    val passwordHash: String,
    val realName: String,
    val createdEpochSeconds: Long
)
