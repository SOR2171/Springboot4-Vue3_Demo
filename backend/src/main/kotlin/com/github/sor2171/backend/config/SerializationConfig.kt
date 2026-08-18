package com.github.sor2171.backend.config

import com.github.sor2171.backend.utils.BigDecimalSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SerializationConfig {

    @Bean
    fun kotlinxSerializationJson(): Json = Json {
        serializersModule = SerializersModule {
            contextual(BigDecimalSerializer)
        }
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
        prettyPrint = true
    }
}