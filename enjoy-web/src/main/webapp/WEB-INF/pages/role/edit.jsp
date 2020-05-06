<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html>
<head>
    <link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
    <script src="${scripts}/plugins/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
    <script src="${scripts}/plugins/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
    <script src="${scripts}/plugins/ztree/jquery.ztree.exhide.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
        	var setting = {
                  callback: {
                      beforeCheck: function (treeId, treeNode) {
                          if (treeNode.isParent) {
                              var treeObj = $.fn.zTree.getZTreeObj("tree");
                              treeObj.expandNode(treeNode, true, true, true);
                              return true;
                          } else {
                              return true;
                          }
                      }
                  },
                  check: {
                      enable: true
                  },
                  data: {
                      simpleData: {
                          enable: true
                      }
                  }
            };
            $.fn.zTree.init($("#tree"), setting, ${treeData});
            $("#saveForm").bootstrapValidator({
                fields: {
                        
                }
            }
            ).on('success.form.bv', function (e) {
            	//获取权限id,以","分隔值
                var treeObj = $.fn.zTree.getZTreeObj("tree");
                var nodes = treeObj.getCheckedNodes(true);
                var permissionIds = [];
                for (var i in nodes) {
                    permissionIds.push(nodes[i].id)
                }
                if (permissionIds.length <= 0) {
                    $.alertWarning("请选择需要授予的菜单");
                    return;
                }
                $(e.target).formSubmit({
                	data: {mids: permissionIds.join(",")},
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
            <form id="saveForm" action="/admin/role/save" method="post" autocomplete="off">
            	<c:if test="${role != null }">
            	<input type="hidden" id="id" name="id" value="${role.id}"/>
            	</c:if>
				<input type="hidden" id="method" name="method" value="${method }"/>
                <div class="form-group">
                    <label>角色名</label>
                    <input type="text" value="${role.roleName}" class="form-control" name="roleName" id="roleName" ${role != null?"readonly":"" } required placeholder="输入角色名">
                </div>
                <div class="form-group">
                    <label>标识字段</label>
                    <input type="text" value="${role.flagStr}" class="form-control" name="flagStr" id="flagStr" required placeholder="输入标识字段">
                </div>
                <div class="form-group">
                    <label>权限</label>
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div id="tree" class="ztree"></div>
                        </div>
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
