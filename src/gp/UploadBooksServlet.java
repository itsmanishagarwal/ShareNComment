package gp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import gp.PMF;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;



public class UploadBooksServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(UploadBooksServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
	    String target="/UploadBooks.jsp";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query query=null;
		try {
		      ServletFileUpload upload = new ServletFileUpload();		      
		      
		      FileItemIterator iterator = upload.getItemIterator(req);
		      while (iterator.hasNext()) {
		        FileItemStream item = iterator.next();
		        
		        if (item.isFormField()) {
		          log.info("Got a form field: " + item.getFieldName());	
		          if(item.getFieldName().equalsIgnoreCase("Delete All")){
		        	    query = pm.newQuery(Books.class); 
		        	    query.setFilter("book != bookName"); 
		        	    query.declareParameters("String bookName"); 
		        	    query.deletePersistentAll("Manish123");
		          }
		        } else if(item.getName()==null || item.getName().equals("")){
		        	 resp.sendRedirect(target);
		        }
		        else{
			        log.info("Got an uploaded file: " + item.getFieldName() +
			                      ", name = " + item.getName());		         
				    InputStream stream = (InputStream)item.openStream();			     
				    
				    BufferedReader in = new BufferedReader(new InputStreamReader(stream));
//				    InputStreamReader in = new InputStreamReader(stream);
//				    List list = IOUtils.readLines(stream);
					String line=in.readLine();
					String temp[]=null;
					log.info(" New Entry"+line);
				    while(line!=null){																			
						temp = line.split("#");
						Books book = new Books(temp[1],temp[0]);
						log.info(" New Book "+temp[0].toLowerCase());
						log.info(" Author "+temp[1]);
						pm.makePersistent(book);
						line=in.readLine();
					}		        
		        }
		      }
		    } catch (Exception ex) {
		      throw new ServletException(ex);
		    }
		    finally {
		    	query.closeAll();
				pm.close();
			}

		    resp.sendRedirect(target);

	}
}