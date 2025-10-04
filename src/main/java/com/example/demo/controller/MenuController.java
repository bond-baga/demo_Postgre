package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MenuController {

    @Autowired
    LoginService loginService;
	
	@GetMapping(value={"/","/menu"})
    public String getHome(Model model, HttpSession session) {
        model.addAttribute("title", "メニュー");
        // セッションの初期化は行わない
        // セッションにログインユーザーを保存
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("user", loginService.select(auth.getName()));        
        return "menu";
    }
    
}    
