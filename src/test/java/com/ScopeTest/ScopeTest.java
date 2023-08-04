package com.ScopeTest;

import com.scope.EmailService;
import com.scope.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@SpringBootTest
public class ScopeTest {
    private static final String NAME = "John Smith";
    private static final String NAME_OTHER = "Anna Jones";
    @Test
    public void givenSingletonScope_whenSetName_thenEqualNames() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("scopes.xml");

        UserService userServiceA=applicationContext.getBean(UserService.class);
        userServiceA.setName(NAME);
        UserService userServiceB=applicationContext.getBean(UserService.class);
        Assert.assertEquals(NAME, userServiceB.getName());

        ((AbstractApplicationContext) applicationContext).close();
    }
    @Test
    public void givenPrototypeScope_whenSetNames_thenDifferentNames() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("scopes.xml");
        EmailService emailServiceA=applicationContext.getBean(EmailService.class);
        EmailService emailServiceB=applicationContext.getBean(EmailService.class);


        emailServiceA.setRecipient(NAME_OTHER);
        //emailServiceB.setRecipient(NAME_OTHER);

        Assert.assertEquals(NAME_OTHER, emailServiceA.getRecipient());
        Assert.assertEquals(null, emailServiceB.getRecipient());

        ((AbstractApplicationContext) applicationContext).close();
    }
}
