package com.github.sor2171.backend.mapper.typehandler

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

@MappedTypes(LocalDateTime::class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
class TimeTypeHandler : BaseTypeHandler<LocalDateTime>() {
    override fun setNonNullParameter(
        ps: PreparedStatement,
        i: Int,
        parameter: LocalDateTime,
        jdbcType: JdbcType?
    ) {
        ps.setObject(i, parameter.toJavaLocalDateTime())
    }

    override fun getNullableResult(
        rs: ResultSet,
        columnName: String
    ): LocalDateTime? =
        rs.getTimestamp(columnName)
            ?.toLocalDateTime()
            ?.toKotlinLocalDateTime()

    override fun getNullableResult(
        rs: ResultSet,
        columnIndex: Int
    ): LocalDateTime? =
        rs.getTimestamp(columnIndex)
            ?.toLocalDateTime()
            ?.toKotlinLocalDateTime()

    override fun getNullableResult(
        cs: CallableStatement,
        columnIndex: Int
    ): LocalDateTime? =
        cs.getTimestamp(columnIndex)
            ?.toLocalDateTime()
            ?.toKotlinLocalDateTime()
}