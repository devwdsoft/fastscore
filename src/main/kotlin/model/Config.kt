package org.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val phases: Map<String, Phase>,
    val phaseDescription: Map<String, String> = emptyMap(),
    val updatedTime: String
)