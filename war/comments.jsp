<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page	import="java.util.List"%>
<%@ page	import="java.io.BufferedInputStream"%>
<%@ page	import="com.google.appengine.api.datastore.Blob"%>
<%@ page 	import="javax.jdo.PersistenceManager"%>
<%@ page 	import="javax.jdo.Query"%>
<%@ page	import="com.google.appengine.api.users.User"%>
<%@ page	import="com.google.appengine.api.users.UserService"%>
<%@ page	import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page	import="gp.*"%>

	<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
	
<body>
<a href="/AllPhotos.jsp">Home</a>
<hr>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if(user==null){
		response.sendRedirect("/AllPhotos.jsp");
	}
	Long id = Long.parseLong(request.getParameter("id"));
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Photograps photo = pm.getObjectById(Photograps.class, id);

%>
	<div align="center">
	<img  align="middle" src="image?id=<%=id%>&imagetype=big">
	</div><hr>
<%
	Query query = pm.newQuery(Comments.class);
	query.setFilter("photoid == id");
	query.setOrdering("date asc");
	query.declareParameters("Long id");
	List<Comments> comments = (List<Comments>) query.execute(id);
	if (comments.isEmpty()) {
	%><p>No comments. Be the first person to comment.</p>
	<%
	} else {
	       for (Comments cm : comments) {
		if (cm.getAuthor() == null) {
	%>Anonymous wrote:
	<%
		} else {
	%><b><%=cm.getAuthor().getNickname()%></b> wrote:
	<%
		}
	%>	
	<blockquote><%=cm.getContent()%></blockquote>
<form action="/delete" method="post">
<div><input type="hidden" name="id" value="<%=cm.getId()%>" /></div>
<div><input type="hidden" name="photoid" value="<%=id%>" /></div>
<% 
if((user!=null && cm.getAuthor()!=null && (cm.getAuthor().equals(user) )) || (user!=null && userService.isUserAdmin())){	
%>
<div align="right">
<input type="submit" value="Delete" />
<input type="hidden" name="source" value="comments" />
</div>
<%} %>
</form>
	<hr>
	<%
	       }
	}
	pm.close();
%>
<form action="/comment" method="post"  >
<input type="hidden" name="photoid" value="<%=id%>"/>
<div><textarea name="content" rows="3" cols="60"></textarea></div>
<div><input type="submit" value="Submit" /></div>
</form>
</body>
</html>