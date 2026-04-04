package com.github.sor2171.backend.entity.vo.response

import com.github.sor2171.backend.utils.DateUtils.getCurrentDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizeVO(
    val username: String = "",
    val role: String = "",
    val token: String = "",
    val expire: LocalDateTime = getCurrentDateTime(),
)