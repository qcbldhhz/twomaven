<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用于显示物流信息</title>
    
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
  
  <form action="${pageContext.request.contextPath }/QueryExpressServlet">
  		<input type="text" name="key">
  		<input  type="submit" value="搜索"/>
  </form>
  
   <table>
  		<tr>
	  		<th>物流<th>
	  		<th>首字母<th>
	  	</tr>
	  	<c:forEach items="${map}" var="item">
		  	<tr>
		  		<td>
			   	  <ul>
			   		<li >${item.key }</li>
			   		  <c:forEach items="${item.value}" var="express">
			   			<li >${express.name }</li>
			   		  </c:forEach> 
			   	  </ul>
		   		</td>
		   		<td>
		   			<input type="button" value="${item.key }"/>
		   		</td>
		    </tr>
		 </c:forEach>
   	</table>
  </body>
</html>
