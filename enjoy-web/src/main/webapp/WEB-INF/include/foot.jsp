<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%
	Calendar cal = Calendar.getInstance();
	pageContext.setAttribute("year", cal.get(Calendar.YEAR));
%>
<!-- Footer -->
<div class="footer text-muted bsbb p-10">
    Copyright©${year}xxxxxxx，ALL Rights Reserved.
</div>
<!-- /footer -->
