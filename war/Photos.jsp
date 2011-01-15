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
	PersistenceManager pm = PMF.get().getPersistenceManager();
	String query = "select from " + Photograps.class.getName();
	List<Photograps> photos = (List<Photograps>) pm.newQuery(query)
	.execute();
	if (photos.isEmpty()) {
%><p>The guestbook has no messages.</p>
<%
	} else {
		for (Photograps ph : photos) {
%>			
<form action="/delete" method="post">
<div><input type="hidden" name="id" value="<%=ph.getId()%>" /></div>

<%			if (ph.getUploader() == null) {
%><p>An anonymous person uploaded this photo:</p>
<%
	} else {
%><p><b><%=ph.getUploader().getNickname()%></b> wrote:</p>
<%
	}
%>
<div align="center">
<img  align="middle" src="image?id=<%=ph.getId()%>&imagetype=big">
</div>
<div align="right" ><input type="submit" value="Delete" />
<input type="hidden" name="source" value="photo" />
</div>
</form>
<hr>
<%
	}
	}
	pm.close();
%>
<form action="/upload" method="post"  ENCTYPE="multipart/form-data" >
<div><input name="photo" type="file"></div>
<div><input type="submit" value="Upload" /></div>
</form>
</body>
</html>