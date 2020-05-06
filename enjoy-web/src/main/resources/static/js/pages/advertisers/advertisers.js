$(function () {
    var dataSource = {};
    var wxAuthStatus = {};
    wxAuthStatus["1"]="已绑定";
    wxAuthStatus["2"]="未绑定";
    dataSource.wxAuthStatus = wxAuthStatus;
    var authStatus  = {};
    authStatus["2"] = "未授权";
    authStatus["1"] = "已授权";
    dataSource.authStatus = authStatus;
    initConfigData(dataSource);
    $(".table").tableBtn({
        pagination: {
            currentPage: currentPage,
            totalPage: totalPage,
            form: "#queryForm"
        }
    });
    $(".dictionary").formatConfigData();
    $("#addAdvertiser").click(function(){
        loadMenuChild(getMenuURL() + "/add", "新增客户");
    });

    $("#queryFormBtn").click(function () {
        $("#queryForm").query();
    });
});
function changeTable(val){
    if (val == 'tab1') {
        $(".tab1").addClass("active");
        $(".tab2").removeClass("active");
        $("#tab1").show();
        $("#tab1_head").show();
        $("#tab2").hide();
    } else {
        $(".tab2").addClass("active");
        $(".tab1").removeClass("active");
        $("#tab2").show();
        $("#tab1").hide();
        $("#tab1_head").hide();
    }
}

var  edit=function(id){
    loadMenuChild(getMenuURL() + "/edit/"+id, "编辑");
}

var wxbind=function(aId){
    if (!verifyLogin()) {
        return;
    }
    var newWindow = window.open();
    var params = {aId:aId};
    $.ajax(getMenuURL()+'/bindWx', {
        data: params
    }).done(function (data) {
        if (data.status == 1) {
            newWindow.location.href = data.data;
        }else{
            $.alertWarning(data.msg);
        }
    }).fail(function (xhr, status) {
        $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
    })
}

var wxdetail=function(wxAppId){
    if (!verifyLogin()) {
        return;
    }
    var params = {wxAppId:wxAppId};
    $.ajax(getMenuURL()+'/wxDetail', {
        data: params
    }).done(function (data) {
        if (data.status == 1) {
            $("#accountId").text(data.data.accountId);
            $("#wechatAccountId").text(data.data.wechatAccountId);
            $("#wechatAccountName").text(data.data.wechatAccountName);
            $("#systemStatus").text(data.data.systemStatus);
            $("#industryName").text(data.data.industryName);
            $("#contactPerson").text(data.data.contactPerson);
            $("#contactPersonTelephone").text(data.data.contactPersonTelephone);
            $('#wx_detail_modal_default').modal('toggle');
        }else{
            $.alertWarning(data.msg);
        }
    }).fail(function (xhr, status) {
        $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
    })

}
var syncAdvInfo = function(aId){
    if (!verifyLogin()) {
        return;
    }
    var params = {aId:aId};
    $.ajax(getMenuURL()+'/syncAdvInfo', {
        data: params
    }).done(function (data) {
        if (data.status == 1) {
            $.alertSuccess(data.msg);
        }else{
            $.alertWarning(data.msg);
        }
    }).fail(function (xhr, status) {
        $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
    })
}

