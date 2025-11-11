package crawler

import extension.getEnv
import model.Config
import model.LeagueTableConfig
import util.MyUtil
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
