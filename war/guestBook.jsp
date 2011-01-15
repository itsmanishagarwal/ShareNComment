<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page	import="java.util.List"%>
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
<a href="/Photos.jsp"> Upload Snaps!!</a>
<%
	}
%>
<%
	PersistenceManager pm = PMF.get().getPersistenceManager();
	String query = "select from " + Greeting.class.getName();
	List<Greeting> greetings = (List<Greeting>) pm.newQuery(query)
			.execute();
	if (greetings.isEmpty()) {
%><p>The guestbook has no messages.</p>
<%
	} else {
		for (Greeting g : greetings) {
%>			
<form action="/delete" method="post">
<div><input type="hidden" name="id" value="<%=g.getId()%>" /></div>

<%			if (g.getAuthor() == null) {
%><p>An anonymous person wrote:</p>
<%
	} else {
%><p><b><%=g.getAuthor().getNickname()%></b> wrote:</p>
<%
	}
%><blockquote><%=g.getContent()%></blockquote>
<div align="right" >
	<input type="submit" value="Delete" />
	<input type="hidden" name="source" value="comment" />
</div>
</form>
<hr>
<%
	}
	}
	pm.close();
%>
<form action="/sign" method="post">
<div><textarea name="content" rows="3" cols="60"></textarea></div>
<div><input type="submit" value="Post Comments" /></div>
</form>
</body>
</html>