package com.example.demo.dto;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Video implements Serializable{	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int loginid;
	private byte[] video;
	private String contenttype;
	
	public Video(){
		id = 0;
		loginid = 0;
		video = null;
		contenttype = "";
	}
}

