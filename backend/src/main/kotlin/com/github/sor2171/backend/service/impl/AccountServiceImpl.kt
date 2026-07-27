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
import reactor.core.scheduler.Schedulers
import javax.security.auth.login.AccountNotFoundException

@Service
class AccountServiceImpl : ServiceImpl<AccountMapper, Account>(), AccountService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(
        username: String
    ): Mono<UserDetails> {

        return findAccountByName(username)
            .map {
                User.withUsername(it.username)
                    .password(it.password)
                    .roles(it.role)
                    .build()
            }
            .onErrorMap(
                AccountNotFoundException::class.java
            ) {
                UsernameNotFoundException(
                    "User not found"
                )
            }
    }

    override fun findAccountByName(username: String): Mono<Account> =
        findAccount("username") {
            ktQuery()
                .eq(Account::username, username)
                .one()
        }

    override fun findAccountByEmail(email: String): Mono<Account> =
        findAccount("email") {
            ktQuery()
                .eq(Account::email, email)
                .one()
        }

    private fun <T : Any> blockingCall(
        block: () -> T?
    ): Mono<T> =
        Mono.fromCallable(block)
            .subscribeOn(Schedulers.boundedElastic())

    private fun findAccount(
        fieldName: String,
        query: () -> Account?
    ): Mono<Account> {
        return blockingCall {
            logger.info(
                "Searching account by {}",
                fieldName
            )
            query()
        }.switchIfEmpty(
            Mono.error(
                AccountNotFoundException()
            )
        )
    }

    override fun findAccountByNameOrEmail(
        text: String
    ): Mono<Account> {
        return blockingCall {
            logger.info(
                "Searching account by identifier"
            )
            ktQuery()
                .and {
                    it.eq(Account::email, text)
                        .or()
                        .eq(Account::username, text)
                }
                .one()
        }.switchIfEmpty(
            Mono.error(AccountNotFoundException())
        )
    }

    override fun existAccountByName(
        username: String
    ): Mono<Boolean> {
        return blockingCall {
            ktQuery()
                .eq(Account::username, username)
                .exists()

        }
    }

    override fun existAccountByEmail(
        email: String
    ): Mono<Boolean> {
        return blockingCall {
            ktQuery()
                .eq(Account::email, email)
                .exists()

        }
    }

    override fun resetPasswordByEmail(
        email: String,
        encodedPassword: String
    ): Mono<Void> {
        return blockingCall {
            ktUpdate()
                .eq(Account::email, email)
                .set(
                    Account::password,
                    encodedPassword
                )
                .update()
        }
            .flatMap { updated ->
                if (updated) {
                    Mono.empty()
                } else {
                    Mono.error(
                        AccountNotFoundException()
                    )
                }
            }
    }
}