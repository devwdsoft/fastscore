package model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val phases: Map<String, Phase>,
    val phaseDescription: Map<String, String> = emptyMap(),
    val dataChangedTime: String = "",
    val updatedTime: String
)