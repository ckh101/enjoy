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

    var _reqParams = ${el:toJsonString(param)};
    var _condition = [{
        name: "search_EQ_storeId",
        label: "店铺ID"
    }, {
        name: "search_LIKE_name",
        label: "店铺名称"
    }];

    for (var i = 0; i < _condition.length; i++) {
        var _name = _condition[i].name;
        _condition[i].defaultValue = _reqParams[_name] || '';
    }

    $("#queryForm").queryTable({
    	<shiro:hasPermission name="/admin/pdd/store/add">
        add:{
            label : ' 新增',
            icon : 'icon-add',
            url : 'onclick:addItem();'
        },
        </shiro:hasPermission>
        conditions: _condition
    });
    $(".table").tableBtn({
    	<shiro:hasPermission name="/admin/pdd/store/update">
        edit:{
            url : 'onclick:editItem(key);'
        },
        </shiro:hasPermission>
        pagination: {
            currentPage: ${page.number+1},
            totalPage: ${page.totalPages},
            form: "#queryForm"
        },
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
            <shiro:hasPermission name="/admin/pdd/store/deposit">
            ,{
                label: "账号充值",
                url: "onclick:deposit(key);",
                icon: "icon-power2"
            }
            </shiro:hasPermission>
            <shiro:hasPermission name="/admin/pdd/store/authorize">
            ,{
                label: "授权管理",
                url: "onclick:showAuthModal(key);",
                icon: "icon-accessibility"
            }
            </shiro:hasPermission>
        ]
    });

    window.refreshPage = function(_page) {
        $('#queryForm').query(_page || _reqParams.pageNumber || 1);
    }
});
</script>
</head>
<body>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<form class="form-inline" id="queryForm" action="/admin/pdd/store/list" method="post" datatype="html"></form>
		</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>#店铺ID</th>
                <th>店铺名称</th>
                <th>描述</th>
                <th>可用余额(元)</th>
                <th>冻结余额(元)</th>
                <th>创建日期</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="obj" varStatus="status">
                <tr id="store-${obj.storeId}">
                    <td>${obj.storeId}</td>
                    <td data-name="name">${fn:escapeXml(obj.name)}</td>
                    <td>${fn:escapeXml(obj.detail)}</td>
                    <td>${obj.balance/100}</td>
                    <td>${obj.blockedBalance/100}</td>
                    <td><fmt:formatDate value="${obj.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td data-key="${obj.storeId}"></td>
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
                <form class="form-horizontal">
                    <input type="hidden" name="storeId" />
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">店铺名称</label>
                            <div class="col-sm-6">
                                <input name="name" type="text" class="form-control" placeholder="请输入店铺名称">
                            </div>
                            <div class="col-sm-2"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-3">描述</label>
                            <div class="col-sm-6">
                                <input name="detail" type="text" class="form-control" placeholder="请输入店铺描述">
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

    <!-- 充值表单 -->
    <div id="depositModal" class="modal fade">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title"></h5>
                </div>
                <form class="form-horizontal" action="${source}/admin/pdd/store/deposit" method="post">
                    <input type="hidden" name="storeId" />
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="control-label col-sm-3">充值金额</label>
                            <div class="col-sm-6">
                                <input name="amount" type="text" class="form-control" placeholder="请输入充值金额，元">
                            </div>
                            <div class="col-sm-3"></div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">充值说明</label>
                            <div class="col-sm-9">
                                <textarea rows="5" maxlength="100" name="remark" type="text" class="form-control" placeholder="请输入充值说明，最多100字"></textarea>
                            </div>
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
    <!-- /充值表单 -->

    <!-- 店铺授权表单 -->
    <div id="authModal" class="modal fade">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title">店铺授权管理</h5>
                </div>
                <form class="form-horizontal" action="${source}/admin/pdd/store/authorize" method="post">
                    <input type="hidden" name="storeId" />
                    <div class="modal-body">
                        <select class="select-item-user" data-placeholder="添加授权用户" multiple="multiple"></select>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /店铺授权表单 -->
