package io.github.stream29.remenberingassistant

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.github.stream29.remenberingassistant.model.ApiAuth
import kotlinx.serialization.decodeFromString

object Global {
    val yaml = Yaml(
        configuration = YamlConfiguration(
            encodeDefaults = true,
            strictMode = false,
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "type"
        )
    )

    val apiProvidersProperty = reloadable {
        apiAuthConfigFile.also { if (!it.exists()) it.createNewFile() }.readText().ifEmpty { "{}" }
            .let {
                runCatching { yaml.decodeFromString<Map<String, ApiAuth>>(it) }.getOrDefault(emptyMap())
            }
    }

    val apiProviders by apiProvidersProperty

    var currentApiAuth by AutoSavableFileDelegate(currentApiAuthFile)

    val apiAuthDelegate = AutoSavableFileDelegate(apiAuthConfigFile)
    var apiAuthText by apiAuthDelegate
    val memoryDelegate = AutoSavableFileDelegate(memoryFile)
    var memoryText by memoryDelegate
}
