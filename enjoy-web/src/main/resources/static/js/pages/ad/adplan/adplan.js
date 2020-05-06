var dataMap = {};
var searchKeyWord="";
var adcreativeTemplateId;
var configuredStatus = "AD_STATUS_SUSPEND";
var targeting_status_map = {
    "TARGETING_STATUS_NARROW":"定向过窄",
    "TARGETING_STATUS_SUITABLE":"定向合适",
    "TARGETING_STATUS_WIDE":"定向过宽",
    "TARGETING_STATUS_UNPREDICTABLE":"定向状态无法预估（当前信息过少，不足以判断）"
}

var suggest_targeting_map={
    "age":"年龄",
    "gender":"性别",
    "education":"学历",
    "marital_status":"婚恋状态",
    "working_status":"工作状态",
    "keyword":"关键字",
    "geo_location":"地理位置",
    "user_os":"操作系统",
    "new_devicee":"新设备",
    "device_price":"设备价格",
    "network_type":"联网方式",
    "network_operator":"运营商",
    "network_scene":"上网场景",
    "consumption_status":"消费能力",
    "residential_community_price":"居民社区价格",
    "wechat_ad_behavior":"再营销/排除营销",
    "custom_audience":"定向用户群",
    "excluded_custom_audience":"排除用户群",
    "financial_situation":"资产状态",
    "consumption_type":"消费类型"
}

var adsense_adcreativeTemplate_map = adsenses;

var adcreative_type;

