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
    <script type="text/javascript" src="${scripts}/plugins/tables/footable/footable.min.js"></script>
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
                            <li class="active"><a class="dis-ib" style="max-width: 120px;"  data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adplanlist/${adv.id}')">广告计划</a></li>
                            <li><a class="dis-ib" style="max-width: 120px;"  data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adlist/${adv.id}')">广告</a></li>
                        </ul>
                        <div class="form-group pb-20" style="border-bottom: 1px solid #ebedf1;">
                            <a href="${source}/admin/ad/admanager/adplan/${adv.id}" target="_blank"><button type="button" class="btn bg-primary-700"><i class="icon-plus3 text-size-base"></i> 新建广告</button></a>
                        </div>
                        <!-- tab content -->

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
                                            <input type="text" class="form-control" name="campaignName" placeholder="输入计划名称" id="campaignName" value="${campaignName}">
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
                                    <div class="form-group mb-20" style="margin-right: 10px;">
                                        <button type="button" id="queryFormBtn" class="btn btn-primary btn-xs" data-label=" 查询"><i class="icon-search4"></i> 查询</button>
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
                                    <th style="min-width: 100px !important" data-toggle="true">Id</th>
                                    <th style="min-width: 100px !important">计划ID</th>
                                    <th style="min-width: 100px !important">计划名称</th>
                                    <th style="min-width: 80px !important" class="text-center">编辑</th>
                                    <th data-hide="phone">状态</th>
                                    <th style="min-width: 100px !important">广告版位</th>
                                    <th style="min-width: 100px !important">推广目标</th>
                                    <th style="min-width: 80px !important">日预算</th>
                                    <th data-hide="phone,tablet">创建时间</th>
                                    <th data-hide="phone,tablet">更新时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list }" var="obj" varStatus="status">
                                    <tr>
                                        <td><input class="ad_check styled" name="adplan_check" value="${obj.id}" type="checkbox"></td>
                                        <td>${obj.id}</td>
                                        <td><a href="#bottom-justified-divided-tab2" data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adlist/${adv.id}?cId=${obj.id}')">${obj.campaignId}</a></td>
                                        <td><a href="#bottom-justified-divided-tab2" data-toggle="tab" onclick="changeTab('广告投放','投放管理','${source}/admin/ad/admanager/adlist/${adv.id}?cId=${obj.id}')">${obj.campaignName}</a></td>
                                        <td class="text-center">
                                            <ul class="icons-list">
                                                <li class="dropdown">
                                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                        <i class="icon-cog7"></i>
                                                        <span class="caret"></span>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-menu-solid">
                                                        <c:if test="${obj.configuredStatus == 'AD_STATUS_PENDING'}">
                                                            <li><a href="${source}/admin/ad/admanager/ad/adplan/continue/${adv.id}/${obj.id}" target="_blank"><i class=""></i>继续创建</a></li>
                                                        </c:if>
                                                        <li><a href="${source}/admin/ad/admanager/ad/adplan/newad/${adv.id}/${obj.id}" target="_blank"><i class=""></i>新建广告</a></li>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </td>
                                        <c:choose>
                                            <c:when test="${obj.configuredStatus == 'AD_STATUS_PENDING'}">
                                                <td><span class="label label-default">创建中</span></td>
                                            </c:when>
                                            <c:when test="${obj.configuredStatus == 'AD_STATUS_SUSPEND'}">
                                                <td><span class="label label-warning">暂停</span></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><span class="label label-success">启用</span></td>
                                            </c:otherwise>
                                        </c:choose>

                                        <td class="dictionary text-center" data-source="adsense" data-value="${obj.adsense}">
                                                ${obj.adsense}
                                        </td>
                                        <td class="dictionary text-center" data-source="promotedObjectType" data-value="${obj.promotedObjectType}">
                                                ${obj.promotedObjectType}
                                        </td>
                                        <td class="text-center">
                                                ${obj.dailyBudget/100}
                                        </td>
                                        <td class="text-center"><fmt:formatDate value="${obj.createTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                        <td class="text-center"><fmt:formatDate value="${obj.updateTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
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

    <script>
        var aId = ${adv.id};
    </script>
    <script type="text/javascript" src="${scripts}/pages/ad/admanager/adplanlist.js"></script>
</body>
</html>
