package com.mipt.ktbook.api.model

import kotlinx.serialization.Serializable

@Serializable
data class GetPostResponse(
    val id: Long,
    val body: String,
    val createdEpochSeconds: Long,
    val modifiedEpochSeconds: Long,
    val authorUsername: String
)