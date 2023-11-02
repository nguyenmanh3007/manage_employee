package com.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Properties;

@Configuration
@EnableScheduling
public class MailConfig {
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); //address server smtp
        mailSender.setPort(587);// port connect smtp

        mailSender.setUsername("nguyenmanh.ptit.3007@gmail.com");
        mailSender.setPassword("phvmcegfagmulzht\n");

        Properties props = mailSender.getJavaMailProperties(); // get information from object javaMailProperties
        props.put("mail.transport.protocol", "smtp"); //set protocol is smtp
        props.put("mail.smtp.auth", "true"); // turn on authenticate
        props.put("mail.smtp.starttls.enable", "true"); // turn on TLS(Transport Layer Security) when connect server
        props.put("mail.debug", "true"); // turn on write detail log to follow process send email
        return mailSender;
    }
}
