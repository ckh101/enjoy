<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="el" uri="/WEB-INF/tag/tojson.tld" %>
<%
final String context = request.getContextPath();
final int port = request.getServerPort();
String source = "";
if(port == 80 || port == 443){
	source = request.getScheme()+"://"+request.getServerName()+context;
}else{
	source = request.getScheme()+"://"+request.getServerName()+":"+port+context;
}
final String webTitle = "腾讯社交广告运营平台";//网站标题
final String scripts = source+"/static/js";
final String css = source+"/static/css";
final String images=source+"/static/images";
pageContext.setAttribute("source",source);
pageContext.setAttribute("context",context);
pageContext.setAttribute("webTitle",webTitle);
pageContext.setAttribute("scripts",scripts);
pageContext.setAttribute("css",css);
pageContext.setAttribute("images",images);

%>