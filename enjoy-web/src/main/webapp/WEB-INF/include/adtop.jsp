<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- Main navbar -->
<div class="navbar navbar-inverse">
    <div class="navbar-header">
        <a style="margin-top: -6px;" class="navbar-brand" href="${source}/index"><img style="height: 28px;" src="https://img.bazhuay.com/yptm/images/2019/9/logoggg_1567409817024.png"
                                                                                   alt=""></a>
    </div>
    <div class="navbar-collapse collapse bgc-blue-main" id="navbar-mobile">
        <ul class="nav navbar-nav">
            <li><a href="javascript:void(0);">${adv.corporationName}</a></li>
            <li><a href="javascript:void(0);" style="color: #8b909a;">/&nbsp;&nbsp;&nbsp;客户ID ${adv.accountId}</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown dropdown-user">
                <a class="dropdown-toggle" data-toggle="dropdown">
                    <img src="${headurl}" alt="">
                    <span>${_user.userName}</span>
                    <i class="caret"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-right">
                    <li><a href="${source }/logout"><i class="icon-switch2"></i> Logout</a></li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<!-- /main navbar -->