<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
<script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>
<script type="text/javascript">

$(function () {

    $(".table").tableBtn({
        del:true,
        edit:true,
        pagination: {
            currentPage: ${page.pageNumber},
            totalPage: ${page.pageCount},
            form: "#queryForm"
        }
    });

    $("#add").click(function(){
        loadMenuChild(getMenuURL() + "/add", "新增数据包");
    });

    $("#queryFormBtn").click(function () {
        var industry_selected = "";
        $('#industry_selected option:selected').each(function () {
            if(industry_selected == ""){
                industry_selected = $(this).val();
            }else{
                industry_selected = industry_selected+","+$(this).val();
            }
        });
        $("#industryIds").val(industry_selected);
        $("#queryForm").query();
    });
    <c:if test="${industryIds != null}">
        var industryIds = "${industryIds}".split(",");
        for(var i = 0; i < industryIds.length; i++){
            var option = "#industry_selected option[value='"+industryIds[i]+"']";
            $(option).attr("selected", true);
        }
        $('#industry_selected').multiselect('rebuild');
        $("#industry_selected").multiselect( 'refresh' );
        $("input:checkbox").uniform();
    </c:if>
    $(".bootstrap-select").selectpicker();
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
            <div class="row">
                <div class="col-md-12">
			        <form class="form-inline" id="queryForm" method="post" datatype="html">
                        <div class="form-group" >
                            <label><span class="text-semibold">行业</span></label>

                            <input type="hidden" id="industryIds" name="industryIds" value="">

                            <select class="multiselect-filtering" id="industry_selected" required multiple="multiple">
                                <c:forEach items="${industrys}" var="obj">
                                    <option value="${obj.id}">${obj.parent.industry}-${obj.industry}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>数据包名称</label>
                            <div class="input-group">
                                <input type="text" class="form-control" name="packageName" id="packageName" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            &nbsp;
                            <button type="button" id="queryFormBtn" class="btn btn-primary" data-label=" 查询"><i class="icon-search4"></i> 查询</button>
                            &nbsp;
                            <button type="button" id="add" class="btn btn-primary" data-label=" 新增"><i class="icon-add"></i> 新增</button>
                        </div>
                    </form>
                </div>
            </div>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>
					<th>数据包名称</th>
					<th>数据包链接</th>
					<th>创建日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="obj" varStatus="status">
					<tr>
						<td>${obj.id}</td>
						<td>${obj.packageName}</td>
						<td>${obj.packageUrl}</td>
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
