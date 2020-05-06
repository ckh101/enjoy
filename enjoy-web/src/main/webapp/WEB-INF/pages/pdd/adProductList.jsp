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
    var pstatus = {
        "1" : "待提交",
        "2" : "待投放",
        "3" : "投放中",
        "4" : "投放完成"
    };
	systemStatus["ALLOW"] = "正常";
	systemStatus["FORBIDDEN"] = "禁用";
	systemStatus["DEL"] = "删除";
	accountType["ADVERTISER"] = "广告主";
	accountType["SYSTEM"] = "系统";
	dataSource.systemStatus = systemStatus;
	dataSource.accountType = accountType;
	dataSource.pstatus = pstatus;
	initConfigData(dataSource);

    // 填充参数值到表单
    var _reqParams = ${el:toJsonString(param)};
    var _condition = [{
        name: "search_EQ_adProductId",
        label: "产品ID"
    }, {
        name: "search_LIKE_name",
        label: "产品名称"
    }, {
        name: "search_EQ_status",
        label: "状态",
        type: "select",
        dataSource: 'pstatus'
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

    window.refreshPage = function(_page) {
        $('#queryForm').query(_page || _reqParams.pageNumber || 1);
    }

    var _statusData = ${el:toJsonString(statusCnt)} || {};
    $('#statusCnt').find('[data-key]').each(function() {
        var _key = this.getAttribute('data-key');
        if (_statusData[_key]) {
            this.innerHTML = _statusData[_key];
        } else {
            this.innerHTML = 0;
        }
    });
});
</script>
<style>
    .opBtn {
        margin-right: 8px;
    }
    #addProductModal .validation-valid-label:before {
        content: "\e9ba";
    }
    #addProductModal .validation-valid-label {
        color: #999999;
    }
    #addProductModal .downloadLabel:before {
        content: none;
    }
    #addProductModal .downloadLabel {
        color: #009fff;
        content: none;
        margin-bottom: 10px;
        padding-left: 0;
    }
    #addProductModal .validation-valid-label u{
        text-decoration: none;
        border-bottom: #999999 1px dashed;
        cursor: pointer;
        color:#666666;
    }
    .downloadLabel .download{
        cursor: pointer;
    }
    .downloadLabel .del{
        margin-left: 30px;
        cursor: pointer;
    }
