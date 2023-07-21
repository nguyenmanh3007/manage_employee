package com.scheduler;


import com.entity.Employee;
import com.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MailRemind {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(cron = "0 0/30 8-9 * * MON-FRI")
    public void remindCheckIn() throws MessagingException{
        List<Employee> lEmployee= employeeService.listNotCheckIn();
        for(Employee employee:lEmployee){
            sendEmailCheckIn(employee.getEmail());
        }
    }
    @Scheduled(cron = "0 0/30 18-19 * * MON-FRI")
    public void remindCheckOut() throws MessagingException{
        List<Employee> lEmployee= employeeService.listNotCheckOut();
        for(Employee employee:lEmployee){
            sendEmailCheckOut(employee.getEmail());
        }
    }

    public void sendEmailCheckIn(String mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3>Congratulations on your successful account creation</h3>"
                + "<img src='https://ncc.asia/images/logo/logo.png'>";
        message.setContent(htmlMsg, "text/html");
        message.setText("You haven't checked in today");
        helper.setTo(mail);
        helper.setSubject("Important notification!");
        javaMailSender.send(message);
    }
    public void sendEmailCheckOut(String mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3>Congratulations on your successful account creation</h3>"
                + "<img src='https://ncc.asia/images/logo/logo.png'>";
        message.setContent(htmlMsg, "text/html");
        message.setText("You haven't checked out today");
        helper.setTo(mail);
        helper.setSubject("Important notification!");
        javaMailSender.send(message);
    }
}