$(function () {
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
    $(".control-success").uniform({
        radioClass: 'choice',
        wrapperClass: 'border-primary text-primary'
    });
    // Override defaults
    $.fn.stepy.defaults.legend = false;
    $.fn.stepy.defaults.transition = 'fade';
    $.fn.stepy.defaults.duration = 150;
    $.fn.stepy.defaults.backLabel = '<i class="icon-arrow-left13 position-left"></i> 上一步';
    $.fn.stepy.defaults.nextLabel = '下一步 <i class="icon-arrow-right14 position-right"></i>';
    $(".stepy-callbacks").stepy({
        titleClick: true,
        nextLabel:"下一步<i class=\"icon-arrow-right14 position-right\"></i>",
        backLabel:"<i class=\"icon-arrow-left13 position-left\"></i>上一步",
        legend:false,
        block: true,
        transition:'fade',
        duration:150,
        next: function(index) {

            if(index == 2){
                if(saveCampaign()){
                    $(".page-content").addClass("col-md-8");
                    var campaignType = $("#campaignType").val();
                    if(campaignType == "CAMPAIGN_TYPE_NORMAL"){
                        $("#CPC_DIV").show();
                        $("#adcreative_title").hide();
                        $("#adcreative_desc").hide();
                        $("#adgroup_daily_budget_div").hide();
                    }else if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"){
                        $("#CPC_DIV").hide();
                        $("#adcreative_title").show();
                        if(adcreative_type == "video"){
                            $("#adcreative_desc").show();
                        }
                        $("#adgroup_daily_budget_div").show();
                    }
                }else{
                    return false;
                }
            }else{
                if(index == 3){
                    if(!saveAdGroup()){
                        return false;
                    }
                }else if(index ==4){
                    if(!saveAdCreative()){
                        return false;
                    }
                }
                $(".page-content").removeClass("col-md-8");
            }

        },
        back: function(index) {
            if(index == 2){
                $(".page-content").addClass("col-md-8");
            }else{
                $(".page-content").removeClass("col-md-8");
            }
        },
        finish: function() {
            Swal.fire({
                title: "确定提交广告到广点通?",
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: "#EF5350",
                confirmButtonText: "是的, 提交",
                cancelButtonText: "取消"
            }).then((result) => {
                if (result.value) {
                    saveAd()
                    layer.load(1, {
                        shade: [0.5, '#cac6c5'] //0.5透明度的白色背景
                    });
                }
            });

        }
    });
    //$(".stepy-callbacks").stepy('step',2)
    $('.stepy-step').find('.button-next').addClass('btn btn-primary');
    $('.stepy-step').find('.button-back').addClass('btn btn-default');
    initTree("loc",locData);
    initTreeInput("loc");
    initTree("behavior",behaviorData);
    initTreeInput("behavior");
    initTree("interest",interestData);
    initTreeInput("interest");
    if(campaign != undefined){
        if(method == "continue"||method=="newad"){
            $("#campaignId").val(campaign.id);
            if(method == "newad"){
                $("input:radio[value='old_campaign']").prop("checked",true);
                changeAddType();
                $("#campaign_select").val(campaign.id);
                $("#campaign_select").selectpicker("refresh");
            }
        }
        $("#campaignName").val(campaign.campaignName);
        $("#adsense").val(campaign.adsense).select2();
        var adsense = adsense_adcreativeTemplate_map[$("#adsense").val()];
        adcreativeTemplateId = adsense["id"];
        adcreative_type = adsense["adcreative_type"];
        $("#img_limit").html(adsense["tips"]);
        $("#campaignType").val(campaign.campaignType).select2();
        $("#promotedObjectType").val(campaign.promotedObjectType).select2();
        $("#dailyBudget").val(campaign.dailyBudget/100);
        if(campaign.configuredStatus == "AD_STATUS_SUSPEND"){
            $("#configuredStatus").trigger("click");
        }
    }
    if(params != undefined){
        if(method == "continue"){
            $("#adGroupId").val(params.id);
        }
        $("#adGroupName").val(params.adGroupName);
        $('.daterange-basic').daterangepicker({
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            startDate:params.beginDate,
            endDate:params.endDate,
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

        var bidMethod = params.bidMethod;
        if(bidMethod == "CPC"){
            $("#default_page_radio").show();
            $("input:radio[value='CPC']").prop("checked",true);
        }else if(bidMethod == "OCPA"){
            $("#default_page_radio").hide();
            $("input:radio[value='OCPA']").prop("checked",true);
        }
        var bidAmount = params.bidAmount;
        $("#bidAmount").val(bidAmount);
        initTargeting(params);

    }else{
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
    }
    if(adCreativeElements != undefined){
        if(method == "continue"){
            $("#adCreativeId").val(adCreativeElements.id);
        }
        $("#adcreativeImgurl").val(adCreativeElements.materialUrls);
        $("#adcreativeImageId").val(adCreativeElements.image);


        var path = adCreativeElements.mini_program_path;
        if(path != undefined && page!= null && page != ""){
            $("#miniProgramPath").val(path.substr(0, path.indexOf("&from=gdt")));
            $("#miniProgramId").val(adCreativeElements.mini_program_id);
            $("input:radio[value='PAGE_TYPE_MINI_PROGRAM_WECHAT']").prop("checked",true);
            changePageType("PAGE_TYPE_MINI_PROGRAM_WECHAT");
        }else{
            $("#page_url").val(adCreativeElements.page_url);
            $("input:radio[value='PAGE_TYPE_DEFAULT']").prop("checked",true);
            changePageType("PAGE_TYPE_DEFAULT");
        }

        if(adCreativeElements.adCreativeType == "image"){
            $("#adcreative_img").attr('src',adCreativeElements.materialUrls);
            $("#adcreative_img").show();
        }else if(adCreativeElements.adCreativeType == "video"){
            $("#adcreative_video_source").attr('src',adCreativeElements.materialUrls);
            $("#adcreative_video").show();
        }

    }
    $.uniform.update();
    initFileInput("adcreativeImg","adcreativeImgurl","adcreativeImgBtn","enjoy/adcreativeImg");
});
function initTree(treeId, treeData){
    $("#search"+treeId).hide();
    var mapId = treeId;
    var setting = {
        callback: {
            onCheck: function (event,treeId, treeNode) {
                //$.fn.zTree.getZTreeObj(mapId).expandNode(treeNode, true);
                initMap(mapId);
            }
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        view:{
            showIcon: false,
            showLine: false
        }
    };
    $.fn.zTree.init($("#"+treeId), setting, treeData);

}
function getValidNode(treeId,node){
    var map = dataMap[treeId];
    var parent = node.getParentNode();
    if(parent == null||parent.check_Child_State != 2){
        if(node.isParent){
            if(node.check_Child_State == 2){
                var children = node.children;
                if(children.length ==1){
                    map.set(children[0].id, children[0]);
                }else{
                    if(map.get(node.id)==null){
                        map.set(node.id, node);
                    }
                }

            }
        }else{
            if(map.get(node.id)==null){
                map.set(node.id, node);
            }
        }

    }else{
        getValidNode(treeId, parent);
    }
}
function initMap(mapId){
    var map = dataMap[mapId];
    if(map == null){
        map = new Map();
        dataMap[mapId] = map;
    }else{
        map.clear();
    }

    var array = $.fn.zTree.getZTreeObj(mapId).getCheckedNodes();
    if(array.length > 0){
        for(var i = 0; i < array.length; i++){
            getValidNode(mapId, array[i]);
        }
    }
    $("#"+mapId+"input").tagsinput('removeAll');
    for(var [key,value] of map){
        $("#"+mapId+"input").tagsinput('add', { id: value.id, text: value.name });
    }

}
function initTreeInput(treeId){
    $("#"+treeId+"input").tagsinput({
        itemValue: 'id',
        itemText: 'text'
    });
    $(".bootstrap-tagsinput input").attr("readonly","readonly");
    $("#"+treeId+"input").on('itemRemoved', function(event) {
        var id = event.item.id;
        var tree = $.fn.zTree.getZTreeObj(treeId);
        var node = tree.getNodesByParam("id", id)[0];
        tree.checkNode(node,false, true, false);
        var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
        if(searchtree != null){
            var searchnode = searchtree.getNodesByParam("id", id)[0];
            if(searchnode != null){
                searchtree.checkNode(searchnode,false, true, false);
            }
        }

        var map = dataMap[treeId];
        map.delete(id);
    });
    $("#"+treeId+"input"+"_removeall").click(function(){
        $("#"+treeId+"input").tagsinput('removeAll');
        var tree = $.fn.zTree.getZTreeObj(treeId);
        tree.checkAllNodes(false);
        var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
        if(searchtree != null){
            searchtree.checkAllNodes(false);
        }
        var map = dataMap[treeId];
        if(map != null){
            map.clear();
        }
    });
}
function search(treeId){
    var tree = $.fn.zTree.getZTreeObj(treeId);
    var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
    var keyword = $("#search_input_"+treeId).val();
    var setting = {
        callback: {
            onCheck: function (event,treeId, treeNode) {
                var node = tree.getNodesByParam("id",treeNode.id)[0];
                tree.checkNode(node,treeNode.checked, true, true);
            }
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            },
            key:{
                name:"fullname"
            }
        },
        view:{
            showIcon: false,
            showLine: false

        }
    };

    if(keyword.length > 0){
        searchKeyWord = keyword;
        $("#"+treeId).hide();
        $("#search"+treeId).show();
        var showNodes = tree.getNodesByFilter(filter);

        if(showNodes != null && showNodes.length > 0) {
            if(searchtree != null){
                searchtree.destroy();
            }
            var nodes = [];
            for(var i = 0;i< showNodes.length;i++){
                if(showNodes[i].name != showNodes[i].pName){
                    showNodes[i].fullname = showNodes[i].pName+"-"+showNodes[i].name;
                }else{
                    showNodes[i].fullname = showNodes[i].name;
                }
            }
            $.fn.zTree.init($("#search"+treeId), setting, showNodes);
        }else{
            $("#search"+treeId).hide();
        }
    }else{
        $("#"+treeId).show();
        $("#search"+treeId).hide();
    }
}
function filter(node) {
    return (node.name.indexOf(searchKeyWord)>-1||node.pName.indexOf(searchKeyWord)>-1);
}
function initFileInput(fileId,fileInputId,uploadbtn){
    $("#"+fileId).fileinput({
        language : 'zh',
        uploadUrl:"/enjoy-web/admin/advertisers/uploadImage",
        uploadExtraData: function(previewId, index) {   //该插件可以向您的服务器方法发送附加数据。这可以通过uploadExtraData在键值对中设置为关联数组对象来完成。所以如果你有设置uploadExtraData={id:'kv-1'}，在PHP中你可以读取这些数据$_POST['id']
            return {aId:aId, adcreative_type:adcreative_type};
        },
        showCaption:false,
        showCancel:false,
        showPreview:true,
        browseLabel: '选择素材',
        showCaption:false,
        removeFromPreviewOnError:true,
        autoReplace:true,
        fileActionSettings:{showZoom:false,indicatorNew:""},
        showUploadedThumbs:false,
        frameClass:"",
        dropZoneEnabled:false,
        browseClass: 'btn btn-primary',
        removeLabel: '清空',
        browseIcon: '<i class="icon-plus22 position-left"></i> ',
        uploadIcon: '<i class="icon-file-upload position-left"></i> ',
        uploadLabel: '上传',
        uploadClass:'btn btn-default kv-fileinput-upload '+uploadbtn,
        removeClass: 'btn btn-danger btn-icon',
        removeIcon: '<i class="icon-cancel-square"></i> ',
        maxFilesNum: 1,
        allowedFileExtensions: ["jpg","png","jpeg","mp4"],
        layoutTemplates:{
            actionUpload:'',
            actionZoom:'',
            actionDownload:'',
            actionDelete:''
        }
    });
    $("#"+fileId).on("filecleared",function(event, data, msg){
        $("#adcreativeImgurl").val("");
        $("#adcreativeImageId").val("");
    });
    $("#"+fileId).on("fileuploaded", function(event, data, previewId, index) {
        $("."+uploadbtn).hide();
        $(".kv-upload-progress").hide();
        if(data.response.status == 1){
            var adsense = adsense_adcreativeTemplate_map[$("#adsense").val()];
            if(adcreative_type == "image"){
                var width = data.response.data.data.width;
                var height= data.response.data.data.height;
                var size = data.response.data.data.file_size/1024;
                if(width != adsense["width"] || height != adsense["height"] ||size > adsense["size"]){
                    $.alertWarning(adsense["tips"]);
                    $("."+uploadbtn).hide();
                    return;
                }
                $("#adcreativeImgurl").val(data.response.data.data.preview_url);
                $("#adcreativeImageId").val(data.response.data.data.image_id+"");
            }else{
                var video_id = data.response.data.data.video_id;
                $("#adcreativeImageId").val(video_id+"");
                layer.msg('视频转码中...', {
                    icon: 16
                    ,shade: [0.5, '#cac6c5']
                });
                var interval = setInterval(function(){
                    $.ajax({url:"/enjoy-web/admin/advertisers/getVideoInfo",
                        data:{videoId:video_id,aId:aId},
                        type:"POST",
                        success:function(data){
                            if (data.status == 1) {
                                var width = data.data.width;
                                var height= data.data.height;
                                var size = data.data.file_size/1024;

                                if(width != adsense["width"] || height != adsense["height"] ||size > adsense["size"]){
                                    $.alertWarning(adsense["tips"]);
                                    $("."+uploadbtn).hide();
                                    return;
                                }
                                var system_status = data.data.system_status;
                                if("MEDIA_STATUS_VALID" == system_status){
                                    $("#adcreativeImgurl").val(data.data.preview_url);
                                    clearInterval(interval);
                                    layer.closeAll();
                                }else if("MEDIA_STATUS_VALID" == system_status){
                                    clearInterval(interval);
                                    $.alertWarning("视频转码失败，重新上传");
                                }
                            }else{
                                clearInterval(interval);
                            }
                        },
                        error:function(xhr, status){
                            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                            clearInterval(interval)
                        }
                    });
                }, 1000);
            }

        }else{
            $.alertWarning(data.response.msg);
        }

    });
    $("#"+fileId).on('fileloaded', function(event, file, previewId, index, reader) {
        if(adcreativeTemplateId == null){
            $.alertWarning("清选择推广版位！");
            return;
        }
        $("."+uploadbtn).hide();
        if(adcreative_type == "video"){
            var video = $('#'+previewId).find("video")[0];
            video.addEventListener('canplay', function () {

                $("#adcreativeImgurl").val("");
                $("#adcreativeImageId").val("");
                $("."+uploadbtn).show();
                $("#adcreative_video").hide();
            });
        }else{
            var img = $('#'+previewId).find('img')[0];
            img.onload = function () {
                $("#adcreativeImgurl").val("");
                $("#adcreativeImageId").val("");
                $("."+uploadbtn).show();
                $("#adcreative_img").hide();
            };
        }



    });


}
function initTreeNodeChecked(treeId,array){
    var tree = $.fn.zTree.getZTreeObj(treeId);
    for ( var i = 0; i <array.length; i++){
        var node = tree.getNodeByParam("id", array[i], null);
        tree.checkNode(node,true,true,true);
    }
}
function initMultiselect(id,array){
    for(var i = 0; i < array.length; i++){
        var option = "#"+id+" option[value='"+array[i]+"']";
        $(option).attr("selected", true);
    }
    $("#"+id).multiselect('rebuild');
    $("#"+id).multiselect( 'refresh' );
    $(".checkbox input:checkbox").uniform();
}
function changeDateType(){
    var dateType = $("input[name='dateType']:checked").val();
    if("custom_time" == dateType){
        $("#datetime").show();
    }else{
        $("#datetime").hide();
    }

}
function changeAddType(){
    var addType = $("input[name='add_type']:checked").val();
    if("new_campaign" == addType){
        $("#new_campaign_div").show();
        $("#old_campaign_div").hide();
        $("#dailyBudget").removeAttrs("readonly");
        $("#adsense").removeAttrs("disabled");
        $("#campaignType").removeAttrs("disabled");
        $("#promotedObjectType").removeAttrs("disabled");
        $("#campaignId").val("");
    }else{
        $("#dailyBudget").attr("readonly", "readonly");
        $("#adsense").attr("disabled", "disabled");
        $("#campaignType").attr("disabled", "disabled");
        $("#promotedObjectType").attr("disabled", "disabled");
        $("#new_campaign_div").hide();
        $("#old_campaign_div").show();
    }
}
function changeCampaign(){
    var selected = $("#campaign_select").val();
    if(selected != "0"){
        var campaign = JSON.parse($("#campaign_select option:selected").attr("data-campaign"));
        $("#campaignId").val(campaign.id);
        $("#dailyBudget").val(campaign.dailyBudget/100);
        if(campaign.configuredStatus == "AD_STATUS_SUSPEND"){
            $("#configuredStatus").trigger("click");
        }
        $("#adsense").val(campaign.adsense).select2();
        var adsense = adsense_adcreativeTemplate_map[$("#adsense").val()];
        adcreativeTemplateId = adsense["id"];
        $("#img_limit").html(adsense["tips"]);
        $("#campaignType").val(campaign.campaignType).select2();
        $("#promotedObjectType").val(campaign.promotedObjectType).select2();
        $("#campaignName").val(campaign.campaignName);
    }

}
function changebillingEvent(){
    var billingEvent = $("input[name='billing_event']:checked").val();
    if(billingEvent == "CPC"){
        $("#default_page_radio").show();
        $("#bidAmount").attr("placeholder", "CPC出价要求介于0.5元-100元之间");
    }else if(billingEvent == "OCPA"){
        $("#default_page_radio").hide();
        $("#bidAmount").attr("placeholder", "OCPA出价要求介于0.1元-2000元之间");
    }

}
function changePageType(pageType){
    if(pageType == "PAGE_TYPE_MINI_PROGRAM_WECHAT"){
        $("#mini_program_page").show();
        $("#default_h5_page").hide();
    }else if(pageType == "PAGE_TYPE_DEFAULT"){
        $("#mini_program_page").hide();
        $("#default_h5_page").show();
    }
}
function changeAdsense(obj){
    var adsense = adsense_adcreativeTemplate_map[$(obj).val()];
    adcreativeTemplateId = adsense["id"];
    $("#img_limit").html(adsense["tips"]);
    adcreative_type = adsense["adcreative_type"];
}
function changeTemplate(){
     cleanTargeting();
     var id = parseInt($("#targetingTemplate").val());
     if(id > 0){
         layer.load(1, {
             shade: [0.5, '#cac6c5'] //0.5透明度的白色背景
         });
         $.ajax({url:"/enjoy-web/admin/targetingTemplate/getTargetingById/"+id,
             success:function(data){
                 if (data.data != null) {
                     initTargeting(data.data);
                 }
                 layer.closeAll('loading');
             },
             error:function(xhr, status){
                 $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                 layer.closeAll('loading');
             }
         });
     }

}
function switchChange(obj,toggleId){
    if($(obj).is(':checked')){
        $("#"+toggleId).hide();
    }else{
        $("#"+toggleId).show();
    }
}
function switchChange1(obj,toggleId){
    if($(obj).is(':checked')){
        $("#"+toggleId).show();
    }else{
        $("#"+toggleId).hide();
    }
}
function reloadEstimate(){
    var block = $("#estimate_div");
    $(block).block({
        message: '<i class="icon-spinner4 spinner"></i>',
        overlayCSS: {
            backgroundColor: '#fff',
            opacity: 0.8,
            cursor: 'wait'
        },
        css: {
            border: 0,
            padding: 0,
            backgroundColor: 'transparent'
        }
    });
    $("#max_active_user_count").html("-");
    $("#approximate_count").html("-");
    $("#impression").html("-");
    $("#min_bid_amount").html("-");
    $("#max_bid_amount").html("-");
    $("#suggest_bid_amount").html("-");
    $("#suggest_bid_content_ocpa").html("");
    $("#users_daily").html("-");
    $("#exposure_daily").html("-");
    $("#targeting_status").html("-");
    $("#suggest_targeting").html("-")
    $("#is_real_exposure_supported").html("-");
    var params = {};
    params["privateCampaignId"] = $("#campaignId").val();
    params["siteSet"] = JSON.stringify(["SITE_SET_WECHAT"]);
    var reg = new RegExp( '/' , "g" )
    var date = $("#put_date").val().split("-");
    var beginDate = date[0].replace(reg,"-");
    var endDate = date[1].replace(reg, "-");
    params["beginDate"] = beginDate.trim();
    params["endDate"] = endDate.trim();
    var dataType=$('input:radio[name="dateType"]:checked').val();
    if(dataType == "custom_time"){
        var start_time = $("#start_time").val();
        var end_time = $("#end_time").val();
        if(start_time >= end_time){
            $.alertWarning("开始时间必须小于结束时间！");
            $(block).unblock();
            return false;
        }else{
            params["timeSeries"] = start_time+"-"+end_time;
        }
    }
    var billingEvent=$('input:radio[name="billing_event"]:checked').val();
    var campaignType = $("#campaignType").val();
    if(campaignType == "CAMPAIGN_TYPE_NORMAL"){
        params["billingEvent"] = "BILLINGEVENT_CLICK";
    }else if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"){
        params["billingEvent"] = "BILLINGEVENT_IMPRESSION";
    }
    if(billingEvent == "CPC"){
        params["optimizationGoal"] = "OPTIMIZATIONGOAL_CLICK";
    }else if(billingEvent == "OCPA"){
        params["optimizationGoal"] = "OPTIMIZATIONGOAL_ECOMMERCE_ORDER";
    }else{
        $.alertWarning("选择出价方式！");
        $(block).unblock();
        return false;
    }
    var bidAmount = $("#bidAmount").val();
    if(bidAmount != ""||!isNaN(bidAmount)){
        if(billingEvent == "CPC"&&bidAmount < 0.5 && bidAmount > 100){
            $.alertWarning("出价不符合规定！");
            $(block).unblock();
            return false;
        }else if(billingEvent == "OCPA"&&bidAmount < 0.1 && bidAmount > 2000){
            $.alertWarning("出价不符合规定！");
            $(block).unblock();
            return false;
        }
        params["bidAmount"] = parseInt(bidAmount*100);
    }

    var targeting = getTargetingInfo(params);
    if(!targeting){
        $(block).unblock();
        return targeting;
    }

    $.ajax({url:"/enjoy-web/admin/ad/admanager/estimation/get/"+aId,
        data:params,
        type:"POST",
        success:function(data){
            if (data.status == 1) {
                var max_active_user_count = data.data.max_active_user_count;
                if(max_active_user_count > 0){
                    $("#max_active_user_count").html(max_active_user_count);
                }
                var approximate_count = data.data.approximate_count;
                if(approximate_count > 0){
                    $("#approximate_count").html(approximate_count);
                }
                var impression = data.data.impression;
                if(impression > 0){
                    $("#impression").html(impression);
                }
                var min_bid_amount = data.data.min_bid_amount;
                if(min_bid_amount > 0){
                    $("#min_bid_amount").html(min_bid_amount/100);
                }
                var max_bid_amount = data.data.max_bid_amount;
                if(max_bid_amount > 0){
                    $("#max_bid_amount").html(max_bid_amount/100);
                }
                var suggest_min_bid_amount = data.data.suggest_min_bid_amount;
                var suggest_max_bid_amount = data.data.suggest_max_bid_amount;
                if(suggest_min_bid_amount > 0 || suggest_max_bid_amount>0){
                    $("#suggest_bid_amount").html((suggest_min_bid_amount/100)+"~"+(suggest_max_bid_amount/100));
                }
                var suggest_bid_content_ocpa = data.data.suggest_bid_content_ocpa;
                $("#suggest_bid_content_ocpa").html(suggest_bid_content_ocpa);
                var min_users_daily = data.data.min_users_daily;
                var max_users_daily = data.data.max_users_daily;
                if(min_users_daily > 0 || max_users_daily > 0){
                    $("#users_daily").html(min_users_daily+"~"+max_users_daily);
                }
                var min_exposure_daily = data.data.min_exposure_daily;
                var max_exposure_daily = data.data.max_exposure_daily;
                if(min_exposure_daily > 0 || max_exposure_daily > 0){
                    $("#exposure_daily").html(min_exposure_daily+"~"+max_exposure_daily);
                }
                var targeting_status = data.data.targeting_status;
                $("#targeting_status").html(targeting_status_map[targeting_status]);
                var suggest_targeting = data.data.suggest_targeting;
                if(suggest_targeting.length > 0){
                    var sts = [];
                    for ( var i = 0; i <suggest_targeting.length; i++){
                        var name = suggest_targeting_map[suggest_targeting[i]];
                        if(name == null || name == undefined){
                            name = suggest_targeting[i];
                        }
                        sts.push(name);
                    }
                    if(sts.length > 0){
                        $("#suggest_targeting").html(sts.join(","))
                    }
                }
                var is_real_exposure_supported = data.data.is_real_exposure_supported;
                if(is_real_exposure_supported){
                    $("#is_real_exposure_supported").html("是");
                }else{
                    $("#is_real_exposure_supported").html("否");
                }
            }else{
                $.alertWarning(data.msg);
            }
            $(block).unblock();
        },
        error:function(xhr, status){
            $(block).unblock();
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
        }
    });



}
function preview(){
    var params = {};
    $("#preview").html("");
    params["path"] = $("#miniProgramPath").val();
    var index = layer.load(1, {
        shade: [0.5, '#cac6c5'] //0.5透明度的白色背景
    });

    $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/qrcode",
        data:params,
        type:"GET",
        success:function(data){
            if (data.status == 1) {
                $("<img src='"+data.data+"'></img>").appendTo($("#preview"));
            }else{
                $.alertWarning(data.msg);
            }
            layer.close(index);
        },
        error:function(xhr, status){
            layer.close(index);
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
        }
    });




}
function saveCampaign(){
    var campaign_div = $("#campaign_info_div");
    campaign_div.html("");
    var params = {};
    if($("#campaignId").val() > 0){
        params["id"] = $("#campaignId").val();
    }
    var campaignName = $("#campaignName").val();
    if(campaignName == ""){
        $.alertWarning("计划名不能为空");
        return false;
    }else{
        params["campaignName"] = campaignName;
    }
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">计划名</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+campaignName+"</div></div>").appendTo(campaign_div);
    var adsense = $("#adsense").val();
    if(adsense == ""){
        $.alertWarning("广告位不能为空");
        return false;
    }else{
        params["adsense"] = adsense;
    }
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">广告位</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+$("#adsense option:selected").text()+"</div></div>").appendTo(campaign_div);
    var campaignType = $("#campaignType").val();
    if(campaignType == ""){
        $.alertWarning("推广计划类型不能为空");
        return false;
    }else{
        params["campaignType"] = campaignType;
    }

    var promotedObjectType = $("#promotedObjectType").val();
    if(promotedObjectType == ""){
        $.alertWarning("推广目标不能为空");
        return false;
    }else{
        params["promotedObjectType"] = promotedObjectType;
    }

    var dailyBudget = $("#dailyBudget").val();
    if(dailyBudget == ""||isNaN(dailyBudget)||dailyBudget < 50 || dailyBudget > 4000000){
        $.alertWarning("日预算不符合规定");
        return false;
    }else{
        params["dailyBudget"] = parseInt(dailyBudget*100);
    }
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">日预算</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+dailyBudget+"</div></div>").appendTo(campaign_div);
    if($("#configuredStatus").is(':checked')){
        configuredStatus = "AD_STATUS_NORMAL";
    }else{
        configuredStatus = "AD_STATUS_SUSPEND";
    }
    params["configuredStatus"] = configuredStatus;
    var addType = $("input[name='add_type']:checked").val();
    if("new_campaign" == addType){
        var result = false;
        $.ajax({url:"/enjoy-web/admin/ad/admanager/campaign/save/"+aId,
            data:params,
            type:"POST",
            async:false,
            success:function(data){
                if (data.status == 1) {
                    $("#campaignId").val(data.data);
                    result = true;
                }else{
                    $.alertWarning(data.msg);
                    result = false;
                }
            },
            error:function(xhr, status){
                $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                result = false;
            }
        });
        return result;
    }else{
        var selected = $("#campaign_select").val();
        if(selected != "0"){
            return true;
        }else{
            $.alertWarning("请先选择广告计划！");
            return false;
        }
    }
}

