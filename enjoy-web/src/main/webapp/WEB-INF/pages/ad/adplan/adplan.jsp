<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${webTitle}</title>
	<link href="https://fonts.loli.net/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
	<link href="${css}/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
	<link href="${css}/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/core.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/components.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/colors.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/common.css" rel="stylesheet" type="text/css">
    <link href="${css}/tool.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/layer/theme/default/layer.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/validation/bootstrapValidator.min.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${scripts}/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/tags/tagsinput.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/blockui.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery_ui/interactions.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/ui/moment/moment.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/daterangepicker.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/selects/select2.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/uniform.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/switch.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/switchery.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/wizards/stepy.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jasny_bootstrap.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/layer/layer.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/notifications/sweet_alert.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/notifications/pnotify.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/validation/validate.min.js"></script>
    <link media="all" rel="stylesheet" type="text/css" href="${scripts}/plugins/uploaders/css/fileinput.min.css"/>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/plugins/piexif.min.js" ></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/fileinput.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/themes/fa/theme.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/locales/zh.js"></script>
    <link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
	<script src="${scripts}/plugins/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
	<script src="${scripts}/plugins/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
	<script src="${scripts}/plugins/ztree/jquery.ztree.exhide.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${scripts}/core/app.js"></script>
	<script type="text/javascript" src="${scripts}/pages/form_select2.js"></script>
	<script type="text/javascript" src="https://unpkg.com/qiniu-js@2.5.4/dist/qiniu.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_select.min.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_bootstrap_select.js"></script>
    <script type="text/javascript" src="${scripts }/base.js?v=1.3"></script>
    <style type="text/css">
        .stepy-header li.stepy-active div {
            cursor: auto;
            border: 2px solid #e2e4e7;
            background-color: #4e85e2;
            color: #fff;
        }

        .stepy-header li div {
            background-color: #4e85e2;
            color: #fff;
            border: 2px solid #e2e4e7;
        }

        .stepy-header li:before,
        .stepy-header li:after {
            background-color: #4e85e2;
            z-index: 9;
        }

        .line-form-value{
            height: 34px;
            line-height: 34px;
        }
    </style>
</head>

