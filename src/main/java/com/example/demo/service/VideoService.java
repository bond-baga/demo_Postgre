package com.example.demo.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.VideoForm;

import jakarta.servlet.ServletContext;


@Service
public class VideoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	ServletContext context;	
	
	public void select(VideoForm form) {
		String query = 
		"SELECT"+
		" ROW_NUMBER() OVER(ORDER BY a.loginid ASC, lastupdate DESC) no"+ 
		",a.loginid"+
		",a.username"+
		",a.fullname"+
		",b.filename"+
		",b.contenttype"+
		",b.video"+
		",b.comment"+
		",b.lastupdate"+
		" FROM login a"+
		" INNER JOIN video b"+
		" ON a.loginid=b.loginid";
		if (form.getLoginid() != 0) {
			// ログインした場合
			query += " WHERE a.loginid = " + form.getLoginid();
		}else {
			// ゲストの場合
			query += " WHERE a.loginid = " + form.getContributor();		
		}
		// 
		query = "SELECT no, loginid, filename, username, fullname, contenttype, video, comment" +
		" FROM (" + query +") WHERE no BETWEEN " + Integer.toString((form.getPage()-1) * 6 + 1) + " AND " + Integer.toString(form.getPage() * 6);

		switch (form.getDispindex()){
		default:
		case 1:
			query += " ORDER BY loginid ASC,lastupdate ASC";		
			break;
		case 2:
			query += " ORDER BY loginid ASC,lastupdate DESC";		
			break;
		case 3:
			query += " ORDER BY loginid ASC,comment ASC";		
			break;
		}

		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		for (int i=0; i<tmp.size(); i++) {
			if (tmp.get(i).get("filename") != null) {
				HashMap<String, Object> mp = new HashMap<String, Object>();
				if (form.getLoginid() != 0) {
					// ログインした場合
					
					Path p = Paths.get(String.format("%06d", form.getLoginid()), (String)tmp.get(i).get("filename"));
					mp.put("filename", p.toString());					
					form.setFullname((String)tmp.get(i).get("fullname"));
				}else {
					// ゲストの場合
					//mp.put("filename", Paths.get("videofiles",String.format("%06d", form.getContributor()), (String)tmp.get(i).get("filename")));
										
					Path p = Paths.get(String.format("%06d", form.getContributor()), (String)tmp.get(i).get("filename"));
					mp.put("filename", p.toString());					
					form.setFullname("guest");
				}
				mp.put("comment", (String)tmp.get(i).get("comment"));
				form.getItems().add(mp);
				// State
				form.addState();
			}
		}
		return;
	}
	
	public void contributors(VideoForm form) {
		String query = 
		"SELECT DISTINCT"+
		" a.loginid"+
		",a.username"+
		",a.fullname"+
		" FROM login a"+
		" INNER JOIN video b"+
		" ON a.loginid=b.loginid"+
		" ORDER BY a.loginid";
		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		for (int i=0; i<tmp.size(); i++) {
			if (i == 0 && form.getContributor() == 0) {
				form.setContributor((Integer)tmp.get(i).get("loginid"));
			}
			form.getContributors().put((Integer)tmp.get(i).get("loginid"), (String)tmp.get(i).get("fullname"));			
		}
		return;		
	}
	
	public Integer count(VideoForm form) {
		String query = 
		"SELECT count(*)"+
		" FROM login a"+
		" INNER JOIN video b"+
		" ON a.loginid=b.loginid";
		if (form.getLoginid() != 0) {
			// ログインした場合
			query += " WHERE a.loginid = " + form.getLoginid();
		}else {
			// ゲストの場合
			query += " WHERE a.loginid = " + form.getContributor();
		}		
		int tmp = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		return tmp;
	}
	
	public Boolean insert(VideoForm form) throws Exception{
		List<MultipartFile> files = form.getVideofiles();
		for (int i = 0; i < files.size(); i++) {
			String contenttype = files.get(i).getContentType();
			String comment = (String) form.getItems().get(i).get("comment");
			String query = "INSERT INTO video (loginid, filename, video, contenttype, comment, lastupdate) VALUES (?,?,?,?,?,?)";  
			jdbcTemplate.update(query, new Object[] {form.getLoginid(), files.get(i).getOriginalFilename(), null, contenttype, comment, LocalDate.now()});		
			
			// アップロードフォルダー
			Path filepath = Paths.get("videofiles",String.format("%06d", form.getLoginid()));			
			File uploadDir = new File(filepath.toString());					
			if (!uploadDir.exists()) {
				// フォルダ作成
				uploadDir.mkdirs();
			}	    
			// FileCopy
			Files.copy(files.get(i).getInputStream(), Paths.get(uploadDir +"/" +  files.get(i).getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
		}
		return true;
	}

	public Boolean update(VideoForm form) throws Exception{
		for (int i = 0; i < form.getItems().size(); i++) {
			String comment = (String) form.getItems().get(i).get("comment");
			String filename = (String) form.getItems().get(i).get("filename");
			// ファイルからファイル名を取得
			File file = new File(filename);			
			if (form.getStates().get(i).isDelete()) {
				String query = "DELETE FROM video WHERE (loginid = ? AND filename = ?)";  
				jdbcTemplate.update(query, new Object[] {form.getLoginid(), file.getName()});
				// FileDelete
				Files.delete(Paths.get("videofiles", form.getItems().get(i).get("filename").toString()));				
			} else if (form.getStates().get(i).isUpdate()) {
				String query = "UPDATE video SET comment = ?, lastupdate = ? WHERE (loginid = ? AND filename = ?)";  
				jdbcTemplate.update(query, new Object[] {comment, LocalDateTime.now(), form.getLoginid(), file.getName()});
			}		
		}
		return true;
	}

	
}