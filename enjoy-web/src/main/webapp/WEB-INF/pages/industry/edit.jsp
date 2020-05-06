<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html lang="en">
<head>

    <!-- Core JS files -->
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>

    <script type="text/javascript">
        $(function () {
            $("#saveForm").bootstrapValidator({
                        fields: {
                            <c:if test="${industry==null}">
                            industry: {
                                validators: {
                                    remote: {
                                        message: "行业已存在",
                                        url: "${source}/admin/industry/verifyIndustry",
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
            <c:if test="${industry != null}">
                $("#parentId").val("${industry.parentId}");
            </c:if>
        });
        $(".bootstrap-select").selectpicker();
    </script>
</head>
<body>
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-body">
                <form id="saveForm" action="/admin/industry/save" method="post" autocomplete="off">
                    <input type="hidden" id="id" name="id" value="${industry.id}"/>
                    <input type="hidden" id="method" name="method" value="${method }"/>
                    <div class="form-group">
                        <label>行业</label>
                        <input type="text" value="${industry.industry}" class="form-control" name="industry" id="industry" ${industry==null?"":"readonly"}  required  placeholder="行业名称">
                    </div>

                    <div class="form-group">
                        <label>父行业</label>
                        <select name="parentId" id="parentId" class="bootstrap-select" data-live-search="true" required data-width="100%">
                            <option value="0">选择父行业</option>
                            <c:forEach items="${plist}" var="obj">
                                <option value="${obj.id}">${obj.industry}</option>
                            </c:forEach>
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
