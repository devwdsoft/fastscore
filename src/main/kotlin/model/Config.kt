package model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val phases: Map<String, Phase>,
    val phaseDescription: Map<String, String> = emptyMap(),
    val updatedTime: String,
    val tournamentStats: Map<String, String> = mapOf(
        "1" to "Goals",
        "3" to "Assists",
        "4" to "Red Cards",
        "6" to "Yellow Cards",
        "8" to "Shots On Target"
    )
)