package com.example.demo.controller;
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
    public String getLogin(@ModelAttribute("LoginForm")LoginForm form, Model model) {
    	model.addAttribute("title", "ログイン");      
    	return "login";
    }

    @GetMapping(value={"/register"})
    public String getregister(@ModelAttribute("LoginForm")LoginForm form, Model model)  {
        model.addAttribute("title", "アカウント登録");
        if (! form.getMsg().isEmpty()) {
            model.addAttribute("LoginForm", form);
        }
        return "register";
    }

    @PostMapping(value={"/register"}) 
    public String postRegister(LoginForm form, RedirectAttributes redirectAttributes) {
        LoginForm loginform = loginService.select(form.getUsername()); 

    	if(loginform.getLoginid() != 0){
            // ユーザーが既に存在する場合の処理
    		form.getMsg().put("exists", "入力されたログインIDは存在します。\n登録できません。");
        	redirectAttributes.addFlashAttribute("LoginForm", form);  
            return "redirect:/register"; 
        } else {
            if (loginService.insert(form)) {
            	// 新規のアカウントが登録された場合　
            	form.getMsg().put("registerd", "新規アカウントが登録されました。\n先ほど登録したパスワードをもう一度入力してください。");
            	redirectAttributes.addFlashAttribute("LoginForm", form);    	
                return "redirect:/login";             	
            } else {
            	// 新規のアカウントの登録に失敗した場合
        		form.getMsg().put("failed", "登録に失敗しました。");
            	redirectAttributes.addFlashAttribute("LoginForm", form);    	
                return "redirect:/register";             	            	
            }        	
        } 
    }        
}