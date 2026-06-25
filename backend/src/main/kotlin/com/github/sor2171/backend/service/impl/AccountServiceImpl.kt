package com.github.sor2171.backend.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.github.sor2171.backend.entity.dto.Account
import com.github.sor2171.backend.mapper.AccountMapper
import com.github.sor2171.backend.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountServiceImpl : ServiceImpl<AccountMapper, Account>(), AccountService {

    val logger = LoggerFactory.getLogger(this::class.java)!!

    override fun findByUsername(username: String): Mono<UserDetails> {
        return findAccountByName(username)
            .switchIfEmpty(Mono.error(UsernameNotFoundException("Account with name $username not found")))
            .map { account ->
                User.withUsername(username)
                    .password(account.password)
                    .roles(account.role)
                    .build()
            }
    }

    override fun findAccountByName(username: String): Mono<Account> =
        findAccount("name", username) {
            ktQuery()
                .eq(Account::username, username)
                .one()
        }

    override fun findAccountByEmail(email: String): Mono<Account> =
        findAccount("email", email) {
            ktQuery()
                .eq(Account::email, email)
                .one()
        }

    private fun findAccount(
        fieldName: String,
        value: String,
        query: () -> Account?
    ): Mono<Account> =
        Mono.fromCallable {
            logger.info("Find account with {} {}", fieldName, value)
            query()
        }.switchIfEmpty(
            Mono.error(
                UsernameNotFoundException("Account with $fieldName $value not found")
            )
        )

    override fun findAccountByNameOrEmail(text: String): Mono<Account> =
        findAccountByEmail(text).onErrorResume(
            UsernameNotFoundException::class.java
        ) {
            findAccountByName(text)
        }

    override fun existAccountByName(username: String): Mono<Boolean> =
        Mono.fromCallable {
            logger.info("Determine if name {} exists", username)
            ktQuery()
                .eq(Account::username, username)
                .exists()
        }

    override fun existAccountByEmail(email: String): Mono<Boolean> =
        Mono.fromCallable {
            logger.info("Determine if email {} exists", email)
            ktQuery()
                .eq(Account::email, email)
                .exists()
        }

    override fun resetPasswordByEmail(
        email: String,
        encodedPassword: String
    ): Mono<Void> =
        existAccountByEmail(email)
            .flatMap { exists ->
                if (!exists) {
                    return@flatMap Mono.error(
                        UsernameNotFoundException(
                            "Account with email $email not found"
                        )
                    )
                }

                Mono.fromCallable {
                    ktUpdate()
                        .eq(Account::email, email)
                        .set(Account::password, encodedPassword)
                        .update()
                }
            }
            .flatMap { success ->
                if (success) {
                    Mono.empty()
                } else {
                    Mono.error(
                        IllegalStateException(
                            "Failed to reset password"
                        )
                    )
                }
            }
}