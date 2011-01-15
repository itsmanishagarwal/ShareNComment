package gp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import gp.PMF;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class ShowImage extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(ShowImage.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Long id = Long.parseLong(req.getParameter("id"));
		String type = req.getParameter("imagetype");
		log.info("showing image type "+type);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Photograps photo = pm.getObjectById(Photograps.class, id);
			log.info("about to show image");
			Blob imageData=null;
			if(type.equals("big")){
				log.info("its big image");
				imageData = photo.getBphoto();
			}
			else{
				log.info("its small image");
				imageData = photo.getSphoto();
			}
			
			log.info("length is greater than 0 - "+imageData.getBytes().length);
		    resp.setContentType("image/jpeg");
		      int len = imageData.getBytes().length;
//		      for(int i=0;i<len;i++){
//		    	  log.warning("count "+i);
//		    	  byte bt = imageData.getBytes()[i];
		    	  resp.getOutputStream().write(imageData.getBytes());
//		      }		
		      
		      log.info("image added to stream");
		      resp.getOutputStream().flush();
		      log.info("image added to stream 1");
		      resp.getOutputStream().close();
		      log.info("image added to stream 2");
		    } catch (Exception ex) {
		      throw new ServletException(ex);
		    }
		    finally {
				pm.close();
			}
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException,ServletException {
		doPost(req,resp);
	}
}