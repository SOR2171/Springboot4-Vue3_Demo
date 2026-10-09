package com.github.sor2171.backend

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.github.sor2171.backend.entity.dto.Account
import com.github.sor2171.backend.mapper.AccountMapper
import com.github.sor2171.backend.utils.DateUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var accountMapper: AccountMapper

    @Test
    fun contextLoads() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoder.encode(rawPassword)
        println("==================================================================")
        println("Default password '$rawPassword' BCrypt hash: $encodedPassword")
        println("==================================================================")

        val queryWrapper = QueryWrapper<Account>().eq("username", "test")
        val existingAccount = accountMapper.selectOne(queryWrapper)

        if (existingAccount == null) {
            val newAccount = Account(
                id = null,
                username = "test",
                password = encodedPassword,
                email = "1234567890@gmail.com",
                role = "user",
                registerTime = DateUtils.getCurrentDateTime()
            )
            accountMapper.insert(newAccount)
            println("Created default test user 'test' in database (ID: ${newAccount.id}).")
        } else {
            val updatedAccount = existingAccount.copy(password = encodedPassword)
            accountMapper.updateById(updatedAccount)
            println("Existing test user 'test' found (ID: ${existingAccount.id}), updated password hash.")
        }
    }
}