package com.example.demo.dto;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class LoginForm implements Serializable{
	private int id;
	
	private String username;
	private String password;
	private String fullname;
	private String email;
	private Map<String, Object> msg;
	
	public LoginForm() {
		id = 0;
		username = "";
		password = "";
		fullname = "";
		email = "";
		msg = new HashMap<String, Object>();
	}
}