function saveAdGroup(){
    var adgroup_div = $("#adgroup_info_div");
    adgroup_div.html("");
    var params = {};
    params["privateCampaignId"] = $("#campaignId").val();
    params["siteSet"] = JSON.stringify(["SITE_SET_WECHAT"]);
    if($("#adGroupId").val() > 0){
        params["id"] = $("#adGroupId").val();
    }
    var adGroupName = $("#adGroupName").val();
    if(adGroupName == ""){
        $.alertWarning("广告名称不能为空");
        return false;
    }else{
        params["adGroupName"] = adGroupName;
    }
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">广告名</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+adGroupName+"</div></div>").appendTo(adgroup_div);
    params["configuredStatus"] = "AD_STATUS_NORMAL";
    var reg = new RegExp( '/' , "g" )
    var date = $("#put_date").val().split("-");
    var beginDate = date[0].replace(reg,"-");
    var endDate = date[1].replace(reg, "-");
    params["beginDate"] = beginDate.trim();
    params["endDate"] = endDate.trim();
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">投放日期</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+beginDate+"~"+endDate+"</div></div>").appendTo(adgroup_div);
    var dataType=$('input:radio[name="dateType"]:checked').val();
    if(dataType == "custom_time"){
        var timeSeries = "";
        $(".custom_time").each(function(){
            var start_time = $(this).find(".start_time").val();
            var end_time = $(this).find(".end_time").val();
            if(start_time > end_time){
                $.alertWarning("开始时间不能大于结束时间！");
                return false;
            }else{
                timeSeries = start_time+"-"+end_time+","+timeSeries;
            }
        })
        params["timeSeries"] = timeSeries;
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">投放时间</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+timeSeries+"</div></div>").appendTo(adgroup_div);
    }
    var customizedCategory = $("#customizedCategory").val();
    if(customizedCategory != ""){
        params["customizedCategory"] = customizedCategory;
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">自定义分类</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+customizedCategory+"</div></div>").appendTo(adgroup_div);
    }
    var billingEvent=$('input:radio[name="billing_event"]:checked').val();
    var campaignType = $("#campaignType").val();
    if(campaignType == "CAMPAIGN_TYPE_NORMAL"){
        params["billingEvent"] = "BILLINGEVENT_CLICK";
    }else if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"){
        params["billingEvent"] = "BILLINGEVENT_IMPRESSION";
    }
    $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">计价方式</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+billingEvent+"</div></div>").appendTo(adgroup_div);
    if(billingEvent == "CPC"){
        params["optimizationGoal"] = "OPTIMIZATIONGOAL_CLICK";
    }else if(billingEvent == "OCPA"){
        params["optimizationGoal"] = "OPTIMIZATIONGOAL_ECOMMERCE_ORDER";

    }else{
        $.alertWarning("请选择出价方式！");
        return false;
    }
    params["bidMethod"] = billingEvent;
    var bidAmount = $("#bidAmount").val();
    if(bidAmount == ""||isNaN(bidAmount)){
        $.alertWarning("出价不能为空！");
        return false;
    }else{
        if(billingEvent == "CPC"&&bidAmount < 0.5 && bidAmount > 100){
            $.alertWarning("出价不符合规定！");
            return false;
        }else if(billingEvent == "OCPA"&&bidAmount < 0.1 && bidAmount > 2000){
            $.alertWarning("出价不符合规定！");
            return false;
        }
        params["bidAmount"] = parseInt(bidAmount*100);
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">当前出价</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+bidAmount+"</div></div>").appendTo(adgroup_div);
    }
    var campaignType = $("#campaignType").val();
    if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"){
        var daily_budget = $("#adgroup_daily_budget").val();
        if(daily_budget == ""||isNaN(daily_budget)){
            $.alertWarning("广告组预算不能为空！");
            return false;
        }else{
            if(daily_budget < 1000 && daily_budget > 10000000){
                $.alertWarning("出价不符合规定！");
                return false;
            }
            params["dailyBudget"] = parseInt(daily_budget*100);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">广告组预算</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+daily_budget+"</div></div>").appendTo(adgroup_div);
        }
    }

    var targeting = getTargetingInfo(params);
    if(!targeting){
        return targeting;
    }
    var result = false;
    $.ajax({url:"/enjoy-web/admin/ad/admanager/adgroup/save/"+aId,
        data:params,
        type:"POST",
        async:false,
        success:function(data){
            if (data.status == 1) {
                $("#adGroupId").val(data.data)
                result = true;
            }else{
                $.alertWarning(data.msg);
                result = false;
            }
        },
        error:function(xhr, status){
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            result = false;
        }
    });
    return result;
}

