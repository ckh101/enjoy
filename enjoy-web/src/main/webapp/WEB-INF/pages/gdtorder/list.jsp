<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.date.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/translations/zh_CN.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
<script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>
<script type="text/javascript">

$(function () {
    $('#startDate').pickadate({format: 'yyyy-mm-dd 00:00:00'});
    $('#endDate').pickadate({format: 'yyyy-mm-dd 23:59:59'});
    $(".table").tableBtn({
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        }
    });

    $("#exportData").click(function(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if(startDate == ""||endDate == ""){
            $.alertWarning("开始日期,结束日期不能为空");
            return;
        }
        if(startDate > endDate){
            $.alertWarning("开始日期不能大于结束日期");
            return;
        }
        var days = dateMinus(startDate, endDate);
        if(days > 90){
            $.alertWarning("查询日期不能超过90天");
            return;
        }
        $("#downloadExcel").attr('href',"${source}/admin/gdt_order/ExcelDownload?startDate="+startDate+"&endDate="+endDate);
        $("#downloadExcel").click();
    });

    $("#queryFormBtn").click(function () {
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if(startDate == ""||endDate == ""){
            $.alertWarning("开始日期,结束日期不能为空");
            return;
        }
        if(startDate > endDate){
            $.alertWarning("开始日期不能大于结束日期");
            return;
        }
        var days = dateMinus(startDate, endDate);
        if(days > 90){
            $.alertWarning("查询日期不能超过90天");
            return;
        }
        $("#queryForm").query();
    });
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
            <div class="row">
                <div class="col-md-12">
                    <form class="form-inline form-horizontal" id="queryForm" method="post" datatype="html">
                        <div class="form-group mb-20">
                            <label>日期</label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                <input type="text" class="form-control pickadate" name="startDate" id="startDate" value="${startDate}" placeholder="开始日期">
                            </div>

                        </div>
                        <div class="form-group mb-20" style="margin-right: 30px;">
                            <label>-</label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                <input type="text" class="form-control pickadate" name="endDate" id="endDate" value="${endDate}" placeholder="结束日期">
                            </div>
                        </div>
                        <div class="form-group mb-20" style="margin-right: 10px;">
                            <label>广告主主体</label>
                            <div class="form-group has-feedback">
                                <input type="text" class="form-control" placeholder="广告主主体" name="accountName" id="accountName" value="${accountName}" >
                            </div>
                        </div>
                        <div class="form-group mb-20" style="margin-right: 10px;">
                            <label>广告主ID</label>
                            <div class="form-group has-feedback">
                                <input type="text" class="form-control" placeholder="广告主ID" name="accountId" id="accountId" value="${accountId}" type="number">
                            </div>
                        </div>
                        <div class="form-group mb-20" style="margin-right: 10px;">
                            <label>商品名称</label>
                            <div class="form-group has-feedback">
                                <input type="text" class="form-control" placeholder="商品名称" name="pro" id="pro" value="${pro}">
                            </div>
                        </div>
                        <div class="form-group mb-20" style="margin-right: 10px;">
                            <button type="button" id="queryFormBtn" class="btn btn-primary btn-xs" data-label=" 查询"><i class="icon-search4"></i> 查询</button>
                        </div>
                        <div class="form-group mb-20" style="margin-right: 10px;">
                            <button type="button" class="btn btn-primary btn-xs"  id="exportData"><a id="downloadExcel" href="#" target="_blank" style="color:#ffffff"><i class="icon-database-export">导出</i></a></button>
                        </div>
                    </form>

                </div>
            </div>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th>广告主</th>
					<th>商品名称</th>
					<th>手机号码</th>
					<th>创建日期</th>
				</tr>
			</thead>
			<tbody>
                <c:forEach items="${page.list}" var="obj">
                    <tr>
                        <td>${obj.account_name}</td>
                        <td>${obj.customized_page_name}</td>
                        <td>${obj.user_phone}</td>
                        <td>${obj.ecommerce_order_time}</td>
                    </tr>
                </c:forEach>
			</tbody>
		</table>
	</div>
	<!-- /basic table -->
</body>
</html>
