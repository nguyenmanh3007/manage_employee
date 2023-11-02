package com.scope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class ScopeConfig {
    @Bean
    @Scope("singleton")
    public Person personSingleton() {
        return new Person();
    }

    @Bean
    @Scope("prototype")
    public Person personPrototype() {
        return new Person();
    }

    @Bean
    @RequestScope
    public MessageScope requestScopedBean() {
        return new MessageScope();
    }

    @Bean
    @SessionScope
    public MessageScope sessionScopedBean() {
        return new MessageScope();
    }

    @Bean
    @ApplicationScope
    public MessageScope applicationScopedBean() {
        return new MessageScope();
    }
}
