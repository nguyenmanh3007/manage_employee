package com.ScopeTest;

import com.scope.Person;
import org.junit.Assert;
import org.junit.Test;
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
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("scopes.xml");

        final Person personSingletonA = (Person) applicationContext.getBean("personPrototype");
        final Person personSingletonB = (Person) applicationContext.getBean("personPrototype");

        personSingletonA.setName(NAME);
        Assert.assertEquals(NAME, personSingletonB.getName());

        ((AbstractApplicationContext) applicationContext).close();
    }

    @Test
    public void givenPrototypeScope_whenSetNames_thenDifferentNames() {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("scopes.xml");

        final Person personPrototypeA = (Person) applicationContext.getBean("personPrototype");
        final Person personPrototypeB = (Person) applicationContext.getBean("personPrototype");


        personPrototypeA.setName(NAME);
        personPrototypeB.setName(NAME_OTHER);

        Assert.assertEquals(NAME, personPrototypeA.getName());
        Assert.assertEquals(NAME_OTHER, personPrototypeB.getName());

        ((AbstractApplicationContext) applicationContext).close();
    }
}
