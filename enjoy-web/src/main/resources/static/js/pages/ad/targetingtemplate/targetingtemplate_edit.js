var dataMap = {};
var searchKeyWord="";
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


    initTree("loc",locData);
    initTreeInput("loc");
    initTree("behavior",behaviorData);
    initTreeInput("behavior");
    initTree("interest",interestData);
    initTreeInput("interest");
    if(params != undefined){
        $("#templateName").val(params.templateName);
        $("#id").val(params.id);
        var wcates =  params.wechat_official_account_category;
        if(wcates != null&&wcates!=undefined){
            $('#wcates_switch').trigger("click");
            initMultiselect("wcates_value", wcates);

        }
        if(params.locs != null && params.locs != undefined){
            initTreeNodeChecked("loc",params.locs)
        }
        var age_min = params.age_min;
        var age_max = params.age_max;
        if(age_min != null&&age_min != undefined){
            $("#age_min").val(age_min).select2();
            $("#age_max").val(age_max).select2();
        }
        var gender = params.gender;
        if(gender != null && gender != undefined){
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

        /*var actions = params.actions;
        if(actions != null){
            $('#zyx_switch').trigger("click");
            $('input[name="actions"]').each(function(){
                if(actions.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
        }
        var excludedActions = params.actions;
        if(excludedActions != null){
            $('#pcyx_switch').trigger("click");
            $('input[name="excludedActions"]').each(function(){
                if(actions.indexOf($(this).val()) > -1){
                    $.uniform.update($(this).prop("checked",true));
                }
            });
        }*/
        var marital_status = params.marital_status;
        if(marital_status != null&&marital_status!=undefined){
            $("#hlzt_switch").trigger("click");
            initMultiselect("marital_status_value", marital_status);
        }
        var residentialCommunityPrice_min = params.residentialCommunityPrice_min;
        var residentialCommunityPrice_max = params.residentialCommunityPrice_max;
        if(residentialCommunityPrice_min != null&&residentialCommunityPrice_min != undefined){
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
        $.uniform.update();
    }else{

    }

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
function changebillingEvent(){
    var billingEvent = $("input[name='billing_event']:checked").val();
    if(billingEvent == "CPC"){
        $("#bidAmount").attr("placeholder", "CPC出价要求介于0.5元-100元之间");
    }else if(billingEvent == "OCPA"){
        $("#bidAmount").attr("placeholder", "OCPA出价要求介于0.1元-2000元之间");
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




function saveAdGroup(){
    var params = {};
    var templateName = $("#templateName").val();
    if(templateName == ""){
        $.alertWarning("模板名称不能为空");
        return false;
    }else{
        params["templateName"] = templateName;
    }


    var targeting = getTargetingInfo(params);
    if(!targeting){
        return targeting;
    }
    var result = false;
    layer.load(1, {
        shade: [0.5, '#cac6c5'] //0.5透明度的白色背景
    });
    $.ajax({url:"/enjoy-web/admin/targetingTemplate/save",
        data:params,
        type:"POST",
        success:function(data){
            layer.closeAll('loading');
            if (data.status == 1) {
                loadMenu();
            }else{
                $.alertWarning(data.msg);
            }
        },
        error:function(xhr, status){
            layer.closeAll('loading');
            $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
        }
    });
    return result;
}

function getTargetingInfo(params){
    var id = $("#id").val();
    var method = $("#method").val();
    if(id!=""){
        params["id"] = id;
    }
    var locMap = dataMap["loc"];
    if(locMap != null){
        var region = [];
        var business = [];
        locMap.forEach(function (value, key, map) {
            if(value.areatype == "region"){
                region.push(key);
            }else if(value.areatype == "business"){
                business.push(key);
            }
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
            params["geoLocation"] = JSON.stringify(geoLocation);
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
            params["age"] = JSON.stringify([age]);
        }
    }
    var gender=$('input:radio[name="gender"]:checked').val();
    if(gender != ""){
        params["gender"] = JSON.stringify([gender]);
    }

    if(!$("#behavior_interest_switch").is(':checked')){
        var behaviorMap = dataMap["behavior"];
        var behavior = [];
        var interest = [];
        if(behaviorMap != null){
            behaviorMap.forEach(function(value, key, map){
                behavior.push(value.id);
            });
        }
        var interestMap= dataMap["interest"];
        if(interestMap != null){
            interestMap.forEach(function (value, key,map) {
                interest.push(value.id);
            });
        }
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
            params["behaviorOrInterest"] = JSON.stringify(behaviorOrInterest);
        }
    }
    if(!$("#wcates_switch").is(':checked')){
        var wcates_selected = [];
        $('#wcates_select option:selected').each(function () {
            wcates_selected.push($(this).val());
        });
        if(wcates_selected.length > 0){
            params["wechatOfficialAccountCategory"] = JSON.stringify(wcates_selected);
        }
    }
    if(!$("#hlzt_switch").is(':checked')){
        var marital_status_selected = [];
        $('#marital_status_select option:selected').each(function () {
            marital_status_selected.push($(this).val());
        });
        if(marital_status_selected.length > 0){
            params["maritalStatus"] = JSON.stringify(marital_status_selected);
        }
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
                params["residentialCommunityPrice"] = JSON.stringify([residentialCommunityPrice]);
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
                params["temperature"] = JSON.stringify([temperature]);
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
        }
        if(excludedActions.length > 0){
            wechatAdBehavior.excluded_actions = excludedActions;
        }
        params["targeting.wechatAdBehavior"] = JSON.stringify(wechatAdBehavior);
    }*/
    $(".switchcheck").each(function () {
        if(!$(this).is(":checked")){
            var name = $(this).data("check-name");
            var value = [];
            $('input[name="'+name+'"]:checked').each(function(){
                value.push($(this).val());
            });
            if(value.length > 0){
                params[name] = JSON.stringify(value);
            }
        }
    });

    return true;
}