<body>
    <%@include file="../../../include/adtop.jsp" %>
    <!-- Page container -->
    <div class="page-container">

        <!-- Page content -->
        <div class="page-content">

            <!-- Main content -->
            <div class="content-wrapper">

                <!-- Dashboard content -->
                <div class="row">

                    <div class="panel panel-flat col-xs-12 col-sm-12 col-md-12 col-lg-12">

                        <div class="panel-body">

                            <!-- Clickable title -->
                            <div class="stepy-callbacks">
                                <input type="hidden" id="campaignId" name="campaignId" value="0">
                                <input type="hidden" id="adGroupId" name="adGroupId" value="0">
                                <input type="hidden" id="adCreativeId" name="adCreativeId" value="0">
                                <fieldset title="1">
                                    <legend class="text-semibold">推广计划</legend>

                                    <h4 class="fw-bold mb-20">新建广告计划</h4>
                                    <div class="form-group form-horizontal row">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                                            <label class="radio-inline">
                                                <input type="radio" name="add_type" checked value="new_campaign" class="control-success" onchange="changeAddType()" >
                                                新计划
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                                            <label class="radio-inline">
                                                <input type="radio" name="add_type" value="old_campaign" class="control-success" onchange="changeAddType()">
                                                已有计划
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group form-horizontal row" id="new_campaign_div">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计划名称</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <input type="text" name="campaignName" id="campaignName" placeholder="输入计划名称" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group row" id="old_campaign_div" style="display: none;">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计划</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <select class="bootstrap-select" data-live-search="true"  id="campaign_select" onchange="changeCampaign()">
                                                <option value="0">选择计划</option>
                                                <c:forEach items="${data.campaigns}" var="obj">
                                                    <option value="${obj.id}" data-campaign='${el:toJsonString(obj)}'>${obj.id}-${obj.campaignName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">广告版位</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <select name="adsense" id="adsense" class="select" onchange="changeAdsense(this)">
                                                <option></option>
                                                <c:forEach var="obj" items="${data.adsenses.values()}">
                                                    <c:choose>
                                                        <c:when test="${adv.wxAuthStatus == 2}">
                                                            <c:if test="${!fn:startsWith(obj.adsense, 'WECHAT_MOMENTS')}">
                                                                <option value="${obj.adsense}">${obj.name}</option>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${obj.adsense}">${obj.name}</option>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">推广计划类型</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <select name="campaignType" id="campaignType"  class="select">
                                                <option value=""></option>
                                                <option value="CAMPAIGN_TYPE_NORMAL">普通广告/微信公众号/小程序广告</option>
                                                <c:if test="${adv.wxAuthStatus == 1}">
                                                    <option value="CAMPAIGN_TYPE_WECHAT_MOMENTS">微信朋友圈</option>
                                                </c:if>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">推广目标</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <select name="promotedObjectType" id="promotedObjectType" class="select">
                                                <option value="PROMOTED_OBJECT_TYPE_ECOMMERCE" selected>推广我的商品</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group form-horizontal row">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计划日限额</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <input type="text" name="dailyBudget" id="dailyBudget" class="form-control" placeholder="此计划内所有广告的每日最高总花费,50元-4,000,000元">
                                        </div>

                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <div class="checkbox checkbox-switchery">
                                                <label>
                                                    <input name="configuredStatus" id="configuredStatus" type="checkbox"  class="form-control switchery" checked="checked">
                                                    启用/暂停
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>


                                <fieldset title="2">
                                    <legend class="text-semibold">广告条件</legend>
                                    <div class="col-md-12">
                                        <h4 class="fw-bold mb-20">新建广告</h4>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">广告名称</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="adGroupName" id="adGroupName" class="form-control">
                                            </div>
                                        </div>

                                        <h4 class="fw-bold mb-20">投放时间</h4>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">类型</label>
                                            <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                                                <label class="checkbox-inline">
                                                    <input type="radio" name="dateType" checked value="all_day" class="control-success" onchange="changeDateType()" >
                                                    全天投放
                                                </label>
                                                <label class="checkbox-inline">
                                                    <input type="radio" name="dateType" value="custom_time" class="control-success" onchange="changeDateType()">
                                                    自定义时段
                                                </label>
                                            </div>
                                        </div>

                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">投放日期 </label>
                                            <div class="input-group col-xs-6 col-sm-6 col-md-6 col-lg-6 pl-10">
                                                <input id="put_date" name="put_date" type="text" class="form-control daterange-basic">
                                                <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                            </div>
                                        </div>

                                        <div class="form-group row" id="datetime" style="display: none;">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">每日投放时段</label>
                                            <div class="col-md-10" id="custom_time_div">
                                                <div class="row" id="custom_time_template" style="display: none;">
                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <i class="icon-alarm"></i>
                                                            </div>
                                                            <select class="form-control start_time" data-width="100%">
                                                                <option value="00:00">00:00</option>
                                                                <option value="00:30">00:30</option>
                                                                <option value="01:00">01:00</option>
                                                                <option value="01:30">01:30</option>
                                                                <option value="02:00">02:00</option>
                                                                <option value="02:30">02:30</option>
                                                                <option value="03:00">03:00</option>
                                                                <option value="03:30">03:30</option>
                                                                <option value="04:00">04:00</option>
                                                                <option value="04:30">04:30</option>
                                                                <option value="05:00">05:00</option>
                                                                <option value="05:30">05:30</option>
                                                                <option value="06:00">06:00</option>
                                                                <option value="06:30">06:30</option>
                                                                <option value="07:00">07:00</option>
                                                                <option value="07:30">07:30</option>
                                                                <option value="08:00">08:00</option>
                                                                <option value="08:30">08:30</option>
                                                                <option value="09:00">09:00</option>
                                                                <option value="09:30">09:30</option>
                                                                <option value="10:00">10:00</option>
                                                                <option value="10:30">10:30</option>
                                                                <option value="11:00">11:00</option>
                                                                <option value="11:30">11:30</option>
                                                                <option value="12:00">12:00</option>
                                                                <option value="12:30">12:30</option>
                                                                <option value="13:00">13:00</option>
                                                                <option value="13:30">13:30</option>
                                                                <option value="14:00">14:00</option>
                                                                <option value="14:30">14:30</option>
                                                                <option value="15:00">15:00</option>
                                                                <option value="15:30">15:30</option>
                                                                <option value="16:00">16:00</option>
                                                                <option value="16:30">16:30</option>
                                                                <option value="17:00">17:00</option>
                                                                <option value="17:30">17:30</option>
                                                                <option value="18:00">18:00</option>
                                                                <option value="18:30">18:30</option>
                                                                <option value="19:00">19:00</option>
                                                                <option value="19:30">19:30</option>
                                                                <option value="20:00">20:00</option>
                                                                <option value="20:30">20:30</option>
                                                                <option value="21:00">21:00</option>
                                                                <option value="21:30">21:30</option>
                                                                <option value="22:00">22:00</option>
                                                                <option value="22:30">22:30</option>
                                                                <option value="23:00">23:00</option>
                                                                <option value="23:30">23:30</option>
                                                            </select>

                                                        </div>
                                                    </div>
                                                    <span class="col-md-1" style="width: 20px">-</span>
                                                    <div class="col-md-6 form-group has-feedback has-feedback-left">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <i class="icon-alarm"></i>
                                                            </div>
                                                            <select class="form-control end_time" data-width="100%">
                                                                <option value="00:00">00:00</option>
                                                                <option value="00:30">00:30</option>
                                                                <option value="01:00">01:00</option>
                                                                <option value="01:30">01:30</option>
                                                                <option value="02:00">02:00</option>
                                                                <option value="02:30">02:30</option>
                                                                <option value="03:00">03:00</option>
                                                                <option value="03:30">03:30</option>
                                                                <option value="04:00">04:00</option>
                                                                <option value="04:30">04:30</option>
                                                                <option value="05:00">05:00</option>
                                                                <option value="05:30">05:30</option>
                                                                <option value="06:00">06:00</option>
                                                                <option value="06:30">06:30</option>
                                                                <option value="07:00">07:00</option>
                                                                <option value="07:30">07:30</option>
                                                                <option value="08:00">08:00</option>
                                                                <option value="08:30">08:30</option>
                                                                <option value="09:00">09:00</option>
                                                                <option value="09:30">09:30</option>
                                                                <option value="10:00">10:00</option>
                                                                <option value="10:30">10:30</option>
                                                                <option value="11:00">11:00</option>
                                                                <option value="11:30">11:30</option>
                                                                <option value="12:00">12:00</option>
                                                                <option value="12:30">12:30</option>
                                                                <option value="13:00">13:00</option>
                                                                <option value="13:30">13:30</option>
                                                                <option value="14:00">14:00</option>
                                                                <option value="14:30">14:30</option>
                                                                <option value="15:00">15:00</option>
                                                                <option value="15:30">15:30</option>
                                                                <option value="16:00">16:00</option>
                                                                <option value="16:30">16:30</option>
                                                                <option value="17:00">17:00</option>
                                                                <option value="17:30">17:30</option>
                                                                <option value="18:00">18:00</option>
                                                                <option value="18:30">18:30</option>
                                                                <option value="19:00">19:00</option>
                                                                <option value="19:30">19:30</option>
                                                                <option value="20:00">20:00</option>
                                                                <option value="20:30">20:30</option>
                                                                <option value="21:00">21:00</option>
                                                                <option value="21:30">21:30</option>
                                                                <option value="22:00">22:00</option>
                                                                <option value="22:30">22:30</option>
                                                                <option value="23:00">23:00</option>
                                                                <option value="23:30">23:30</option>
                                                            </select>
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="addCustomTime()"><i class="icon-add"></i></a></div>
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="subCustomTime(this)"><i class="icon-subtract"></i></a></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row custom_time">
                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <i class="icon-alarm"></i>
                                                            </div>
                                                            <select class="form-control start_time" id="start_time" data-width="100%">
                                                                <option value="00:00">00:00</option>
                                                                <option value="00:30">00:30</option>
                                                                <option value="01:00">01:00</option>
                                                                <option value="01:30">01:30</option>
                                                                <option value="02:00">02:00</option>
                                                                <option value="02:30">02:30</option>
                                                                <option value="03:00">03:00</option>
                                                                <option value="03:30">03:30</option>
                                                                <option value="04:00">04:00</option>
                                                                <option value="04:30">04:30</option>
                                                                <option value="05:00">05:00</option>
                                                                <option value="05:30">05:30</option>
                                                                <option value="06:00">06:00</option>
                                                                <option value="06:30">06:30</option>
                                                                <option value="07:00">07:00</option>
                                                                <option value="07:30">07:30</option>
                                                                <option value="08:00">08:00</option>
                                                                <option value="08:30">08:30</option>
                                                                <option value="09:00">09:00</option>
                                                                <option value="09:30">09:30</option>
                                                                <option value="10:00">10:00</option>
                                                                <option value="10:30">10:30</option>
                                                                <option value="11:00">11:00</option>
                                                                <option value="11:30">11:30</option>
                                                                <option value="12:00">12:00</option>
                                                                <option value="12:30">12:30</option>
                                                                <option value="13:00">13:00</option>
                                                                <option value="13:30">13:30</option>
                                                                <option value="14:00">14:00</option>
                                                                <option value="14:30">14:30</option>
                                                                <option value="15:00">15:00</option>
                                                                <option value="15:30">15:30</option>
                                                                <option value="16:00">16:00</option>
                                                                <option value="16:30">16:30</option>
                                                                <option value="17:00">17:00</option>
                                                                <option value="17:30">17:30</option>
                                                                <option value="18:00">18:00</option>
                                                                <option value="18:30">18:30</option>
                                                                <option value="19:00">19:00</option>
                                                                <option value="19:30">19:30</option>
                                                                <option value="20:00">20:00</option>
                                                                <option value="20:30">20:30</option>
                                                                <option value="21:00">21:00</option>
                                                                <option value="21:30">21:30</option>
                                                                <option value="22:00">22:00</option>
                                                                <option value="22:30">22:30</option>
                                                                <option value="23:00">23:00</option>
                                                                <option value="23:30">23:30</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <span class="col-md-1" style="width: 20px">-</span>
                                                    <div class="col-md-5 form-group has-feedback has-feedback-left">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <i class="icon-alarm"></i>
                                                            </div>
                                                            <select class="form-control end_time" id="end_time" data-width="100%">
                                                                <option value="00:00">00:00</option>
                                                                <option value="00:30">00:30</option>
                                                                <option value="01:00">01:00</option>
                                                                <option value="01:30">01:30</option>
                                                                <option value="02:00">02:00</option>
                                                                <option value="02:30">02:30</option>
                                                                <option value="03:00">03:00</option>
                                                                <option value="03:30">03:30</option>
                                                                <option value="04:00">04:00</option>
                                                                <option value="04:30">04:30</option>
                                                                <option value="05:00">05:00</option>
                                                                <option value="05:30">05:30</option>
                                                                <option value="06:00">06:00</option>
                                                                <option value="06:30">06:30</option>
                                                                <option value="07:00">07:00</option>
                                                                <option value="07:30">07:30</option>
                                                                <option value="08:00">08:00</option>
                                                                <option value="08:30">08:30</option>
                                                                <option value="09:00">09:00</option>
                                                                <option value="09:30">09:30</option>
                                                                <option value="10:00">10:00</option>
                                                                <option value="10:30">10:30</option>
                                                                <option value="11:00">11:00</option>
                                                                <option value="11:30">11:30</option>
                                                                <option value="12:00">12:00</option>
                                                                <option value="12:30">12:30</option>
                                                                <option value="13:00">13:00</option>
                                                                <option value="13:30">13:30</option>
                                                                <option value="14:00">14:00</option>
                                                                <option value="14:30">14:30</option>
                                                                <option value="15:00">15:00</option>
                                                                <option value="15:30">15:30</option>
                                                                <option value="16:00">16:00</option>
                                                                <option value="16:30">16:30</option>
                                                                <option value="17:00">17:00</option>
                                                                <option value="17:30">17:30</option>
                                                                <option value="18:00">18:00</option>
                                                                <option value="18:30">18:30</option>
                                                                <option value="19:00">19:00</option>
                                                                <option value="19:30">19:30</option>
                                                                <option value="20:00">20:00</option>
                                                                <option value="20:30">20:30</option>
                                                                <option value="21:00">21:00</option>
                                                                <option value="21:30">21:30</option>
                                                                <option value="22:00">22:00</option>
                                                                <option value="22:30">22:30</option>
                                                                <option value="23:00">23:00</option>
                                                                <option value="23:30">23:30</option>
                                                            </select>
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="addCustomTime()"><i class="icon-add"></i></a></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>


                                        </div>
                                        <c:choose>
                                            <c:when test="${empty targetings}">
                                                <h4 class="fw-bold mb-20">定向条件</h4>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="form-group row">
                                                    <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">定向条件</label>
                                                    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                        <select class="bootstrap-select" data-live-search="true"  id="targetingTemplate" onchange="changeTemplate()">
                                                            <option value="0">选择定向模板</option>
                                                            <c:forEach items="${targetings}" var="obj">
                                                                <option value="${obj.id}">${obj.templateName}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">地域</label>
                                            <input type="hidden" name="location_types" id="location_types" value="LIVE_IN">
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" class="form-control col-md-10" id="locinput" placeholder="地域列表">
                                            </div>
                                            <div class="dis-ib">
                                                <a href="javascript:void(0)" id="locinput_removeall">清空</a>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal has-feedback row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2"></label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" class="form-control input-lg" id="search_input_loc" onKeyUp="search('loc');" placeholder="搜索国家、省、市、区、商圈">
                                                <div class="form-control-feedback mr-10">
                                                    <i class="icon-search4 text-size-base"> </i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal has-feedback row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2"></label>
                                            <div style="min-width: 240px;" class="ml-10 col-xs-6 col-sm-6 col-md-6 col-lg-6 p-20 border-all-default">
                                                <div id="searchloc" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                <div id="loc" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">年龄</label>
                                            <div style="min-width:140px;" class="dis-ib ml-10">
                                                <select name="age_min" id="age_min" class="select">
                                                    <option value="">起始年龄</option>
                                                    <c:forEach var="i" begin="14" end="66" step="1">
                                                        <c:choose>
                                                            <c:when test="${i != 66}">
                                                                <option value="${i}">${i}岁</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${i}">${i}岁以上</option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="dis-ib">&nbsp;&nbsp;-&nbsp;&nbsp;</div>
                                            <div style="min-width:140px;" class="dis-ib">
                                                <select id="age_max" name="age_max" class="select">
                                                    <option value="">结束年龄</option>
                                                    <c:forEach var="i" begin="14" end="66" step="1">
                                                        <c:choose>
                                                            <c:when test="${i != 66}">
                                                                <option value="${i}">${i}岁</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${i}">${i}岁以上</option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">性别</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <label class="checkbox-inline">
                                                    <input type="radio" name="gender" value="" class="control-success" checked="checked">
                                                    全部
                                                </label>
                                                <label class="checkbox-inline">
                                                    <input type="radio" name="gender" value="MALE" class="control-success" >
                                                    男
                                                </label>
                                                <label class="checkbox-inline">
                                                    <input type="radio" name="gender" value="FEMALE" class="control-success">
                                                    女
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">兴趣/行为</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <div class="checkbox checkbox-switchery">
                                                    <label>
                                                        <input type="checkbox" id="behavior_interest_switch" class="switchery" checked="checked" onclick="switchChange(this, 'behavior_interest')">
                                                        全部</label>
                                                </div>
                                                <div id="behavior_interest" style="display: none;">
                                                    <div class="form-group col-md-12 bottom-border">
                                                        <label class="control-label col-md-2">兴趣</label>
                                                        <div class="col-md-10">
                                                            <div class="row">
                                                                <div class="form-group has-feedback has-feedback-left">
                                                                    <input type="text" class="form-control col-md-10" id="interestinput" placeholder="兴趣列表">
                                                                    <a href="javascript:void(0)" id="interestinput_removeall">清空</a>
                                                                </div>
                                                                <div class="form-group has-feedback has-feedback-left">
                                                                    <input type="text" class="form-control input-lg" id="search_input_interest" onblur="searchtags('INTEREST');" placeholder="搜索兴趣">
                                                                    <div class="form-control-feedback">
                                                                        <i class="icon-search4 text-size-base"></i>
                                                                    </div>
                                                                    <select multiple="multiple" id="interest_tags" style="display:none;" class="form-control">
                                                                    </select>
                                                                    <div id="interest" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                    <div class="form-group col-md-12 bottom-border">
                                                        <label class="control-label col-md-2">行为</label>
                                                        <div class="col-md-10">
                                                            <div class="row">
                                                                <div class="form-group has-feedback has-feedback-left">
                                                                    <input type="text" class="form-control col-md-10" id="behaviorinput" placeholder="行为列表">
                                                                    <a href="javascript:void(0)" id="behaviorinput_removeall">清空</a>
                                                                </div>
                                                                <div class="form-group has-feedback has-feedback-left">
                                                                    <input type="text" class="form-control input-lg" id="search_input_behavior" onblur="searchtags('BEHAVIOR');" placeholder="搜索行为">
                                                                    <div class="form-control-feedback">
                                                                        <i class="icon-search4 text-size-base"></i>
                                                                    </div>
                                                                    <select multiple="multiple" id="behavior_tags" style="display:none;" class="form-control">
                                                                    </select>
                                                                    <div id="behavior" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-12 bottom-border">
                                                        <label class="control-label col-md-2">行为场景</label>
                                                        <div class="col-md-10">
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_ALL" type="checkbox" class="control-success">
                                                                            不限
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_APP" type="checkbox" class="control-success">
                                                                            APP
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_ECOMMERCE" type="checkbox" class="control-success">
                                                                            电商
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_INFORMATION" type="checkbox" class="control-success">
                                                                            咨询
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-12 bottom-border">
                                                        <label class="control-label col-md-2">行为时效性</label>
                                                        <div class="col-md-10">
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left" style="margin-bottom: 10px;">
                                                                        <select class="form-control" id="time_window" data-width="100%">
                                                                            <option value="">请选择</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_SEVEN_DAY">7 天</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_FIFTEEN_DAY">15 天</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_THIRTY_DAY">30 天</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_THREE_MONTH">3 个月</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_SIX_MONTH">6 个月</option>
                                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_ONE_YEAR">1 年</option>
                                                                        </select>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-12 bottom-border">
                                                        <label class="control-label col-md-2">行为强度</label>
                                                        <div class="col-md-10">
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="intensity"  value="BEHAVIOR_INTEREST_INTENSITY_ALL" type="checkbox" class="control-success">
                                                                            不限
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="intensity" value="BEHAVIOR_INTEREST_INTENSITY_HIGH" type="checkbox" class="control-success">
                                                                            高
                                                                        </label>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">自定义分类</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="customizedCategory", id="customizedCategory" class="form-control" style="margin-bottom: 30px" placeholder="关键词用半角逗号','分隔，如：本地生活,餐饮">
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">公众号媒体类型</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <div class="checkbox checkbox-switchery">
                                                    <label>
                                                        <input type="checkbox" id="wcates_switch"  class="switchery" checked="checked" onclick="switchChange(this, 'wcates_select')">
                                                        全部</label>
                                                </div>
                                                <div id="wcates_select" style="display: none;">
                                                    <div class="form-group col-md-12" style="margin-bottom:10px;">
                                                        <div class="multi-select-full">
                                                            <select class="multiselect-filtering" id="wcates_value" multiple="multiple">
                                                                <c:forEach items="${wechatCateData}" var="obj">
                                                                    <option value="${obj.id}">${obj.name}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">自动扩量</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <div class="checkbox checkbox-switchery">
                                                    <label>
                                                        <input type="checkbox" class="switchery" id="expand_enabled_switch"   onclick="switchChange1(this,'expand_enabled')">
                                                        是/否</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" id="expand_enabled" style="display: none">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2"></label>
                                            <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                                                <div class="form-group form-horizontal">
                                                    <label class="control-label text-semibold col-xs-2 col-sm-2 col-md-2 col-lg-2" style="width: 120px !important;">不可突破定向</label>
                                                    <label class="checkbox-inline">
                                                        <input name="expand_targeting" value="geo_location" type="checkbox" class="control-success">
                                                        地理位置
                                                    </label>

                                                    <label class="checkbox-inline">
                                                        <input name="expand_targeting" value="age" type="checkbox" class="control-success">
                                                        年龄
                                                    </label>
                                                    <label class="checkbox-inline">
                                                        <input name="expand_targeting" value="gender" type="checkbox" class="control-success">
                                                        性别
                                                    </label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="panel panel-flat no-border">
                                            <div class="panel-heading">
                                                <h6 class="panel-title text-primary">更多定向信息 <ul class="icons-list dis-i">
                                                    <li class="dis-i"><a class="text-primary" data-action="collapse"> </a></li>
                                                </ul>
                                                </h6>
                                            </div>
                                            <div class="panel-body no-border">
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">学历</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="学历" data-check-name="education" checked="checked" onclick="switchChange(this,'xueli')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div id="xueli" style="display: none" class="col-md-12">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="DOCTOR" type="checkbox" class="control-success">
                                                                            博士
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="MASTER" type="checkbox" class="control-success">
                                                                            硕士
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="BACHELOR" type="checkbox" class="control-success">
                                                                            本科
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="SENIOR" type="checkbox" class="control-success">
                                                                            专科
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="JUNIOR" type="checkbox" class="control-success">
                                                                            高中
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="PRIMARY" type="checkbox" class="control-success">
                                                                            初中
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="education" value="JUNIOR_COLLEGE" type="checkbox" class="control-success">
                                                                            小学
                                                                        </label>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">婚恋状态</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" id="hlzt_switch" class="switchery" checked="checked" onchange="switchChange(this,'hlzt')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="hlzt" style="display:none;">
                                                                    <div id="marital_status_select">
                                                                        <div class="form-group col-md-12" style="margin-bottom:10px">
                                                                            <input type="hidden" id="maritalStatus" name="maritalStatus" value="">
                                                                            <div class="multi-select-full">
                                                                                <select class="multiselect-filtering" id="marital_status_value" multiple="multiple">
                                                                                    <option value="SINGLE">单身</option>
                                                                                    <option value="IN_LOVE">恋爱</option>
                                                                                    <option value="NEWLY_MARRIED">新婚</option>
                                                                                    <option value="MARRIED">已婚</option>
                                                                                    <option value="PARENTING">育儿</option>
                                                                                    <option value="PARENTING_0">育儿（孕育中）</option>
                                                                                    <option value="PARENTING_0_6">育儿（宝宝 0-6 个月）</option>
                                                                                    <option value="PARENTING_6_12">育儿（宝宝 6-12 个月</option>
                                                                                    <option value="PARENTING_12_24">育儿（宝宝 1-2 岁）</option>
                                                                                    <option value="PARENTING_24_36">育儿（宝宝 2-3 岁）</option>
                                                                                    <option value="CHILD_PRE_SCHOOL">育儿（孩子 3-6 周岁）</option>
                                                                                    <option value="CHILD_PRIMARY_SCHOOL">育儿（孩子 6-12 周岁）</option>
                                                                                    <option value="CHILD_JUNIOR_SCHOOL">育儿（孩子 12-15 周岁）</option>
                                                                                    <option value="CHILD_HIGH_SCHOOL">育儿（孩子 15-18 周岁）</option>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">工作状态</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="工作状态" data-check-name="workingStatus" checked="checked" onchange="switchChange(this,'gzzt')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="gzzt" style="display:none;">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="COLLEGE_STUDENT" type="checkbox" class="control-success">
                                                                            在校大学生
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="BUSINESS_USER" type="checkbox" class="control-success">
                                                                            商旅用户
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="GOVERNMENT_OFFICER" type="checkbox" class="control-success">
                                                                            政府公职人员
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="SCIENCE_EDUCATOR" type="checkbox" class="control-success">
                                                                            科研教育者
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="IT_WORKER" type="checkbox" class="control-success">
                                                                            IT 互联网工作者
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="workingStatus" value="HEALTH_CARE_WORKER" type="checkbox" class="control-success">
                                                                            医护工作者
                                                                        </label>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">操作系统</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="操作系统" data-check-name="userOs" checked="checked" onchange="switchChange(this,'oscheck')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="oscheck" style="display: none;">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="userOs" value="IOS" type="checkbox" class="control-success">
                                                                            ios系统
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="userOs" value="ANDROID" type="checkbox" class="control-success">
                                                                            安卓系统
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">新设备</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="新设备" data-check-name="newDevice" checked="checked" onchange="switchChange(this,'xsb')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="xsb" style="display: none;">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="newDevice" value="IOS" type="checkbox" class="control-success">
                                                                            iOS 新用户
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="newDevice" value="ANDROID" type="checkbox" class="control-success">
                                                                            Android 新用户
                                                                        </label>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">手机价格</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-6 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="手机价格" data-check-name="devicePrice" checked="checked" onchange="switchChange(this,'phone_price_check')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="phone_price_check" style="display:none;">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="devicePrice" value="PRICE_1500_LESS" type="checkbox" class="control-success">
                                                                            1500元以下
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="devicePrice" value="PRICE_1500_2500"  type="checkbox" class="control-success">
                                                                            1500~2500元
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="devicePrice" value="PRICE_2500_3500" type="checkbox" class="control-success">
                                                                            2500~3500元
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="devicePrice" value="PRICE_3500_4500" type="checkbox" class="control-success">
                                                                            3500元~4500元
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="devicePrice" value="PRICE_4500_MORE" type="checkbox" class="control-success">
                                                                            4500元以上
                                                                        </label>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">运营商</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="运营商" data-check-name="networkOperator" checked="checked" onchange="switchChange(this,'yls')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="yls" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkOperator" value="CTC" type="checkbox" class="control-success">
                                                                            电信
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkOperator" value="CMCC" type="checkbox" class="control-success">
                                                                            移动
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkOperator" value="CUC" type="checkbox" class="control-success">
                                                                            联通
                                                                        </label>
                                                                    </div>


                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">联网方式</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="联网方式" data-check-name="networkType" checked="checked" onchange="switchChange(this,'lwfs')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="lwfs" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkType" value="WIFI" type="checkbox" class="control-success">
                                                                            wifi
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkType" value="NET_4G" type="checkbox" class="control-success">
                                                                            4G
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkType" value="NET_3G" type="checkbox" class="control-success">
                                                                            3G
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkType" value="NET_2G" type="checkbox" class="control-success">
                                                                            2G
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">上网场景</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="上网场景" data-check-name="networkScene" checked="checked" onchange="switchChange(this,'swcj')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="swcj" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkScene" value="PUBLIC_PLACE" type="checkbox" class="control-success">
                                                                            公共场所
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkScene" value="HOME" type="checkbox" class="control-success">
                                                                            家庭
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkScene" value="COMPANY" type="checkbox" class="control-success">
                                                                            企业
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="networkScene" value="SCHOOL" type="checkbox" class="control-success">
                                                                            学校
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">消费能力</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="消费能力" data-check-name="consumptionStatus" checked="checked" onchange="switchChange(this,'xfnl')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="xfnl" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="consumptionStatus" value="HIGH" type="checkbox" class="control-success">
                                                                            高消费
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="consumptionStatus" value="LOW" type="checkbox" class="control-success">
                                                                            低消费
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">居民社区价格</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery" id="sqjg_switch" checked="checked" onchange="switchChange(this,'sqjg')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="sqjg" style="display: none">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <select id="residential_community_price_min" name="residential_community_price_min" class="select">
                                                                            <option value="">起始价格</option>
                                                                            <option value="1">1元/m²</option>
                                                                            <option value="1000">1000元/m²</option>
                                                                            <option value="2000">2000元/m²</option>
                                                                            <option value="3000">3000元/m²</option>
                                                                            <option value="4000">4000元/m²</option>
                                                                            <option value="5000">5000元/m²</option>
                                                                            <option value="6000">6000元/m²</option>
                                                                            <option value="7000">7000元/m²</option>
                                                                            <option value="8000">8000元/m²</option>
                                                                            <option value="9000">9000元/m²</option>
                                                                            <option value="10000">10000元/m²</option>
                                                                            <option value="20000">20000元/m²</option>
                                                                            <option value="30000">30000元/m²</option>
                                                                            <option value="40000">40000元/m²</option>
                                                                            <option value="50000">50000元/m²</option>
                                                                            <option value="60000">60000元/m²</option>
                                                                            <option value="70000">70000元/m²</option>
                                                                            <option value="80000">80000元/m²</option>
                                                                            <option value="90000">90000元/m²</option>
                                                                            <option value="100000">100000元/m²</option>
                                                                        </select>
                                                                    </div>
                                                                    <span class="col-md-1" style="width: 20px">-</span>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <select id="residential_community_price_max" name="residential_community_price_max" class="select">
                                                                            <option value="">结束价格</option>
                                                                            <option value="1000">1000元/m²</option>
                                                                            <option value="2000">2000元/m²</option>
                                                                            <option value="3000">3000元/m²</option>
                                                                            <option value="4000">4000元/m²</option>
                                                                            <option value="5000">5000元/m²</option>
                                                                            <option value="6000">6000元/m²</option>
                                                                            <option value="7000">7000元/m²</option>
                                                                            <option value="8000">8000元/m²</option>
                                                                            <option value="9000">9000元/m²</option>
                                                                            <option value="10000">10000元/m²</option>
                                                                            <option value="20000">20000元/m²</option>
                                                                            <option value="30000">30000元/m²</option>
                                                                            <option value="40000">40000元/m²</option>
                                                                            <option value="50000">50000元/m²</option>
                                                                            <option value="60000">60000元/m²</option>
                                                                            <option value="70000">70000元/m²</option>
                                                                            <option value="80000">80000元/m²</option>
                                                                            <option value="90000">90000元/m²</option>
                                                                            <option value="100000">100000元/m²</option>
                                                                            <option value="999999999">10万以上/m²</option>
                                                                        </select>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">资产状态</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="资产状态" data-check-name="financialSituation" checked="checked" onchange="switchChange(this,'zczt')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="zczt" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="financialSituation" value="CAR_OWNERS" type="checkbox" class="control-success">
                                                                            有车人士
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="financialSituation" value="HOME_OWNERS" type="checkbox" class="control-success">
                                                                            有房人士
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">消费类型</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="消费类型" data-check-name="consumptionType" checked="checked" onchange="switchChange(this,'xflx')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="xflx" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="consumptionType" value="PAID_GOODS_VIRTUAL" type="checkbox" class="control-success">
                                                                            虚拟商品
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="consumptionType" value="PAID_GOODS_REAL" type="checkbox" class="control-success">
                                                                            实物商品
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <%--<div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">再营销
                                                    </label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery" id="zyx_switch" checked="checked" onchange="switchChange(this,'zyx')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="zyx" style="display: none;">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="actions" value="WECHAT_OFFICIAL_ACCOUNT_FOLLOWED" type="checkbox" class="control-success">
                                                                            已关注你的公众号
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="actions" value="WECHAT_COUPON_OBTAINED" type="checkbox" class="control-success">
                                                                            曾领取你的微信卡券
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="actions" value="WECHAT_OFFICIAL_ACCOUNT_AD_LIKE" type="checkbox" class="control-success">
                                                                            曾对你的微信公众号广告感兴趣
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="actions" value="WECHAT_MOMENTS_AD_LIKE" type="checkbox" class="control-success">
                                                                            曾对你的微信朋友圈广告感兴趣
                                                                        </label>
                                                                    </div>

                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">排除营销
                                                    </label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery" id = "pcyx_switch" checked="checked" onchange="switchChange(this,'pcyx')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="pcyx" style="display:none;">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="excludedActions" value="WECHAT_OFFICIAL_ACCOUNT_FOLLOWED" type="checkbox" class="control-success">
                                                                            已关注你的公众号
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="excludedActions" value="WECHAT_COUPON_OBTAINED" type="checkbox" class="control-success">
                                                                            曾领取你的微信卡券
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="excludedActions" value="WECHAT_OFFICIAL_ACCOUNT_AD_LIKE" type="checkbox" class="control-success">
                                                                            曾对你的微信公众号广告感兴趣
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="excludedActions" value="WECHAT_MOMENTS_AD_LIKE" type="checkbox" class="control-success">
                                                                            曾对你的微信朋友圈广告感兴趣
                                                                        </label>
                                                                    </div>

                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>--%>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">自定义人群</label>
                                                    <div class="col-md-10" style="margin-bottom:10px">
                                                        <div class="row">
                                                            <div class="col-md-10 form-group has-feedback has-feedback-left">
                                                                <div class="multi-select-full">
                                                                    <select class="multiselect-filtering" name="customAudience" id="customAudience" multiple="multiple">
                                                                        <c:forEach items="${audiences}" var="obj">
                                                                            <c:if test="${obj.status == 'SUCCESS'}">
                                                                                <option value="${obj.audience_id}">${obj.name}</option>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">排除自定义人群</label>
                                                    <div class="col-md-10" style="margin-bottom:10px">
                                                        <div class="row">
                                                            <div class="col-md-10 form-group has-feedback has-feedback-left">
                                                                <div class="multi-select-full">
                                                                    <select class="multiselect-filtering" name="excludedCustomAudience" id="excludedCustomAudience" multiple="multiple">
                                                                        <c:forEach items="${audiences}" var="obj">
                                                                            <c:if test="${obj.status == 'SUCCESS'}">
                                                                                <option value="${obj.audience_id}">${obj.name}</option>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">气温</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery" id="qw_switch" checked="checked" onchange="switchChange(this,'qw')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="qw" style="display: none">
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <select id="temperature_min" name="temperature_min" class="form-control" data-width="100%">
                                                                            <c:forEach begin="223" end="323" var="i" step="1" varStatus="status">
                                                                                <c:choose>
                                                                                    <c:when test="${i-223-50 == 0}">
                                                                                        <option value="${i}" selected="selected">${i-223-50}&#8451;</option>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <option value="${i}">${i-223-50}&#8451;</option>
                                                                                    </c:otherwise>
                                                                                </c:choose>

                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                    <span class="col-md-1" style="width: 20px">-</span>
                                                                    <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                                        <select id="temperature_max" name="temperature_max" class="form-control" data-width="100%">
                                                                            <c:forEach begin="223" end="323" var="i" step="1" varStatus="status">
                                                                                <c:choose>
                                                                                    <c:when test="${i-223-50 == 50}">
                                                                                        <option value="${i}" selected="selected">${i-223-50}&#8451;</option>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <option value="${i}">${i-223-50}&#8451;</option>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">紫外线指数</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="紫外线指数" data-check-name="uvIndex" checked="checked" onchange="switchChange(this,'zwx')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="zwx" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="uvIndex" value="WEAK" type="checkbox" class="control-success">
                                                                            弱
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="uvIndex" value="TEND_WEAK" type="checkbox" class="control-success">
                                                                            偏弱
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="uvIndex" value="MEDIUM" type="checkbox" class="control-success">
                                                                            中等
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="uvIndex" value="TEND_STRONG" type="checkbox" class="control-success">
                                                                            偏强
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="uvIndex" value="STRONG" type="checkbox" class="control-success">
                                                                            强
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">穿衣指数</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="穿衣指数" data-check-name="dressingIndex" checked="checked" onchange="switchChange(this,'cy')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="cy" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="FREEZING" type="checkbox" class="control-success">
                                                                            寒冷
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="COLD" type="checkbox" class="control-success">
                                                                            冷
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="CHILLY" type="checkbox" class="control-success">
                                                                            凉
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="COOL" type="checkbox" class="control-success">
                                                                            温凉
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="MILDLY_COOL" type="checkbox" class="control-success">
                                                                            凉舒适
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="MILD" type="checkbox" class="control-success">
                                                                            舒适
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="WARM" type="checkbox" class="control-success">
                                                                            热舒适
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="dressingIndex" value="TORRIDITY" type="checkbox" class="control-success">
                                                                            炎热
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">化妆指数</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="化妆指数" data-check-name="makeupIndex" checked="checked" onchange="switchChange(this,'hz')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="hz" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="makeupIndex" value="PREVENT_CRACKING" type="checkbox" class="control-success">
                                                                            防龟裂
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="makeupIndex" value="MOISTURING" type="checkbox" class="control-success">
                                                                            保湿
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="makeupIndex" value="OIL_CONTROL" type="checkbox" class="control-success">
                                                                            控油
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="makeupIndex" value="UV_PROTECT" type="checkbox" class="control-success">
                                                                            防晒
                                                                        </label>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">气象</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="气象" data-check-name="climate" checked="checked" onchange="switchChange(this,'qx')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="qx" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="SHINE" type="checkbox" class="control-success">
                                                                            晴天
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="CLOUDY" type="checkbox" class="control-success">
                                                                            阴天
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="RAINY" type="checkbox" class="control-success">
                                                                            雨天
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="FOGGY" type="checkbox" class="control-success">
                                                                            雾
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="SNOWY" type="checkbox" class="control-success">
                                                                            雪
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="climate" value="SANDY" type="checkbox" class="control-success">
                                                                            沙尘
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12 bottom-border">
                                                    <label class="control-label col-md-2">空气质量</label>
                                                    <div class="col-md-10">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                                                    <label>
                                                                        <input type="checkbox" class="switchery switchcheck" data-name="空气质量" data-check-name="airQualityIndex" checked="checked" onchange="switchChange(this,'kqzl')">
                                                                        全部
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-12" id="kqzl" style="display: none">
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="GOOD" type="checkbox" class="control-success">
                                                                            优
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="MODERATE" type="checkbox" class="control-success">
                                                                            良
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="LIGHTLY_POLLUTED" type="checkbox" class="control-success">
                                                                            轻度污染
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="MODERATELY_POLLUTED" type="checkbox" class="control-success">
                                                                            中度污染
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="HEAVILY_POLLUTED" type="checkbox" class="control-success">
                                                                            重度污染
                                                                        </label>
                                                                    </div>
                                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                                        <label>
                                                                            <input name="airQualityIndex" value="SEVERELY_POLLUTED" type="checkbox" class="control-success">
                                                                            严重污染
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <h4 class="fw-bold mb-20">出价</h4>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计费类型</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <label class="checkbox-inline" id="CPC_DIV">
                                                    <input type="radio" value="CPC"  name="billing_event"  checked  class="control-success" onchange="changebillingEvent()">
                                                    CPC
                                                </label>
                                                <label class="checkbox-inline">
                                                    <input type="radio" value="OCPA" name="billing_event"  class="control-success" onchange="changebillingEvent()">
                                                    OCPA
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">广告出价</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="bidAmount" id="bidAmount" class="form-control" placeholder="CPC出价要求介于0.5元-100元之间">
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row" id="adgroup_daily_budget_div" style="display: none">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">广告组预算</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="adgroup_daily_budget" id="adgroup_daily_budget" class="form-control" placeholder="日预算要求介于 1,000 元-10,000,000元">
                                            </div>
                                        </div>
                                    </div>
                                    <div  class="col-md-4" id="estimate_div" style="position: fixed;right:15px;overflow: hidden;bottom: 0;">
                                        <div class="panel panel-flat">
                                            <div class="panel-heading">
                                                <h6 class="panel-title"><i class="icon-files-empty position-left"></i>覆盖人群预估</h6>
                                                <div class="heading-elements">
                                                    <ul class="icons-list">
                                                        <a href="javascript:void(0);" onclick="reloadEstimate();" style="color:#000000"><li class="icon-reload-alt"></li></a>
                                                    </ul>
                                                </div>
                                            </div>
                                            <table class="table table-borderless table-xs content-group-sm">
                                                <tbody>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 预估最大覆盖用户数:</td>
                                                    <td class="text-right"><span class="pull-right" id="max_active_user_count">-</span></td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 预估日覆盖用户数最大值:</td>
                                                    <td class="text-right" id="approximate_count">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 预估日曝光量最大值:</td>
                                                    <td class="text-right" id="impression">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 出价下限:</td>
                                                    <td class="text-right" id="min_bid_amount">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 出价上限:</td>
                                                    <td class="text-right" id="max_bid_amount">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i>建议出价:</td>
                                                    <td class="text-right" id="suggest_bid_amount">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i>OCPA广告建议:</td>
                                                    <td class="text-right" id="suggest_bid_content_ocpa"></td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 预估日覆盖用户数:</td>
                                                    <td class="text-right" id="users_daily">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 预估日曝光量:</td>
                                                    <td class="text-right" id="exposure_daily">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 定向状态:</td>
                                                    <td class="text-right" id="targeting_status">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 调整的定向:</td>
                                                    <td class="text-right" id="suggest_targeting">-</td>
                                                </tr>
                                                <tr>
                                                    <td><i class="icon-file-check position-left"></i> 是否满足真实曝光量预估要求:</td>
                                                    <td class="text-right" id="is_real_exposure_supported">-</td>
                                                </tr>
                                                </tbody>
                                            </table>

                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset title="3">
                                    <legend class="text-semibold">广告创意</legend>

                                    <h4 class="fw-bold mb-20">添加创意素材:<span id="img_limit"></span></h4>
                                    <div class="form-group form-horizontal row">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">落地页类型</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <label class="checkbox-inline">
                                                <input type="radio" name="pageType" value="PAGE_TYPE_MINI_PROGRAM_WECHAT" class="control-success" onchange="changePageType('PAGE_TYPE_MINI_PROGRAM_WECHAT');">
                                                小程序
                                            </label>
                                            <label class="checkbox-inline" id="default_page_radio">
                                                <input type="radio" name="pageType" value="PAGE_TYPE_DEFAULT" class="control-success" onchange="changePageType('PAGE_TYPE_DEFAULT');">
                                                自定义H5
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group form-horizontal row">

                                        <div style="min-width:360px ;" class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <img id="adcreative_img" style="display:none;margin-bottom:10px;">
                                            <video id="adcreative_video" style="display:none;margin-bottom:10px;" controls>
                                                <source id="adcreative_video_source"  type="video/mp4">
                                            </video>
                                            <input type="hidden" name="adcreativeImgurl" id="adcreativeImgurl">
                                            <input type="hidden" name="adcreativeImageId" id="adcreativeImageId">
                                            <input type="file" name="file" class="file-input-extensions" id="adcreativeImg">
                                        </div>
                                    </div>
                                    <div class="form-group form-horizontal row" id="adcreative_title" style="display: none;">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">创意文案标题</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <input type="text" name="title" id="title" class="form-control" value="${adCreativeElements.title}">
                                        </div>
                                    </div>
                                    <div class="form-group form-horizontal row" id="adcreative_desc" style="display: none;">
                                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">创意外层文案</label>
                                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <textarea rows="5" cols="5" id="desc" name="desc" class="form-control" placeholder="输入广告外层文案"></textarea>
                                        </div>
                                    </div>
                                    <div id="mini_program_page" style="display:none;">
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">小程序原始ID</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <select class="bootstrap-select" data-live-search="true"  name="miniProgramId" id="miniProgramId">
                                                    <option value="">选择小程序</option>
                                                    <c:forEach items="${miniPrograms}" var="obj">
                                                        <option value="${obj.miniProgramId}">${obj.miniProgramName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">小程序链接</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="miniProgramPath" id="miniProgramPath" class="form-control">
                                            </div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2"></label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <div class="btn text-primary">
                                                    <a href="javascript:void(0);" onclick="preview()">预览</a>
                                                </div>
                                                <div class="form-group" id="preview">

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="default_h5_page" style="display:none;">
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">自定义H5链接</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <input type="text" name="page_url" id="page_url" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset title="4">
                                    <legend class="text-semibold">广告</legend>

                                    <h3 class="fw-bold mb-20">广告详情</h3>
                                    <hr />
                                    <h4 class="fw-bold mb-20">广告计划</h4>
                                    <div id="campaign_info_div">
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计划名</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">广告位</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">日预算</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                    </div>

                                    <h4 class="fw-bold mb-20">定向条件</h4>
                                    <div id="adgroup_info_div">
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">投放日期</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">计价方式</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">当前出价</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                    </div>


                                    <h4 class="fw-bold mb-20">广告创意</h4>
                                    <div id="adcreative_info_div">
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">小程序</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                        <div class="form-group form-horizontal row">
                                            <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">落地页链接</label>
                                            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 line-form-value">xxxxxxx</div>
                                        </div>
                                    </div>


                                </fieldset>

                                <button type="button" class="btn btn-primary stepy-finish">提交<i class="icon-check position-right"></i></button>
                            </div>
                            <!-- /clickable title -->

                        </div>

                    </div>

                </div>
                <!-- /dashboard content -->

            </div>
            <!-- /main content -->

        </div>
        <!-- /page content -->

    </div>
    <!-- /page container -->
    <script>
        var locData = ${locData};
        var behaviorData = ${behaviorData};
        var interestData = ${interestData};
        var aId = ${adv.id};
        var account_id = ${adv.accountId};
        var user_action_set_id = ${adv.webUserActionSetId};
        var adsenses = ${data}.adsenses;
        var campaign = ${data}.campaign;
        var params = ${data}.params;
        var adCreativeElements = ${data}.adCreativeElements;
        var method = "${method}";
    </script>
    <script type="text/javascript" src="${scripts}/pages/ad/adplan/adplan.js"></script>
</body>

</html>
