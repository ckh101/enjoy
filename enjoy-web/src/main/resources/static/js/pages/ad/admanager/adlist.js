$(function () {
    $(".control-success").uniform({
        radioClass: 'choice',
        wrapperClass: 'border-success-600 text-success-800'
    });
    $('.table-togglable').footable();

    // Styled checkboxes, radios
    $('.styled, .multiselect-container input').uniform({
        radioClass: 'choice',
        wrapperClass: 'border-primary text-primary'
    });
    var $dataTable = $('#dataTable');
    $dataTable.on('click', '.ad_check', function() {
        var _$this = $(this);
        var _clickCheckAll = _$this.attr('data-role') == 'check_all';
        var _checked = _$this.prop("checked");

        $.uniform.update(_$this);
        var _$checkList = $dataTable.find('.ad_check:not(:first)');
        var _checkListLength = _$checkList.length;
        if (_checkListLength == 0) {
            return;
        }

        if (_clickCheckAll) { // 点击了全选
            $.uniform.update(_$checkList.prop("checked", _checked));
        }

        var _uncheckedSize = _$checkList.filter(':not(:checked)').length; // 未勾选的checkbox个数
        $.uniform.update($dataTable.find('.ad_check[data-role="check_all"]').prop("checked", _uncheckedSize == 0));
        updateBatchBtn(_checkListLength > _uncheckedSize);
    });

    $(".table").tableBtn({
        pagination: {
            currentPage: currentPage,
            totalPage: totalPage,
            form: "#queryForm"
        }
    });
    $('.pickadate').pickadate({format: 'yyyy/mm/dd'});
    var dataSource = {};
    var configuredStatus = {"AD_STATUS_NORMAL":"启用","AD_STATUS_SUSPEND":"暂停","AD_STATUS_PENDING":"创建中"};
    var systemStatus = {"AD_STATUS_NORMAL":"有效",
        "AD_STATUS_PENDING":"待审核",
        "AD_STATUS_DENIED":{text:"审核不通过",color:'red'},
        "AD_STATUS_DELETED":"已经删除"};
    dataSource.configuredStatus = configuredStatus;
    dataSource.systemStatus = systemStatus;
    dataSource.adsense = adsense;
    initConfigData(dataSource);

    $("#queryFormBtn").click(function () {
        $("#queryForm").query();
    });
    $(".dictionary").formatConfigData();

    $(".styled, .multiselect-container input").uniform({
        radioClass: 'choice'
    });

    $('table').on('click', '[data-role="editAlarmConfig"]', function() {
        var _ids = this.getAttribute('data-id').split(',');
        editAlarmConfig(_ids[0], _ids[1]);
    });

    $('#adMonitorConfigModal form').submit(function(evt) {
        var _form = $(this);
        var _paramArray = _form.serializeArray();
        var _params = {};
        for (var i in _paramArray) {
            var _item = _paramArray[i];
            _params[_item.name] = _item.value;
        }

        _params['dailyCostThreshold'] = accMul(_params['dailyCostThreshold'], 100);
        _params['dailyBudgetThreshold'] = accDiv(_params['dailyBudgetThreshold'], 100);
        _params['dailyFirstOrderAlarm'] = $('[name="dailyFirstOrderAlarm"]').prop('checked') ? 1 : 0;
        _params['roiAlarmAutoPause'] = $('[name="roiAlarmAutoPause"]').prop('checked') ? 1 : 0;

        $.post(_form.attr('action'), _params, function(_data) {
            var _title = '操作成功！';
            if (_data.status != 0) {
                _title = _data.msg;
            } else {
                $('#adMonitorConfigModal').modal('hide');
            }

            swal.fire({
                title: _title,
                confirmButtonColor: "#2196F3"
            });
        });

        evt.preventDefault();
    });

    // 提示
    $('[data-popup=popover-solid]').popover({
        template: '<div class="popover bg-teal"><div class="arrow"></div><div class="popover-content"></div></div>'
    });

    // 审核原因弹窗
    $('[data-value=AD_STATUS_DENIED]').popover({
        title : '审核不通过原因',
        placement : 'top',
        trigger : 'hover'
    });

    // 创意详情
    var $creativeDetail = $('.creative-detail');
    $creativeDetail.each(function() {
        var _content = this.getAttribute('data-content');
        try {
            _content = JSON.stringify(JSON.parse(_content), null, "\t");
        } catch (e) {
            // do nothing
        }
        this.setAttribute('data-content', _content);
    });
    $creativeDetail.popover({
        title : '创意详细',
        placement : 'left',
        trigger : 'click hover',
        template : '<div class="popover" style="max-width: none;" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><pre class="popover-content language-javascript content-group"><code></code></pre></div>',

    });
});

