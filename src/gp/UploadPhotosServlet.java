package gp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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



public class UploadPhotosServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(UploadPhotosServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
	    String target="/AllPhotos.jsp";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		      ServletFileUpload upload = new ServletFileUpload();		      
		      
		      FileItemIterator iterator = upload.getItemIterator(req);
		      while (iterator.hasNext()) {
		        FileItemStream item = iterator.next();
		        
		        if (item.isFormField()) {
		          log.info("Got a form field: " + item.getFieldName());		         
		        } else if(item.getName()==null || item.getName().equals("")){
		        	 resp.sendRedirect(target);
		        }
		        else{
		          log.info("Got an uploaded file: " + item.getFieldName() +
		                      ", name = " + item.getName());		         
			     InputStream stream = (InputStream)item.openStream();			     
			     
			    Blob bb = new Blob(IOUtils.toByteArray(stream));
			    log.info("size of the file to be uploaded "+bb.getBytes().length);
			    if(bb.getBytes().length>1000000){
			    	req.getSession().setAttribute("message","Please upload a photo of size less than 1 MB.");
			    	resp.sendRedirect(target);
			    }
			    ImagesService imagesService = ImagesServiceFactory.getImagesService();

			    
			    Image oldImage = ImagesServiceFactory.makeImage(bb.getBytes());
			    
			    Transform resize = ImagesServiceFactory.makeResize(800, 800);			
			    Image newImage = imagesService.applyTransform(resize, oldImage);
			    Blob bb1 = new Blob(newImage.getImageData());
			    
			    Transform sresize = ImagesServiceFactory.makeResize(200, 200);
			    Image smImage = imagesService.applyTransform(sresize, newImage);			   			   
			    Blob smbb = new Blob(smImage.getImageData());
			    
			    
			     Date date = new Date();
			     Photograps ptgp1 = new Photograps(user, bb1,smbb ,date);
			     pm.makePersistent(ptgp1);
		        }
		      }
		    } catch (Exception ex) {
		      throw new ServletException(ex);
		    }
		    finally {
				pm.close();
			}

		    resp.sendRedirect(target);

	}
}