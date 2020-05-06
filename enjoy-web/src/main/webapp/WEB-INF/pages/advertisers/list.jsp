<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="../../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${scripts}/plugins/pagination/jquery.twbsPagination.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/tables/datatables/datatables.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_select.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/tables/footable/footable.min.js"></script>
</head>
<script>
    var currentPage = ${page.pageNumber};
    var totalPage = ${page.pageCount};
</script>
<body>
        <div class="content">
            <!-- 客户数 -->
            <div class="panel panel-flat">
                <div class="container-fluid">
                    <div class="row text-center flex flex-aic">
                        <div class="col-xs-3 col-sm-3">
                            <div style="height: 116px;" class="flex flex-center fs-20">
                                <div>
                                    <img style="width: 76px;" src="https://img.bazhuay.com/yptm/images/2019/9/kehuzong_1567479253158.png" />
                                </div>
                                <div style="margin-left: 12px;">
                                    <div class="text-primary fw-bold no-margin">${page.totalCount}</div>
                                    <span class="text-grey-800 text-size-small">客户总数</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 col-sm-3">
                            <div style="height: 116px;" class="flex flex-center fs-20">
                                <div>
                                    <img style="width: 76px;" src="https://img.bazhuay.com/yptm/images/2019/9/kehuyouxiao_1567479313112.png" />
                                </div>
                                <div style="margin-left: 12px;">
                                    <div class="text-primary fw-bold no-margin">${authCount}</div>
                                    <span class="text-grey-800 text-size-small">有效客户数</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 col-sm-3">
                            <div style="height: 116px;" class="flex flex-center fs-20">
                                <div>
                                    <img style="width: 76px;" src="https://img.bazhuay.com/yptm/images/2019/9/kehuyouxiao_1567479313112.png" />
                                </div>
                                <div style="margin-left: 12px;">
                                    <div class="text-primary fw-bold no-margin">${wxAuthCount}</div>
                                    <span class="text-grey-800 text-size-small">朋友圈授权数</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 col-sm-3">
                            <div style="height: 116px;" class="flex flex-center fs-20">
                                <button id="addAdvertiser" type="button" class="btn bg-primary-700"><i class=" icon-user-plus"></i> 新增客户</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="panel panel-flat">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-md-12">
                            <form class="form-inline" id="queryForm" method="post" datatype="html">
                                <div class="form-group ">
                                    <label>广告主</label>
                                    <div class="input-group has-feedback">
                                        <input type="text" name="corporationName" class="form-control" value="${advertiser.corporationName}" placeholder="输入广告主名称">
                                        <div class="form-control-feedback">
                                            <i class="icon-search4 text-size-base"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>客户ID</label>
                                    <div class="input-group has-feedback">
                                        <input type="text" name="accountId" class="form-control" value="${advertiser.accountId}" placeholder="输入客户ID">
                                        <div class="form-control-feedback">
                                            <i class="icon-search4 text-size-base"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>授权状态</label>
                                    <div class="input-group">
                                        <select class="select">
                                            <option value="-1">
                                                全部
                                            </option>
                                            <option value="1" ${advertiser.authStatus == 1?"selected":""}>
                                                已授权
                                            </option>
                                            <option value="2" ${advertiser.authStatus == 2?"selected":""}>
                                                未授权
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>朋友圈授权状态</label>
                                    <div class="input-group">
                                        <select class="select">
                                            <option value="0">
                                                全部
                                            </option>
                                            <option value="1" ${advertiser.wxAuthStatus == 1?"selected":""}>
                                                已授权
                                            </option>
                                            <option value="2" ${advertiser.wxAuthStatus == 2?"selected":""}>
                                                未授权
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div style="margin-top: 10px;">
                                    <button type="button" id="queryFormBtn" class="btn bg-primary-700" data-label=" 查询"><i class="icon-search4"></i> 查询</button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
                <div class="tabbable-outbox">
                    <table class="table table-borderless table-togglable table-hover no-border">
                        <thead class="bg-f1f1f1">
                        <tr>
                            <th data-toggle="true">广告主名称</th>
                            <th>客户ID</th>
                            <th>日限额</th>
                            <th>授权状态</th>
                            <th data-hide="phone,tablet">web行为数据源ID</th>
                            <th>微信公众号</th>
                            <th data-hide="phone,tablet">创建时间</th>
                            <th data-hide="phone,tablet">更新时间</th>
                            <th class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${page.list }" var="obj" varStatus="status">
                            <tr>
                                <td>${obj.corporationName}</td>
                                <td><a href="#">${obj.accountId}</a></td>
                                <td>${obj.dailyBudget/100}</td>
                                <c:choose>
                                    <c:when test="${obj.authStatus == 1}">
                                        <td><span class="label label-success">已授权</span></td>
                                    </c:when>
                                    <c:when test="${obj.authStatus == 2}">
                                        <td><a href="https://developers.e.qq.com/oauth/authorize?client_id=1108287192&redirect_uri=${source}/admin/advertisers/authcallback/${obj.accountId}"><span class="label label-default">未授权</span></a></td>
                                    </c:when>
                                </c:choose>
                                <td>${obj.webUserActionSetId}</td>
                                <c:choose>
                                    <c:when test="${!empty obj.wxAppId}">
                                        <c:choose>
                                            <c:when test="${obj.wxAuthStatus == 1}">
                                                <td><span class="label label-success">${obj.wxOfficialAccount}</span></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><a href="javascript:void(0);" onclick="wxbind('${obj.id}')"><span class="label label-default">未绑定公众号</span></a></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <td><span class="label label-default">未绑定公众号</span></td>
                                    </c:otherwise>
                                </c:choose>
                                <td><fmt:formatDate value="${obj.createTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                <td><fmt:formatDate value="${obj.updateTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                <td class="text-center">
                                        <%--
                                        <c:choose>
                                            <c:when test="${obj.authStatus == 1}">
                                                <a href="${source}/admin/ad/adIndex/${obj.id}" target="_blank"><button type="button" class="btn btn-xs bg-primary-700">广告投放</button></a>
                                            </c:when>
                                            <c:when test="${obj.authStatus == 2}">
                                                <button type="button" class="btn btn-xs bg-default-700">广告投放</button>
                                            </c:when>
                                        </c:choose>
                                        --%>
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-xs bg-primary-700 dropdown-toggle" data-toggle="dropdown">更多<span
                                                    class="caret"></span></button>
                                            <ul class="dropdown-menu">
                                                <shiro:hasPermission name="/admin/advertisers/edit">
                                                    <li><a href="javascript:void(0);" onclick="edit('${obj.id}')">编辑信息</a></li>
                                                </shiro:hasPermission>
                                                <%--<li><a href="#"><a href="javascript:void(0);" onclick="syncAdvInfo('${obj.id}')>同步信息</a></li>--%>
                                            <c:if test="${obj.wxAuthStatus == 2 && obj.authStatus == 1 && !empty obj.wxAppId}">
                                                <li><a href="javascript:void(0);" onclick="wxbind('${obj.id}')">绑定微信</a></li>
                                            </c:if>
                                            <c:if test="${obj.authStatus == 2}">
                                                <li><a href="https://developers.e.qq.com/oauth/authorize?client_id=1108287192&redirect_uri=${source}/admin/advertisers/authcallback/${obj.accountId}">授权广点通</a></li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
        <!-- wxdetail modal -->
        <div id="wx_detail_modal_default" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-body">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label class="text-semibold">公众号名称</label>
                                    <div id="wechatAccountName"></div>
                                </div>
                                <div class="col-sm-4">
                                    <label class="text-semibold">公众号AppId</label>
                                    <div id="wechatAccountId"></div>
                                </div>
                                <div class="col-sm-4">
                                    <label class="text-semibold">客户ID</label>
                                    <div id="accountId"></div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label class="text-semibold">系统状态</label>
                                    <div id="systemStatus"></div>
                                </div>
                                <div class="col-sm-4">
                                    <label class="text-semibold">行业名称</label>
                                    <div id="industryName"></div>
                                </div>

                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label class="text-semibold">联系人</label>
                                    <div id="contactPerson"></div>
                                </div>
                                <div class="col-sm-4">
                                    <label class="text-semibold">联系电话</label>
                                    <div id="contactPersonTelephone"></div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- /wxdetail modal -->
        <script type="text/javascript" src="${scripts}/pages/advertisers/advertisers.js"></script>
        <script type="text/javascript">
            $(function() {

                $('.select').select2({
                    minimumResultsForSearch: "-1",
                    width: 100
                });

                $('.table-togglable').footable();


            })
        </script>
</body>
</html>
