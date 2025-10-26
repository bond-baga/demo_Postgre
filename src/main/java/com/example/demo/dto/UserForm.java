package com.example.demo.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserForm implements Serializable{		
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String code;
	private String name;
	private String kana;
	private String password;
	private Integer statecode;
	private Integer statuscode;
	private String divisionname;
	// 写真
	private MultipartFile imgfile;
	private String imgString;
	
	private String searchname;
	private String searchkana;
	// 状態（複数）
	private List<Integer> statekeys;

	// 雇用形態
	private Integer statusKey;
	
	// 検索で状態と雇用形態の選択肢を保持する
	private Map<String, Map<Integer, String>> mapitems;
	private String mapstring;	
	private Map<String, Object> msg;
	
	// 検索結果
	private Integer count;
	private Integer page;
	private Integer maxpage;
	
	public UserForm() {
		code = "";
		name = "";
		kana= "";
		password = "";
		statecode = 0;
		statuscode = 0;
		divisionname = "";
		imgfile = null;
		imgString = "";
		
		searchname = "";
		searchkana= "";
		statekeys = new ArrayList<Integer>();
		statusKey = 0;
		mapitems = new HashMap<String, Map<Integer, String>>();
		mapstring = "";
		msg = new HashMap<String, Object>();
		
		count = 0;
		page = 0;
		maxpage = 0;
	}
}
