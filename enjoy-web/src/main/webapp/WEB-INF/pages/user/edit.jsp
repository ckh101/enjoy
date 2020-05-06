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
                    <div class="form-group">
                        <label>账号类型</label>
                        <select name="accountType" id="accountType" class="bootstrap-select" data-width="100%">
                            <option>选择账号类型</option>

                            <option value="SYSTEM">系统</option>

                        </select>
                    </div>
                    <div class="form-group">
                        <label>角色</label>
                        <select name="roleId" id="roleId" class="bootstrap-select" required data-width="100%">
                            <option>选择角色</option>
                            <c:forEach items="${rlist}" var="role">
                                        <option value="${role.id}">${role.roleName}</option>

                            </c:forEach>
                        </select>
                    </div>

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
