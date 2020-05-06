<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${webTitle}</title>

	<!-- Global stylesheets -->
	<link href="https://fonts.loli.net/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
	<link href="${css}/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
	<link href="${css}/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/core.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/components.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/colors.min.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/layer/theme/default/layer.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/validation/bootstrapValidator.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/tool.css" rel="stylesheet" type="text/css">
	<!-- /global stylesheets -->

	<!-- Core JS files -->
	<script type="text/javascript" src="${scripts}/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jQuery.form.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/blockui.min.js"></script>
	<!-- /core JS files -->

	<!-- Theme JS files -->
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/uniform.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/ui/moment/moment.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/layer/layer.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/notifications/sweet_alert.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/notifications/pnotify.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/validation/bootstrapValidator.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/validation/zh_CN.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_select.min.js"></script>
	<!-- /theme JS files -->

	<!-- private library files-->
	<script type="text/javascript" src="${scripts }/ajax-util.js"></script>
	<script type="text/javascript" src="${scripts }/base.js?v=1.3"></script>
	<script type="text/javascript" src="${scripts }/pageNav.js"></script>
	<script type="text/javascript" src="${scripts }/tableData.js"></script>
	<script type="text/javascript" src="${scripts }/adIndex.js"></script>
	<script type="text/javascript">
	$(function () {
        //初始化根目录
        setContext('${source}/');
		<%--loadMenu('广告投放','概览','${source}/admin/ad/overview/index/${adv.id}',true);--%>
        loadMenu('广告投放','投放管理','${source}/admin/ad/admanager/adplanlist/${adv.id}',true);
    });
	</script>
</head>

<body>
	<div style="background-color: #f7f7f7">
		<%@include file="../include/adtop.jsp" %>
		<!-- Page container -->
		<div class="page-container" style="padding: 0px 0px;">
			<!-- Page content -->
			<div class="page-content">

				<!-- Main content -->
				<div class="content-wrapper" id="content">
				</div>
				<!-- /main content -->
			</div>
			<!-- /page content -->
		</div>
		<!-- /page container -->
	</div>
</body>
</html>
