package com.example.demo.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class UserController {
    @Autowired
    UserService userService;
        
    @Autowired
    LoginService loginService;
    
    @Autowired
    NameService nameService;

    @GetMapping(value={"/","/menu"})
    public String getHome(Model model, HttpSession session) {
        model.addAttribute("title", "メニュー");
        // セッションの初期化は行わない
        // セッションにログインユーザーを保存
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("user", loginService.select(auth.getName()));        
        return "menu";
    }
    
    @GetMapping(value={"/list"})
    public String getList(@ModelAttribute("UserForm")UserForm form, Model model, HttpSession session) {
    	model.addAttribute("title", "一覧");    	
        if (session.getAttribute("user") == null || form.getStateKeys().size()==0) {
            form.getStateKeys().add(1);
            form.setStatusKey(1);
        }
        // セッションの初期化は行わない
        // セッションにログインユーザーを保存
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("user", loginService.select(auth.getName()));
        
        // 職員データの取得
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("users", userService.select(form));
        model.addAttribute("data", data);
        // プルダウン項目の取得
        form.getItems().put("state", nameService.getState());
        form.getItems().put("status", nameService.getStatus());
        return "list";
    }
    
    @GetMapping("/insert")
    public String getInsert(@ModelAttribute("UserForm") UserForm form, @ModelAttribute("map") HashMap<String,String> map, Model model, HttpSession session) {
        model.addAttribute("title", "新規");
    	if (map.containsKey("msg")) {
            //更新：@PostMapping("/update_write")から遷移した場合
        	//フラッシュスコープのformを使用する
        }else {
        	//一覧から遷移した場合
        	UserForm formInsert = new UserForm();//userService.select(form.getCode());
            // プルダウン項目の取得
            formInsert.getItems().put("state", nameService.getState());
            formInsert.getItems().put("status", nameService.getStatus());        	
        	model.addAttribute("UserForm", formInsert);
        }
        model.addAttribute("user", session.getAttribute("user"));
        return "edit";
    } 

    @GetMapping("/update")
    public String getUpdate(@ModelAttribute("UserForm") UserForm form, @ModelAttribute("map") HashMap<String,String> map, Model model, HttpSession session) {
        model.addAttribute("title", "更新");
    	if (map.containsKey("msg")) {
            //更新：@PostMapping("/update_write")から遷移した場合
        	//フラッシュスコープのformを使用する
        }else {
        	//一覧から遷移した場合
        	UserForm formUpdate = userService.select(form.getCode());
        	formUpdate.setStateKeys(form.getStateKeys());
        	formUpdate.setStatusKey(form.getStatusKey());
        	// プルダウン項目の取得
            formUpdate.getItems().put("state", nameService.getState());
            formUpdate.getItems().put("status", nameService.getStatus());        	
        	model.addAttribute("UserForm", formUpdate);
        }
        model.addAttribute("user", session.getAttribute("user"));
        return "edit";
    } 

    @GetMapping("/delete")
    public String getDelete(@ModelAttribute("UserForm") UserForm form, @ModelAttribute("map") HashMap<String,String> map, Model model, HttpSession session) {
        model.addAttribute("title", "削除");
    	if (map.containsKey("msg")) {
            //更新：@PostMapping("/update_write")から遷移した場合
        	//フラッシュスコープのformを使用する
        }else {
        	//一覧から遷移した場合
        	UserForm formDelete = userService.select(form.getCode());
        	formDelete.setStateKeys(form.getStateKeys());
        	formDelete.setStatusKey(form.getStatusKey());
            // プルダウン項目の取得
            formDelete.getItems().put("state", nameService.getState());
            formDelete.getItems().put("status", nameService.getStatus());        	
        	model.addAttribute("UserForm", formDelete);
        }
        model.addAttribute("user", session.getAttribute("user"));
        return "edit";
    }
    
    @PostMapping(value={"/list", "/search"})
    public String postList(UserForm form, RedirectAttributes redirectAttributes, HttpSession session) {
    	redirectAttributes.addFlashAttribute("UserForm", form); 
        return "redirect:/list";
    }
    
    @PostMapping(value= {"/name"})
    public String postName(UserForm form, Model model,HttpSession session) {
        model.addAttribute("title", "一覧");
        if (session.getAttribute("user") == null) {
            form.getStateKeys().add(1);
        }
        model.addAttribute("UserForm", form);
    	//redirectAttributes.addFlashAttribute("UserForm", form);    	
        // セッションにログインユーザーを保存
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("user", auth.getName());
        //session.setAttribute("user",loginService.select(auth.getName()));
        
        // 職員データの取得
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("users", userService.select(form));
        model.addAttribute("data", data);
        // プルダウン項目の取得
        Map<String, Map<Integer, String>> items = new HashMap<>();
        items.put("state", nameService.getState()); 
        items.put("status", nameService.getStatus());
        model.addAttribute("items", items);
        
        return "list";
    }
    
    @PostMapping("/insert")
    public String postInsert(UserForm form, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("UserForm", form); 
        return "redirect:/insert";
    } 

    @PostMapping("/update")
    public String postUpdate(UserForm form, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("UserForm", form); 
    	return "redirect:/update";
    } 

    @PostMapping("/delete")
    public String postDelete(UserForm form,  RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("UserForm", form); 
        return "redirect:/delete";
    } 

    //新規
    @PostMapping("/insert_write")
    public String postinsert_write(UserForm form,  Model model, RedirectAttributes redirectAttributes) {
    	// validation
    	HashMap<String, String> map = new HashMap<>();
    	if (form.getName()=="") {
        	redirectAttributes.addFlashAttribute("UserForm", form);
        	map.put("msg", "氏名が未入力です。");        	
        	redirectAttributes.addFlashAttribute("map", map);    	
        	return "redirect:/insert";    		
    	}    	
    	userService.insert(form);	
    	redirectAttributes.addFlashAttribute("UserForm", form);
        return "redirect:/list";
    }
    
    //更新
    @PostMapping("/update_write")
    public String postupdate_write(UserForm form, Model model, RedirectAttributes redirectAttributes) {
    	// validation
    	HashMap<String, String> map = new HashMap<>();
    	if (form.getName()=="") {
        	redirectAttributes.addFlashAttribute("UserForm", form);
        	map.put("msg", "氏名が未入力です。");        	
        	redirectAttributes.addFlashAttribute("map", map);    	
        	return "redirect:/update";    		
    	}    	
    	userService.update(form);	
    	redirectAttributes.addFlashAttribute("UserForm", form);
    	return "redirect:/list";
    }
 
    //削除
    @PostMapping("/delete_write")
    public String postdelete_write(UserForm form, Model model, RedirectAttributes redirectAttributes) {
    	//引数のformに検索条件は無い。
    	userService.delete(form);	
    	redirectAttributes.addFlashAttribute("UserForm", form);
    	return "redirect:/list";
    }
}