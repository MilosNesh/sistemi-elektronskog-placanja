package org.example.pspbackend.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMfaCode(String recipientEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("PSP Sistem - MFA Kod");
        message.setText("Vaš verifikacioni kod je: " + code);

        System.out.println("Mejl je poslat");
        mailSender.send(message);
    }
}
