<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" errorPage="/WEB-INF/jsp/include/defaultException.jsp" %>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="org.guzz.GuzzContext"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

GuzzContext guzzContext =  GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()) ;

%>

<!--
  Read：http://code.google.com/p/guzz/wiki/TutorialTaglib 
-->
<g:inc business="guzzSlowUpdate" pkValue="1" updatePropName="countToInc" count="10" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    This is my Guzz JSP page. <br>
  </body>
</html>
