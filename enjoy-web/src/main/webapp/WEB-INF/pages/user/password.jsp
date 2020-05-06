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
                            }
                        }
                    }
            ).on('success.form.bv', function (e) {
            	var password = $("#password");
            	password.val($.md5(password.val().toString()));
            	
                $(e.target).formSubmit({
                    success: function () {
                        loadMenu();
                        if('${uid}' == ${_user.id}){
                        	window.location.href="${source}/login"
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="panel">
    <div class="panel-heading">
        <div class="panel-body">
            <form id="saveForm" action="/admin/user/resetpassword" method="post" autocomplete="off">
                <input type="hidden" id="uid" name="uid" value="${uid}"/>
                <c:if test="${user==null}">
                    <div class="form-group">
                        <label>新密码</label>
                        <input type="password" class="form-control" name="password" id="password" required maxlength="30" placeholder="请输入密码">
                    </div>
                    <div class="form-group">
                        <label>确认密码</label>
                        <input type="password" class="form-control" name="confirmPassword" required maxlength="30" placeholder="请再次输入密码">
                    </div>
                </c:if>
                <div class="btn-toolbar list-toolbar">
                    <button class="btn btn-primary"><i class="fa fa-save"></i> 保存</button>
                </div>
            </form>
        </div>
    </div>
    </div>
</body>
