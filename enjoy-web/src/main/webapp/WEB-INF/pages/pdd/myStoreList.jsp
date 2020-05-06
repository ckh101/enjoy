<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        conditions: []
    });
    $(".table").tableBtn({
        buttons: [
            {
                label: "数据统计",
                url: "../../../adReport/list",
                keyName: 'storeId',
                icon: "icon-stats-bars2"
            },
            {
                label: "资金记录",
                url: "../../../store/fundStatement/list",
                keyName: 'storeId',
                icon: "icon-coin-yen"
            },
            {
                label: "产品投放",
                url: "../../../adProduct/list",
                keyName: 'storeId',
                icon: "icon-laptop"
            }
        ]
    });
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<!--<form class="form-inline" id="queryForm" method="post" datatype="html"></form>-->
		</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>#店铺ID</th>
                <th>店铺名称</th>
                <th>可用余额(元)</th>
                <th>冻结余额(元)</th>
                <th>描述</th>
                <th>创建日期</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="obj" varStatus="status">
                <tr>
                    <td>${obj.storeId}</td>
                    <td><c:out value="${obj.name}" /></td>
                    <td>${obj.balance/100}</td>
                    <td>${obj.blockedBalance/100}</td>
                    <td><c:out value="${obj.detail}" /></td>
                    <td><fmt:formatDate value="${obj.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td data-key="${obj.storeId}"></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
	</div>
	<!-- /basic table -->

</body>
<script>

</script>
</html>
