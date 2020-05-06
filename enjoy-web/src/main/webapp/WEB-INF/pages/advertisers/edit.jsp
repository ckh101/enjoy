<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <script type="text/javascript">
        $(function () {
            $("#saveForm").bootstrapValidator({
                        fields: {
                            <c:if test="${advertiser==null}">
                            appName: {
                                validators: {
                                    remote: {
                                        message: "客户ID已经存在",
                                        url: "${source}/admin/advertisers/checkAccountId",
                                        delay:2000
                                    }
                                }
                            },
                            </c:if>
                        }
                    }
            ).on('success.form.bv', function (e) {
                $(e.target).formSubmit({
                    success: function (data) {
                        console.log(data);
                        loadMenu();
                    }
                });
            });
        });
    </script>
    <style>
        .blk {
            padding-bottom: 30px;
            display: block;
            margin-left:15px;
            margin-right: 15px;
        }
        .remark {
            padding: 15px 15px 15px 15px;
            margin-bottom: 10px;
            border: 1px solid #efd6b6;
            background-color: #fff8e7;
            position: relative;
        }
        li {
            display: list-item;
            text-align: -webkit-match-parent;
        }
        .ico-warn-mid {
            position: absolute;
            left: 35px;
            top: 17px;
        }
        ul {
            padding-left: 0;
            margin-left: 55px;
        }
    </style>
</head>
<body>
    <div class="panel">

        <div class="panel-heading">
            <div class="blk">
                <div class="remark">
                    <div>
                        <i class="icon-notification2 ico-warn-mid" style="color:#ff7600 "><i></i></i></div>
                    <ul>
                        <li>1、投放微信朋友圈广告的前提是广告主必须绑定一个微信公众号且微信公众号已开通广告功能</li>
                        <li>2、目前朋友圈广告仅支持微信朋友圈品牌活动页和电商推广页的投放，并且要求公众号已经开通相关权限</li>
                        <li>3、微信公众号一旦绑定，不可解除。</li>
                        <li>4、一个广告帐号只能绑定一个公众号，但一个公众号可被多个推广帐号进行绑定。</li>
                    </ul>
                </div>
                <p></p>
            </div>
            <div class="panel-body">
                <form id="saveForm" action="/admin/advertisers/save" method="post" autocomplete="off">
                    <c:if test="${advertiser != null }">
                        <input type="hidden" id="id" name="id" value="${advertiser.id}"/>
                    </c:if>
                    <input type="hidden" id="method" name="method" value="${method }"/>
                    <div class="form-group">
                        <label>代理商</label>
                        <input type="text" value="" class="form-control"   disabled  required  placeholder="常州商邦网络科技有限公司">
                        <input type="hidden" id="agentId" name="agentId" value="1"/>
                    </div>
                    <div class="form-group">
                        <label>客户ID</label>
                        <input type="text" value="${advertiser.accountId}" class="form-control" name="accountId" id="accountId" ${advertiser != null?"readonly":""} required  placeholder="客户ID">
                    </div>
                    <div class="form-group">
                        <label>WEB数据源ID</label>
                        <input type="text" value="${advertiser.webUserActionSetId}" class="form-control" name="webUserActionSetId" id="webUserActionSetId"  required  placeholder="web数据源ID">
                    </div>

                    <div class="form-group">
                        <label>公众号名称</label>
                        <input type="text" value="${advertiser.wxOfficialAccount}" class="form-control" name="wxOfficialAccount" ${advertiser.wxAuthStatus == 1?"readonly":""}  placeholder="请输入公众号名称">
                    </div>
                    <div class="form-group">
                        <label>公众号appId</label>
                        <input type="text" value="${advertiser.wxAppId}" class="form-control" name="wxAppId" ${advertiser.wxAuthStatus == 1?"readonly":""}  placeholder="请输入公众号appId">
                    </div>
                    <div class="form-group">
                        <label>原始id</label>
                        <input type="text" value="${advertiser.originalId}" class="form-control" name="originalId" ${advertiser.wxAuthStatus == 1?"readonly":""}   placeholder="请输入公众号原始ID">
                    </div>
                    <div class="btn-toolbar list-toolbar">
                        <button class="btn bg-primary-700"><i class="fa fa-save"></i> 保存</button>
                    </div>
                </form>
            </div>
        </div>

    </div>
</body>