</body>
<script>
    var _modal = $("#addStoreModal");
    var _depositModal = $("#depositModal");
    var _authModal = $("#authModal");
    function addItem() {
        _modal.find('.modal-title').text('新增店铺');
        _modal.find('form').attr('action', '${source}/admin/pdd/store/add')[0].reset();
        _modal.modal("show");
    }

    function editItem(_id) {
        _modal.find('.modal-title').text('编辑店铺');
        _modal.find('[name="storeId"]').val(_id);
        _modal.find('form').attr('action', '${source}/admin/pdd/store/update')[0].reset();
        _modal.modal("show");

        $.get('admin/pdd/store/detail?storeId=' + _id, function(ret) {
            if (ret.status == 0) {
                fillFormData(_modal.find('form'), ret.data);
            } else {
                _modal.modal("hide");
                alert(ret.msg);
            }
        });
    }

    function deposit(_id) {
        var _dataEle = $('#store-' + _id);
        if (!_dataEle[0]) {
            alert('请检查店铺是否存在！');
        }

        _depositModal.find('[name="storeId"]').val(_id);
        var _storeName = $('#store-' + _id).find('[data-name="name"]').text();

        _depositModal.find('.modal-title').text('账户充值 - ' + _storeName + ' - ' + 'ID:' + _id);
        _depositModal.modal("show");
    }

    $('#addStoreModal form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        evt.preventDefault();

        var _actionUrl = _form.attr('action');
        $.post(_actionUrl, _params, function(_data) {
            var _title = '操作成功！';
            if (_data.status != 0) {
                _title = _data.msg;
                swal.fire({
                    title: _title,
                    confirmButtonColor: "#2196F3"
                });
            } else {
                $('#addStoreModal').modal('hide');

                setTimeout(function() {
                    if (_actionUrl.indexOf('/update') > -1) {
                        refreshPage();
                    } else {
                        refreshPage(1);
                    }
                }, 300);
            }
        });

    });

    _depositModal.find('form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        _params['amount'] = accMul(_params['amount'] || 0, 100);

        evt.preventDefault();

        var _actionUrl = _form.attr('action');
        $.post(_actionUrl, _params, function(_data) {
            var _title = '操作成功！';
            if (_data.status != 0) {
                _title = _data.msg;
            } else {
                _depositModal.modal('hide');
            }

            swal.fire({
                title: _title,
                confirmButtonColor: "#2196F3"
            }).then((result) => {
                if (result.value) {
                    refreshPage();
                }
            });
        });
    });

    var _authSelect = _authModal.find('.modal-body').find('.select-item-user');
    _authSelect.select2({
        formatSelectionCssClass: function (data, container) { return "bg-slate-600"; },
    }); // 初始化

    _authModal.find('form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        _params['userIds'] = _authSelect.select2("val").join(',');

        evt.preventDefault();

        var _actionUrl = _form.attr('action');
        $.post(_actionUrl, _params, function(_data) {
            var _title = '操作成功！';
            if (_data.status != 0) {
                _title = _data.msg;
            } else {
                _authModal.modal('hide');
            }

            swal.fire({
                title: _title,
                confirmButtonColor: "#2196F3"
            });
        });
    });

    /**
     * 显示授权框
     * @param _id
     */
    function showAuthModal(_id) {
        _authModal.find('[name="storeId"]').val(_id);
        _authSelect.empty().select2('destroy');
        $.get('${source}/admin/pdd/store/getAuthorizedUsers',{storeId:_id}, function(ret) {
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
                _options += '<option value="' + _o.id + '"' + (_o.selected ? 'selected' : '') + '>' + _o.text + '</option>';
            }

            _authSelect.append(_options);
            _authSelect.select2({
                maximumSelectionSize: 20,
                formatSelectionCssClass: function (data, container) { return "bg-slate-600"; },
                formatSelectionTooBig:function(a) {
                    return "每个店铺最多授权" + a + "个用户！"
                }
            });
        });

        _authModal.modal('show');
    }
</script>
</html>
