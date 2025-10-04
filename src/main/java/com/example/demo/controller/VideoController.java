package com.example.demo.controller;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginForm;
import com.example.demo.dto.VideoForm;
import com.example.demo.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VideoController {
    @Autowired
    VideoService videoService;
    
    @GetMapping(value={"/video"})
    public String getList(@ModelAttribute("VideoForm")VideoForm form, Model model, HttpSession session) {
		form.setLoginid(((LoginForm)session.getAttribute("user")).getLoginid());
    	model.addAttribute("title", "動画"); 
        Integer count = videoService.count(form);
        form.setCount(count);
        form.setMaxpage((int) (Math.floor((count - 1) / 9) + 1));
        if (form.getPage() == 0) form.setPage(1);
        form.getItems().clear();
        form.getStates().clear();
        videoService.select(form);
        model.addAttribute("VideoForm", form);
        return "video";
    }
        
    @PostMapping(value={"/video"})
    public String postList(VideoForm form, BindingResult resul, RedirectAttributes redirectAttributes, HttpSession session) {
		redirectAttributes.addFlashAttribute("VideoForm", form); 
        return "redirect:/video";
    }
        
    @PostMapping(value={"/video_page"})
    public String postPage(VideoForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
    	redirectAttributes.addFlashAttribute("VideoForm", form); 
        return "redirect:/video";
    }
    
    
    //新規
    @PostMapping("/video_insert")
    public String postvideo_insert(VideoForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
    	redirectAttributes.addFlashAttribute("VideoForm", form);    	
    	try {
    		form.setLoginid(((LoginForm)session.getAttribute("user")).getLoginid());
    		videoService.insert(form);
    	} catch(SQLIntegrityConstraintViolationException e) {
    		form.getMsg().put("error", "同名のファイルが登録済みです。");
        	redirectAttributes.addFlashAttribute("VideoForm", form);
            return "redirect:/video";    		            
    	} catch(Exception e) {
    		form.getMsg().put("error", "登録に失敗しました。\nエラー："+e.toString());
        	redirectAttributes.addFlashAttribute("VideoForm", form);
            return "redirect:/video";    		
    	}
        return "redirect:/video";
    }
    
    //更新
    @PostMapping("/video_update")
    public String postvideo_update(VideoForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
    	redirectAttributes.addFlashAttribute("VideoForm", form);    	
    	try {
    		videoService.update(form);
    	} catch(Exception e) {
    		form.getMsg().put("error", "登録に失敗しました。\nエラー："+e.toString());
        	redirectAttributes.addFlashAttribute("VideoForm", form);
            return "redirect:/video";    		
    	}
        return "redirect:/video";
    }
}