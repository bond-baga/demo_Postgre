 package com.example.demo.service;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlBinaryValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.UserForm;


@Service
public class UserService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	LobHandler lobHandler = new DefaultLobHandler(); 
	
	public UserForm clear(UserForm form) {
		form.setCode("");
		form.setKana("");
		form.setName("");
		
		return form;
	}
	
	public List<Map<String, Object>> select(UserForm form) {
		String query = 
		"SELECT"+
		" ROW_NUMBER() OVER(ORDER BY a.code) no"+ 
		",a.code"+
		",a.name"+
		",a.kana"+
		",b.name statename"+
		",c.name statusname"+
		",a.divisionname"+
		",a.remarks"+
		",d.contenttype"+
		",d.image"+
		" FROM members a"+
		" LEFT JOIN name b"+
		" ON a.statecode=b.code AND b.category=2"+
		" LEFT JOIN name c"+
		" ON a.statuscode=c.code AND c.category=1"+
		" LEFT JOIN image d"+
		" ON a.code=d.code"+
		" WHERE a.name IS NOT null";
		
		if (form.getSearchname() != null && form.getSearchname() != "") {
			query += " AND a.name LIKE '" + form.getSearchname() + "%'";
		}
		if (form.getSearchkana() != null && form.getSearchkana() != "") {
			query += " AND a.kana LIKE '" + form.getSearchkana() + "%'";
		}
		
		if (form.getStatekeys().size() != 0) {
			//状態　通常　休職　退職
			query += " AND (";
			for (int i = 0; i < form.getStatekeys().size(); i++) {
				if (i == 0) {
					query += " a.statecode = '" + form.getStatekeys().get(i) + "'";
				}else{
					query += " OR a.statecode = '" + form.getStatekeys().get(i) + "'";				
				}
			}
			query += ")";
		}

		if (form.getStatusKey() != 0) {
			query += " AND (a.statuscode = '" + form.getStatusKey() + "')";			
		}
		query += " ORDER BY code";		
		query = "SELECT no,code,name,kana,statename,statusname,divisionname,remarks,contenttype, image" +
		" FROM (" + query +") WHERE no BETWEEN " + Integer.toString((form.getPage()-1)*10+1) + " AND " + Integer.toString(form.getPage()*10);

		List<Map<String, Object>> tmp = jdbcTemplate.queryForList(query);
		for (int i=0; i<tmp.size(); i++) {
			if (tmp.get(i).get("image") != null) {
				StringBuffer encoded = new StringBuffer();
				encoded.append("data:"+ tmp.get(i).get("contenttype")+ ";base64,");
				encoded.append(Base64.getEncoder().encodeToString((byte[])tmp.get(i).get("image")));
				tmp.get(i).put("imgString",encoded.toString());
			}else {
				tmp.get(i).put("imgString",null);
			}
		}
		return tmp;
	}
	

	public Integer count(UserForm form) {
		String query = 
		"SELECT count(*)"+
		" FROM members a"+
		" LEFT JOIN name b"+
		" ON a.statecode=b.code AND b.category=2"+
		" LEFT JOIN name c"+
		" ON a.statuscode=c.code AND c.category=1"+
		" WHERE a.name IS NOT null";
		
		if (form.getSearchname() != null && form.getSearchname() != "") {
			query += " AND a.name LIKE '" + form.getSearchname() + "%'";
		}
		if (form.getSearchkana() != null && form.getSearchkana() != "") {
			query += " AND a.kana LIKE '" + form.getSearchkana() + "%'";
		}
		
		if (form.getStatekeys().size() != 0) {
			//状態　通常　休職　退職
			query += " AND (";
			for (int i = 0; i < form.getStatekeys().size(); i++) {
				if (i == 0) {
					query += " a.statecode = '" + form.getStatekeys().get(i) + "'";
				}else{
					query += " OR a.statecode = '" + form.getStatekeys().get(i) + "'";				
				}
			}
			query += ")";
		}

		if (form.getStatusKey() != 0) {
			query += " AND (a.statuscode = '" + form.getStatusKey() + "')";			
		}
		int tmp = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		return tmp;
	}

	public UserForm select(String code) {
		String query = "SELECT a.*,b.image,b.contenttype FROM members a" +
					   " LEFT JOIN image b" +
				       " ON a.code = b.code" + 
				       " WHERE a.code = '" + code + "'";
		Map<String, Object> tmp = null;
		try {
			tmp = jdbcTemplate.queryForMap(query);
		} catch(Exception e){
			
		}
		UserForm form = new UserForm();
		if (tmp != null) {
			form.setCode((String) tmp.get("code")); 
			form.setName((String) tmp.get("name")); 
			form.setKana((String) tmp.get("kana")); 			
			form.setStatecode((Integer) tmp.get("statecode")); 			
			form.setStatuscode((Integer) tmp.get("statuscode")); 			
			form.setDivisionname((String) tmp.get("divisionname")); 	
			if (tmp.get("image") != null) {
				StringBuffer encoded = new StringBuffer();
				encoded.append("data:"+ tmp.get("contenttype")+ ";base64,");
				encoded.append(Base64.getEncoder().encodeToString((byte[])tmp.get("image")));
				form.setImgString(encoded.toString());
			} else {
				form.setImgString("");
			}
		}
		return form;
	}
	
	public Boolean insert(UserForm form) throws Exception{
		String query = "INSERT INTO members (code, name, kana, statecode, statuscode, divisionname) VALUES ("
		+ "'" + form.getCode() + "',"
		+ "'" + form.getName() + "',"
		+ "'" + form.getKana() + "',"
        + form.getStatecode().toString() + ","
        + form.getStatuscode().toString() + ","
        + "'" + form.getDivisionname() + "')";
		jdbcTemplate.execute(query);
		MultipartFile multipartFile = form.getImgfile();
		// アップロードファイルをバイト値に変換
		if (!multipartFile.isEmpty()) {
        	query = "DELETE FROM image WHERE code = ?";        	
        	jdbcTemplate.update(query, new Object[] {form.getCode()});

        	byte[] bytes = multipartFile.getBytes();
        	String contenttype = multipartFile.getContentType();
        	SqlBinaryValue data = new SqlBinaryValue(bytes);
        	query = "INSERT INTO image (code, image, contentType) VALUES (?,?,?)";  
        	jdbcTemplate.update(query, new Object[] {form.getCode(), data, contenttype});
        }		
		return true;
	}

	public Boolean update(UserForm form) throws Exception{
    	String query = "UPDATE members SET"
		+ " name = '" + form.getName() + "',"
		+ " kana = '" + form.getKana() + "',"
        + " stateCode = " + form.getStatecode().toString() + ","
        + " statusCode = " + form.getStatuscode().toString() + ","
        + " divisionname = '" + form.getDivisionname() + "'"
        + " WHERE code = '"  +  form.getCode() + "'";
		jdbcTemplate.update(query); 
		MultipartFile multipartFile = form.getImgfile();
		if (!multipartFile.isEmpty()) {
        	query = "DELETE FROM image WHERE code = ?";        	
        	jdbcTemplate.update(query, new Object[] {form.getCode()});

        	byte[] bytes = filedata(multipartFile);
        	String contenttype = multipartFile.getContentType();
        	SqlBinaryValue data = new SqlBinaryValue(bytes);
        	query = "INSERT INTO image (code, image, contentType) VALUES (?,?,?)";  
        	jdbcTemplate.update(query, new Object[] {form.getCode(), data, contenttype});
        }else if (form.getImgString().equals("")) {
        	query = "DELETE FROM image WHERE code = ?";        	
        	jdbcTemplate.update(query, new Object[] {form.getCode()});        	
        }
		return true;
	}
	
	public Boolean delete(UserForm form) throws Exception{
		String query = "DELETE FROM members WHERE code = ?";
    	jdbcTemplate.update(query, new Object[] {form.getCode()});
    	query = "DELETE FROM image WHERE code = ?";        	
    	jdbcTemplate.update(query, new Object[] {form.getCode()});
    	query = "DELETE FROM image WHERE code = ?";        	
    	jdbcTemplate.update(query, new Object[] {form.getCode()});
		return true;
	}
	
	/*
	アップロードしたファイルをサーバーの指定ディレクトリに格納する
	*/
	public void fileupload(MultipartFile multipartFile) throws Exception{
		StringBuffer filePath = new StringBuffer("uploadfiles/");
		// アップロードファイルを格納するディレクトリを作成する
		File uploadDir = mkdirs(filePath);
	    int resizeW;
	    int resizeH;		
	    double rateW = 1;
	    double rateH = 1;
	    double rate = 1;
		BufferedImage originalImg = ImageIO.read(multipartFile.getInputStream());
		if (originalImg.getWidth() > 200) {
			rateW = (double)200/originalImg.getWidth();
		}
		if (originalImg.getHeight() > 300) {
			rateH = (double)300/originalImg.getHeight();
		}
		rate = rateW < rateH ? rateW: rateH;
		
		resizeW = (int)Math.floor((double)originalImg.getWidth() * rate);
		resizeH = (int)Math.floor((double)originalImg.getHeight() * rate);
		BufferedImage resizeImg = Scalr.resize(originalImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, resizeW, resizeH);
		String formatName="png";
		if (multipartFile.getContentType().equals("image/jpeg")) formatName="jpeg";
		ImageIO.write(resizeImg, formatName, new File(uploadDir.getPath() + "/" + multipartFile.getOriginalFilename()));
	}	
	
	public byte[] filedata(MultipartFile multipartFile) throws Exception{
	    int resizeW;
	    int resizeH;		
	    double rateW = 1;
	    double rateH = 1;
	    double rate = 1;
	     
		BufferedImage originalImg = ImageIO.read(multipartFile.getInputStream());
		if (originalImg.getWidth() > 200) {
			rateW = (double)200/originalImg.getWidth();
		}
		if (originalImg.getHeight() > 300) {
			rateH = (double)300/originalImg.getHeight();
		}
		rate = rateW < rateH ? rateW: rateH;
		
		resizeW = (int)Math.floor((double)originalImg.getWidth() * rate);
		resizeH = (int)Math.floor((double)originalImg.getHeight() * rate);
		BufferedImage resizeImg = Scalr.resize(originalImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, resizeW, resizeH);
		String formatName="png";
		if (multipartFile.getContentType().equals("image/jpeg")) formatName="jpeg";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizeImg, formatName, baos);
		return baos.toByteArray();
	}	

	/**
	 * アップロードファイルを格納するディレクトリを作成する
	 *
	 * @param filePath
	 * @return
	 */
	private File mkdirs(StringBuffer filePath){
		Date now = new Date();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		File uploadDir = new File(filePath.toString(), sdf.format(now));
		// 既に存在する場合はプレフィックスをつける
		/*
		int prefix = 0;
		while(uploadDir.exists()){
			prefix++;
			uploadDir = new File(filePath.toString() + sdf.format(now) + "-" + String.valueOf(prefix));
		}
		*/
		if (!uploadDir.exists()) {
			// フォルダ作成
			uploadDir.mkdirs();
		}
		return uploadDir;
	}	

	
}