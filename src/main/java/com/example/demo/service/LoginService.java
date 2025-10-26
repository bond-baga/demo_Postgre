package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginForm;

@Service
public class LoginService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	  
    private final BCryptPasswordEncoder passwordEncoder;
    
    public LoginService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder; 	
    }
    
	public LoginForm clear(LoginForm form) {
		form.setUsername("");
		return form;
	}
	
	public LoginForm select(String username) {
		String query = "SELECT * FROM login WHERE username = '" + username + "'";
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		LoginForm form = new LoginForm();
		
		if (tmp.size() != 0) {
			form.setLoginid((int)tmp.get(0).get("loginid")); 
			form.setUsername((String) tmp.get(0).get("username")); 
			form.setPassword((String) tmp.get(0).get("password")); 
			form.setFullname((String) tmp.get(0).get("fullname")); 			
		}
		return form;		
	}
	
	public List<Map<String, Object>> contributors() {
		String query = "SELECT a.* FROM login a INNER JOIN video b ON a.loginid = b.loginid" ;
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		return tmp;		
	}
	
	
	public Boolean update(LoginForm form) {
		Boolean ret;
		String query = "UPDATE login SET"
		+ " username = '" + form.getUsername() + "',"
		+ " password = '" + form.getPassword() + "',"
		+ " fullname = '" + form.getFullname() + "',"
		+ " email = '" + form.getEmail() + "',"
        + " WHERE loginid = "  +  form.getLoginid();
		try {
			jdbcTemplate.execute(query);
			ret = true;	
		}catch(Exception ex) {
			ret = false;
		}
		return ret;
	}
	
	public Boolean insert(LoginForm form) {
		//form.setPassword(this.passwordEncoder.encode(form.getPassword()));
		Boolean ret;
		String query = "INSERT INTO login (username, password, fullname, email) VALUES ("
		+ "'" + form.getUsername() + "',"
		+ "'" + passwordEncoder.encode(form.getPassword()) + "',"
		+ "'" + form.getFullname() + "',"
		+ "'" + form.getEmail() + "')";
		try {
			jdbcTemplate.execute(query);
			ret = true;	
		}catch(Exception ex) {
			ret = false;
		}
		return ret;
	}	
}