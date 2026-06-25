package com.github.sor2171.backend.config

import com.github.sor2171.backend.entity.ApiResponse
import com.github.sor2171.backend.entity.vo.response.AuthorizeVO
import com.github.sor2171.backend.filter.JwtAuthorizeFilter
import com.github.sor2171.backend.service.AccountService
import com.github.sor2171.backend.utils.DateUtils
import com.github.sor2171.backend.utils.JwtUtils
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtUtils: JwtUtils,
    private val service: AccountService,
    private val jwtAuthorizeFilter: JwtAuthorizeFilter,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        val manager = UserDetailsRepositoryReactiveAuthenticationManager(service)
        manager.setPasswordEncoder(passwordEncoder)
        return manager
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange {
                it
                    .pathMatchers(
                        "/api/v1/auth/relogin"
                    ).authenticated()
                    .pathMatchers(
                        "/api/v1/auth/**",
                        "/ws/**",
                        "/error"
                    ).permitAll()
                    .anyExchange()
                    .authenticated()
            }
            .addFilterAt(loginAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(jwtAuthorizeFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .exceptionHandling {
                it.authenticationEntryPoint { exchange, ex ->
                    writeJsonResponse(
                        exchange,
                        HttpStatus.UNAUTHORIZED,
                        ApiResponse.unauthenticated(ex.message)
                    )
                }
                    .accessDeniedHandler { exchange, ex ->
                        writeJsonResponse(
                            exchange,
                            HttpStatus.FORBIDDEN,
                            ApiResponse.forbidden(ex.message)
                        )
                    }
            }
            .csrf { it.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .build()
    }

    @Bean
    fun loginAuthenticationFilter(): AuthenticationWebFilter {
        val filter = AuthenticationWebFilter(reactiveAuthenticationManager())
        filter.setRequiresAuthenticationMatcher(
            ServerWebExchangeMatchers.pathMatchers("/api/v1/auth/login")
        )
        filter.setServerAuthenticationConverter(loginAuthenticationConverter())
        filter.setAuthenticationSuccessHandler { webFilterExchange, authentication ->
            val user = authentication.principal as User
            service.findAccountByNameOrEmail(user.username)
                .flatMap { account ->
                    val vo = account.toAnotherObject(
                        AuthorizeVO::class,
                        mapOf(
                            "token" to jwtUtils.createJwt(user, account.id!!, account.username),
                            "expire" to DateUtils.dateToLocalDateTime(jwtUtils.expiresTime())
                        )
                    )
                    writeJsonResponse(
                        webFilterExchange.exchange,
                        HttpStatus.OK,
                        ApiResponse.success(vo)
                    )
                }
        }
        filter.setAuthenticationFailureHandler { webFilterExchange, exception ->
            writeJsonResponse(
                webFilterExchange.exchange,
                HttpStatus.UNAUTHORIZED,
                ApiResponse.unauthenticated(exception.message)
            )
        }
        return filter
    }

    private fun loginAuthenticationConverter(): ServerAuthenticationConverter {
        return ServerAuthenticationConverter { exchange ->
            exchange.formData.flatMap { formData ->
                val username = formData.getFirst("username")
                val password = formData.getFirst("password")
                if (username != null && password != null) {
                    Mono.just(
                        UsernamePasswordAuthenticationToken(
                            username,
                            password
                        )
                    )
                } else {
                    Mono.empty()
                }
            }
        }
    }

    private inline fun <reified T> writeJsonResponse(
        exchange: ServerWebExchange,
        status: HttpStatus,
        body: ApiResponse<T>
    ): Mono<Void> {
        val response = exchange.response
        response.statusCode = status
        response.headers.contentType = MediaType.APPLICATION_JSON

        val jsonString = Json.encodeToJsonElement(body).toString()
        val buffer = response.bufferFactory().wrap(jsonString.toByteArray())
        return response.writeWith(Mono.just(buffer))
    }
}