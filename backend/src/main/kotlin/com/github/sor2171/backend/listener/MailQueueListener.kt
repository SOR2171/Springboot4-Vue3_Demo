package com.github.sor2171.backend.listener

import com.github.sor2171.backend.entity.dto.EmailVerificationMsg
import com.github.sor2171.backend.entity.enums.EmailType
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
@RabbitListener(queues = ["mail"])
class MailQueueListener(
    @param:Value($$"${spring.mail.username}")
    private val username: String,
    private val json: Json,
    private val sender: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitHandler
    fun senderMailMessage(content: String) {
        val data = json.decodeFromString<EmailVerificationMsg>(content)
        val (email, type, code) = data
        
        val message = when (type) {
            EmailType.REGISTER -> createMailMessage(
                "Welcome to Our Service",
                "Thank you for registering! Your verification code is: $code"
                        + "\nThis code is valid for 3 minutes.",
                email
            )

            EmailType.RESET -> createMailMessage(
                "Password Reset Request",
                "You requested a password reset. Your verification code is: $code"
                        + "\nThis code is valid for 3 minutes.",
                email
            )
        }
        
        sender.send(message)
        logger.info("Email sent to $email.")
    }

    private fun createMailMessage(
        title: String,
        content: String,
        to: String
    ): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.subject = title
        message.text = content
        message.from = username
        message.setTo(to)
        return message
    }
}