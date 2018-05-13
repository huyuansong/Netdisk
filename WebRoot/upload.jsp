<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  <body>
  	<h1>文件上传</h1><hr>
  	<form action="${pageContext.request.contextPath }/servlet/UploadServlet" method="POST" enctype="multipart/form-data">
  		选择文件:<input type="file" name="file1"/><br>
  		描述信息:<textarea rows="5" cols="45" name="description"></textarea><br>
  		<input type="submit" value="上传"/>
  	</form>
  </body>
</html>
