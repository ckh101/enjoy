<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${scripts}/plugins/treegrid/jquery.treegrid.css">
<script type="text/javascript" src="${scripts}/plugins/treegrid/jquery.treegrid.js"></script>
<script type="text/javascript" src="${scripts}/plugins/treegrid/jquery.treegrid.bootstrap3.js"></script>
<script type="text/javascript">

$(function () {
	var dataSource = {};
	var mtype = {};
	mtype["0"]="[root]";
	mtype["1"]="菜单";
	mtype["2"]="页面";
	mtype["3"]="功能项";
    dataSource.mtype = mtype;
    initConfigData(dataSource);
       
    $("#queryForm").queryTable({
    	<shiro:hasPermission name="/admin/module/add">
        add:true,
        </shiro:hasPermission>
        conditions: [{
            defaultValue: "${model.mname}",
            name: "mname",
            label: "模块名"
        }]
    });
    $(".table").tableBtn({
    	<shiro:hasPermission name="/admin/module/del">
        del:true,
        </shiro:hasPermission>
    	<shiro:hasPermission name="/admin/module/edit">
        edit:true
        </shiro:hasPermission>
    });
    $('.tree').treegrid({
        initialState: "collapsed",
        expanderExpandedClass: 'icon-minus-circle2',
        expanderCollapsedClass: 'icon-plus-circle2'
    });
    $(".dictionary").formatConfigData();
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<form class="form-inline" id="queryForm" method="post" datatype="html"></form>
		</div>
	
		<table class="table table-hover  tree">
			<thead>
				<tr class="active">
					<th>模块名称</th>
					<th>路径</th>
					<th>类型</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data }" var="obj" varStatus="status">
					<tr class="treegrid-${obj.id} <c:if test="${obj.mname != 'root'}"> treegrid-parent-${obj.parentModule.id}</c:if> ">
					    <c:choose>
					    	<c:when test="${obj.mname != 'root' }">
					    		<td>${obj.mname}</td>
					    	</c:when>
					    	<c:otherwise>
					    		<td>${webTitle}</td>
					    	</c:otherwise>
					    </c:choose>
						
						<td>${obj.url}</td>
						<td class="dictionary" data-source="mtype" data-value="${obj.mtype}"></td>
						<td class="text-center" data-key="${obj.id }" <c:if test="${obj.mname == 'root'}">style="visibility:hidden"</c:if>></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	<!-- /basic table -->
</body>
</html>
