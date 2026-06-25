package com.github.sor2171.backend.service.v1

import com.github.sor2171.backend.entity.dto.Account
import com.github.sor2171.backend.entity.dto.EmailVerificationMsg
import com.github.sor2171.backend.entity.enums.EmailType
import com.github.sor2171.backend.entity.vo.request.EmailRegisterVO
import com.github.sor2171.backend.entity.vo.request.PasswordResetVO
import com.github.sor2171.backend.entity.vo.response.ReloginVO
import com.github.sor2171.backend.service.AccountService
import com.github.sor2171.backend.utils.Const
import com.github.sor2171.backend.utils.DateUtils.getCurrentDateTime
import com.github.sor2171.backend.utils.FlowUtils
import com.github.sor2171.backend.utils.JwtUtils
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

@Service
class AuthorizationService(
    private val accountService: AccountService,
    private val utils: FlowUtils,
    private val jwtUtils: JwtUtils,
    private val amqpTemplate: AmqpTemplate,
    private val stringRedisTemplate: ReactiveStringRedisTemplate,
    private val encoder: PasswordEncoder
) {
    fun askEmailVerifyCode(type: EmailType, email: String, ip: String): Mono<String> {
        return verifyLimit(ip).flatMap { allowed ->
            if (allowed) {
                val code = (100000..999999).random().toString()
                val data = EmailVerificationMsg(email, type, code)

                Mono.fromRunnable<Void> {
                    amqpTemplate.convertAndSend("mail", data)
                }.then(
                    stringRedisTemplate.opsForValue()
                        .set(
                            Const.VERIFY_EMAIL_DATA + email,
                            code,
                            Duration.ofMinutes(3)
                        )
                ).thenReturn("").subscribeOn(Schedulers.boundedElastic())
            } else {
                Mono.just("Request limit exceeded. Please try again later.")
            }
        }
    }

    fun registerEmailAccount(vo: EmailRegisterVO): Mono<String> {
        val (email, code, username, password) = vo

        return verifyCode(email, code)
            .filter { it.isBlank() }
            .switchIfEmpty(Mono.just("wrong code"))
            .flatMap {
                accountService.existAccountByEmail(email)
            }
            .flatMap { emailExists ->
                if (emailExists) {
                    Mono.just("account with the same email already exists.")
                } else {
                    accountService.existAccountByName(username).flatMap { usernameExists ->
                        if (usernameExists) {
                            Mono.just("username already exists.")
                        } else {
                            val account = Account(
                                id = null,
                                username = username,
                                password = encoder.encode(password)!!,
                                email = email,
                                role = "user",
                                registerTime = getCurrentDateTime()
                            )

                            Mono.fromCallable { accountService.save(account) }
                                .flatMap { success ->
                                    if (success) {
                                        stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email)
                                            .thenReturn("")
                                    } else {
                                        Const.INTERNAL_ERROR_MONO
                                    }
                                }
                        }
                    }
                }
            }
    }

    fun resetEmailAccountPassword(vo: PasswordResetVO): Mono<String> {
        val (email, code, password) = vo

        val encodedPassword =
            encoder.encode(password)
                ?: return Const.INTERNAL_ERROR_MONO

        return verifyCode(email, code)
            .then(
                accountService.resetPasswordByEmail(
                    email,
                    encodedPassword
                )
            )
            .then(
                stringRedisTemplate.delete(
                    Const.VERIFY_EMAIL_DATA + email
                )
            )
            .thenReturn("Password reset successfully")
    }

    fun jwtTokenRelogin(headerToken: String?): Mono<ReloginVO>? {
        val user = jwtUtils.resolveJwt(headerToken?.substring("Bearer ".length))
            ?.let { jwtUtils.toUser(it) }
            ?: return null
        val accountMono = accountService.findAccountByName(user.username)
        val voMono = accountMono.map {
            ReloginVO(
                jwtUtils.createJwt(user, it.id!!, it.username),
                jwtUtils.expiresTime().toInstant().toString()
            )
        }
        return voMono
    }

    fun invalidateJwt(headerToken: String?): Boolean {
        return jwtUtils.invalidateJwt(headerToken)
    }

    private fun verifyLimit(ip: String): Mono<Boolean> {
        val key = Const.VERIFY_EMAIL_LIMIT + ip
        return utils.limitOnceCheck(key, 60)
    }

    private fun verifyCode(email: String, receivedCode: String?): Mono<String> {
        return stringRedisTemplate.opsForValue()
            .get(Const.VERIFY_EMAIL_DATA + email)
            .flatMap { code ->
                if (receivedCode == null || code != receivedCode)
                    Mono.just("verify code is wrong.")
                else Mono.just("")
            }
            .switchIfEmpty(Mono.just("verify code has not been sent"))
    }
}