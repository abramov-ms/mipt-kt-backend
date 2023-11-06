package com.mipt.ktbook.blogpost.api.model

import kotlinx.serialization.Serializable

@Serializable
data class EditRequest(
    val newBody: String
)