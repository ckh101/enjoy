<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/daterangepicker.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.date.js"></script>
<script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/translations/zh_CN.js"></script>
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

    var _condition = [{
        name: "search_EQ_adProductId",
        label: "产品ID"
    }, {
        name: "search_LIKE_adProductName",
        label: "产品名称"
    }, {
        name: "statDateRange",
        label: "日期",
        type: "date",
        options: {
            locale: {
                format: 'YYYY-MM-DD',
                startLabel: "起始日期",
                endLabel: "结束日期",
                daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
            }
        }
    }];

    for (var i = 0; i < _condition.length; i++) {
        var _name = _condition[i].name;
        _condition[i].defaultValue = _reqParams[_name] || '';
    }

    $("#queryForm").queryTable({
        <shiro:hasPermission name="/admin/pdd/adReport/add">
        add:{
            label : ' 新增',
            icon : 'icon-add',
            url : 'onclick:addItem();'
        },
        </shiro:hasPermission>
        <shiro:hasPermission name="/admin/adReport/update">
        edit:{
            url : 'onclick:editItem(key);'
        },
        </shiro:hasPermission>
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
			<form class="form-inline" id="queryForm" method="post" action="/admin/pdd/adReport/list?storeId=${param.storeId}" datatype="html"></form>
		</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>日期</th>
                <th>产品</th>
                <th>展示数</th>
                <th>点击数</th>
                <th>消耗金额</th>
                <shiro:hasPermission name="/admin/pdd/adReport/add">
                <th>操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="obj" varStatus="status">
                <tr>
                    <td><fmt:formatDate value="${obj.statDate}" pattern="yyyy-MM-dd"/></td>
                    <td>
                        ${obj.adProductName} (id:${obj.adProductId})
                    <td>
                        ${obj.displayCnt}
                    </td>
                    <td>
                        ${obj.clickCnt}
                    </td>
                    <td>
                        ${obj.cost/100}
                    </td>
                    <shiro:hasPermission name="/admin/pdd/adReport/update">
                    <td data-name="operation" data-key="${obj.id}">
                        <button type="button" class="opBtn btn btn-xs bg-info-600" onclick="editItem(${obj.id})"><i class="icon-pencil7 position-left"></i>编辑</button>
                        <button type="button" class="opBtn btn btn-xs btn-danger" onclick="deleteItem(${obj.id})"><i class="icon-pencil7 position-left"></i>删除</button>
                    </td>
                    </shiro:hasPermission>
                </tr>
            </c:forEach>
            </tbody>
        </table>
	</div>
	<!-- /basic table -->

    <!-- 新增数据表单 -->
    <div id="adReportModal" class="modal fade">
        <div class="modal-dialog" style="width:540px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title"></h5>
                </div>
                <form class="form-horizontal" data-storeId="${param.storeId}">
                    <input type="hidden" name="id" />
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">报表日期</label>
                            <div class="col-sm-7">
                                <input name="statDate" type="text" class="form-control pickadate" placeholder="请选择日期">
                            </div>
                            <div class="col-sm-1"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">选择产品</label>
                            <div class="col-sm-7">
                                <select class="select-prod" data-placeholder="请选择产品"></select>
                            </div>
                            <div class="col-sm-1"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">展示数</label>
                            <div class="col-sm-7">
                                <input name="displayCnt" type="text" class="form-control" placeholder="请输入展示数">
                            </div>
                            <div class="col-sm-1"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">点击数</label>
                            <div class="col-sm-7">
                                <input name="clickCnt" type="text" class="form-control" placeholder="请输入点击数">
                            </div>
                            <div class="col-sm-1"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">消耗金额</label>
                            <div class="col-sm-7">
                                <input name="cost" type="text" class="form-control" placeholder="请输入消耗金额，元">
                            </div>
                            <div class="col-sm-1"></div>
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
    <!-- /新增数据表单 -->
</body>
<script>
    var _modal = $("#adReportModal");
    // 日期选择框初始化
    $(".pickadate").pickadate({
        today: '今天',
        clear: false,
        close: false,
        format: 'yyyy-mm-dd',
        formatSubmit: 'yyyy-mm-dd'
    });

    // 产品选择框
    var _prodSelect = _modal.find('.modal-body').find('.select-prod');
    _prodSelect.select2(); // 初始化
    function addItem() {
        _modal.find('.modal-title').text('新增数据');
        _modal.find('form').attr('action', '${source}/admin/pdd/adReport/add')[0].reset();
        _modal.modal("show");

        _prodSelect.prop('readonly', false).empty().select2('destroy');

        var _storeId = _modal.find('form').attr('data-storeId');
        $.get('${source}/admin/pdd/adProduct/recentlyPlace',{storeId:_storeId}, function(ret) {
            if (ret.status != 0) {
                _authModal.modal('hide');
                swal.fire({
                    title: ret.msg,
                    confirmButtonColor: "#2196F3"
                });

                return;
            }

            // 构建下拉选项
            var _data = ret.data;
            var _options = '';
            for (var i = 0; i < _data.length; i++) {
                var _o = _data[i];
                _options += '<option value="' + _o.id + '"' + (_o.selected ? 'selected' : '') + '>' + _o.text + '-' + _o.id + '</option>';
            }

            _prodSelect.append(_options);
            _prodSelect.select2();
        });
    };

    function editItem(_id) {
        _modal.find('.modal-title').text('编辑数据');
        _modal.find('[name="id"]').val(_id);
        _modal.find('form').attr('action', '${source}/admin/pdd/adReport/update')[0].reset();
        _prodSelect.empty().select2('destroy');

        _modal.modal("show");
        $.get('admin/pdd/adReport/detail?id=' + _id, function(ret) {
            if (ret.status == 0) {
                ret.data.cost = ret.data.cost / 100;
                fillFormData(_modal.find('form'), ret.data);
                _prodSelect.prop('readonly', true).append('<option value="' + ret.data.adProductId + '" selected>' + ret.data.adProductName + '(id:' + ret.data.adProductId + ')</option>');
                _prodSelect.select2();
            } else {
                _modal.modal("hide");
                alert(ret.msg);
            }
        });
    }

    function deleteItem(_id) {
        Swal.fire({
            title: "确定删除所选的记录?",
            text: "你的数据被删除后将无法恢复!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: "#EF5350",
            confirmButtonText: "是的, 删除",
            cancelButtonText: "取消"
        }).then((result) => {
            if (result.value) {
                $.post('admin/pdd/adReport/delete?id=' + _id, function (ret) {
                    console.log(ret);
                    if (ret.status === 0) {
                        Swal.fire({
                            position: 'top-end',
                            title: "成功!",
                            text: "数据已被成功删除.",
                            showConfirmButton: false,
                            timer: 1500
                        });
                        refreshPage();
                    } else {
                        Swal.fire({
                            type: 'error',
                            title: "错误",
                            text: ret.msg
                        })
                    }
                });
            }
        });
    }

    function fillFormData($formEL, obj) {
        $.each(obj, function(index, item) {
            $formEL.find("[name=" + index + "]").val(item);
        });
    }

    _modal.find('form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        _params['cost'] = accMul(_params['cost'] || 0, 100);
        _params['adProductId'] = _prodSelect.select2("val");

        evt.preventDefault();

        $.post(_form.attr('action'), _params, function(_data) {
            var _title = '操作成功！';
            if (_data.status != 0) {
                _title = _data.msg;
                swal.fire({
                    title: _title,
                    confirmButtonColor: "#2196F3"
                });
            } else {
                _modal.modal('hide');

                setTimeout(function() {
                    if (_params['id']) {
                        refreshPage();
                    } else {
                        refreshPage(1);
                    }
                }, 300);
            }
        });

    });
</script>
</html>
