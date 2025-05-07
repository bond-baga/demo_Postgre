package com.example.demo.service;

import java.util.ArrayList;
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
	
	// 状態（複数）
	private Map<Integer, String> state;
	private List<Integer> stateKeys;

	// 雇用形態
	private Map<Integer, String> status;
	private Integer statusKey = 0;
	
	// 状態（複数）
	public Map<Integer, String> getState() {		
		state = new HashMap<>();	
		String query = "SELECT code, name FROM name WHERE category=2 ORDER BY code";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach((x)->{state.put((Integer) x.get("code"), (String) x.get("name"));});
		return state;
	}	
	
	public List<Integer> getStateKeys() {
		stateKeys = new ArrayList<Integer>();
		String query = "SELECT code, name FROM name WHERE category=2 ORDER BY code";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach((x)->{if (stateKeys.isEmpty()) {stateKeys.add((Integer) x.get("code"));}});
		return stateKeys;
	}

	// 雇用形態
	public Map<Integer, String> getStatus() {		
		status = new HashMap<>();	
		String query = "SELECT code, name FROM name WHERE category=1 ORDER BY code";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach((x)->{status.put((Integer) x.get("code"), (String) x.get("name"));});
		return status;
	}	

	public Integer getStatusKey() {
		String query = "SELECT code, name FROM name WHERE category=1 ORDER BY code";	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		tmp.forEach((x)->{if (statusKey == 0) {statusKey = (Integer) x.get("code");}});
		return statusKey;
	}
	
}