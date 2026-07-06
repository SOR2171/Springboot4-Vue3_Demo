package com.github.sor2171.backend.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ApiResponse<T>(
    val code: Int,
    val data: T?,
    val message: String
) {
    companion object {
        fun <T> success(data: T, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(200, data, message)
        }

        // 针对无 data 返回的情况，统一使用 Any?
        fun <T> success(message: String = "Success"): ApiResponse<T> {
            return ApiResponse(200, null, message)
        }

        fun <T> failure(code: Int, data: T?, message: String?): ApiResponse<T> {
            return ApiResponse(code, data, message ?: "Failure")
        }

        fun <T> unauthenticated(message: String?) =
            failure<T>(401, null, message)

        fun <T> forbidden(message: String?) =
            failure<T>(403, null, message)

        fun <T> innerError(message: String?) =
            failure<T>(500, null, message)

        fun <T> logoutFailed(message: String = ""): ApiResponse<T> {
            val formatMessage = if (message.isBlank()) "" else ": $message"
            return failure(400, null, "Logout Failed$formatMessage")
        }
    }
}

inline fun <reified T> ApiResponse<T>.toJsonString(): String {
    return Json.encodeToString(this)
}