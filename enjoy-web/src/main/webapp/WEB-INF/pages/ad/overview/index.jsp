<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="../../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${scripts}/charts/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="${scripts}/pages/ad/overview/overview.js"></script>
    <script>
        var last7DayViewCount = JSON.parse("${last7DayViewCount}");
        last7DaysViewCountLine();
    </script>
</head>
<body>
    <!--概览-->
    <div class="panel-body panel-white" v-show="cur_nav=='概览'">
        <div class="content-group-sm">
            <h6 class="no-margin text-thin font_size_12">实时数据仅供参考，请以前一日数据为准。</h6>
        </div>
        <div class="panel">
            <h5 class="no-margin text-thin bg-grey-f0f4f9 font_style_1">基础财务信息</h5>
            <div class="padding_t_20 clearfix">
                <div class="col-lg-12 flex padding_b_20" style="padding-top:20px;">
                    <p class="flex_column col-lg-4 text_center">
                        <span>总金额（元）</span>
                        <span>${sumBalance/100}</span>
                    </p>
                    <div class="border-left col-lg-4">
                        <div class="padding_l_20 no_padding_right flex">
                            <div class="col-lg-6 text_right no_padding">
                                <select id="fundType" name="select no_border" class="input-xlg no_border font_size_12 no_padding_right" onchange="changeFundType()">
                                    <c:forEach items="${funds}" var="fund">
                                        <c:choose>
                                            <c:when test="${fund.fundType == 'FUND_TYPE_CASH'}">
                                                <option value="${fund.balance/100}" data-cost="${fund.realtimeCost/100}" data-status="${fund.fundStatus}" selected="selected">
                                                    <font style="vertical-align: inherit;">
                                                        <font style="vertical-align: inherit;">现金账户</font>
                                                    </font>
                                                </option>
                                            </c:when>
                                            <c:when test="${fund.fundType == 'FUND_TYPE_GIFT'}">
                                                <option value="${fund.balance/100}" data-cost="${fund.realtimeCost/100}" data-status="${fund.fundStatus}">
                                                    <font style="vertical-align: inherit;">
                                                        <font style="vertical-align: inherit;">赠送账户</font>
                                                    </font>
                                                </option>
                                            </c:when>
                                            <c:when test="${fund.fundType == 'FUND_TYPE_SHARED'}">
                                                <option value="${fund.balance/100}" data-cost="${fund.realtimeCost/100}" data-status="${fund.fundStatus}">
                                                    <font style="vertical-align: inherit;">
                                                        <font style="vertical-align: inherit;">分成账户</font>
                                                    </font>
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${fund.balance/100}" data-cost="${fund.realtimeCost/100}">
                                                    <font style="vertical-align: inherit;">
                                                        <font style="vertical-align: inherit;">${fund.fundType}</font>
                                                    </font>
                                                </option>
                                            </c:otherwise>
                                        </c:choose>

                                    </c:forEach>
                                </select>
                            </div>
                            <p class="col-lg-6 text_left" id="balance">${cash.balance/100}</p>
                        </div>

                        <div class="padding_l_20 clearfix flex">
                            <p class="col-lg-6 text_right no_padding_right">
                                <font  style="vertical-align: inherit;">
                                    <font style="vertical-align: inherit;">账户状态</font>

                                </font>
                            </p>
                            <c:choose>
                                <c:when test="${cash.fundStatus == 'FUND_STATUS_NORMAL'}">
                                    <p class="col-lg-6 text_left" id="fundStatus">有效</p>
                                </c:when>
                                <c:when test="${cash.fundStatus == 'FUND_STATUS_NOT_ENOUGH'}">
                                    <p class="col-lg-6 text_left" id="fundStatus">余额不足</p>
                                </c:when>
                                <c:when test="${cash.fundStatus == 'FUND_STATUS_FROZEN'}">
                                    <p class="col-lg-6 text_left" id="fundStatus">资金冻结</p>
                                </c:when>
                            </c:choose>
                        </div>

                    </div>

                    <div class="border-left col-lg-4" style="padding-top:20px;padding-bottom: 20px">
                        <div class="padding_l_20 clearfix flex">
                            <p class="col-lg-6 text_right no_padding_right">
                                <font  style="vertical-align: inherit;">
                                    <font style="vertical-align: inherit;">当日花费(元)</font>

                                </font>
                            </p>
                            <p class="col-lg-6 text_left" id="cost">${cash.realtimeCost/100}</p>
                        </div>
                    </div>


                </div>

            </div>
        </div>
        <div class="col-md-12 no_padding">
            <div class="col-lg-12 is_moblie_l">
                <div class="panel">
                    <div class="panel-heading bg-grey-f0f4f9 border_style_2">
                        <h5 class="no-margin text-thin  panel-title">广告概况</h5>
                        <a class="btn bg-teal-400 flex_center_center" style="padding:5px 12px" href="${source}/admin/ad/admanager/campaign/add/${adv.id}" target="_blank">
                            <i class="icon-plus2 position-left"></i>
                            <font style="vertical-align: inherit;">
                                <font style="vertical-align: inherit;">创建广告</font>
                            </font>
                        </a>
                    </div>
                    <div class="flex border-bottom border_b_style font_style1 font_style2">
                        <p class="border_right_style" style="width: 33.3%;text-align: left">
                            <span>投放中</span>
                            <span>0</span>
                        </p>
                        <p class="border_right_style" style="width: 33.3%;text-align: center">
                            <span>未通过</span>
                            <span>0</span>
                        </p>
                        <p class="font_style1" style="width: 33.3%;text-align: right">
                            <span>已暂停</span>
                            <span class="color_style_1">0</span>
                        </p>
                    </div>
                    <div class="flex font_style2  border-bottom border_b_style">
                        <span>近7天曝光量</span>
                        <span class="text-primary">数据详情 > </span>
                    </div>
                    <div class="data_list">
                        <!--线性图-->
                        <div class="panel panel-flat no_border">
                            <div class="panel-body">
                                <div class="chart-container">
                                    <div class="chart has-fixed-height" id="c3-line-chart"></div>
                                </div>
                            </div>
                        </div>
                        <p class="no_data" id="no_data" style="display: none;">暂无数据</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--/概览-->
</body>
</html>
