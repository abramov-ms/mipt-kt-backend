package com.mipt.ktbook.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val password: String,
    val realName: String
)
