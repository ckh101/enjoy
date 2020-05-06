/**
 * Created by yanghui on 15/5/12.
 * 页面导航
 * 页面查询
 */
//页面导航
var pageNav = new Object();

function setPageHeader(pageTitle, pageMenu, menuURL) {
    pageNav.pageTitle = pageTitle;
    pageNav.pageMenu = pageMenu;
    pageNav.menuURL = menuURL;
}
function getMenuURL() {
    return pageNav.menuURL;
}
function getQueryParam() {
    return pageNav.queryParam;
}
function setQueryParam(data) {
     pageNav.queryParam = data;
}
function clearQueryParam() {
    pageNav.queryParam = new Object();
}

function loadMenu(pageTitle, pageMenu, menuURL, noneedCallBack) {
    if (pageTitle && pageMenu && menuURL) {
        setPageHeader(pageTitle, pageMenu, menuURL);
        //清除查询条件
        clearQueryParam();
    }
    var noneedCallBack = noneedCallBack || false;
    if(!noneedCallBack){
        loadData(pageNav.menuURL, getQueryParam(), function () {
            //初始化页头
            var header = $(".breadcrumb");
            header.pageNav();

        });
    }else{
        loadData(pageNav.menuURL, getQueryParam());
    }

    pageNav.isMenu = true;
}

function loadMenuChild(url, title, data) {
    data = data ? data : {};
    loadData(url, data, function () {
        //初始化页头
        var header = $(".breadcrumb");
        header.pageNav(title);
    });
    pageNav.isMenu = false;
}

$.fn.pageNav = function (title) {
    $("#back_li").html("");
    $(this).html("");

    if(title) {
        $("#back_li").html("<a style=\"color: #b6b9bf;\" href=\"javascript:loadMenu();\"><i class=\"icon-arrow-left8 position-left\"></i>返回</a>");
        $(this).append("<li><a style=\"color: #b6b9bf;\" href=\"/enjoy-web/index\">首页</a></li>");
        $(this).append("<li>" + pageNav.pageMenu + "</li>");
        $(this).append("<li class=\"active\">" + title + "</li>");
    } else {
        $("#back_li").html("<a style=\"color: #b6b9bf;\" href=\"javascript:void(0);\"><i class=\"icon-arrow-left8 position-left\"></i>返回</a>")
        $(this).append("<li><a style=\"color: #b6b9bf;\" href=\"/enjoy-web/index\">首页</a></li>");
        $(this).append("<li class=\"active\">" +pageNav.pageMenu+ "</li>");
    }
};

$.fn.setQueryParam = function() {
    if(pageNav.isMenu) {
        pageNav.queryParam = $(this).serializeArray();
    }
};

//获取当前时间，格式YYYY-MM-DD
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}