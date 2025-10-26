package com.example.demo.controller;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.example.demo.service.NameService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;
        
//    @Autowired
//    LoginService loginService;
    
    @Autowired
    NameService nameService;

	@Autowired
    ObjectMapper mapper;
	
	@SuppressWarnings("unchecked")
	private void setMapItems(UserForm form) {
    	if (form.getMapstring().isEmpty()) {
        	form.getMapitems().put("state", nameService.getState());
        	form.getMapitems().put("status", nameService.getStatus());
        	try {
        		form.setMapstring(mapper.writeValueAsString(form.getMapitems()));
        	} catch (Exception e) {
        		e.printStackTrace();
        	}        
        }else{
        	try {
        		form.setMapitems((Map<String, Map<Integer, String>>) mapper.readValue(form.getMapstring(), Map.class));
        	} catch  (Exception e) {
        		e.printStackTrace();        		
        	}
        }		
	}
	
	@SuppressWarnings("unchecked")
	private void setSearchKey(UserForm form, HttpSession session) {
		if (form.getStatekeys().isEmpty()) {
			form.setStatekeys((List<Integer>) session.getAttribute("StateKeys"));
		}
		if (form.getStatusKey() == 0) {
			form.setStatusKey((Integer) session.getAttribute("StatusKey"));
		}
	}
