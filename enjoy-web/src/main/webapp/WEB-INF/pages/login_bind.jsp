<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../include/config.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${webTitle}-绑定手机号</title>

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
    <!-- /theme JS files -->
    <!-- private library files-->
    <script type="text/javascript" src="${scripts }/base.js?v=1.3"></script>
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
            display: none;
        }

        .code-view .code-img {
            width: 240px;
            height: 240px;
            background-color: #fff;
            margin: 22px auto 10px;
        }
        .error_tit{
            line-height:18px;
            font-size: 12px;
            color: red;
            text-align: center;
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

            <!-- login form -->
            <form id="login-form" action="${source }/loginbind" method="post">
                <div class="login-form login-panel">
                    <div class="text-center">
                        <img src="${wxuser.headUrl}" class="avatar">
                        <div class="text-center">
                            <div class="name font-bold">${wxuser.nickName}</div>
                            <div class="tip">*首次登录绑定手机号码*</div>
                            <input type="hidden" name="userId" value="${wxuser.id}">
                            <input type="hidden" name="openId" value="${wxuser.openId}">
                            <input type="hidden" name="unionId" value="${wxuser.unionId}">
                            <input type="hidden" name="nickName" value="${wxuser.nickName}">
                            <input type="hidden" name="headUrl" value="${wxuser.headUrl}">
                            <input type="hidden" name="sex" value="${wxuser.sex}">
                            <div class="error_tit" id="error_tit">${binderror}</div>
                        </div>
                    </div>

                    <div class="form-view">
                        <div class="row input1">
                            <input type="number" id="phone" name="phone" class="input" required="required" placeholder="请输入手机号码">
                        </div>
                        <div class="row">
                            <div class="flex flex-sb">
                                <div style="width:158px;" class="input2">
                                    <input type="number" class="input" name="captcha" id="captcha" required="required" placeholder="请输入验证码">
                                </div>
                                <!-- 发送验证码按钮 -->
                                <button type="button" id="code" class="btn-view verification-view pointer" onclick="codeButton()">发送验证码</button>
                                <!-- 验证码倒计时 -->
                                <div class="btn-view countdown-view">(60秒)后重新获取</div>
                            </div>
                        </div>

                        <div class="row" style="height: 48px;">
                            <button type="submit" class="submit-view pointer">立即绑定</button>
                        </div>
                    </div>

                </div>
            </form>
            <!-- /login form -->
        </div>
        <!-- /main content -->

    </div>
    <!-- /page content -->


</div>
<!-- /page container -->
<script type="text/javascript">
    function codeButton(){
        var phone = $("#phone").val();
        if(!isPhoneNumber(phone)){
            $("#error_tit").text("输入正确手机号码");
            return;
        }
        var params = {phone:phone};
        var time = 60;
        $('.verification-view').hide();
        $('.countdown-view').show();
        var set=setInterval(function(){
            $(".countdown-view").text("("+--time+")秒后重新获取");
        }, 1000);
        setTimeout(function(){
            $('.verification-view').show();
            $('.countdown-view').hide();
        }, 60000);
        $.post("${source}/sendPhoneCode",params,function(data){
            if(data.status != 1){
                $("#error_tit").text(data.msg);
                return;
            }
        });
    }

</script>

</body>
</html>

