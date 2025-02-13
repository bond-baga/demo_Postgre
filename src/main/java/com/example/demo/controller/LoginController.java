package com.example.demo.controller;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginForm;
import com.example.demo.service.LoginService;

@Controller
public class LoginController {

	@Autowired
    LoginService loginService;

    @GetMapping(value={"/login"})
    public String getLogin(@ModelAttribute("LoginForm")LoginForm form, @ModelAttribute("map")HashMap<String, Boolean> map, Model model) {
      model.addAttribute("title", "ログイン");
      if (!map.containsKey("registered")) {
      	map.put("registered", false);
      }
      
  	  return "login";
    }

    @GetMapping(value={"/register"})
    public String getregister(@ModelAttribute("LoginForm")LoginForm form, @ModelAttribute("map")HashMap<String, Boolean> map, Model model)  {
        model.addAttribute("title", "アカウント登録");
        if (!map.containsKey("exists")) {
        	map.put("exists", false);
        }
        if (!map.containsKey("failed")) {
        	map.put("failed", false);
        }
        if (!map.containsKey("registered")) {
        	map.put("registerd", false);
        } 
        return "register";
    }

    @PostMapping(value={"/register"}) 
    public String postRegister(LoginForm form, RedirectAttributes redirectAttributes) {
        LoginForm loginform = loginService.select(form.getUsername()); 
    	HashMap<String, Boolean> map = new HashMap<>();

    	if(loginform.getId() != 0){
            // ユーザーが既に存在する場合の処理
        	redirectAttributes.addFlashAttribute("LoginForm", form);  
        	map.put("exists", true);
        	redirectAttributes.addFlashAttribute("map", map);    	
            return "redirect:/register"; 
        } else {
            if (loginService.insert(form)) {
            	// 新規のアカウントが登録された場合　
            	redirectAttributes.addFlashAttribute("LoginForm", form);    	
            	map.put("registered", true);
            	redirectAttributes.addFlashAttribute("map", map);    	
                return "redirect:/login";             	
            } else {
            	// 新規のアカウントの登録に失敗した場合
            	redirectAttributes.addFlashAttribute("LoginForm", form);    	
            	map.put("failed", true);
            	redirectAttributes.addFlashAttribute("map", map);    	
                return "redirect:/register";             	            	
            }        	
        } 
    }        
}