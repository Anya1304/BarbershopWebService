package com.spdu.app.mail_sending;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MailService {
    public static final String CLOSING_MESSAGE_PATTERN = """
            Dear %s , We`re sorry, but your order has been canceled due to the fact that salon was closed
            """;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender mailSender;
    private final AtomicLong countOfCancelMessages;
    private final String senderMail;

    public MailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String senderMail) {
        this.mailSender = mailSender;
        this.senderMail = senderMail;
        this.countOfCancelMessages = new AtomicLong();
    }

    public void greetingNewUser(User user) {
        String subject = "we are glad to see you on our website";
        String message = "Hello %s , it's cool that you decided to use our site, we hope you enjoy it"
                .formatted(user.getUsername());
        sendUserNotification(user, message, subject);
        logger.info("Greeting mail to {} was send successful", user.getEmail());
    }

    public void sendVerificationCode(User user, ConfirmationToken confirmationToken) {
        String subject = "Verification code";
        String text = "To confirm your account, please click here : "
                + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken();
        sendUserNotification(user, text, subject);
        logger.info("Verification code was send to {} successfully", user.getEmail());
    }

    public void sendUserNotification(User user, String message, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom(senderMail);
        mailSender.send(mailMessage);
    }

    @Async("mailSenderExecutorService")
    public void sendClosedSalonNotification(User user) {
        String subject = "About your order";
        String message = CLOSING_MESSAGE_PATTERN.formatted(user.getUsername());
        sendUserNotification(user, message, subject);

        countOfCancelMessages.incrementAndGet();
    }

    public long getCountOfCancelMessages() {
        return countOfCancelMessages.get();
    }
}
