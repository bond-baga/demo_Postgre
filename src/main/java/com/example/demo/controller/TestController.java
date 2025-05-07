package com.example.demo.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UserForm;
import com.example.demo.service.LoginService;
import com.example.demo.service.NameService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TestController {
    @Autowired
    UserService userService;
        
    @Autowired
    LoginService loginService;
    
    @Autowired
    NameService nameService;

    
    @GetMapping(value={"/test"})
    public String getList(@ModelAttribute("UserForm")UserForm form, Model model, HttpSession session) {
    	model.addAttribute("title", "一覧"); 
        // 職員データの取得
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("users", userService.select(form));
        model.addAttribute("data", data);
        // プルダウンとチェックボックスのデータを取得
        form.getMapitems().put("state", nameService.getState());
        form.getMapitems().put("status", nameService.getStatus());
        return "test";
    }
        
    @PostMapping(value={"/test"})
    public String postList(UserForm form, BindingResult resul, RedirectAttributes redirectAttributes, HttpSession session) {
    	// 
		form.setStatekeys(new ArrayList<Integer>(Arrays.asList(1)));
		form.setStatusKey(1);
    	redirectAttributes.addFlashAttribute("UserForm", form); 
        return "redirect:/test";
    }
    

}