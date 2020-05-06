<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@include file="../include/config.jsp" %>
<%
    pageContext.setAttribute("token", SecurityUtils.getSubject().getSession().getAttribute("token"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${webTitle}-登录</title>

    <!-- Global stylesheets -->
    <link href="https://fonts.loli.net/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
    <link href="${css}/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
    <link href="${css}/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/core.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/components.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/colors.min.css" rel="stylesheet" type="text/css">
    <link href="${css}/tool.css" rel="stylesheet" type="text/css">
    <!-- /global stylesheets -->

    <!-- Core JS files -->
    <script type="text/javascript" src="${scripts}/plugins/loaders/pace.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jquery.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/bootstrap.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/loaders/blockui.min.js"></script>
    <!-- /core JS files -->


    <!-- Theme JS files -->
    <script type="text/javascript" src="${scripts}/core/app.js"></script>
    <script src="https://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
    <!-- /theme JS files -->

    <style type="text/css">
        body {
            background: url(https://img.bazhuay.com/yptm/images/2019/8/login_bg_1567046041120.png) no-repeat center top;
            background-size: cover;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            -ms-background-size: cover;
            background-attachment: fixed;
        }

        .head {
            margin: 0 auto 40px;
            color: #FFFFFF;
        }

        .head .line1 {
            font-size: 69px;
            letter-spacing: 0.86px;
            height: 100px;
            line-height: 100px;
        }

        .head .line2 {
            font-size: 28px;
            letter-spacing: 0.35px;
            height: 40px;
            line-height: 40px;
        }

        .login-panel {
            width: 378px !important;
            height: 400px;
            background-color: rgba(255, 255, 255, 0.22);
            border-radius: 5px;
            position: relative;
        }

        .login-panel::after {
            width: 40px;
            height: 40px;
            border: 20px solid transparent;
            border-bottom: 20px solid rgba(255, 255, 255, 0.22);
            position: absolute;
            content: '';
            top: -40px;
            left: 169px;
        }

        .login-panel .avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            margin-top: 20px;
        }

        .login-panel .name {
            font-size: 18px;
            color: #FFFFFF;
            letter-spacing: 0.22px;
            margin-top: 10px;
        }

        .login-panel .tip {
            opacity: 0.65;
            font-size: 12px;
            color: #FFFFFF;
            letter-spacing: 0.15px;
            text-align: center;
            margin-top: 4px;
        }

        .form-view {
            width: 304px;
            margin: 18px auto 0px;
        }

        .form-view .row {
            display: block;
            margin-top: 16px;
        }

        .form-view .input {
            height: 40px;
            box-sizing: border-box;
            padding-left: 36px;
            background-color: #fff;
            width: 100%;
            font-size: 16px;
            color: #333;
            background-image: none;
            border: none;
            outline: none;
            border-radius: 2px;
            position: relative;
        }


        .input1,
        .input2 {
            position: relative;
        }

        .input1::before {
            position: absolute;
            left: 0px;
            top: 0px;
            content: '';
            background: url('https://img.bazhuay.com/yptm/images/2019/8/symbols-login_mobile-alt_1567052529372.png') no-repeat center center;
            background-size: 20px 20px;
            width: 40px;
            height: 40px;
            z-index: 3;
        }

        .input2::before {
            position: absolute;
            left: 0px;
            top: 0px;
            content: '';
            background: url('https://img.bazhuay.com/yptm/images/2019/8/symbols-login_code_1567052529038.png') no-repeat center center;
            background-size: 20px 20px;
            width: 40px;
            height: 40px;
            z-index: 3;
        }

        .form-view input::-moz-placeholder {
            color: rgba(0, 0, 0, 0.5);
        }

        .form-view input::-ms-input-placeholder {
            color: rgba(0, 0, 0, 0.5);
        }

        .form-view input::-webkit-input-placeholder {
            color: rgba(0, 0, 0, 0.5);
        }

        .form-view input::-webkit-outer-spin-button,
        .form-view input::-webkit-inner-spin-button {
            -webkit-appearance: none;
        }

        .form-view input[type="number"] {
            -moz-appearance: textfield;
        }

        .form-view .btn-view {
            border-radius: 2px;
            font-size: 16px;
            text-align: center;
            box-sizing: border-box;
            height: 40px;
            line-height: 40px;
        }

        .form-view .verification-view {
            width: 158px;
            background-color: #fff;
            border: 1px solid #4E85E2;
            color: #4E85E2;
        }

        .form-view .countdown-view {
            width: 158px;
            border: 1px solid #c2c5c8;
            color: #c2c5c8;
            background-color: transparent;
            display: none;
        }

        .form-view .submit-view {
            height: 48px;
            line-height: 48px;
            color: #fff;
            text-align: center;
            background-color: #4e85e2;
            width: 100%;
            border-radius: 2px;
            cursor: pointer;
        }

        .code-view {
            text-align: center;
        }

        .code-view .code-img {
            width: 240px;
            height: 240px;
            background-color: #fff;
            margin: 22px auto 10px;
        }
    </style>

</head>

<body>

<!-- Page container -->
<div class="page-container login-container fs-16">

    <!-- Page content -->
    <div class="page-content">

        <!-- Main content -->
        <div class="content-wrapper text-center">

            <div class="head font-bold">
                <div class="line1">HB</div>
                <div class="line2">${webTitle}</div>
            </div>
            <!-- 扫码 -->
            <div class="code-view login-panel m-auto fs-26" id="qrcode">

            </div>
            <!-- /扫码 -->

        </div>
        <!-- /main content -->

    </div>
    <!-- /page content -->


</div>
<!-- /page container -->
<script type="text/javascript">
    $(document).ready(function() {
        var obj = new WxLogin({
            self_redirect:false,
            id:"qrcode",
            appid: "",
            scope: "snsapi_login",
            redirect_uri: encodeURI("${source}/logincheck"),
            state: "${token}",
            style: "white",
            href: ""
        });
        window.history.forward(1);
        //OR
        window.history.forward(-1);
    });
</script>

</body>
</html>

