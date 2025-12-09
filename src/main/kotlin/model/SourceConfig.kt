package model

import crawler.TableConfigCrawler
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import util.MyUtil
import java.io.File
import java.util.*
import kotlin.collections.get

@Serializable
data class LeagueTableConfig(
    @SerialName("leagueTable")
    val leagueTable: LeagueTable
) {
    fun toConfig(): Config {
        val colorsMap = leagueTable.phaseColors
        val oldConfig =
            MyUtil.json.decodeFromString(Config.serializer(), File(TableConfigCrawler.tableConfigPath).readText())

        // Load localizable config
        val localizableConfigPath = "fastscore/res/localizable_config.json"
        val localizableConfig: Map<String, String> = try {
            val localizableFile = File(localizableConfigPath)
            if (localizableFile.exists()) {
                MyUtil.json.decodeFromString<Map<String, String>>(localizableFile.readText())
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        }

        val newPhase = oldConfig.phases.toMutableMap()
        leagueTable.phases.forEach { (key, phase) ->
            // Determine des value: use existing des, or lookup from localizable config if des_localization_key exists
            val des = if (phase.des.isNullOrBlank() && !phase.desLocalizationKey.isNullOrBlank()) {
                localizableConfig[phase.desLocalizationKey]?.takeIf { it.isNotBlank() }
            } else {
                phase.des?.takeIf { it.isNotBlank() }
            }

            // Create Phase without desLocalizationKey to avoid writing it to table_config.json
            newPhase[key] = Phase(
                col = "0x${colorsMap[phase.col]}",
                des = des
            )
        }
        val newPhaseDescription = oldConfig.phaseDescription.toMutableMap()
        leagueTable.phaseInfo.forEach { (key, value) ->
            newPhaseDescription[key] = value
        }

        val now = Calendar.getInstance().time.toString()
        val hasChanged = newPhase != oldConfig.phases || newPhaseDescription != oldConfig.phaseDescription
        val dataChangedTime = if (hasChanged) {
            now
        } else {
            oldConfig.dataChangedTime.ifBlank { oldConfig.updatedTime }
        }

        return Config(
            phases = newPhase,
            phaseDescription = newPhaseDescription,
            dataChangedTime = dataChangedTime,
            updatedTime = now
        )
    }
}

@Serializable
data class LeagueTable(
    val phases: Map<String, Phase>,
    @SerialName("phase_info")
    val phaseInfo: Map<String, String> = emptyMap(),
    @SerialName("split_table_info")
    val splitTableInfo: String? = null,
    @SerialName("phase_colors")
    val phaseColors: Map<String, String> = emptyMap()
)

@Serializable
data class Phase(
    val col: String? = null,
    val des: String? = null,
    @SerialName("des_localization_key")
    val desLocalizationKey: String? = null
)
