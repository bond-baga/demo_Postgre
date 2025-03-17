package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NameService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<Integer, String> getStatus() {		
		Map<Integer, String> items = new HashMap<>();	
		String query = "SELECT code, name FROM name WHERE category=1";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach(x->items.put((Integer) x.get("code"), (String) x.get("name")));
		return items;
	}	
	
	public Map<Integer, String> getState() {		
		Map<Integer, String> items = new HashMap<>();	
		String query = "SELECT code, name FROM name WHERE category=2";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach(x->items.put((Integer) x.get("code"), (String) x.get("name")));
		return items;
	}	
}