<%@ page pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  	
  	<c:if test="${not empty param.retUrl}">
  		<meta http-equiv="refresh" content="3; url='${param.retUrl}'" />
    </c:if>
    
    <title>投票成功</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
  
  <body>
    <p/>
    <p align="center" style="font-size: 14pt;">投票成功，非常感谢您的参与！</p>
    
    <c:if test="${not empty param.retUrl}">
  		 <p align="center" style="font-size: 14pt;">3&nbsp;秒钟后跳转到投票结果页</p>
    </c:if>
        
  </body>
</html>