function updateBatchBtn(_switchOn) {
    var _$batchBtn = $('.batchBtn');
    if (_switchOn) {
        _$batchBtn.prop('disabled', false);
    } else {
        _$batchBtn.prop('disabled', true);
    }

}

var modalInit = 0;
function editAlarmConfig(advId, adId) {
    var _modalEle = $('#adMonitorConfigModal');
    var _form = $('#adMonitorConfigModal form');
    var _block = _modalEle.find('.modal-content');
    _form[0].reset();

    var _eventId = new Date().getTime();
    _modalEle.data('eventId', _eventId);
    $.get(context.contextPath + 'admin/ad/admanager/admonitor/get/' + advId, {'adId':adId}, function(data) {
        if (_eventId != _modalEle.data('eventId')) {
            return;
        }

        var _obj = data.data;
        if (!_obj) {
            swal.fire({
                title: data.msg || '拉取数据出错！',
                confirmButtonColor: "#2196F3"
            });
            _modalEle.modal('hide');
        } else {
            _modalEle.modal('show');
        }

        for (var i in _obj) {
            var _field = _form.find('[name="' + i + '"]');
            if (!_field[0]) {
                continue;
            }

            var _val = _obj[i];
            if (_field.attr('type') == 'checkbox') {
                checkUniformCheckbox(_field, !!_val);
            } else if (i == 'dailyBudgetThreshold'){
                _field.val(accMul(_val, 100)); // 小数转百分比
            } else if (i == 'dailyCostThreshold'){
                _field.val(accDiv(_val, 100)); // 分转元
            } else {
                _field.val(_val == null ? '' : _val);
            }
        }

        _modalEle.data('eventId', null);
    });

    ++modalInit == 1 && _modalEle.on('shown.bs.modal', function() {
        if (!_modalEle.data('eventId')) {
            return;
        }
        _block.block({
            message: '<i class="icon-spinner4 spinner"></i>',
            overlayCSS: {
                backgroundColor: '#fff',
                opacity: 0.8,
                cursor: 'wait'
            },
            css: {
                border: 0,
                padding: 0,
                backgroundColor: 'none'
            }
        });

        function unblock() {
            setTimeout(function() {
                if (!_modalEle.data('eventId')) {
                    _block.unblock();
                } else {
                    unblock();
                }
            }, 200);
        }

        unblock();
    });

    setTimeout(function() {
        if (_modalEle.data('eventId')) {
            _modalEle.modal('show');
        }
    }, 300);
}

function checkUniformCheckbox(_ele, _isChecked) {
    if (_isChecked && !_ele.parent().hasClass('checked')) {
        _ele.click();
        return;
    }

    if (!_isChecked && _ele.parent().hasClass('checked')) {
        _ele.click();
        return;
    }
}

/**
 * 除法函数，用来得到精确的除法结果
 * 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function accDiv(arg1, arg2) {
    var t1 = 0, t2 = 0, r1, r2;
    try { t1 = arg1.toString().split(".")[1].length } catch (e) { }
    try { t2 = arg2.toString().split(".")[1].length } catch (e) { }
    with (Math) {
        r1 = Number(arg1.toString().replace(".", ""));
        r2 = Number(arg2.toString().replace(".", ""));
        return (r1 / r2) * pow(10, t2 - t1);
    }
}

/**
 * 乘法函数，用来得到精确的乘法结果
 * 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function accMul(arg1, arg2) {
    var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
    try { m += s1.split(".")[1].length } catch (e) { }
    try { m += s2.split(".")[1].length } catch (e) { }
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

/**
 * 限制数值为两位小数，用于表单
 * @param obj
 */
function num(obj) {
    obj.value = obj.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
    obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字
    obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
    obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //控制可输入的小数
}

function preBatchcopy() {
    $("#accountId").val("");
    $("#modal_theme_success").modal('show');
}

var taskmonitor = $.taskmonitor({queryUrl : '/enjoy-web/admin/ad/task/batch/queryTaskState'}).active();
function batchcopy(){
    var _ids = [];
    $('input[name="ad_check"]:checked').each(function(){
        var _val = $(this).val();
        if (parseInt(_val)) {
            _ids.push($(this).val());
        }
    });

    var accountId = $("#accountId").val();
    if(accountId == ""){
        $.alertWarning("广告主账号ID不能为空！")
        return;
    }
    var params = {};
    var campaignType = $("input[name='add_type']:checked").val();
    if(campaignType == "old_campaign"){
        var campaigns = $("#campaigns").val();
        if(campaigns==""){
            $.alertWarning("请选择广告计划！")
            return;
        }
        params["campaignId"] = campaigns;
    }
    params["campaignType"] = campaignType;
    params["accountId"] = accountId;
    params["ids"] = JSON.stringify(_ids);

    params["taskType"] = 'adCopy';
    params["idList"] = _ids;
    // $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/adplan/batchcopy/"+aId,
    //     data:params,
    //     type:"POST",
    //     success:function(data){
    //         if (data.status == 1) {
    //             $("#modal_theme_success").modal('hide');
    //             $.alertSuccess(data.msg);
    //         }else{
    //             $.alertWarning(data.msg);
    //         }
    //     },
    //     error:function(xhr, status){
    //         $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
    //     }
    // });

    $.ajax({
        url: "/enjoy-web/admin/ad/task/batch/add/" + aId,
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(params),
        success: function (data) {
            if (data.status == 0) {
                $("#modal_theme_success").modal('hide');
                taskmonitor.addTask(params.taskType, data.data.taskId);
            } else {
                $.alertWarning(data.msg);
            }
        },
        error: function (xhr, status) {
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
        }
    });

}

