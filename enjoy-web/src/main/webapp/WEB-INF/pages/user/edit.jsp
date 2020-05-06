<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html lang="en">
<head>

	<!--<script type="text/javascript" src="${scripts}/core/libraries/jquery.md5.js"></script>-->
    <!-- Core JS files -->
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>

    <script type="text/javascript">
        $(function () {



            $("#saveForm").bootstrapValidator({
                        fields: {
                            <c:if test="${user==null}">
                            account: {
                                validators: {
                                    remote: {
                                        message: "登录账号已存在",
                                        url: "${source}/admin/user/verifyAccount",
                                        delay:2000
                                    }
                                }
                            },
                            </c:if>
                            <%--
                            password: {
                                validators: {
                                    identical: {
                                        message: "密码不一致",
                                        field: 'confirmPassword'
                                    }
                                }
                            },
                            confirmPassword: {
                                validators: {
                                    identical: {
                                        message: "密码不一致",
                                        field: 'password'
                                    }
                                }
                            },
                            --%>
                            phone: {
                                validators: {
                                    notEmpty: {
                                        message: "手机号码必填"
                                    },
                                    regexp: {
                                        message: "手机号码格式不正确",
                                        regexp: /^((16[0-9])|(17[0-9])|(14[0-9])|(19[0-9])|(13[0-9])|(15[^4,\D])|(18[0,1-9]))\d{8}$/
                                    },
                                    <c:if test="${user==null}">
                                    remote: {
                                        message: "电话号码已存在",
                                        url: "${source}/admin/user/verifyPhone",
                                        delay:2000
                                    }
                                    </c:if>
                                }
                            }
                        }
                    }
            ).on('success.form.bv', function (e) {
                <%--
            	<c:if test="${user==null}">
            	var password = $("#password");
            	password.val($.md5(password.val().toString()));
            	</c:if>
            	--%>
                var advs_selected = "";
                $('#advs_selected option:selected').each(function () {
                    if(advs_selected == ""){
                        advs_selected = $(this).val();
                    }else{
                        advs_selected = advs_selected+","+$(this).val();
                    }
                });
                $("#advs").val(advs_selected);
                var wx_advs_selected = "";
                $('#wx_advs_selected option:selected').each(function () {
                    if(wx_advs_selected == ""){
                        wx_advs_selected = $(this).val();
                    }else{
                        wx_advs_selected = wx_advs_selected+","+$(this).val();
                    }
                });
                $("#wxadvs").val(wx_advs_selected);
                $(e.target).formSubmit({
                    success: function () {
                        loadMenu();
                    }
                });
            });
          //默认选择状态

           /*$('#advs').multiselect('select', ['21']);*/
            <c:if test="${user != null}">
                $("#roleId").val("${user.role.id}");
                $("#accountType").val("${user.accountType}");
                $("#systemStatus").val("${user.systemStatus}");
            </c:if>
            <c:if test="${advsSelected != null}">
               var advsSelected = "${advsSelected}".split(",");
               for(var i = 0; i < advsSelected.length; i++){
                   var option = "#advs_selected option[value='"+advsSelected[i]+"']";
                   $(option).attr("selected", true);
               }
                $('#advs_selected').multiselect('rebuild');
                $("#advs_selected").multiselect('refresh');
                $("input:checkbox").uniform();
            </c:if>
            <c:if test="${wxAdvsSelected != null}">
                var wxAdvsSelected = "${wxAdvsSelected}".split(",");
                for(var i = 0; i < wxAdvsSelected.length; i++){
                    var option = "#wx_advs_selected option[value='"+wxAdvsSelected[i]+"']";
                    $(option).attr("selected", true);
                }
                $('#wx_advs_selected').multiselect('rebuild');
                $("#wx_advs_selected").multiselect( 'refresh' );
                $("input:checkbox").uniform();
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
                <form id="saveForm" action="/admin/user/save" method="post" autocomplete="off">
                    <input type="hidden" id="id" name="id" value="${user.id}"/>
                    <input type="hidden" id="method" name="method" value="${method }"/>
                    <div class="form-group">
                        <label>登录账号</label>
                        <input type="text" value="${user.account}" class="form-control" name="account" id="account" ${user==null?"":"readonly"}  required data-bv-stringlength="true" data-bv-stringlength-min="5" data-bv-stringlength-max="15" data-bv-stringlength-message="长度5-20位" placeholder="输入登录账号">
                    </div>
                    <div class="form-group">
                        <label>员工姓名</label>
                        <input type="text" value="${user.userName}" class="form-control" name="userName" data-bv-stringlength="true" data-bv-stringlength-min="2" required data-bv-stringlength-max="20" data-bv-stringlength-message="长度5-20位" placeholder="请输入员工真实姓名">
                    </div>
                    <div class="form-group">
                        <label>公司主体</label>
                        <input type="text" value="${user.company}" class="form-control" name="company" required  placeholder="请输入公司主体">
                    </div>
                    <div class="form-group">
                        <label>手机号码</label>
                        <input type="text" value="${user.phone}" class="form-control" name="phone"  id="phone" placeholder="请输入手机号码">
                    </div>
                    <%--
                    <c:if test="${user==null}">
                        <div class="form-group">
                            <label>密码</label>
                            <input type="password" class="form-control" name="password" id="password" required maxlength="30" placeholder="请输入密码">
                        </div>
                        <div class="form-group">
                            <label>确认密码</label>
                            <input type="password" class="form-control" name="confirmPassword" required maxlength="30" placeholder="请再次输入密码">
                        </div>
                    </c:if>
                    --%>
                    <div class="form-group">
                        <label>账号类型</label>
                        <select name="accountType" id="accountType" class="bootstrap-select" data-width="100%">
                            <option>选择账号类型</option>
                            <c:if test="${_user.accountType == 'SYSTEM'}">
                                <option value="SYSTEM">系统</option>
                            </c:if>
                            <option value="ADVERTISER">广告主</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>角色</label>
                        <select name="roleId" id="roleId" class="bootstrap-select" required data-width="100%">
                            <option>选择角色</option>
                            <c:forEach items="${rlist}" var="role">
                                <c:choose>
                                    <c:when test="${_user.accountType == 'ADVERTISER'}">
                                        <c:if test="${fn:startsWith(role.flagStr, 'ad_') && role.flagStr != 'ad_sys'}">
                                            <option value="${role.id}">${role.roleName}</option>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${role.id}">${role.roleName}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <c:if test="${!fn:startsWith(role.flagStr, 'pdd_')}">
                        <div class="form-group">
                            <label><span class="text-semibold">授权广点通广告主</span></label>
                            <input type="hidden" id="advs" name="advs" value="">
                            <div class="multi-select-full">
                                <select class="multiselect-filtering" id="advs_selected" required multiple="multiple">
                                    <c:forEach items="${advs}" var="obj">
                                        <option value="${obj.id}">${obj.corporationName}(${obj.accountId})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                    <%--<div class="form-group">
                        <label><span class="text-semibold">授权微信广告主</span></label>
                        <input type="hidden" id="wxadvs" name="wxadvs" value="">
                        <div class="multi-select-full">
                            <select class="multiselect-filtering" id="wx_advs_selected" required multiple="multiple">
                                <c:forEach items="${wxadvs}" var="obj">
                                    <option value="${obj.id}">${obj.appName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>--%>
                    <div class="form-group">
                        <label>状态</label>
                        <select name="systemStatus" id="systemStatus" class="bootstrap-select" data-width="100%">
                            <option>选择状态</option>
                            <option value="ALLOW" selected>正常</option>
                            <option value="FORBIDDEN">禁用</option>
                            <option value="DEL">删除</option>
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
