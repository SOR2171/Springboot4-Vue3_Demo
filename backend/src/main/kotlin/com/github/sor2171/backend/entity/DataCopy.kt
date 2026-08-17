package com.github.sor2171.backend.entity

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

interface DataCopy {
    fun <T : Any> toAnotherObject(
        toClass: KClass<T>,
        otherProperties: Map<String, Any?> = emptyMap()
    ): T {
        @Suppress("UNCHECKED_CAST")
        val sourceProps = this::class.memberProperties
            .associate { it.name to (it as KProperty1<Any, *>).get(this) }
        return try {
            val vo = toClass.createInstance()
            for (prop in toClass.declaredMemberProperties) {
                val value = when {
                    sourceProps.containsKey(prop.name) -> sourceProps[prop.name]
                    otherProperties.containsKey(prop.name) -> otherProperties[prop.name]
                    else -> continue
                }
                @Suppress("UNCHECKED_CAST")
                val mutableProp = prop as? KMutableProperty1<Any, Any?>
                if (mutableProp != null) {
                    mutableProp.isAccessible = true
                    mutableProp.set(vo, value)
                } else {
                    val field = prop.javaField ?: continue
                    field.isAccessible = true
                    field.set(vo, value)
                }
            }
            vo
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(
                "Failed to instantiate ${toClass.simpleName}. Does it have a no-arg constructor?", e
            )
        }
    }
}