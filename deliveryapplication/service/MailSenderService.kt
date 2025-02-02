package org.example.deliveryapplication.service

import org.springframework.mail.MailMessage

interface MailSenderService {
    fun sendAuthorizationCode(mail: String)
}