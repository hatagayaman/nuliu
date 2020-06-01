package com.example.demo.controller;

import java.security.Principal;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Scope("prototype")
@Controller
public class LoginController {

    @RequestMapping("/loginForm")
    public String index() {
        return "loginForm";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "redirect:/loginForm";
    }

    @RequestMapping("/login-ok")
    public String loginok(Model model, Principal principal) {
    	Authentication authentication = (Authentication) principal;
    	UserDetails user = (UserDetails) authentication.getPrincipal();
    	if (user != null) {
    		if (user.getAuthorities().toString().contains("ADMIN")) {
    			return "redirect:/admin";
    		} else if (user.getAuthorities().toString().contains("CAST")) {
    			return "redirect:/cast";
    		} else if (user.getAuthorities().toString().contains("MEMBER")) {
    			return "redirect:/member";
    		}
    	}
    	return "top";
    }
}
