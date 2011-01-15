<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page	import="java.util.List"%>
<%@ page	import="java.io.BufferedInputStream"%>
<%@ page	import="com.google.appengine.api.datastore.Blob"%>
<%@ page 	import="javax.jdo.PersistenceManager"%>
<%@ page	import="com.google.appengine.api.users.User"%>
<%@ page	import="com.google.appengine.api.users.UserService"%>
<%@ page	import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page	import="gp.*"%>

	<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
	
<body>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
%><p>Hello, <%=user.getNickname()%>! (You can<a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)</p>
<%
	} else {
%><p>Hello!<a
	href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
in</a> to include your name with greetings you post.</p>

<%
	}
%>
<%
	Long id = Long.parseLong(request.getParameter("id"));
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Photograps photo = pm.getObjectById(Photograps.class, id);

	Blob imageData = photo.getBphoto();
	response.setContentType("image/jpg");
	response.getOutputStream().write(imageData.getBytes());

	
%>
</body>
</html>