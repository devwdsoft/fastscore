package org.example.crawler

import extension.getEnv
import org.example.model.Config
import org.example.model.LeagueTableConfig
import org.example.util.MyUtil
import java.io.File

object ConfigCrawler {

    internal val configPath = "fastscore/config/config.json"

    fun crawlConfig() {
        val newConfig = MyUtil.fetchFromUrl(
            getEnv("CONFIG_URL").orEmpty(),
            LeagueTableConfig.serializer()
        )?.toConfig()
        if (newConfig != null) {
            File(configPath).writeText(MyUtil.json.encodeToString(Config.serializer(), newConfig))
        }
    }
}
