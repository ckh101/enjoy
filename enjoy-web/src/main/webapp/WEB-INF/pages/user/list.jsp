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
	var systemStatus = {};
	var accountType = {};
	systemStatus["ALLOW"] = "正常";
	systemStatus["FORBIDDEN"] = "禁用";
	systemStatus["DEL"] = "删除";
	accountType["ADVERTISER"] = "广告主";
	accountType["SYSTEM"] = "系统";
	dataSource.systemStatus = systemStatus;
	dataSource.accountType = accountType;
	initConfigData(dataSource);
    $("#queryForm").queryTable({
    	<shiro:hasPermission name="/admin/user/add">
        add:true,
        </shiro:hasPermission>
        conditions: [{
            defaultValue: "${user.account}",
            name: "account",
            label: "账号"
        }]
    });
    $(".table").tableBtn({
    	<shiro:hasPermission name="/admin/user/del">
        del:true,
        </shiro:hasPermission>
    	<shiro:hasPermission name="/admin/user/edit">
        edit:true,
        </shiro:hasPermission>
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        }
        <%--
        ,
        <shiro:hasPermission name="/admin/user/resetpassword">
        buttons: [
            {
                label: "重置密码",
                url: "/password",
                icon: "icon-key"
            }
        ]
        </shiro:hasPermission>
		--%>
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
	    <div class="table-responsive">
		    <table class="table table-hover">
			<thead>
				<tr>
					<th>#ID</th>
					<th>公司主体</th>
					<th>账号</th>
					<th>名称</th>
					<th>手机号码</th>
					<th>角色</th>
					<th>账号类型</th>
					<th>状态</th>
					<th>操作人</th>
					<th>创建日期</th>
					<th>最后登录时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="obj" varStatus="status">
					<tr>
						<td>${obj.id}</td>
						<td>${obj.company}</td>
						<td>${obj.account}</td>
						<td>${obj.userName}</td>
						<td>${obj.phone}</td>
						<td></td>
						<td class="dictionary" data-source="accountType" data-value="${obj.accountType}"></td>
						<td class="dictionary" data-source="systemStatus" data-value="${obj.systemStatus}"></td>
                        <c:choose>
                            <c:when test="${not empty obj.operator}">
                                <td>${obj.operator.userName}</td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
						<td><fmt:formatDate value="${obj.createTime }" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td><fmt:formatDate value="${obj.lastLoginTime }" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td class="text-center" data-key="${obj.id }"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
        </div>
	</div>
	<!-- /basic table -->
</body>
</html>
