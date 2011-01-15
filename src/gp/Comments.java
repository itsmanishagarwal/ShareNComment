package gp;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
		
public class Comments {    
	@PrimaryKey    
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)    
	private Long id;    
	@Persistent   
	private Long photoid;    
	@Persistent 	
	private User author;    
	@Persistent    
	private String content;    
	@Persistent    
	private Date date;    
	public Comments(User author,Long photoid, String content, Date date) {
		this.author = author;
		this.photoid = photoid;
		this.content = content;        
		this.date = date;    
		}    
	public Long getId() { 
      return id;    
      }
	public Long getPhotoid() { 
	      return photoid;    
	      } 	
	public User getAuthor() {  
		return author;   
		}    
	public String getContent() { 
		return content;   
		}    
	public Date getDate() { 
		return date;    
		}    
	public void setPhotoid(Long photoid) { 
	      this.photoid=photoid;    
	      } 	
	public void setAuthor(User author) {   
		this.author = author;    
		}    
	public void setContent(String content) {  
		this.content = content;   
		}   
	public void setDate(Date date) {  
		this.date = date;   
		}
}
