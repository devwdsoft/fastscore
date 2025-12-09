package crawler

import extension.getEnv
import kotlinx.serialization.json.Json
import model.Config
import model.LeagueTableConfig
import util.MyUtil
import java.io.File

object TableConfigCrawler {

    internal val tableConfigPath = "fastscore/config/table_config.json"

    // JSON serializer that excludes null values
    private val jsonWithoutNulls = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = false
        explicitNulls = false
    }

    fun crawlConfig() {
        val newConfig = MyUtil.fetchFromUrl(
            getEnv("CONFIG_URL").orEmpty(),
            LeagueTableConfig.serializer()
        )?.toConfig()
        if (newConfig != null) {
            File(tableConfigPath).writeText(jsonWithoutNulls.encodeToString(Config.serializer(), newConfig))
        }
    }
}
