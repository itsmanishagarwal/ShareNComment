package gp;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
		
public class Books {    
	@PrimaryKey    
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)    
	private Long id;    
	@Persistent   
	private String book;    
	@Persistent 	
	private String author;    
    
	public Books(String author,String book) {
		this.author = author;
		this.book = book;
		}    
	public Long getId() { 
      return id;    
      }
	public String getBook() { 
	      return book;    
	      } 	
	public String getAuthor() {  
		return author;   
		}    
	public void setBook(String book) { 
	      this.book= book;    
	      } 
	public void setAuthor(String author) {   
		this.author = author;    
		}    	
}
