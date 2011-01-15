package gp;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import gp.PMF;

public class AddCommentsServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(AddCommentsServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String content = req.getParameter("content");
		Date date = new Date();
		Long photoid = Long.parseLong(req.getParameter("photoid"));		
		Comments comments = new Comments(user,photoid,content,date);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(comments);
			
		} finally {
			pm.close();
		}
		resp.sendRedirect("/comments.jsp?id="+photoid);
	}
}