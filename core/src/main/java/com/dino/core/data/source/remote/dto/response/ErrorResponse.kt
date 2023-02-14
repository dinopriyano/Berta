package com.dino.core.data.source.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
	val code: String,
	val message: String,
	val status: String
)
