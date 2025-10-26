package com.example.demo.dto;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Image implements Serializable{		
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String code;
	private byte[] image;
	private String contenttype;
	
	public Image() {
		code = "";
		image = null;
		contenttype = "";
	}
}
