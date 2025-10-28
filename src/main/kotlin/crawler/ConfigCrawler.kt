package crawler

import extension.getEnv
import org.example.model.Config
import org.example.model.LeagueTableConfig
import org.example.util.MyUtil
import java.io.File

object TableConfigCrawler {

    internal val tableConfigPath = "fastscore/config/table_config.json"

    fun crawlConfig() {
        val newConfig = MyUtil.fetchFromUrl(
            getEnv("CONFIG_URL").orEmpty(),
            LeagueTableConfig.serializer()
        )?.toConfig()
        if (newConfig != null) {
            File(tableConfigPath).writeText(MyUtil.json.encodeToString(Config.serializer(), newConfig))
        }
    }
}
