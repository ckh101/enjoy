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
	var balanceAccountType = {
        "1" : "余额账户",
        "2" : "冻结账户"
    };
    var handleType = {
        "1" : "扣除",
        "2" : "转入"
    };
	systemStatus["ALLOW"] = "正常";
	systemStatus["FORBIDDEN"] = "禁用";
	systemStatus["DEL"] = "删除";

	dataSource.systemStatus = systemStatus;
	dataSource.balanceAccountType = balanceAccountType;
    dataSource.handleType = handleType;
	initConfigData(dataSource);

    // 填充参数值到表单
    var _reqParams = ${el:toJsonString(param)};

    var _condition = [/*{
        name: "search_EQ_storeId",
        label: "店铺ID"
    }, {
        name: "search_LIKE_storeName",
        label: "店铺名称"
    },*/{
        name: "search_EQ_adProductId",
        label: "产品ID"
    }, {
        name: "search_LIKE_adProductName",
        label: "产品名称"
    }, {
        name: "search_EQ_accountType",
        label: "账户",
        type: "select",
        dataSource: 'balanceAccountType'
    }, {
        name: "search_EQ_type",
        label: "类型",
        type: "select",
        dataSource: 'handleType'
    }];

    for (var i = 0; i < _condition.length; i++) {
        var _name = _condition[i].name;
        _condition[i].defaultValue = _reqParams[_name] || '';
    }

    $("#queryForm").queryTable({
        conditions: _condition
    });


    $(".table").tableBtn({
        pagination: {
            currentPage: ${page.number+1},
            totalPage: ${page.totalPages},
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
            <c:if test="${not empty store}">
            <div class="container-fluid">
                <div class="row text-center">
                    <div class="col-md-4">
                        <div class="content-group">
                            <h5 class="text-semibold no-margin fs-24"><i class="icon-store2 position-left text-blue fs-24"></i> ${fn:escapeXml(store.name)}</h5>
                            <span class="text-muted text-size-small">店铺名称</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="content-group">
                            <h5 class="text-semibold no-margin fs-24"><i class="icon-coin-yen position-left text-orange-800 fs-24"></i> ${store.balance/100}元</h5>
                            <span class="text-muted text-size-small">可用余额</span>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="content-group">
                            <h5 class="text-semibold no-margin fs-24"><i class="icon-coin-yen position-left text-orange-300 fs-24"></i> ${store.blockedBalance/100}元</h5>
                            <span class="text-muted text-size-small">冻结余额</span>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>
			<form class="form-inline" id="queryForm" method="post" action="/admin/pdd/store/fundStatement/list?storeId=${param.storeId}" datatype="html"></form>
		</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <c:if test="not empty param.storeId">
                <th>店铺</th>
                </c:if>
                <th>产品</th>
                <th>账户</th>
                <th>金额(元)</th>
                <th>说明</th>
                <th>操作人</th>
                <th>创建日期</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="obj" varStatus="status">
                <tr>
                    <c:if test="not empty param.storeId">
                    <td>${obj.storeName} <span class="text-muted">(id:${obj.storeId})</span></td>
                    </c:if>
                    <td>
                        <c:choose>
                            <c:when test="${empty obj.adProductId}">无</c:when>
                            <c:otherwise>
                                ${obj.adProductName} <span class="text-muted">(id:${obj.adProductId})</span>
                            </c:otherwise>
                        </c:choose>
                    <td>
                        <c:choose>
                            <c:when test="${obj.accountType == 1}">余额账户</c:when>
                            <c:when test="${obj.accountType == 2}">冻结账户</c:when>
                            <c:otherwise>
                                未知账户
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${obj.type == 1}"><span class="label label-default">-${obj.amount/100}</span></c:when>
                            <c:when test="${obj.type == 2}"><span class="label label-primary">+${obj.amount/100}</span></c:when>
                            <c:otherwise>
                                符号未知${obj.amount/100}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${fn:escapeXml(obj.remark)}</td>
                    <td>${obj.operator}</td>
                    <td><fmt:formatDate value="${obj.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
	</div>
	<!-- /basic table -->

    <!-- 新增店铺表单 -->
    <div id="addStoreModal" class="modal fade">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title">新增店铺</h5>
                </div>
                <form action="${source}/admin/ad/admanager/admonitor/save" class="form-horizontal">
                    <input type="hidden" name="adId" />
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">店铺名称</label>
                            <div class="col-sm-6">
                                <input name="receivePhone" type="text" class="form-control" placeholder="请输入店铺名称">
                            </div>
                            <div class="col-sm-2"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">广告主ID</label>
                            <div class="col-sm-6">
                                <input name="receivePhone" type="text" class="form-control" placeholder="请输入广告主ID">
                            </div>
                            <div class="col-sm-2"></div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /新增店铺表单 -->
</body>
<script>
    function showAddForm() {
        alert('aaabbbccc');
    }
</script>
</html>
