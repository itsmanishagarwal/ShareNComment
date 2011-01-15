package gp;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
		
public class Photograps {    
	@PrimaryKey    
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)    
	private Long id;    
	@Persistent    
	private User uploader;    
	@Persistent    
	private Blob bphoto;
	@Persistent    
	private Blob sphoto;	
	@Persistent    
	private Date date;    

	
	public Photograps(User uploader, Blob bphoto, Blob sphoto, Date date) {
		this.uploader = uploader;        
		this.bphoto = bphoto;
		this.sphoto = sphoto; 
		this.date = date;    
		}    
	public Long getId() { 
      return id;    
      }    
	public User getUploader() {  
		return uploader;   
		}    
	public Blob getBphoto() { 
		return bphoto;   
		}    
	public Blob getSphoto() { 
		return sphoto;   
		}  
	public Date getDate() { 
		return date;    
		}    
	public void setUploader(User uploader) {   
		this.uploader = uploader;    
		}    
	public void setBphoto(Blob photo) {  
		this.bphoto = photo;   
		}   
	public void setSphoto(Blob photo) {  
		this.sphoto = photo;   
		}	
	public void setDate(Date date) {  
		this.date = date;   
		}
}
