<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="../../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${scripts}/plugins/ui/moment/moment.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jquery_ui/interactions.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/picker.date.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/pickadate/translations/zh_CN.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_select2.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_select.min.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_bootstrap_select.js"></script>
    <script>
        var currentPage = ${page.pageNumber};
        var totalPage = ${page.pageCount};
        var adsense = ${adsense};
    </script>
</head>
<body>
    <!-- Dashboard content -->
    <div class="row">
        <div class="">

            <div class="panel panel-flat">

                <div class="panel-body">
                    <div class="tabbable">
                        <!-- tab nav -->
                        <ul class="nav nav-tabs nav-tabs-bottom bottom-divided">
                            <li><a class="dis-ib" style="max-width: 120px;"  data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adplanlist/${adv.id}')">广告计划</a></li>
                            <li class="active"><a class="dis-ib" style="max-width: 120px;"  data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adlist/${adv.id}')">广告</a></li>
                        </ul>
                        <div class="form-group pb-20" style="border-bottom: 1px solid #ebedf1;">
                            <a href="${source}/admin/ad/admanager/adplan/${adv.id}" target="_blank"><button type="button" class="btn bg-primary-700"><i class="icon-plus3 text-size-base"></i> 新建广告</button></a>
                        </div>

                        <div class="panel-heading">
                            <div class="col-xs-12 col-sm-12">

                                <form class="form-inline form-horizontal" id="queryForm" method="post" datatype="html">
                                    <div class="form-group mb-20">
                                        <label>创建日期</label>
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                            <input type="text" class="form-control pickadate" name="start_date" value="${startDate}" placeholder="开始日期">
                                        </div>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 30px;">
                                        <label>-</label>
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                            <input type="text" class="form-control pickadate" name="end_date" value="${endDate}" placeholder="结束日期">
                                        </div>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <label>计划名称</label>
                                        <div class="form-group has-feedback">
                                            <input type="text" class="form-control" placeholder="输入计划名称" name="campaignName" id="campaignName" value="${campaignName}">
                                        </div>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 30px;">
                                        <label>计划ID</label>
                                        <div class="form-group has-feedback">
                                            <input type="text" class="form-control" placeholder="输入计划ID" name="campaignId" id="campaignId" value="${campaignId}">
                                        </div>
                                    </div>

                                </form>
                                <div class="form-inline form-horizontal">
                                    <div class="form-group mb-20" style="margin-right: 30px;">
                                        <label>广告名称</label>
                                        <div class="form-group has-feedback">
                                            <input type="text" class="form-control" placeholder="输入广告名称" name="adName" id="adName" value="${adName}">
                                        </div>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 30px;">
                                        <label>广告ID</label>
                                        <div class="form-group has-feedback">
                                            <input type="text" class="form-control" placeholder="输入广告ID" name="adGroupId" id="adGroupId" value="${adGroupId}">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-inline form-horizontal">
                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <button type="button" id="queryFormBtn" class="btn btn-primary btn-xs" data-label="查询"><i class="icon-search4"></i> 查询</button>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <button type="button" class="btn btn-icon btn-rounded btn-primary" title="批量复制" onclick="preBatchcopy()"><i class="icon-copy3"></i></button>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <button type="button" class="btn btn-icon btn-rounded btn-primary" title="暂停" onclick="batchsub()"><i class="icon-pause2"></i></button>
                                    </div>

                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <button type="button" class="btn btn-icon btn-rounded btn-primary" title="启动" onclick="batchon()"><i class="icon-play4"></i></button>
                                    </div>

                                    <div class="form-group mb-20">
                                        <button type="button" class="btn btn-icon btn-rounded btn-primary" title="删除" onclick="batchdel()"><i class="icon-trash"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="tabbable-outbox">
                            <table id="dataTable" class="table table-borderless table-togglable table-hover no-border" width="100%">
                                <thead class="bg-f1f1f1">
                                <tr>
                                    <th><input type="checkbox" value="0" class="ad_check styled" data-role="check_all"></th>
                                    <th style="min-width: 50px !important" data-toggle="true">Id</th>
                                    <th style="min-width: 50px !important">广告ID</th>
                                    <th style="min-width: 50px !important">广告名称</th>
                                    <th style="min-width: 50px !important">所属计划</th>
                                    <th style="min-width: 50px !important" class="text-center">编辑</th>
                                    <th style="min-width: 100px !important">素材</th>
                                    <th style="min-width: 50px !important">素材类型</th>
                                    <th style="min-width: 50px !important">广告版位</th>
                                    <th style="min-width: 50px !important">投放状态</th>
                                    <th style="min-width: 50px !important">系统状态</th>
                                    <th style="min-width: 50px !important">出价类型</th>
                                    <th style="min-width: 50px !important">当前出价</th>
                                    <th style="min-width: 50px !important" data-hide="phone,tablet">投放时间</th>
                                    <th style="min-width: 50px !important">广告计划日预算</th>
                                    <th style="min-width: 50px !important" data-hide="phone,tablet">创建时间</th>
                                    <th style="min-width: 50px !important" data-hide="phone,tablet">更新时间</th>
                                    <th style="min-width: 50px !important" data-hide="phone,tablet">审核内容</th>
                                    <th style="min-width: 50px !important" data-hide="phone,tablet">创意内容</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <c:forEach items="${page.list }" var="obj" varStatus="status">
                                        <td><input type="checkbox" class="ad_check styled" name="ad_check" value="${obj.id}"></td>
                                        <td>${obj.adGroup.id}</td>
                                        <td>${obj.adGroup.adGroupId}</td>
                                        <td><div class="input-group"><input type="text" class="form-control" value="${obj.adGroup.adGroupName}" readonly="readonly" style="width:200px;"><span class="input-group-addon"><a href="javacript:void(0);" onclick="editName(this, ${obj.id});"><i class="icon-pencil4"></i></a></span></div></td>
                                        <td>${obj.campaign.id}-${obj.campaign.campaignName}</td>
                                        <td class="text-center">
                                            <ul class="icons-list">
                                                <li class="dropdown">
                                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                        <i class="icon-cog7"></i>
                                                        <span class="caret"></span>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-menu-solid">
                                                        <li><a href="${source}/admin/ad/admanager/ad/editpage/${adv.id}/${obj.id}" target="_blank"><i class=""></i>修改广告</a></li>
                                                        <li><a href="${source}/admin/ad/admanager/ad/editadcreative/${adv.id}/${obj.id}" target="_blank"><i class=""></i>修改创意</a></li>
                                                        <li><a href="${source}/admin/ad/admanager/ad/adplan/continue/${adv.id}/${obj.campaign.id}?method=copy&adId=${obj.id}" target="_blank"><i class=""></i>复制广告</a></li>
                                                        <li><a href="${source}/admin/ad/admanager/ad/adplan/batchadd/${adv.id}/${obj.id}" target="_blank"><i class=""></i>批量创建</a></li>
                                                        <li><a href="javascript:void(0);" data-role="editAlarmConfig" data-id="${adv.id},${obj.adId}"><i class=""></i>预警配置</a></li>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${obj.adCreative.adcreativeType == 'image'}">
                                                    <div style="width:100px"><img src="${obj.adCreative.materialUrls}" width="100%" ></div>
                                                </c:when>
                                                <c:when test="${obj.adCreative.adcreativeType == 'video'}">
                                                    <div style="width:100px">
                                                        <video width="100%" controls>
                                                            <source src="${obj.adCreative.materialUrls}" type="video/mp4">
                                                        </video>
                                                    </div>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${obj.adCreative.adcreativeType == 'image'}">
                                                    单图
                                                </c:when>
                                                <c:when test="${obj.adCreative.adcreativeType == 'video'}">
                                                    视频
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td class="dictionary text-center" data-source="adsense" data-value="${obj.campaign.adsense}">
                                            ${obj.campaign.adsense}
                                        </td >
                                        <c:choose>
                                            <c:when test="${obj.adGroup.configuredStatus == 'AD_STATUS_PENDING'}">
                                                <td><span class="label label-default">创建中</span></td>
                                            </c:when>
                                            <c:when test="${obj.adGroup.configuredStatus == 'AD_STATUS_SUSPEND'}">
                                                <td><span class="label label-warning">暂停</span></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><span class="label label-success">启用</span></td>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${obj.systemStatus == 'AD_STATUS_PENDING'}">
                                                <td><span class="label label-default">待审核</span></td>
                                            </c:when>
                                            <c:when test="${obj.systemStatus == 'AD_STATUS_DENIED'}">
                                                <td><span class="label label-warning">审核不通过</span></td>
                                            </c:when>
                                            <c:when test="${obj.systemStatus == 'AD_STATUS_DELETED'}">
                                                <td><span class="label label-danger">已删除</span></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><span class="label label-success">启用</span></td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>${obj.adGroup.bidMethod}</td>
                                        <td>${obj.adGroup.bidAmount/100}</td>
                                        <td>${obj.adGroup.beginDate}/${obj.adGroup.endDate==""?"长期投放":obj.adGroup.endDate}</td>
                                        <td>${obj.campaign.dailyBudget/100}</td>
                                        <td class="text-center"><fmt:formatDate value="${obj.createTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                        <td class="text-center"><fmt:formatDate value="${obj.updateTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                        <td>${obj.rejectMessage}</td>
                                        <td class="text-center">
                                            <div class="creative-detail" data-popup="popover" data-content="<c:out value="${obj.adCreative.adCreativeElements}"/>"><i class="icon-eye"></i></div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>

                    </div>
                </div>

            </div>

        </div>
    </div>
    <!-- /dashboard content -->
    <!-- 预警配置表单 -->
    <div id="adMonitorConfigModal" class="modal fade">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h5 class="modal-title">预警配置</h5>
                </div>
                <form action="${source}/admin/ad/admanager/admonitor/save/${adv.id}" class="form-horizontal">
                    <input type="hidden" name="adId" />
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2">出单通知</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-md-8">
                                        <div class="input-group bootstrap-touchspin">
                                            <span class="input-group-addon bootstrap-touchspin-prefix">每</span>
                                            <input name="dailyOrderInterval" class="form-control touchspin-button-group" placeholder="整数。0表示关闭" style="display: block;">
                                            <span class="input-group-addon bootstrap-touchspin-postfix">单发送通知</span>
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                        <div class="checkbox">
                                            <label>
                                                <input name="dailyFirstOrderAlarm" type="checkbox" class="styled" checked="checked">
                                                首单通知
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">消耗通知</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-md-8">
                                        <div class="input-group bootstrap-touchspin">
                                            <span class="input-group-addon bootstrap-touchspin-prefix">消耗超过</span>
                                            <input name="dailyCostThreshold" class="form-control touchspin-button-group" onkeyup="num(this)" onblur="num(this)" placeholder="整数。0表示关闭" style="display: block;">
                                            <span class="input-group-addon bootstrap-touchspin-postfix">元时通知</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">预算预警</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-md-8">
                                        <div class="input-group bootstrap-touchspin">
                                            <span class="input-group-addon bootstrap-touchspin-prefix">预算低于</span>
                                            <input name="dailyBudgetThreshold" onkeyup="num(this)" onblur="num(this)" class="form-control touchspin-button-group" placeholder="最多两位小数。0表示关闭" style="display: block;">
                                            <span class="input-group-addon bootstrap-touchspin-postfix">%</span>
                                            <span class="input-group-addon bootstrap-touchspin-postfix">时通知</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">ROI预警</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-md-8">
                                        <div class="input-group bootstrap-touchspin">
                                            <span class="input-group-addon bootstrap-touchspin-prefix">ROI低于</span>
                                            <input name="roiThreshold" onkeyup="num(this)" onblur="num(this)" class="form-control touchspin-button-group" placeholder="最多两位小数。0表示关闭" style="display: block;">
                                            <span class="input-group-addon bootstrap-touchspin-postfix">时通知</span>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="checkbox">
                                            <label>
                                                <input name="roiAlarmAutoPause" type="checkbox" class="styled">
                                                且暂停计划
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">接收手机</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-md-8">
                                        <input name="receivePhone" type="text" class="form-control" placeholder="接收手机，多个用英文逗号隔开">
                                    </div>
                                    <div class="col-md-4" style="padding-top: 8px">
                                        <i class="icon-help" data-popup="popover-solid" title="Popover title" data-placement="right" data-trigger="hover" data-content="最多5个，用英文逗号隔开"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /预警配置表单 -->

    <div id="modal_theme_success" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-success">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h6 class="modal-title">批量复制</h6>
                </div>

                <div class="modal-body">
                    <div class="form-group">
                        <div class="input-group">
                            <input type="text" class="form-control" name="accountId" id="accountId" placeholder="输入广告主账号ID">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                            <label class="radio-inline">
                                <input type="radio" name="add_type" checked value="new_campaign" class="control-success" onchange="changeCampaignTypeForBatchCopy()">
                                新计划
                            </label>
                        </div>
                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                            <label class="radio-inline">
                                <input type="radio" name="add_type" value="old_campaign" class="control-success" onchange="changeCampaignTypeForBatchCopy()">
                                老计划
                            </label>
                        </div>
                    </div>
                    <div class="form-group" style="display: none" id="campaign_div">
                        <select class="bootstrap-select" name="campaigns" data-live-search="true"  id="campaigns" width="100%">
                        </select>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-link" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-success" onclick="batchcopy()">提交</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        var aId = ${adv.id};
    </script>
    <!-- /colored tabs -->
    <script type="text/javascript" src="${scripts }/task.js"></script>
    <script type="text/javascript" src="${scripts}/pages/ad/admanager/adlist.js"></script>
</body>
</html>