function batchdel(){
    var _ids = [];
    $('input[name="ad_check"]:checked').each(function(){
        _ids.push($(this).val());
    });
    if(_ids.length <= 0){
        $.alertWarning("请先选择需要删除的广告!");
        return;
    }
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
            var params = {};
            params["ids"]=JSON.stringify(_ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/adplan/batchdel/"+aId,
                data:params,
                type:"POST",
                success:function(data){
                    if (data.status == 1) {
                        $("#queryForm").query();
                        $.alertSuccess();
                    }else{
                        $.alertWarning(data.msg);
                    }
                },
                error:function(xhr, status){
                    $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                }
            });
        }
    });

}
function batchsub(){
    var _ids = [];
    $('input[name="ad_check"]:checked').each(function(){
        _ids.push($(this).val());
    });
    if(_ids.length <= 0){
        $.alertWarning("请先选择需要暂停的广告!");
        return;
    }
    Swal.fire({
        title: '确定暂停所选的广告?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 暂停",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            var params = {};
            params["ids"]=JSON.stringify(_ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/adplan/batchsub/"+aId,
                data:params,
                type:"POST",
                success:function(data){
                    if (data.status == 1) {
                        $.alertSuccess();
                        $("#queryForm").query();
                    }else{
                        $.alertWarning(data.msg);
                    }
                },
                error:function(xhr, status){
                    $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                }
            });
        }
    });
}
function batchon(){
    var _ids = [];
    $('input[name="ad_check"]:checked').each(function(){
        _ids.push($(this).val());
    });
    if(_ids.length <= 0){
        $.alertWarning("请先选择需要启动的广告!");
        return;
    }
    Swal.fire({
        title: '确定启动所选的广告?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 启动",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            var params = {};
            params["ids"]=JSON.stringify(_ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/adplan/batchon/"+aId,
                data:params,
                type:"POST",
                success:function(data){
                    if (data.status == 1) {
                        $.alertSuccess();
                        $("#queryForm").query();
                    }else{
                        $.alertWarning(data.msg);
                    }
                },
                error:function(xhr, status){
                    $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                }
            });
        }
    });
}
function changeCampaignTypeForBatchCopy(){
    var campaignType = $("input[name='add_type']:checked").val();
    if(campaignType == "old_campaign"){
        var accountId = $("#accountId").val();
        if(accountId == ""){
            $.alertWarning("广告主账号ID不能为空!");
            return;
        }
        $("#campaign_div").show();
        $.ajax({url:"/enjoy-web/admin/ad/admanager/campaign/list_by_account/"+aId+"/"+accountId,
            type:"POST",
            success:function(data){
                if (data.status == 1) {
                    $("#campaigns").html("");
                    $("#campaigns").append("<option value=''>请选择计划</option>");
                    for(var i=0;i < data.data.length;i++){
                        $("#campaigns").append("<option value='"+data.data[i].id+"'>"+data.data[i].id+"-"+data.data[i].campaignName+"</option>");
                    }
                    $('#campaigns').selectpicker('refresh');
                    $('#campaigns').selectpicker('render');

                }else{
                    $.alertWarning();
                }
            },
            error:function(xhr, status){
                $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            }
        });
    }else if(campaignType == "new_campaign"){
        $("#campaign_div").hide();
    }
}
function editName(obj, id){
    var input = $(obj).parent().parent().find(".form-control");
    if(input.attr("readonly")=="readonly"){
        input.removeAttr("readonly");
    }else{
        var params = {};
        input.attr("readonly", "readonly");
        params["adId"]=id;
        params["adName"]=input.val();
        $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/editname/"+aId,
            type:"POST",
            data:params,
            success:function(data){
                if (data.status == 1) {
                   $.alertSuccess("修改成功!");
                }else{
                   $.alertWarning(data.msg);
                }
            },
            error:function(xhr, status){
                $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            }
        });
    }
}