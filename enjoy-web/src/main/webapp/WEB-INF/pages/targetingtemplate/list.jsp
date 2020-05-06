<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
<script type="text/javascript">

$(function () {
    $("#queryForm").queryTable({
    	<shiro:hasPermission name="/admin/targetingTemplate/add">
        add:true,
        </shiro:hasPermission>
        conditions: [{
            defaultValue: "${targetingTemplate.templateName}",
            name: "templateName",
            label: "模板名称"
        }]
    });

    $(".table").tableBtn({
    	<shiro:hasPermission name="/admin/targetingTemplate/del">
        del:true,
        </shiro:hasPermission>
        <shiro:hasPermission name="/admin/targetingTemplate/edit">
        edit:true,
        </shiro:hasPermission>
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        }
    });
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<form class="form-inline" id="queryForm" method="post" datatype="html"></form>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>模板名称</th>
					<th>创建日期</th>
					<th class="text-center">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="obj" varStatus="status">
					<tr>
						<td>${status.index+1 }</td>
						<td>${obj.templateName}</td>
						<td><fmt:formatDate value="${obj.createTime }" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td class="text-center" data-key="${obj.id }"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	<!-- /basic table -->
</body>
</html>
