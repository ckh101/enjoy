<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@include file="../include/config.jsp" %>
<%
	pageContext.setAttribute("token", SecurityUtils.getSubject().getSession().getAttribute("token"));
%>
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
	<!-- /global stylesheets -->

	<!-- Core JS files -->
	<script type="text/javascript" src="${scripts}/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery.md5.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/blockui.min.js"></script>
	<!-- /core JS files -->


	<!-- Theme JS files -->
	<script type="text/javascript" src="${scripts}/core/app.js"></script>
	<!-- /theme JS files -->
	<style type="text/css">
		.error_tit{
		    line-height:18px;
		    font-size: 12px;
		    color: red;
		    text-align: center;
		}
	</style>

</head>

<body>
	<!-- Main navbar -->
	<div class="navbar navbar-inverse">
		<div class="navbar-header">
			<a class="navbar-brand" href="index.html"><img src="${images }/logo_light.png" alt=""></a>

			<ul class="nav navbar-nav pull-right visible-xs-block">
				<li><a data-toggle="collapse" data-target="#navbar-mobile"><i class="icon-tree5"></i></a></li>
			</ul>
		</div>
	</div>
	<!-- /main navbar -->
	<!-- Page container -->
	<div class="page-container login-container">

		<!-- Page content -->
		<div class="page-content">

			<!-- Main content -->
			<div class="content-wrapper">

				<!-- Content area -->
				<div class="content">

					<!-- Simple login form -->
					
					<form action="${source }/logincheck" method="post" onsubmit='return md5password()'>
						<div class="panel panel-body login-form" style="margin: auto 0 20px auto;">
							
							<div class="text-center">
								<div class="icon-object border-slate-300 text-slate-300"><i class="icon-reading"></i></div>
								<h5 class="content-group">Login to your account <small class="display-block">Enter your credentials below</small></h5>
							</div>
							<div class="error_tit" id="error_tit">${verifyMsg }</div>
							<div class="form-group has-feedback has-feedback-left">
								<input type="text" class="form-control" name="username" placeholder="账号" required="required">
								<div class="form-control-feedback">
									<i class="icon-user text-muted"></i>
								</div>
							</div>

							<div class="form-group has-feedback has-feedback-left">
								<input type="password" class="form-control" id="password" name="password" placeholder="密码" required="required">
								<div class="form-control-feedback">
									<i class="icon-lock2 text-muted"></i>
								</div>
							</div>
							
							<div class="form-group has-feedback has-feedback-left" style="height:34px">
								<input type="text" class="form-control" placeholder="验证码" name="captcha" id="captcha" style="float:left;width:170px" required="required">
								<div class="form-control-feedback">
									<i class=" icon-cc text-muted"></i>
								</div>
								<img id="captchaImg" src='${source}/images/captcha.jpg' style="float:right;border-radius: 3px;" onclick="this.src='${source }/images/captcha.jpg?now=' + new Date().getTime()" alt="看不清，点击换一张"/>
							</div>
							<input type="hidden" name="tokenCode"  id="tokenCode" value="${token }">
							<div class="form-group">
								<button type="submit" class="btn btn-primary btn-block">登录 <i class="icon-circle-right2 position-right"></i></button>
							</div>
							<!-- 
								<div class="text-center">
									<a href="login_password_recover.html">忘记密码?</a>
								</div>
							 -->
						</div>
					</form>
					<!-- /simple login form -->


					<%@include file="../include/foot.jsp"%>

				</div>
				<!-- /content area -->

			</div>
			<!-- /main content -->

		</div>
		<!-- /page content -->

	</div>
	<!-- /page container -->
	<script type="text/javascript">
        $(document).ready(function() {
            
             window.history.forward(1);
             //OR
             window.history.forward(-1);
        });
        function md5password(){
        	var password = $("#password");
        	password.val($.md5(password.val()));
        	return true;
        }
   </script> 
</body>
</html>