function saveAdCreative(){
    var adcreative_div = $("#adcreative_info_div");
    adcreative_div.html("");
    var params = {};
    var imageUrl = $("#adcreativeImgurl").val();
    var image = $("#adcreativeImageId").val();
    var campaignType = $("#campaignType").val();
    var title = $("#title").val();
    var desc = $("#desc").val();
    if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"&&title == ""){
        $.alertWarning("标题不能为空");
        return false;
    }
    if(image == ""){
        $.alertWarning("请上传素材图片");
        return false;
    }
    var pageType=$('input:radio[name="pageType"]:checked').val();
    params["pageType"] = pageType;
    if(pageType == "PAGE_TYPE_MINI_PROGRAM_WECHAT"){
        var miniProgramId = $("#miniProgramId").val();
        if(miniProgramId == ""){
            $.alertWarning("小程序ID不能为空！");
            return false;
        }
        var miniProgramPath = $("#miniProgramPath").val();
        if(miniProgramPath == ""){
            $.alertWarning("小程序链接不能为空");
            return false;
        }else{

            if(miniProgramPath.indexOf("&from=gdt") > -1){
                miniProgramPath = miniProgramPath.substr(0, miniProgramPath.indexOf("&from=gdt"))
            }
            var adGroupId = $("#adGroupId").val();
            if(miniProgramPath.indexOf("?") != -1){
                miniProgramPath = miniProgramPath+"&from=gdt&account_id="+account_id+"&user_action_set_id="+user_action_set_id+"&adGroupId="+adGroupId;
            }else{
                miniProgramPath = miniProgramPath+"?from=gdt&account_id="+account_id+"&user_action_set_id="+user_action_set_id+"&adGroupId="+adGroupId;
            }
        }
        params["pageSpec"] = "{\"mini_program_spec\":{\"mini_program_id\":\""+miniProgramId+"\",\"mini_program_path\":\""+miniProgramPath+"\"}}";
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">小程序</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+miniProgramId+"</div></div>").appendTo(adcreative_div);
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">落地页链接</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+miniProgramPath+"</div></div>").appendTo(adcreative_div);
    }else if(pageType == "PAGE_TYPE_DEFAULT"){
        var page_url = $("#page_url").val();
        params["pageSpec"] = "{\"page_url\":\""+page_url+"\"}";
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">落地页链接</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+page_url+"</div></div>").appendTo(adcreative_div);
    }
    if($("#adCreativeId").val() > 0){
        params["id"] = $("#adCreativeId").val();
    }
    params["privateCampaignId"] = $("#campaignId").val();
    params["siteSet"] = JSON.stringify(["SITE_SET_WECHAT"]);
    var adcreative_name = $("#adGroupName").val();
    params["adCreativeName"] = adcreative_name;
    params["adCreativeTemplateId"] = adcreativeTemplateId;
    var adCreativeElements = {};
    if(campaignType == "CAMPAIGN_TYPE_NORMAL"){
        adCreativeElements.image = image;
        /*adCreativeElements.mini_program_id = miniProgramId;
        adCreativeElements.mini_program_path = miniProgramPath;*/
    }else if(campaignType == "CAMPAIGN_TYPE_WECHAT_MOMENTS"){
        adCreativeElements.title = title;
        if(adcreative_type == "video"){
            adCreativeElements.description = desc;
            adCreativeElements.short_video_struct = {"short_video2": image};
        }else if(adcreative_type == "image"){
            adCreativeElements.image_list = [image];
            //adCreativeElements.link_name_type="ENTER_MINI_PROGRAM";
        }
        /*adCreativeElements.mini_program_id = miniProgramId;
        adCreativeElements.mini_program_path = miniProgramPath;*/
    }

    params["adCreativeElements"] = JSON.stringify(adCreativeElements);
    /*params["pageType"] = "PAGE_TYPE_DEFAULT";
    params["pageSpec"] = "{\"page_url\":\"https://a.weixin.qq.com\"}";*/

    params["materialUrls"] = imageUrl;
    params["adcreativeType"] = adcreative_type;
    var result = false;
    $.ajax({url:"/enjoy-web/admin/ad/admanager/adcreative/save/"+aId,
        data:params,
        type:"POST",
        async:false,
        success:function(data){
            if (data.status == 1) {
                $("#adCreativeId").val(data.data)
                result = true;
            }else{
                $.alertWarning(data.msg);
                result = false;
            }
        },
        error:function(xhr, status){
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            result = false;
        }
    });
    return result;
}

