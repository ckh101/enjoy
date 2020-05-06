<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html>
<head>
    <script type="text/javascript">
        $(function () {
            $("#saveForm").bootstrapValidator({
                fields: {
                        
                }
            }
            ).on('success.form.bv', function (e) {

                $(e.target).formSubmit({
                    success: function () {
                        loadMenu();
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
            <form id="saveForm" action="/admin/miniprogram/save" method="post" autocomplete="off">
            	<c:if test="${miniProgram != null }">
            	<input type="hidden" id="id" name="id" value="${miniProgram.id}"/>
            	</c:if>
				<input type="hidden" id="method" name="method" value="${method }"/>
                <div class="form-group">
                    <label>小程序名称</label>
                    <input type="text" value="${miniProgram.miniProgramName}" class="form-control" name="miniProgramName" id="miniProgramName"  required placeholder="输入小程序名称">
                </div>
                <div class="form-group">
                    <label>小程序原始ID</label>
                    <input type="text" value="${miniProgram.miniProgramId}" class="form-control" name="miniProgramId" id="miniProgramId"  required placeholder="输入小程序原始ID">
                </div>
                <div class="btn-toolbar list-toolbar">
                    <button class="btn btn-primary"><i class="fa fa-save"></i> 保存</button>
                </div>
            </form>
        </div>
    </div>
    </div>
</body>
