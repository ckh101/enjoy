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
            <c:if test="${module != null}">
            	$("#pid").val("${module.parentModule.id}");
            	$("#mtype").val("${module.mtype}");
            	$("#level").val("${module.level}");
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
            <form id="saveForm" action="/admin/module/save" method="post" autocomplete="off">
            	<c:if test="${module != null }">
            	<input type="hidden" id="id" name="id" value="${module.id}"/>
            	</c:if>
				<input type="hidden" id="method" name="method" value="${method }"/>
                <div class="form-group">
                    <label>模块名</label>
                    <input type="text" value="${module.mname}" class="form-control" name="mname" id="mname" required placeholder="输入模块名">
                </div>
                <div class="form-group">
                    <label>父模块</label>
                    <select name="parentModule.id" id="pid" class="bootstrap-select" data-live-search="true" data-width="100%">
                        <c:forEach items="${mlist }" var="obj">
                            <option ${module.parentModule.id==obj.id?"selected":"" } value="${obj.id}" >${obj.mname}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>链接</label>
                    <input type="text" value="${module.url}" class="form-control" name="url" placeholder="请链接">
                </div>
              
                <div class="form-group">
                    <label>图标</label>
                    <input type="text" value="${module.menuicon}" class="form-control" name="menuicon" placeholder="请输入图标">
                </div>
                
                <div class="form-group">
                    <label>类型</label>
                    <select name="mtype" id="mtype" class="bootstrap-select" data-width="100%">
                        <option value="1">菜单</option>
                        <option value="2">页面</option>
                        <option value="3">操作</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>level</label>
                    <select name="level" id="level" class="bootstrap-select" data-width="100%">
                        <option value="1">一级</option>
                        <option value="2">二级</option>
                        <option value="3">三级</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>顺序号</label>
                    <input type="text" value="${module.seq}" class="form-control" name="seq" placeholder="请输入顺序号">
                </div>
                <div class="form-group">
					<label class="display-block text-semibold">状态</label>
					<label class="radio-inline">
						<input type="radio" class="styled" value="1" name="mstatus" ${module.mstatus==1||module==null?"checked='checked'":"" }>
						启用
					</label>

					<label class="radio-inline">
						<input type="radio" class="styled" value="0" name="mstatus"  ${module.mstatus==0?"checked='checked'":"" }>
						禁用
					</label>
				</div>
				<div class="form-group">
                    <label>备注</label>
                    <textarea id="remark" name="remark" class="form-control" value="${module.remark }"></textarea>
                </div>
                <div class="btn-toolbar list-toolbar">
                    <button class="btn btn-primary"><i class="fa fa-save"></i> 保存</button>
                </div>
            </form>
        </div>
    </div>
    </div>
</body>