function saveAd(){
    var params = {};
    var adGroupId = $("#adGroupId").val();
    var campaignId = $("#campaignId").val();
    var adCreativeId = $("#adCreativeId").val();
    params["adGroup.id"] = adGroupId;
    params["campaign.id"] = campaignId;
    params["adCreative.id"] = adCreativeId;
    params["campaign.configuredStatus"] = configuredStatus;
    $.ajax({url:"/enjoy-web/admin/ad/admanager/ad/save/"+aId,
        data:params,
        type:"POST",
        success:function(data){
            if (data.status == 1) {
                location.reload();
            }else{
                $.alertWarning(data.msg);
            }
            layer.closeAll('loading');
        },
        error:function(xhr, status){
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            layer.closeAll('loading');
        }
    });
}

function getTargetingInfo(params){
    var adgroup_div = $("#adgroup_info_div");
    var locMap = dataMap["loc"];
    if(locMap != null){
        var region = [];
        var business = [];
        var cname="";
        locMap.forEach(function (value, key, map) {
            if(value.areatype == "region"){
                region.push(key);
            }else if(value.areatype == "business"){
                business.push(key);
            }
            cname = value.name + "," + cname;
        });
        if(region.length > 0 || business.length){
            var geoLocation = {};
            geoLocation.location_types=["LIVE_IN"];
            if(region.length > 0){
                geoLocation.regions = region;
            }
            if(business.length > 0){
                geoLocation.business_districts = business;
            }
            params["targeting.geoLocation"] = JSON.stringify(geoLocation);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">定向位置</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+cname+"</div></div>").appendTo(adgroup_div);
        }
    }
    var age_min = $("#age_min").val();
    var age_max = $("#age_max").val();
    if(age_min != "" && age_max != ""){
        if(age_min > age_max){
            $.alertWarning("起始年龄不能大于结束年龄！");
            return false;
        }else{
            var age = {};
            age.min = age_min;
            age.max = age_max;
            params["targeting.age"] = JSON.stringify([age]);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">定向年龄</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+age_min+"~"+age_max+"</div></div>").appendTo(adgroup_div);
        }
    }
    var gender=$('input:radio[name="gender"]:checked').val();
    if(gender != ""){
        params["targeting.gender"] = JSON.stringify([gender]);
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">性别</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+$('input:radio[name="gender"]:checked').parent().parent().parent().text()+"</div></div>").appendTo(adgroup_div);
    }
    if($("#expand_enabled_switch").is(':checked')){
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">启动自动扩量</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">是</div>").appendTo(adgroup_div);
        var expand_targeting = "";
        if(params["bidMethod"] == "CPC"){
            params["cpcExpandEnabled"] = "true";
        }else if(params["bidMethod"] = "OCPA"){
            params["ocpaExpandEnabled"] = "true";
        }

        $('input[name="expand_targeting"]:checked').each(function(){
            if($(this).val() == 'age'&&params["targeting.age"] != null){
                expand_targeting = $(this).val()+","+expand_targeting;
            }
            if($(this).val() == 'gender'&&params["targeting.gender"] != null){
                expand_targeting = $(this).val()+","+expand_targeting;
            }
            if($(this).val() == 'geo_location'&&params["targeting.geoLocation"] != null){
                expand_targeting = $(this).val()+","+expand_targeting;
            }
        });
        if(expand_targeting != ""){
            params["ExpandTargeting"] = expand_targeting;
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">不突破定向</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+expand_targeting+"</div></div>").appendTo(adgroup_div);
        }

    }
    if(!$("#behavior_interest_switch").is(':checked')){
        var behaviorMap = dataMap["behavior"];
        var behavior = [];
        var interest = [];
        var biname = "";
        if(behaviorMap != null){
            behaviorMap.forEach(function(value, key, map){
                behavior.push(value.id);
                biname = value.name+","+biname;
            });
        }
        var interestMap= dataMap["interest"];
        if(interestMap != null){
            interestMap.forEach(function (value, key,map) {
                interest.push(value.id);
                biname = value.name+","+biname;
            });
        }
        $("#interest_tags option:selected").each(function () {
            interest.push($(this).val());
            biname = $(this).val()+","+biname;
        });
        $("#behavior_tags option:selected").each(function () {
            behavior.push($(this).val());
            biname = $(this).val()+","+biname;
        });
        if(behavior.length > 0 || interest.length > 0){
            var behaviorOrInterest = {};
            if(behavior.length > 0){
                var behaviorStruct = {};
                behaviorStruct.targeting_tags = behavior;
                var scene = [];
                $('input[name="scene"]:checked').each(function(){
                    scene.push($(this).val());
                });
                if(scene.length > 0){
                    behaviorStruct.scene=scene;
                }else{
                    behaviorStruct.scene=["BEHAVIOR_INTEREST_SCENE_ALL"];
                }
                var time_window = $("#time_window").val();
                if(time_window != ""){
                    behaviorStruct.time_window=time_window;
                }else{
                    behaviorStruct.time_window="BEHAVIOR_INTEREST_TIME_WINDOW_THIRTY_DAY";
                }
                var intensity = [];
                $('input[name="intensity"]:checked').each(function(){
                    intensity.push($(this).val());
                });
                if(intensity.length > 0){
                    behaviorStruct.intensity=intensity;
                }else{
                    behaviorStruct.intensity=["BEHAVIOR_INTEREST_INTENSITY_ALL"];
                }

                behaviorOrInterest.behavior = [behaviorStruct];
            }
            if(interest.length > 0){
                var interestStruct = {}
                interestStruct.targeting_tags = interest;
                behaviorOrInterest.interest = interestStruct;
            }
            params["targeting.behaviorOrInterest"] = JSON.stringify(behaviorOrInterest);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">兴趣行为</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+biname+"</div></div>").appendTo(adgroup_div);
        }
    }
    if(!$("#wcates_switch").is(':checked')){
        var wcates_selected = [];
        var wname="";
        $('#wcates_select option:selected').each(function () {
            wcates_selected.push($(this).val());
            wname = $(this).text()+","+wname;
        });
        if(wcates_selected.length > 0){
            params["targeting.wechatOfficialAccountCategory"] = JSON.stringify(wcates_selected);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">公众号媒体类型</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+wname+"</div></div>").appendTo(adgroup_div);
        }
    }
    if(!$("#hlzt_switch").is(':checked')){
        var marital_status_selected = [];
        var msname = "";
        $('#marital_status_select option:selected').each(function () {
            marital_status_selected.push($(this).val());
            msname = $(this).text()+msname;
        });
        if(marital_status_selected.length > 0){
            params["targeting.maritalStatus"] = JSON.stringify(marital_status_selected);
            $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">婚恋状态</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+msname+"</div></div>").appendTo(adgroup_div);
        }
    }
    var customAudiences = [];
    var customAudiencesname = "";
    $('#customAudience option:selected').each(function(){
        customAudiences.push($(this).val());
        customAudiencesname = $(this).text()+customAudiencesname;
    });
    if(customAudiences.length > 0){
        params["targeting.customAudience"] = JSON.stringify(customAudiences);
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">自定义人群</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+customAudiencesname+"</div></div>").appendTo(adgroup_div);
    }

    var excludedCustomAudiences = [];
    var excludedCustomAudiencesname="";
    $('#excludedCustomAudience option:selected').each(function(){
        excludedCustomAudiences.push($(this).val());
        excludedCustomAudiencesname = $(this).text()+excludedCustomAudiencesname;
    });
    if(excludedCustomAudiences.length > 0){
        params["targeting.excludedCustomAudience"] = JSON.stringify(excludedCustomAudiences);
        $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">排除人群</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+excludedCustomAudiencesname+"</div></div>").appendTo(adgroup_div);
    }

    if(!$("#sqjg_switch").is(":checked")){
        var residential_community_price_min = parseInt($("#residential_community_price_min").val());
        var residential_community_price_max = parseInt($("#residential_community_price_max").val());
        if(residential_community_price_min != "" && residential_community_price_max != ""){
            if(residential_community_price_min > residential_community_price_max){
                $.alertWarning("居民社区起始价格不能大于结束价格！");
                return false;
            }else{
                var residentialCommunityPrice = {};
                residentialCommunityPrice.min = residential_community_price_min;
                residentialCommunityPrice.max = residential_community_price_max;
                params["targeting.residentialCommunityPrice"] = JSON.stringify([residentialCommunityPrice]);
                $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">居民社区价格</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+$("#residential_community_price_min option:selected").text()+"~"+$("#residential_community_price_max option:selected").text()+"</div></div>").appendTo(adgroup_div);
            }
        }
    }

    if(!$("#qw_switch").is(":checked")){
        var temperature_min = parseInt($("#temperature_min").val());
        var temperature_max = parseInt($("#temperature_max").val());
        if(temperature_min != "" && temperature_max != ""){
            if(temperature_min >= temperature_max){
                $.alertWarning("起始温度必须小于结束温度！");
                return false;
            }else{
                var temperature = {};
                temperature.min = temperature_min;
                temperature.max = temperature_max;
                var mintxt = temperature_min-223-50;
                var maxtxt = temperature_max-223-50;
                params["targeting.temperature"] = JSON.stringify([temperature]);
                $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">气温</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+mintxt+"~"+maxtxt+"</div></div>").appendTo(adgroup_div);
            }
        }
    }


    /*var actions = [];
    var excludedActions = [];
    var aname="";
    var exaname = "";
    if(!$("#zyx_switch").is(":checked")){
        $('input[name="actions"]:checked').each(function(){
            actions.push($(this).val());
            aname= $(this).parent().parent().parent().text()+","+aname;
        });
    }
    if(!$("#pcyx_switch").is(":checked")){
        $('input[name="excludedActions"]:checked').each(function(){
            excludedActions.push($(this).val());
            exaname = $(this).parent().parent().parent().text()+","+exaname;
        });
    }
    if(actions.length > 0 || excludedActions.length > 0){
        var wechatAdBehavior = {};
        if(actions.length > 0){
            wechatAdBehavior.actions = actions;
            $("<p class=\"content-group\">再营销："+aname+"</p>").appendTo(adgroup_div);
        }
        if(excludedActions.length > 0){
            wechatAdBehavior.excluded_actions = excludedActions;
            $("<p class=\"content-group\">排除再营销："+exaname+"</p>").appendTo(adgroup_div);
        }
        params["targeting.wechatAdBehavior"] = JSON.stringify(wechatAdBehavior);
    }*/
    $(".switchcheck").each(function () {
        if(!$(this).is(":checked")){
            var name = $(this).data("check-name");
            var infoname = $(this).data("name");
            var value = [];
            var infos = "";
            $('input[name="'+name+'"]:checked').each(function(){
                value.push($(this).val());
                infos = $(this).parent().parent().parent().text()+","+infos;
            });
            if(value.length > 0){
                params["targeting."+name] = JSON.stringify(value);
                $("<div class=\"form-group form-horizontal row\"><label class=\"control-label col-xs-2 col-sm-2 col-md-2 col-lg-2\">"+infoname+"</label><div class=\"col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value\">"+infos+"</div></div>").appendTo(adgroup_div);
            }
        }
    });

    return true;
}
function initTargeting(params){
    if(params.custom_time!=null&&params.custom_time!=undefined){
        $("#datetime").show();
        $("input:radio[value='custom_time']").prop("checked",true);
        var timeSeries = params.timeSeries;
        for(var i = 0;i<timeSeries.length;i++){
            var time = timeSeries[i];
            var startTime = time.start;
            var endTime = time.end;
            if(i==0){
                $("#start_time").val(startTime);
                $("#end_time").val(endTime);
            }else{
                addCustomTime(startTime,endTime);
            }
        }
    }
    if(params.locs != null&&params.locs != undefined){
        initTreeNodeChecked("loc",params.locs)
    }
    var age_min = params.age_min;
    var age_max = params.age_max;
    if(age_min != null && age_min != undefined){
        $("#age_min").val(age_min).select2();
        $("#age_max").val(age_max).select2();
    }
    var gender = params.gender;
    if(gender != null&&gender != undefined){
        if(gender.indexOf("FEMALE") > -1){
            $("input:radio[value='FEMALE']").prop("checked",true);
        }else{
            $("input:radio[value='MALE']").prop("checked",true);
        }
    }
    var interest = params.interest;
    var behavior = params.behavior;
    if((interest != null&&interest!=undefined) || (behavior != null&&behavior!=undefined)){
        $('#behavior_interest_switch').trigger("click");
        if(interest != null){
            initTreeNodeChecked("interest",interest);
        }
        if(behavior != null){
            initTreeNodeChecked("behavior",behavior);
            var scene = params["scene"];
            $('input[name="scene"]').each(function(){
                if(scene.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
            var intensity = params["intensity"];
            $('input[name="intensity"]').each(function(){
                if(intensity.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
            $("#time_window").val(params["time_window"]);

        }
    }
    var wcates =  params.wechat_official_account_category;
    if(wcates != null&&wcates != undefined){
        $('#wcates_switch').trigger("click");
        initMultiselect("wcates_value", wcates);

    }
    var expand_enabled = params.expand_enabled;
    if(expand_enabled != null&&expand_enabled != undefined){
        var expand_targeting = params.expand_targeting;
        $('#expand_enabled_switch').trigger("click");
        if(expand_targeting!=null && expand_targeting != undefined){
            $('input[name="expand_targeting"]').each(function(){
                if(expand_targeting.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
        }
    }
    var marital_status = params.marital_status;
    if(marital_status != null && marital_status != undefined){
        $("#hlzt_switch").trigger("click");
        initMultiselect("marital_status_value", marital_status);
    }
    var residentialCommunityPrice_min = params.residentialCommunityPrice_min;
    var residentialCommunityPrice_max = params.residentialCommunityPrice_max;
    if(residentialCommunityPrice_min != null && residentialCommunityPrice_min != undefined){
        $("#sqjg_switch").trigger("click");
        $("#residential_community_price_min").val(residentialCommunityPrice_min).select2();
        $("#residential_community_price_max").val(residentialCommunityPrice_max).select2();
    }

    var temperature_min = params.temperature_min;
    var temperature_max = params.temperature_max;
    if(temperature_min != null && temperature_min != undefined){
        $("#qw_switch").trigger("click");
        $("#temperature_min").val(temperature_min);
        $("#temperature_max").val(temperature_max);
    }
    var customized_category = params.customized_category;
    if(customized_category != null && customized_category != undefined){
        $("#customizedCategory").val(customized_category);
    }
    var customAudience = params.custom_audience;
    if(customAudience != null&&customAudience != undefined){
        initMultiselect("customAudience", customAudience.split(","));
    }
    var excludedCustomAudience = params.excluded_custom_audience;
    if(excludedCustomAudience != null && excludedCustomAudience != undefined){
        initMultiselect("excludedCustomAudience", excludedCustomAudience.split());
    }
    $(".switchcheck").each(function () {
        if(params[$(this).data("check-name")] != null){
            $(this).trigger("click");
            var name = $(this).data("check-name");
            var valueStr = params[name];
            $('input[name="'+name+'"]').each(function(){
                if(valueStr.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
        }
    });
}

function cleanTargeting(){
    $('#locinput_removeall').trigger('click');
    $("#age_min").val("").select2();
    $("#age_max").val("").select2();
    $('input:radio[name="gender"][value=""]').prop("checked",true);
    if(!$("#behavior_interest_switch").is(':checked')){
        $('#behavior_interest_switch').trigger("click");
        $("#interestinput_removeall").trigger("click");
        $("#behaviorinput_removeall").trigger("click");
    }
    if(!$("#wcates_switch").is(':checked')){
        $('#wcates_switch').trigger("click");
        $("#wcates_value").multiselect('clearSelection');
        $("#wcates_value").multiselect( 'refresh' );
    }
    if(!$("#hlzt_switch").is(':checked')){
        $('#hlzt_switch').trigger("click");
        $("#marital_status_value").multiselect('clearSelection');
        $("#marital_status_value").multiselect( 'refresh' );

    }
    if(!$("#sqjg_switch").is(':checked')){
        $('#sqjg_switch').trigger("click");
        $("#residential_community_price_min").val("").select2();
        $("#residential_community_price_max").val("").select2();
    }
    $(".switchcheck").each(function () {
        if(!$(this).is(':checked')){
            $(this).trigger("click");
            var name = $(this).data("check-name");
            $('input[name="'+name+'"]').each(function(){
                $.uniform.update($(this).prop("checked",false));
            });
        }
    });
    $("#customAudience").multiselect('clearSelection');
    $("#customAudience").multiselect( 'refresh' );
    $("#excludedCustomAudience").multiselect('clearSelection');
    $("#excludedCustomAudience").multiselect( 'refresh' );
    $(".checkbox input:checkbox").uniform();

}

function addCustomTime(start_time, end_time){
    var html = "<div class=\"row custom_time\">"+$("#custom_time_template").html()+"</div>";
    $(html).appendTo("#custom_time_div");
    if(start_time != undefined&&end_time != undefined){
        $(".custom_time").last().find(".start_time").val(start_time);
        $(".custom_time").last().find(".end_time").val(end_time);
    }

}

function subCustomTime(obj){
    $(obj).parent().parent().parent().parent().remove();
}

function searchtags(type){
    var keyword = "";
    if("INTEREST" == type){
        keyword = $("#search_input_interest").val();
    }else if("BEHAVIOR" == type){
        keyword = $("#search_input_behavior").val();
    }
    if(keyword != ""){
        $.ajax({url:"/enjoy-web/admin/ad/admanager/target_tags/behavior_interest/get/"+aId,
            data:{type:type,keyword:keyword},
            type:"GET",
            success:function(data){
                if (data.status == 1) {
                    var array = data.data;
                    if(array != null && array.length > 0){
                        if("INTEREST" == type){
                            $("#interest_tags").show();
                            $("#interest_tags").html("");
                            for(var i=0; i < array.length; i++){
                                $("#interest_tags").append("<option value='"+array[i].name+"'>"+array[i].name+"</option>");
                            }
                        }else if("BEHAVIOR" == type){
                            $("#behavior_tags").show();
                            $("#behavior_tags").html("");
                            for(var i=0; i < array.length; i++){
                                $("#behavior_tags").append("<option value='"+array[i].name+"'>"+array[i].name+"</option>");
                            }
                        }
                    }else{
                        if("INTEREST" == type){
                            $("#interest_tags").html("");
                            $("#interest_tags").hide();
                        }else if("BEHAVIOR" == type){
                            $("#behavior_tags").html("");
                            $("#behavior_tags").hide();
                        }
                    }
                }
            },
            error:function(xhr, status){
                $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
            }
        });
    }else{
        if("INTEREST" == type){
            $("#interest_tags").html("");
            $("#interest_tags").hide();
        }else if("BEHAVIOR" == type){
            $("#behavior_tags").html("");
            $("#behavior_tags").hide();
        }
    }

}
