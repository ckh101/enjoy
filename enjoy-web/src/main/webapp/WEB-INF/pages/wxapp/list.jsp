<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
<script type="text/javascript">

$(function () {
	var dataSource = {};
	var authStatus = {};
	authStatus["0"]="未授权";
	authStatus["1"]="已授权";
    dataSource.authStatus = authStatus;
    var typeId = {};
    typeId["1"]="微信公众号";
    typeId["2"]="微信小程序";
	dataSource.typeId = typeId;
    initConfigData(dataSource);
    $("#queryForm").queryTable({
    	<shiro:hasPermission name="/admin/wxapp/add">
        add:true,
        </shiro:hasPermission>
        conditions: [{
            defaultValue: "${app.company}",
            name: "company",
            label: "公司主体"
        },{
            defaultValue: "${app.appName}",
            name: "appName",
            label: "公众号名称"
        }]
    });

    $(".table").tableBtn({
    	<shiro:hasPermission name="/admin/wxapp/del">
        del:true,
        </shiro:hasPermission>
    	<shiro:hasPermission name="/admin/wxapp/edit">
        edit:true,
        </shiro:hasPermission>
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        },
        <shiro:hasPermission name="/mpauth/auth">
        buttons: [
            {
                label: "授权",
                url: "/mpauth/auth",
                icon: "icon-user-check"
            }
        ]
        </shiro:hasPermission>
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
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>公司主体</th>
					<th>公众号名称</th>
					<th>appId</th>
					<th>类型</th>
					<th>授权状态</th>
					<th>创建日期</th>
					<th>更新日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="obj" varStatus="status">
					<tr>
						<td>${status.index+1 }</td>
						<td>${obj.company}</td>
						<td>${obj.appName}</td>
						<td>${obj.appId}</td>
						<td class="dictionary" data-source="typeId" data-value="${obj.typeId}"></td>
						<td class="dictionary" data-source="authStatus" data-value="${obj.authStatus}"></td>
						<td><fmt:formatDate value="${obj.createTime }" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td><fmt:formatDate value="${obj.updateTime }" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td class="text-center" data-key="${obj.id }"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	<!-- /basic table -->
</body>
</html>
