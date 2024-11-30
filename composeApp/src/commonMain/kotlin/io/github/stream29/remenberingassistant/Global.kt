package io.github.stream29.remenberingassistant

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.github.stream29.remenberingassistant.model.ApiAuth
import kotlinx.serialization.decodeFromString
import java.time.Instant

object Global {
    val yaml = Yaml(
        configuration = YamlConfiguration(
            encodeDefaults = true,
            strictMode = false,
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "type"
        )
    )

    @Volatile
    var reload = Instant.now()!!

    val apiProviders by AutoReloadableDelegate {
        Global.runCatching {
            yaml.decodeFromString<Map<String, ApiAuth>>(apiAuthText)
        }.getOrDefault(emptyMap())
    }

    var currentApiAuth by AutoSavableFileDelegate(currentApiAuthFile)

    val apiAuthDelegate = AutoSavableFileDelegate(apiAuthConfigFile)
    var apiAuthText by apiAuthDelegate
    val memoryDelegate = AutoSavableFileDelegate(memoryFile)
    var memoryText by memoryDelegate
}
