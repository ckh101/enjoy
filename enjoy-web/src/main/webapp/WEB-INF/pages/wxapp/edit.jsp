<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html lang="en">
<head>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery.md5.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#saveForm").bootstrapValidator({
                        fields: {
                            <c:if test="${app==null}">
                            appName: {
                                validators: {
                                    remote: {
                                        message: "公众号已存在",
                                        url: "${source}/admin/wxapp/verifyAppName",
                                        delay:2000
                                    }
                                }
                            },
                            </c:if>
                        }
                    }
            ).on('success.form.bv', function (e) {
                $(e.target).formSubmit({
                    success: function () {
                        loadMenu();
                    }
                });
            });
          //默认选择状态
           <c:if test="${app != null}">
                $("#typeId").val("${app.typeId}");
           </c:if>
           $(".bootstrap-select").selectpicker();
           $(".dictionary").formatConfigData();
        });
    </script>
</head>
<body>
<div class="panel">
    <div class="panel-heading">
        <div class="panel-body">
            <form id="saveForm" action="/admin/wxapp/save" method="post" autocomplete="off">
                <c:if test="${app != null }">
                	<input type="hidden" id="id" name="id" value="${app.id}"/>
                </c:if>
				<input type="hidden" id="method" name="method" value="${method }"/>
                <div class="form-group">
                    <label>公司主体</label>
                    <input type="text" value="${app.company}" class="form-control" name="company" id="company" required  placeholder="请输入公司主体">
                </div>
                <div class="form-group">
                    <label>公众号名称</label>
                    <input type="text" value="${app.appName}" class="form-control" name="appName" required placeholder="请输入公众号名称">
                </div>
                <div class="form-group">
                    <label>公众号appId</label>
                    <input type="text" value="${app.appId}" class="form-control" name="appId" required  placeholder="请输入appId">
                </div>
                <div class="form-group">
                    <label>应用类型</label>
                    <select name="typeId" id="typeId" class="bootstrap-select"  data-width="100%">
                            <option value="1">微信公众号</option>
                            <option value="2">微信小程序</option>
                    </select>
                </div>
                <div class="btn-toolbar list-toolbar">
                    <button class="btn btn-primary"><i class="fa fa-save"></i> 保存</button>
                </div>
            </form>
        </div>
    </div>
    </div>
</body>
