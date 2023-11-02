package com.scope;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class ScopeController {

    @Resource(name = "requestScopedBean")
    MessageScope requestScopedBean;

    @Resource(name = "sessionScopedBean")
    MessageScope sessionScopedBean;

    @Resource(name = "applicationScopedBean")
    MessageScope applicationScopedBean;

    @RequestMapping("/scopes/request")
    public String getRequestScopeMessage(final Model model) {
        model.addAttribute("previousMessage", requestScopedBean.getMessage());
        requestScopedBean.setMessage("Request Scope Message!");
        model.addAttribute("currentMessage", requestScopedBean.getMessage());
        return "scopeExample";
    }

    @RequestMapping("/scopes/session")
    public String getSessionScopeMessage(final Model model) {
        model.addAttribute("previousMessage", sessionScopedBean.getMessage());
        sessionScopedBean.setMessage("Session Scope Message!");
        model.addAttribute("currentMessage", sessionScopedBean.getMessage());
        return "scopeExample";
    }

    @RequestMapping("/scopes/application")
    public String getApplicationScopeMessage(final Model model) {
        model.addAttribute("previousMessage", applicationScopedBean.getMessage());
        applicationScopedBean.setMessage("Application Scope Message!");
        model.addAttribute("currentMessage", applicationScopedBean.getMessage());
        return "scopeExample";
    }
}
