package com.mipt.ktbook.blogpost.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateResponse(
    val id: Long
)
