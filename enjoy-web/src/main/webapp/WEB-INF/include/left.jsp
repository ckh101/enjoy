<%@ page import="java.util.Calendar" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    Calendar cal = Calendar.getInstance();
    pageContext.setAttribute("year", cal.get(Calendar.YEAR));
%>
<!-- Main sidebar -->
<div class="sidebar sidebar-main sidebar-default">
    <div class="sidebar-fixed">
        <div class="sidebar-content">

            <!-- Main navigation -->
            <div class="sidebar-category sidebar-category-visible">

                <div class="category-content no-padding">
                    <ul class="navigation navigation-main navigation-accordion">
                        <li class="navigation-header icon-father">
                            <span><i class="icon-grid mr-15"></i>全部菜单</span>
                        </li>
                        <c:forEach items="${_user.modtree.children}" var="pmod" varStatus="status">

                            <li class="${status.index == 0?'active':''}">
                                <a href="${pmod.url }"><i class="${pmod.menuicon }"></i> <span>${pmod.mname}</span></a>
                                <ul>
                                    <c:forEach items="${pmod.children }" var="cmod">
                                        <li><a href="javascript:loadMenu('${pmod.mname}','${cmod.mname}','${source}${cmod.url}')">${cmod.mname}</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>

                    </ul>
                </div>
            </div>
            <!-- /main navigation -->

        </div>
    </div>
    <!-- Footer -->
    <div class="footer text-muted bsbb p-10">
        Copyright©${year} 广东和邦网络科技有限公司，ALL Rights Reserved.
    </div>
    <!-- /footer -->
</div>
<!-- /main sidebar -->
