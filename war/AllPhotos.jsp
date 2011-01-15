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
%>Hello, <%=user.getNickname()%>! <div align="right"><a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">Sign
out</a></div>
<%
	} else {
%><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
in</a> to upload images and post comments.
<%}
	if(session.getAttribute("message")!=null){
		%><%=session.getAttribute("message")%><%
	}
	PersistenceManager pm = PMF.get().getPersistenceManager();
	String query = "select from " + Photograps.class.getName();
	List<Photograps> photos = (List<Photograps>) pm.newQuery(query).execute();
	if (photos.isEmpty()) {
%><p>Oop!! No photos.</p>
<%
	} else {
		%>
		<table>
		<%
		int count=0;
		boolean flag=false;
		for (Photograps ph : photos) {
		 User uploader = ph.getUploader();
		 if(count%4==0){
			%><tr><% 
			flag=true;
		 }
%>			<td width="250" height="250">
<form action="/delete" method="post">
<div><input type="hidden" name="id" value="<%=ph.getId()%>" /></div>
<div align="center">
<%if (user != null) {
	%>
<a href="/comments.jsp?id=<%=ph.getId()%>"><%} %>
<img align="middle" src="image?id=<%=ph.getId()%>&imagetype=small">
<%if (user != null) {
	%>
</a>
<%} %>
<br>
<font size="2">by:<strong><%
if (ph.getUploader() == null) {
%>
Anonymous
<%}else{
%><%=uploader.getNickname() %>
<%} %>
</strong></font>
</div>
<% 
if((user!=null && uploader!=null && (uploader.equals(user) )) || (user!=null && userService.isUserAdmin())){	
%>
<div align="right">
<input type="submit" value="Delete" />
<input type="hidden" name="source" value="allphoto" />
</div>
<%} %>
</form></td>
<%
		if(flag && (count+1)%4==0 ){
			%></tr><%
			flag=false;
		}
		count++;
		} 
		if(!flag){
		  %></tr><%
		}
		%>
			</table>
<%  }		
	pm.close();
	if (user != null) {
%>
<form action="/upload" method="post"  ENCTYPE="multipart/form-data" >
<div>
<input name="photo" type="file" />
<input type="submit" value="Upload" />
</div>
</form>
<%} %>
</body>
</html>