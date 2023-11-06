package com.mipt.ktbook.model

data class User(
    val username: String,
    val password: String,
    val realName: String,
    val createdEpochSeconds: Long
)
