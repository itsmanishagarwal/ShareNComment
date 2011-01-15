<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
	
<body>
<form action="/uploadbooks" method="post"  ENCTYPE="multipart/form-data" >
<div>
<input name="photo" type="file" />
<input type="submit" value="Upload" /><br>
<input type="Submit" name="Delete All" value="deleteAll"> 
</div>
</form>
</body>
</html>