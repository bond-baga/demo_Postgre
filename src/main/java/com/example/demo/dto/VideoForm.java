package com.example.demo.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

//import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class VideoForm implements Serializable{
	private int loginid;
	private String fullname;
	private List<MultipartFile> videofiles;
	private Map<String, Object> msg;
	private List<Map<String, Object>> items;
	
	// 検索結果
	private Integer count;
	private Integer page;
	private Integer maxpage;
	
	// 表示順   id + 表示名称
	private Map<Integer, String> disporder;
	private Integer dispindex;
	
	// 投稿者   loginid + filename
	private Map<Integer, String> contributors;
	private Integer contributor;
	
	public VideoForm() {
		loginid = 0;
		fullname = "";
		videofiles = null;
		msg = new HashMap<String, Object>();

		items = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> mp = new HashMap<String, Object>();
		items.add(mp);
		
		count = 0;
		page = 0;
		maxpage = 0;
		// 表示順
		dispindex = 2;
		disporder = new HashMap<Integer, String>();
		disporder.put(1, "更新日時 古い順");
		disporder.put(2, "更新日時 新しい順");	
		disporder.put(3, "タイトル順");	
		// 投稿者
		contributor = 0;
		contributors = new HashMap<Integer, String>();
	} 
}
