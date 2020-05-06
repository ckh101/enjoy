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
    <script type="text/javascript" src="${scripts}/plugins/layer/layer.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/notifications/sweet_alert.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/notifications/pnotify.min.js"></script>
    <link media="all" rel="stylesheet" type="text/css" href="${scripts}/plugins/uploaders/css/fileinput.min.css"/>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/plugins/piexif.min.js" ></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/fileinput.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/themes/fa/theme.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/locales/zh.js"></script>
	<script type="text/javascript" src="${scripts}/core/app.js"></script>
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
                <h5 class="fw-bold mb-20">${adname}-广告创意</h5>
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-white">
                            <div class="panel-body">
                                <div class="form-group">
                                <label>创意素材:<span id="img_limit"></span></label>
                                <div class="form-group" >
                                    <c:choose>
                                        <c:when test="${adCreativeType == 'image'}">
                                            <img  id="adcreative_img" style="display:none;margin-bottom:10px;">
                                        </c:when>
                                        <c:when test="${adCreativeType == 'video'}">
                                            <video id="adcreative_video" style="display:none;margin-bottom:10px;" controls>
                                                <source id="adcreative_video_source"  type="video/mp4">
                                            </video>
                                        </c:when>
                                    </c:choose>
                                </div>
                                <input type="hidden" name="adcreativeId" id="adcreativeId" value="${adcreativeId}">
                                <input type="hidden" name="adcreativeImgurl" id="adcreativeImgurl">
                                <input type="hidden" name="adcreativeImageId" id="adcreativeImageId">
                                <input type="file" name="file" class="file-input-extensions" id="adcreativeImg">
                            </div>
                                <button type="button" class="btn bg-primary-700" style="float: right;margin-right: 20px;" onclick="saveAdCreative()">提交</button>
                            </div>
                        </div>
                    </div>
                </div>

			</div>
			<!-- /main content -->
		</div>
		<!-- /page content -->
	</div>
	<!-- /page container -->
    <script>
        var aId = ${adv.id};
        var imgurl = "${imgUrl}";
        var adsense = "${adsense}";
        var adsenses = ${adsenses};
        var adcreative_type = "${adCreativeType}";
    </script>
    <script type="text/javascript" src="${scripts}/pages/ad/adplan/adcreative_edit.js"></script>

</body>

</html>
