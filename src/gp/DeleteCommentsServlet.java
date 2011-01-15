package gp;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import gp.PMF;

@SuppressWarnings("serial")
public class DeleteCommentsServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(DeleteCommentsServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String src = req.getParameter("source");
		String target="";
		Long id = Long.parseLong(req.getParameter("id"));		
		log.log(Level.WARNING, "got the id "+id);
		Date date = new Date();
		//Greeting greeting = new Greeting(user, content, date);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			if(src.equals("comments")){
				Long photoid = Long.parseLong(req.getParameter("photoid"));
				Comments comment = pm.getObjectById(Comments.class, id);
				pm.deletePersistent(comment);
				target ="/comments.jsp?id="+photoid;
				log.log(Level.WARNING, "comment deleted");
			}
			if(src.equals("photo") || src.equals("allphoto")){
				Photograps photo = pm.getObjectById(Photograps.class, id);
				pm.deletePersistent(photo);				
				Query query = pm.newQuery(gp.Comments.class);
				query.setFilter("photoid == phIdParam");
				query.declareParameters("Long phIdParam");
				query.deletePersistentAll(id);
				log.log(Level.WARNING, "in the photo.. id is "+id);
				if(src.equals("allphoto")){
					target ="/AllPhotos.jsp";
				}
				else
					target ="/Photos.jsp";
				log.log(Level.WARNING, "photo deleted");
			}
		} finally {
			pm.close();
		}
		resp.sendRedirect(target);
	}
}