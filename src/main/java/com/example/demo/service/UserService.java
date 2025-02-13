package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserForm;

@Service
public class UserService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserForm clear(UserForm form) {
		form.setCode("");
		form.setKana("");
		form.setName("");
		
		return form;
	}
	
	public List<Map<String, Object>> select(UserForm form) {
		String query = 
		"SELECT"+
		" a.code"+
		",a.name"+
		",a.kana"+
		",b.name statename"+
		",c.name statusname"+
		",a.divisionname"+
		",a.remarks"+
		" FROM members a"+
		" LEFT JOIN name b"+
		" ON a.statecode=b.code AND b.category=2"+
		" LEFT JOIN name c"+
		" ON a.statuscode=c.code AND c.category=1"+
		" WHERE a.name IS NOT null";
		
		if (form.getName() != null && form.getName() != "") {
			query += " AND a.name LIKE '" + form.getName() + "%'";
		}
		if (form.getKana() != null && form.getKana() != "") {
			query += " AND a.kana LIKE '" + form.getKana() + "%'";
		}
		
		if (form.getStateKeys().size() != 0) {
			//状態　通常　休職　退職
			query += " AND (";
			for (int i = 0; i < form.getStateKeys().size(); i++) {
				if (i == 0) {
					query += " a.statecode = '" + form.getStateKeys().get(i) + "'";
				}else{
					query += " OR a.statecode = '" + form.getStateKeys().get(i) + "'";				
				}
			}
			query += ")";
		}

		if (form.getStatusKey() != 0) {
			query += " AND a.statuscode = '" + form.getStatusKey() + "'";			
		}
		
		return jdbcTemplate.queryForList(query);
	}
	
	public UserForm select(String code) {
		String query = "SELECT a.* FROM members a" +
				       " WHERE code = '" + code + "'";
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		UserForm form = new UserForm();
		
		if (tmp.size() != 0) {
			form.setCode((String) tmp.get(0).get("code")); 
			form.setName((String) tmp.get(0).get("name")); 
			form.setKana((String) tmp.get(0).get("kana")); 			
			form.setStateCode((Integer) tmp.get(0).get("stateCode")); 			
			form.setStatusCode((Integer) tmp.get(0).get("statusCode")); 			
			form.setDivisionname((String) tmp.get(0).get("divisionname")); 			
		}
	
		return form;		
	}
	
	public Boolean insert(UserForm form) {
		String query = "INSERT INTO members (code, name, kana, statecode, statuscode, divisionname) VALUES ("
		+ "'" + form.getCode() + "',"
		+ "'" + form.getName() + "',"
		+ "'" + form.getKana() + "',"
        + form.getStateCode().toString() + ","
        + form.getStatusCode().toString() + ","
        + "'" + form.getDivisionname() + "')";
		jdbcTemplate.execute(query);
		return true;
	}

	public Boolean update(UserForm form) {
		String query = "UPDATE members SET"
		+ " name = '" + form.getName() + "',"
		+ " kana = '" + form.getKana() + "',"
        + " stateCode = " + form.getStateCode().toString() + ","
        + " statusCode = " + form.getStatusCode().toString() + ","
        + " divisionname = '" + form.getDivisionname() + "'"
        + " WHERE code = '"  +  form.getCode() + "'";
		jdbcTemplate.execute(query);
		return true;
	}
	
	public Boolean delete(UserForm form) {
		String query = "DELETE FROM members"
        + " WHERE code = '"  +  form.getCode() + "'";
		jdbcTemplate.execute(query);
		return true;
	}
	
	/*
	public List<Map<String, Object>> items(String table) {		
		String query = MessageFormat.format("SELECT ROW_NUMBER() OVER() no, {0} FROM (SELECT DISTINCT {0} FROM usermst) ORDER BY {0}", table);	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		// リストの先頭にマップ配列を挿入する
		tmp.add(0, new HashMap<String, Object>(){{put("no", "0");put(table, "");}});
		return tmp;	
	}	

	public List<Map<String, Object>> items_no_blank(String table) {		
		String query = MessageFormat.format("SELECT ROW_NUMBER() OVER() no, {0} FROM (SELECT DISTINCT {0} FROM usermst) ORDER BY {0}", table);	
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		return tmp;	
	}	
	*/
	
}