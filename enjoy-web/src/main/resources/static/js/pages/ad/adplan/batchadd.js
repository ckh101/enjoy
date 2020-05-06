var adNums = 1;
$(function () {
    // Initialize multiple switches
    if (Array.prototype.forEach) {
        var elems = Array.prototype.slice.call(document.querySelectorAll('.switchery'));
        elems.forEach(function(html) {
            var switchery = new Switchery(html, { color: '#2196F3' });
        });
    } else {
        var elems = document.querySelectorAll('.switchery');
        for (var i = 0; i < elems.length; i++) {
            var switchery = new Switchery(elems[i], { color: '#2196F3' });
        }
    }

    $(".styled, .multiselect-container input").uniform({
        radioClass: 'choice'
    });

    // Success
    $(".control-primary").uniform({
        radioClass: 'choice',
        wrapperClass: 'border-primary text-primary'
    });
    $('.daterange-basic').daterangepicker({
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        minDate:new Date(),
        locale:{
            format: 'YYYY/MM/DD',
            applyLabel: "确定",
            cancelLabel: "取消",
            startLabel: "起始时间",
            endLabel: "结束时间",
            daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
            monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    });

});
function addad(){
    if(adNums > 10){
        $.alertWarning("每次最多能批量创建10个广告");
        return;
    }
    var first_date = $("#first_date").val().split("-");
    adNums++;
    var html = "<div class=\"form-group col-md-12 bottom-border ad_div\">"+$("#ad_template").html()+"</div>";
    $(html).appendTo("#ads_div");
    $(".ad_div").last().find('.daterange-basic').daterangepicker({
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        startDate:first_date[0],
        endDate:first_date[1],
        locale:{
            format: 'YYYY/MM/DD',
            applyLabel: "确定",
            cancelLabel: "取消",
            startLabel: "起始时间",
            endLabel: "结束时间",
            daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
            monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    });
}
function subad(obj){
    adNums--;
    $(obj).parent().parent().parent().parent().parent().remove();
}
function batchadd(){
    var reg = new RegExp( '/' , "g" );
    var adId = $("#adId").val();
    var ads = [];
    $(".ad_div").each(function(){
        var date = $(this).find(".daterange-basic").val().split("-");
        var beginDate = date[0].replace(reg,"-");
        var endDate = date[1].replace(reg, "-");
        var dateMap = {};
        dateMap["beginDate"] = beginDate.trim();
        dateMap["endDate"] = endDate.trim();
        ads.push(dateMap);
    });
    var params = {};
    params["adId"] = adId;
    params["ads"] = JSON.stringify(ads);
    params["addType"] = $("input[name='add_type']:checked").val();
    $.ajax({url:"/smartad-web/admin/ad/admanager/ad/adplan/batchsave/"+aId,
        data:params,
        type:"POST",
        success:function(data){
            if (data.status == 1) {
                $.alertSuccess(data.msg);
            }else{
                $.alertWarning(data.msg);
            }
        },
        error:function(xhr, status){
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
        }
    });
}