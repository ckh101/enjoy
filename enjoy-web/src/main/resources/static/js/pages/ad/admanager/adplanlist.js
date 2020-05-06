var ids;
$(function () {
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
    
    $('.pickadate').pickadate({format: 'yyyy/mm/dd'});
    var dataSource = {};
    var configuredStatus = {"AD_STATUS_NORMAL":"启用","AD_STATUS_SUSPEND":"暂停","AD_STATUS_PENDING":"创建中"};
    var promotedObjectType = {"PROMOTED_OBJECT_TYPE_ECOMMERCE":"推广我的电商"};
    dataSource.adsense = adsense;
    dataSource.configuredStatus = configuredStatus;
    dataSource.promotedObjectType = promotedObjectType;
    initConfigData(dataSource);
    $(".table").tableBtn({
        pagination: {
            currentPage: currentPage,
            totalPage: totalPage,
            form: "#queryForm"
        }
    });
    $("#queryFormBtn").click(function () {
        $("#queryForm").query();
    });
    $(".dictionary").formatConfigData();
});

function updateBatchBtn(_switchOn) {
    var _$batchBtn = $('.batchBtn');
    if (_switchOn) {
        _$batchBtn.prop('disabled', false);
    } else {
        _$batchBtn.prop('disabled', true);
    }

}

function batchdel(){
    ids = [];
    $('input[name="adplan_check"]:checked').each(function(){
        ids.push($(this).val());
    });
    if(ids.length <= 0){
        $.alertWarning("请先选择需要删除的计划!");
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
            params["ids"]=JSON.stringify(ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/adplan/batchdel/"+aId,
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
    ids = [];
    $('input[class="adplan_check"]:checked').each(function(){
        ids.push($(this).val());
    });
    if(ids.length <= 0){
        $.alertWarning("请先选择需要暂停的计划!");
        return;
    }
    Swal.fire({
        title: '确定暂停所选的计划?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 暂停",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            var params = {};
            params["ids"]=JSON.stringify(ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/adplan/batchsub/"+aId,
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
    ids = [];
    $('input[class="adplan_check"]:checked').each(function(){
        ids.push($(this).val());
    });
    if(ids.length <= 0){
        $.alertWarning("请先选择需要启动的计划!");
        return;
    }
    Swal.fire({
        title: '确定启动所选的计划?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 启动",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            var params = {};
            params["ids"]=JSON.stringify(ids);
            $.ajax({url:"/enjoy-web/admin/ad/admanager/adplan/batchon/"+aId,
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