<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- Main navbar -->
    <div class="navbar navbar-inverse">
        <div class="navbar-header">
            <a style="margin-top: -6px;" class="navbar-brand" href="javascript:void(0);">
                <img style="height: 28px;" src="" alt="">
            </a>
        </div>
        <div class="navbar-collapse collapse bgc-blue-main" id="navbar-mobile">
            <ul class="nav navbar-nav">
                <li id="back_li">
                    <a style="color: #b6b9bf;" href="javascript:void(0);"><i class="icon-arrow-left8 position-left"></i>返回</a>
                </li>
                <li>
                    <div class="breadcrumb-line-wide">
                        <ul class="breadcrumb">
                            <li><a style="color: #b6b9bf;" href="${source}/index">首页</a></li>

                        </ul>
                        <ul class="breadcrumb-elements">

                        </ul>
                    </div>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown dropdown-user">
                    <a class="dropdown-toggle" data-toggle="dropdown">
                        <img src="${headurl}" alt="">
                        <span>${_user.userName}</span>
                        <i class="caret"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="${source }/logout"><i class="icon-switch2"></i>Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>


    </div>
    <!-- /main navbar -->


    <!-- Page header -->
    <div class="page-header">

    </div>
    <!-- /page header -->

