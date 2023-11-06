package com.mipt.ktbook.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostResponse(
    val id: Long
)
