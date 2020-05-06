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
    <script type="text/javascript" src="${scripts }/ajax-util.js"></script>
    <script type="text/javascript" src="${scripts }/base.js?v=1.3"></script>

</head>
<body>
    <%@include file="../../../include/adtop.jsp" %>
	<!-- Page container -->
	<div class="page-container">
		<!-- Page content -->
		<div class="page-content">
			<!-- Main content -->
			<div class="content-wrapper">
				<div class="row">
                    <div>
                        <div class="panel panel-white">
                            <div class="panel-heading">
                                <h4 class="fw-bold mb-20">批量创建广告</h4>
                            </div>
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-10">
                                        <div class="row">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label class="radio-inline">
                                                    <input type="radio" name="add_type" checked value="old_campaign" class="control-primary">
                                                    当前计划
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label class="radio-inline">
                                                    <input type="radio" name="add_type"  value="new_campaign" class="control-primary">
                                                    新计划
                                                </label>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <h6 class="fw-bold mb-20">计划名称:${ad.campaign.id}-${ad.campaign.campaignName}</h6>
                                        </div>
                                        <div class="form-group">
                                            <h6 class="fw-bold mb-20">广告名称:${ad.adGroup.id}-${ad.adGroup.adGroupName}</h6>
                                        </div>
                                        <input type="hidden" id="adId" name="adId" value="${ad.id}">
                                        <fieldset id="ads_div">
                                            <div class="form-group col-md-12 bottom-border" id="ad_template" style="display: none">
                                                <label class="control-label col-md-2">投放日期</label>
                                                <div class="col-md-10" style="margin-bottom:15px">
                                                    <div class="row">
                                                        <div class="col-md-8 input-group">
                                                            <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                                            <input name="put_date" type="text" class="form-control col-md-8 daterange-basic">
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="addad()"><i class="icon-add"></i></a></div>
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="subad(this)"><i class="icon-subtract"></i></a></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-md-12 bottom-border ad_div">
                                                <label class="control-label col-md-2">投放日期</label>
                                                <div class="col-md-10" style="margin-bottom:15px">
                                                    <div class="row">
                                                        <div class="col-md-8 input-group">
                                                            <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                                            <input  name="put_date" type="text" id="first_date" class="form-control col-md-8 daterange-basic">
                                                            <div class="input-group-addon"><a href="javacript:void(0);" onclick="addad()"><i class="icon-add"></i></a></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </fieldset>
                                        <button type="button" class="btn bg-primary-700" style="float:right;" onclick="batchadd()">提交</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
				</div>
				<!-- /panel titles -->
			</div>
			<!-- /main content -->
		</div>
		<!-- /page content -->
	</div>
	<!-- /page container -->
    <script>
        var aId = ${adv.id};
    </script>
    <script type="text/javascript" src="${scripts}/pages/ad/adplan/batchadd.js"></script>
</body>

</html>