</style>
</head>
<body>
    <c:if test="${not empty statusCnt}">
    <div class="panel panel-flat">
        <div class="container-fluid">
            <div id="statusCnt" class="row text-center flex flex-aic">
                <div class="col-xs-3 col-sm-3">
                    <div style="height: 116px;" class="flex flex-center fs-20">
                        <div>
                            <i class="icon-star-empty3" style="font-size: 64px;color: #1976d2;"></i>
                        </div>
                        <div style="margin-left: 12px;">
                            <div class="text-primary fw-bold no-margin" data-key="2"></div>
                            <span class="text-grey-800 text-size-small">待投放</span>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3 col-sm-3">
                    <div style="height: 116px;" class="flex flex-center fs-20">
                        <div>
                            <i class="icon-star-half" style="font-size: 64px;color: #1976d2;"></i>
                        </div>
                        <div style="margin-left: 12px;">
                            <div class="text-primary fw-bold no-margin" data-key="3"></div>
                            <span class="text-grey-800 text-size-small">投放中</span>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3 col-sm-3">
                    <div style="height: 116px;" class="flex flex-center fs-20">
                        <div>
                            <i class="icon-star-full2" style="font-size: 64px;color: #1976d2;"></i>
                        </div>
                        <div style="margin-left: 12px;">
                            <div class="text-primary fw-bold no-margin" data-key="4"></div>
                            <span class="text-grey-800 text-size-small">投放完成</span>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3 col-sm-3">
                    <div style="height: 116px;" class="flex flex-center fs-20">
                        <shiro:hasPermission name="/admin/pdd/adProduct/add">
                        <button type="button" class="btn bg-primary-700" onclick="addItem()"><i class="icon-add"></i> 新增投放产品</button>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </c:if>
	<!-- Basic table -->
	<div class="panel panel-flat">
		<div class="panel-heading">
			<form class="form-inline" id="queryForm" method="post" action="/admin/pdd/adProduct/list?storeId=${param.storeId}" datatype="html"></form>
		</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>#产品ID</th>
                <th>产品名称</th>
                <th>投放链接</th>
                <th>人群包</th>
                <th>投放金额(元)</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="obj" varStatus="status">
                <tr class="adproduct" id="apItem-${obj.adProductId}">
                    <td>${obj.adProductId}</td>
                    <td>${obj.name}</td>
                    <td>${obj.targetUrl}</td>
                    <td><c:if test="${not empty obj.crowdPackUrl}"><a href="obj.crowdPackUrl" target="_blank">下载</a></c:if></td>
                    <td>${not empty obj.amount ? obj.amount/100 : '未投放'}</td>
                    <td data-name="status" data-value="${obj.status}">${obj.status}</td>
                    <td><fmt:formatDate value="${obj.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td data-name="operation" data-key="${obj.storeId}"></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
	</div>
	<!-- /basic table -->

    <!-- 产品表单 -->
    <div id="addProductModal" class="modal fade">
        <div class="modal-dialog" style="width:640px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title">弹窗标题</h5>
                </div>
                <form class="form-horizontal">
                    <input type="hidden" name="storeId" />
                    <input type="hidden" name="adProductId" />
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-2">产品名称</label>
                            <div class="col-sm-8">
                                <input data-role="editItem" name="name" type="text" class="form-control" placeholder="请输入产品名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-2">投放链接</label>
                            <div class="col-sm-8">
                                <input data-role="editItem" name="targetUrl" type="text" class="form-control" placeholder="请输入投放链接">
                                <label class="validation-valid-label">仅允许yangkeduo.com或其子域的链接</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div>
                                <div class="col-sm-1"></div>
                                <label class="control-label col-sm-2">人群包</label>
                                <div class="col-sm-5">
                                    <input data-role="editItem" data-name="crowdPackUrl" id="crowdPack" type="file" accept="applicatgion/zip,txt/plain,.txt,.zip" class="file-styled" placeholder="请选择人群包">
                                    <label class="validation-valid-label downloadLabel"><i class="icon-download4"></i> <span class="download">下载文件</span>  <span class="del text-danger">删除</span></label>
                                    <input data-role="editItem" data-name="crowdPackUrl" name="crowdPackUrl" type="hidden" class="form-control" />
                                </div>
                                <div class="col-sm-3">
                                    <button data-role="editItem" data-name="crowdPackUrl" type="button" class="opBtn btn bg-slate-600" onclick="uploadCrowdPack()"><i class="icon-cloud-upload position-left"></i>上传到广点通</button>
                                </div>
                            </div>
                            <div style="clear: both;">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-8">
                                    <label class="validation-valid-label"><u data-toggle="modal" data-target="#crowdPackTip">查看帮助</u> txt文件或zip压缩文件，不超过10MB。
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" data-role="postItem">
                            <div class="col-sm-1"></div>
                            <label class="control-label col-sm-2">投放金额</label>
                            <div class="col-sm-6">
                                <input name="amount" type="text" class="form-control" placeholder="金额需>1000且整百，元">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                        <button id="submitForm" type="submit" class="btn btn-primary">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /产品表单 -->

    <!-- 人群包提示 -->
    <div id="crowdPackTip" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <h6 class="text-semibold">人群包说明：</h6>
                    <p>人群包即为对商品感兴趣或者购买过该商品的手机号码包。可留空。</p>
                    <p>建议上传 <span class="text-warning">不少于2000个</span> 手机号码，人群包数据越多越有利于微信广告平台扩展精准投放人群，投放效果更佳。</p>
                    <hr>
                    <h6 class="text-semibold">文件格式：</h6>
                    <p>人群包请使用txt文件，每个手机号一行。多个txt文件可用zip格式压缩。</p>
                    <img width="420" src="https://img.bazhuay.com/yptm/images/2019/10/15705029778103532.jpg"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <!-- /人群包提示 -->
