package com.mipt.ktbook.blogpost.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequest(
    val postBody: String
)
