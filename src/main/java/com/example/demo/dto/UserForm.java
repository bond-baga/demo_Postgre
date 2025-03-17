package com.example.demo.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UserForm implements Serializable{
	private String code;
	private String name;
	private String kana;
	private String password;
	private Integer stateCode;
	private Integer statusCode;
	private String divisionname;

	private String searchname;
	private String searchkana;
	// 検索で複数の状態を選択できるようにする
	private List<Integer> stateKeys;
	// 検索で一つの雇用形態を選択できるようにする
	private Integer statusKey;
	
	// 検索で状態と雇用形態の選択肢を保持する
	private Map<String, Map<Integer, String>> mapitems;
	private String mapstring;	
	private Map<String, Object> msg;
	
	public UserForm() {
		code = "";
		name = "";
		kana= "";
		password = "";
		stateCode = 0;
		statusCode = 0;
		divisionname = "";
		
		searchname = "";
		searchkana= "";
		stateKeys = new ArrayList<Integer>();
		statusKey = 0;
		mapitems = new HashMap<String, Map<Integer, String>>();
		mapstring = "";
		msg = new HashMap<String, Object>();
	}
}
