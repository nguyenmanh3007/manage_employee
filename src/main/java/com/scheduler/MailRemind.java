package com.scheduler;


import com.dto.EmployeeDTO;
import com.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MailRemind {

    private final EmployeeService employeeService;
    private final JavaMailSender javaMailSender;

    @Scheduled(cron = "0 0/30 8-9 * * MON-FRI")
//    @Scheduled(cron = "0/5 * * * * *")
    public void remindCheckIn() throws MessagingException{
        List<EmployeeDTO> lEmployee= employeeService.listNotCheckIn();
        for(EmployeeDTO employee:lEmployee){
            sendEmailCheckIn(employee.getEmail());
        }
    }
    @Scheduled(cron = "0 0/30 18-19 * * MON-FRI")
    public void remindCheckOut() throws MessagingException{
        List<EmployeeDTO> lEmployee= employeeService.listNotCheckOut();
        for(EmployeeDTO employee:lEmployee){
            sendEmailCheckOut(employee.getEmail());
        }
    }
    @Async
    public void sendEmailCheckIn(String mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3>Congratulations on your successful account creation</h3>"
                + "<img src='https://cdnb.artstation.com/p/assets/images/images/029/729/759/large/ethan-clark-redesign-circles-whole.jpg?1598461208'>";
        message.setContent(htmlMsg, "text/html");
        message.setText("You haven't checked in today");
        helper.setTo(mail);
        helper.setSubject("Important notification!");
        javaMailSender.send(message);
    }
    @Async
    public void sendEmailCheckOut(String mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3>Congratulations on your successful account creation</h3>"
                + "<img src='https://cdnb.artstation.com/p/assets/images/images/029/729/759/large/ethan-clark-redesign-circles-whole.jpg?1598461208'>";
        message.setContent(htmlMsg, "text/html");
        message.setText("You haven't checked out today");
        helper.setTo(mail);
        helper.setSubject("Important notification!");
        javaMailSender.send(message);
    }
}