/*	
	@GetMapping(value={"/","/menu"})
    public String getHome(Model model, HttpSession session) {
        model.addAttribute("title", "メニュー");
        // セッションの初期化は行わない
        // セッションにログインユーザーを保存
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("user", loginService.select(auth.getName()));        
        return "menu";
    }
*/    
	@GetMapping(value={"/list"})
    public String getList(@ModelAttribute("UserForm") UserForm form, Model model, HttpSession session) {
    	model.addAttribute("title", "一覧"); 
        setMapItems(form);
        setSearchKey(form, session);
        // 職員データの取得
        List<Map<String, Object>> data = new ArrayList<>();
        Integer count = userService.count(form);
        form.setCount(count);
        form.setMaxpage((int) (Math.floor((count - 1) / 10) + 1));
        if (form.getPage() == 0) form.setPage(1);
        data = userService.select(form);
        model.addAttribute("data", data);
        model.addAttribute("UserForm", form);
        return "list";
    }
    
	@GetMapping("/insert")
    public String getInsert(@ModelAttribute("UserForm") UserForm form, Model model) {
        model.addAttribute("title", "新規");
        setMapItems(form);        
        model.addAttribute("UserForm", form);
        return "edit";
    } 

	@GetMapping("/update")
    public String getUpdate(@ModelAttribute("UserForm") UserForm form, Model model) {
        model.addAttribute("title", "更新");
        setMapItems(form);
        model.addAttribute("UserForm", form);
        return "edit";
    } 
	
	@GetMapping("/delete")
    public String getDelete(@ModelAttribute("UserForm") UserForm form, Model model) {
        model.addAttribute("title", "削除");
        setMapItems(form);
        model.addAttribute("UserForm", form);
        return "edit";
    }
    
    @PostMapping(value={"/list"})
    public String postList(UserForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
    	// 状態と雇用形態の検索項目にはそれぞれのコードの先頭番号を指定する。
    	// 状態（複数）
    	form = new UserForm();
    	form.setStatekeys(nameService.getStateKeys());
    	// 雇用形態
    	form.setStatusKey(nameService.getStatusKey());
    	// メニューから遷移した場合の初期ページ
		session.setAttribute("StateKeys", form.getStatekeys()); 
		session.setAttribute("StatusKey", form.getStatusKey()); 
		redirectAttributes.addFlashAttribute("UserForm", form); 
        return "redirect:/list";
    }
    
    @PostMapping(value={"/search"})
    public String postSearch(UserForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
    	form.setPage(1);
    	redirectAttributes.addFlashAttribute("UserForm", form); 
    	// 検索の場合の初期ページ
		session.setAttribute("StateKeys", form.getStatekeys()); 
		session.setAttribute("StatusKey", form.getStatusKey()); 
        return "redirect:/list";
    }
    
    @PostMapping(value={"/page"})
    public String postPage(UserForm form, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
    	redirectAttributes.addFlashAttribute("UserForm", form); 
		session.setAttribute("StateKeys", form.getStatekeys()); 
		session.setAttribute("StatusKey", form.getStatusKey()); 
        return "redirect:/list";
    }

    @PostMapping("/insert")
    public String postInsert(UserForm form, BindingResult result, RedirectAttributes redirectAttributes) {  
        //form = userService.select(form.getCode());
    	redirectAttributes.addFlashAttribute("UserForm", userService.select(form.getCode())); 
        return "redirect:/insert";
    } 

    @PostMapping("/update")
    public String postUpdate(UserForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        //form = userService.select(form.getCode());
    	redirectAttributes.addFlashAttribute("UserForm", userService.select(form.getCode())); 
    	return "redirect:/update";
    } 

    @PostMapping("/delete")
    public String postDelete(UserForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        //form = userService.select(form.getCode());
    	redirectAttributes.addFlashAttribute("UserForm", userService.select(form.getCode())); 
        return "redirect:/delete";
    } 

    //新規
    @PostMapping("/insert_write")
    public String postinsert_write(UserForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("UserForm", form);
    	if (form.getCode().isEmpty()) {
        	form.getMsg().put("code", "コードが未入力です。");  
    	}
    	if (form.getName().isEmpty()) {
        	form.getMsg().put("name", "氏名が未入力です。");  
    	}
    	if (form.getKana().isEmpty()) {
        	form.getMsg().put("kana", "カナが未入力です。");  
    	}
    	if (!form.getImgfile().isEmpty() & form.getImgfile().getSize() > 1 * 1024 * 1024) {
        	form.getMsg().put("file", "ファイルサイズが１MBを超えています。");      		
    	}
    	if (!form.getMsg().isEmpty()) {
        	return "redirect:/insert";    		
    	}  
    	
    	try {
    		userService.insert(form);
    	} catch(Exception e) {
    		form.getMsg().put("error", "登録に失敗しました。\nエラー："+e.toString());
        	redirectAttributes.addFlashAttribute("UserForm", form);
            return "redirect:/insert";    		
    	}
        return "redirect:/list";
    }
    
    //更新
    @PostMapping("/update_write")
    public String postupdate_write(UserForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    	// 変更後の一覧で変更データを表示するために変更後のデータの状態と雇用形態を検索条件に設定する。
    	form.setStatekeys(new ArrayList<Integer>(Arrays.asList(form.getStatecode())));
    	form.setStatusKey(form.getStatuscode());
    	redirectAttributes.addFlashAttribute("UserForm", form);
    	if (form.getName().isEmpty()) {
        	form.getMsg().put("name", "氏名が未入力です。");  
    	}
    	if (form.getKana().isEmpty()) {
        	form.getMsg().put("kana", "カナが未入力です。");  
    	}
    	/*
    	if (!form.getImgfile().isEmpty() & form.getImgfile().getSize() > 1 * 1024 * 1024) {
        	form.getMsg().put("file", "ファイルサイズが１MBを超えています。");      		
    	}
    	*/
		if (!form.getMsg().isEmpty()) {
        	return "redirect:/update";    		
    	}    

		try {
    		userService.update(form);
    	} catch(Exception e) {
    		form.getMsg().put("error", "登録に失敗しました。\nエラー："+e.toString());
            return "redirect:/update";    		
    	}
    	form.setSearchname(form.getName());
    	form.setSearchkana(form.getKana());
    	return "redirect:/list";
    }
 
    //削除
    @PostMapping("/delete_write")
    public String postdelete_write(UserForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("UserForm", form);
    	try {
    		userService.delete(form);
    	} catch(Exception e) {
    		form.getMsg().put("error", "削除に失敗しました。\nエラー："+e.toString());
            return "redirect:/delete";    		
    	}
    	return "redirect:/list";
    }
}