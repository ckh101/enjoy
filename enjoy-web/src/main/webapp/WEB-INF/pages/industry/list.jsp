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
        add:true,
        conditions: [{
            defaultValue: "${industry.industry}",
            name: "industry",
            label: "行业名称"
        }]
    });
    $(".table").tableBtn({
        del:true,
        edit:true,
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        }
    });

    <c:if test="${industry != null}">
        $("#parentId").val("${industry.parent.id}");
    </c:if>
    $(".bootstrap-select").selectpicker();
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<form class="form-inline" id="queryForm" method="post" datatype="html">
                <div class="form-group" data-width="68%">
                    <label>父行业</label>
                    <select name="parentId" id="parentId"  class="bootstrap-select" data-live-search="true" required>
                        <option value="0">选择父行业</option>
                        <c:forEach items="${plist}" var="obj">
                            <option value="${obj.id}">${obj.industry}</option>
                        </c:forEach>
                    </select>
                </div>
            </form>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>
					<th>行业</th>
					<th>父行业</th>
					<th>创建日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="obj" varStatus="status">
					<tr>
						<td>${obj.id}</td>
						<td>${obj.industry}</td>
						<td>${obj.parent!=null?obj.parent.industry:""}</td>
						<td><fmt:formatDate value="${obj.createTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						<td class="text-center" data-key="${obj.id }"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	<!-- /basic table -->
</body>
</html>
