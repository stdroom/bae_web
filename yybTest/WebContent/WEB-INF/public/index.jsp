<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
    <title>mike首页面</title>
  </head>
  <body>
  <%out.println("哈哈"); %>
     <form action="fileupload" enctype="multipart/form-data"
             method="post">
             <input type="text" name="versionCode" value="版本号"/>
             <br />
             <input type="text" name="versionLog" value="修改日志" />
             <br />
             <input type="file" name="myfile" />
             <br />
             <input type="submit" />
     </form>
     <br />
     
     
	</td></tr>
  </body>
</html>