</body>
<script>
    function rowFormat(_id) {
        var _parent = $('table.table').find('.adproduct');
        if (_id) {
            _parent = _parent.filter('#apItem-' + _id);
        }

        _parent.each(function() {
            var _this = $(this);
            var _id = _this.attr('id').substring(7);
            var _statusEle = _this.find('[data-name="status"]');
            var _statusVal = _statusEle.attr('data-value');

            // 状态值转中文
            var _text;
            var _label;
            var _btns = [];
            var _edit = '<button type="button" class="opBtn btn btn-xs bg-info-600" onclick="editItem(\'' + _id + '\')"><i class="icon-pencil7 position-left"></i>编辑</button>';
            var _cancel, _posting, _complete;

            <shiro:hasPermission name="/admin/pdd/adProduct/updatePlaceStatus">
            _cancel = '<button type="button" class="opBtn btn btn-xs bg-grey-700" onclick="cancelAd(\'' + _id + '\')"><i class="icon-move-left position-left"></i>撤回投放</button>';
            _posting = '<button type="button" class="opBtn btn btn-xs bg-teal-400" onclick="postingAd(\'' + _id + '\')"><i class="icon-station position-left"></i>开始投放</button>';
            _complete = '<button type="button" class="opBtn btn btn-xs bg-success-600" onclick="completeAd(\'' + _id + '\')"><i class="icon-checkmark-circle position-left"></i>投放完成</button>';
            </shiro:hasPermission>
            switch (1 * _statusVal) {
                case 1:
                    _text = '待提交';
                    _label = 'default';
                    _btns.push(_edit);
                    _btns.push('<button type="button" class="opBtn btn btn-xs bg-primary-600" onclick="postItem(\'' + _id + '\')"><i class="icon-paperplane position-left"></i>提交投放</button>');
                    break;
                case 2:
                    _label = 'info';
                    _text = '待投放';
                    _btns.push(_edit);
                    _cancel && _btns.push(_cancel);
                    _posting && _btns.push(_posting);
                    break;
                case 3:
                    _label = 'primary';
                    _text = '投放中';
                    _btns.push(_edit);
                    _complete && _btns.push(_complete);
                    break;
                case 4:
                    _label = 'success';
                    _text = '投放完成';
                    break;
            }

            _statusEle.html('<span class="label label-' + _label + '">' + _text + '</span>');

            var _opEle = _this.find('[data-name="operation"]');
            for (var i in _btns) {
                _opEle.append(_btns[i]);
            }

        })
    }

    rowFormat();

    var storeId = '${param.storeId}';
    var _modal = $("#addProductModal");
    function addItem() {
        _modal.find('[data-role="editItem"]').prop('readonly', false);
        _modal.find('[data-role="postItem"]').hide();
        _modal.find('.modal-title').text('新增投放产品');

        var _form = _modal.find('form');
        _form.find('input[type="hidden"]').val('');
        _form.attr('action', '${source}/admin/pdd/adProduct/add')[0].reset();

        _modal.modal("show");
        _modal.find('[name="storeId"]').val(storeId);
        $('#submitForm').text('保存');

        initDownloadBtn();
    }

    function editItem(_id, isPost) {
        if (isPost) { // 弹框是否做投放用
            _modal.find('[data-role="editItem"]').prop('disabled', true); // 弹出投放框
            _modal.find('[data-role="postItem"]').show();
        } else {
            _modal.find('[data-role="editItem"]').prop('disabled', false); // 弹出编辑框
            _modal.find('[data-role="postItem"]').hide();
        }

        _modal.find('.modal-title').text(isPost ? '投放金额设置' : '编辑投放产品');

        var _path = isPost ? 'place' : 'update';

        var _form = _modal.find('form');
        _form.find('input[type="hidden"]').val('');
        _form.attr('action', '${source}/admin/pdd/adProduct/' + _path)[0].reset();

        _modal.modal("show");
        _modal.find('[name="adProductId"]').val(_id);
        $('#submitForm').text(isPost ? '提交投放' : '保存');

        $.get('admin/pdd/adProduct/detail?adProductId=' + _id, function(ret) {
            if (ret.status == 0) {
                ret.data.amount = accDiv(ret.data.amount || 0, 100);
                fillFormData(_modal.find('form'), ret.data);
                var _prodStatus = ret.data.status; // 产品状态
                if (_prodStatus > 1) {
                    _modal.find('[data-role="editItem"]').not('[data-name="crowdPackUrl"]').prop('disabled', true);
                }
                initDownloadBtn();
            } else {
                _modal.modal("hide");
                alert(ret.msg);
            }
        });
    }

    function postItem(_id) {
        editItem(_id, true);
    }

    function fillFormData($formEL, obj) {
        $.each(obj, function(index, item) {
            $formEL.find("[name=" + index + "]").val(item);
        });
    }

    $('#addProductModal form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        _params['amount'] = accMul(_params['amount'] || 0, 100);

        evt.preventDefault();

        var _post = function() {
            $.post(_form.attr('action'), _params, function(_data) {
                var _title = '操作成功！';
                if (_data.status != 0) {
                    _title = _data.msg;
                    swal.fire({
                        title: _title,
                        confirmButtonColor: "#2196F3"
                    });
                } else {
                    $('#addProductModal').modal('hide');

                    setTimeout(function() {
                        if (_params['adProductId']) {
                            refreshPage();
                        } else {
                            refreshPage(1);
                        }
                    }, 300);
                }
            });
        };

        if ($('#submitForm').text() == '提交投放') {
            var _amountYuan = accDiv(_params['amount'], 100);
            confirmReq({
                title: "确定设置为投放吗?",
                text: "提交后将冻结可用余额" + _amountYuan + "元",
                type: 'question'
            }, _post);
        } else {
            _post();
        }
    });

    function confirmReq(_opt, _cb) {
        _opt = $.extend({}, {
            title: "确认执行本次操作吗?",
            text: "",
            type: 'question',
        }, _opt);
        Swal.fire({
            title: _opt.title,
            text: _opt.text,
            type: _opt.type,
            showCancelButton: true,
            confirmButtonText: "确认",
            cancelButtonText: "取消"
        }).then((result) => {
            result.value && _cb();
         });
    }

    <shiro:hasPermission name="/admin/pdd/adProduct/updatePlaceStatus">
    /**
     * 投放中
     * @param _id
     */
    function postingAd(_id) {
        confirmReq({}, function() {
            $.post('admin/pdd/adProduct/updatePlaceStatus', {adProductId:_id,status:3}, function(_data) {
                var _title = '操作成功！';
                if (_data.status != 0) {
                    _title = _data.msg;
                    swal.fire({
                        title: _title,
                        confirmButtonColor: "#2196F3"
                    });
                } else {
                    setTimeout(function() {
                        refreshPage();
                    }, 300);
                }
            });
        });
    }

    /**
     * 撤回投放
     * @param _id
     */
    function cancelAd(_id) {
        confirmReq({
            text : '撤回后将冻结资金将转回可用余额'
        }, function() {
            $.post('admin/pdd/adProduct/cancelPlace', {adProductId:_id,status:3}, function(_data) {
                var _title = '操作成功！';
                if (_data.status != 0) {
                    _title = _data.msg;
                    swal.fire({
                        title: _title,
                        confirmButtonColor: "#2196F3"
                    });
                } else {
                    setTimeout(function() {
                        refreshPage();
                    }, 300);
                }
            });
        });
    }

    /**
     * 投放完成
     * @param _id
     */
    function completeAd(_id) {
        confirmReq({}, function() {
            $.post('admin/pdd/adProduct/updatePlaceStatus', {adProductId:_id,status:4}, function(_data) {
                var _title = '操作成功！';
                if (_data.status != 0) {
                    _title = _data.msg;
                    swal.fire({
                        title: _title,
                        confirmButtonColor: "#2196F3"
                    });
                } else {
                    setTimeout(function() {
                        refreshPage();
                    }, 300);
                }
            });
        });
    }
    </shiro:hasPermission>

    var xhrOnProgress = function (fun) {
        xhrOnProgress.onprogress = fun; //绑定监听
        //使用闭包实现监听绑
        return function () {
            //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
            var xhr = $.ajaxSettings.xhr();
            //判断监听函数是否为函数
            if (typeof xhrOnProgress.onprogress !== 'function')
                return xhr;
            //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
            if (xhrOnProgress.onprogress && xhr.upload) {
                xhr.upload.onprogress = xhrOnProgress.onprogress;
            }
            return xhr;
        }
    }

    function uploadCrowdPack() {
        var fileObj = document.getElementById("crowdPack").files[0]; // js 获取文件对象
        if (!fileObj) {
            alert('请先选择文件');
            return;
        }
        var formFile = new FormData();

        var fileSuffix = fileObj.name.substring(fileObj.name.lastIndexOf('.'));
        if (fileSuffix != '.txt' && fileSuffix != '.zip') {
            alert('请上传txt文件或zip压缩文件');
            return;
        }

        var overLimit = fileObj.size/1024/1024 > 10; // 大于10兆
        if (overLimit) {
            alert('文件大小超过10MB');
            return;
        }
        console.log(fileObj.name + '--' + fileObj.size);

        formFile.append("file", fileObj); //加入文件对象

        var data = formFile;
        $.ajax({
            url: 'admin/pdd/adProduct/uploadCrowdPack',
            data: data,
            type: "post",
            dataType: "json",
            cache: false, // 上传文件无需缓存
            processData: false, // 用于对data参数进行序列化处理 这里必须false
            contentType: false, // 必须
            xhr: xhrOnProgress(function (e) {
                var percent = e.loaded / e.total;//文件上传百分比
                console.log(percent);
            }),
            success: function (ret) {
                var _title = '上传成功';
                if (ret.status != 0) {
                    _title = ret.msg;
                } else {
                    _modal.find('[name="crowdPackUrl"]').val(ret.data.url);
                }

                swal.fire({
                    title: _title,
                    confirmButtonColor: "#2196F3"
                });

                initDownloadBtn();
            },
        })
    }

    $(".file-styled").uniform({
        wrapperClass: 'bg-slate-400',
        fileDefaultHtml: '请选择人群包',
        fileButtonHtml: '<i class="icon-file-zip"></i>'
    });

    // 下载文件点击操作
    var _downloadBtn = $('.download');
    var _downloadLabel = $('.downloadLabel');
    function initDownloadBtn() {
        var _url = _downloadLabel.siblings('[name="crowdPackUrl"]').val();
        if (_url) {
            _downloadLabel.show();
            $('#uniform-crowdPack').hide();
            _modal.find('[data-name="crowdPackUrl"]').hide();
        } else {
            _downloadLabel.hide();
            $('#uniform-crowdPack').show().find('.filename').text('');
            _modal.find('[data-name="crowdPackUrl"]').show();
        }
    }

    _downloadBtn.click(function() {
        window.open(_downloadLabel.siblings('[name="crowdPackUrl"]').val(), '_blank');
    });

    $('.downloadLabel>.del').click(function() {
        confirmReq({
            title : '确定删除该人群包吗？',
        }, function() {
            _modal.find('[name="crowdPackUrl"]').val('');
            initDownloadBtn();
        })
    });

</script>
</html>
