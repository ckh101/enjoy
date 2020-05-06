<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html lang="en">
<head>

    <!-- Core JS files -->
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/fileinput.min.js"></script>
    <script type="text/javascript" src="https://unpkg.com/qiniu-js@2.5.4/dist/qiniu.min.js"></script>

    <script type="text/javascript">
        $(function () {
            $("#saveForm").bootstrapValidator({
                        fields: {
                            <c:if test="${packageName==null}">
                            packageName: {
                                validators: {
                                    remote: {
                                        message: "数据包已存在",
                                        url: "${source}/admin/datapackage/verifyPackage",
                                        delay:2000
                                    }
                                }
                            },
                            </c:if>
                        }
                    }
            ).on('success.form.bv', function (e) {
                var industry_selected = "";
                $('#industry_selected option:selected').each(function () {
                    if(industry_selected == ""){
                        industry_selected = $(this).val();
                    }else{
                        industry_selected = industry_selected+","+$(this).val();
                    }
                });
                $("#industryIds").val(industry_selected);
                $(e.target).formSubmit({
                    success: function () {
                        loadMenu();
                    }
                });
            });
          //默认选择状态
            <c:if test="${industryIds != null}">
            var industryIds = "${industryIds}".split(",");
            for(var i = 0; i < industryIds.length; i++){
                var option = "#industry_selected option[value='"+industryIds[i]+"']";
                $(option).attr("selected", true);
            }
            $('#industry_selected').multiselect('rebuild');
            $("#industry_selected").multiselect( 'refresh' );
            $("input:checkbox").uniform();
            </c:if>
            <c:if test="${method == 'add'}">
                initFileInput("datapackage","datapackageurl","data-package-upload-btn","smartad/datapackage")
            </c:if>
        });
        $(".bootstrap-select").selectpicker();

    </script>
</head>
<body>
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-body">
                <form id="saveForm" action="admin/datapackge/save" method="post" autocomplete="off">
                    <input type="hidden" id="id" name="id" value="${datapackge.id}"/>
                    <input type="hidden" id="method" name="method" value="${method }"/>
                    <div class="form-group">
                        <label><span class="text-semibold">数据包名称</span></label>
                        <input type="text" value="${datapackge.packageName}" class="form-control" name="packageName" id="packageName" ${datapackge==null?"":"readonly"}  required  placeholder="数据包名称">
                    </div>
                    <c:if test="${method=='add'}">
                        <div class="form-group has-feedback has-feedback-left">
                            <label><span class="text-semibold">数据包</span></label>
                            <input type="text" class="form-control col-md-10" id="datapackageurl" name="datapackageurl" placeholder="数据包链接" style="margin-bottom: 10px">
                            <input type="file" class="file-input-extensions" id="datapackage">
                            <div class="progress col-md-12" style="padding-left: 0px;padding-right: 0px;display: none;">
                                <div class="progress-bar bg-teal" style="width: 0;">
                                    <span>0% Complete</span>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group" >
                        <label><span class="text-semibold">行业</span></label>

                        <input type="hidden" id="industryIds" name="industryIds" value="">
                        <div class="multi-select-full">

                            <select class="multiselect-filtering" id="industry_selected" required multiple="multiple">
                                <c:forEach items="${industrys}" var="obj">
                                    <option value="${obj.id}">${obj.parent.industry}-${obj.industry}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="btn-toolbar list-toolbar">
                        <button class="btn btn-primary"><i class="fa fa-save"></i> 保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
