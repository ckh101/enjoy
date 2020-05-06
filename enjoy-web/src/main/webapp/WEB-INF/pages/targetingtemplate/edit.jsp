<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../include/config.jsp" %>
<!doctype html>
<html>
<head>
    <script type="text/javascript" src="${scripts}/plugins/forms/tags/tagsinput.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jquery_ui/interactions.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/daterangepicker.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/select2.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/styling/switch.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/styling/switchery.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jasny_bootstrap.min.js"></script>
    <link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
    <link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
    <script src="${scripts}/plugins/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
    <script src="${scripts}/plugins/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
    <script src="${scripts}/plugins/ztree/jquery.ztree.exhide.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${scripts}/pages/form_select2.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/forms/selects/bootstrap_multiselect.js"></script>
    <script type="text/javascript" src="${scripts}/pages/form_multiselect.js"></script>
</head>
<body>
<div class="panel panel-white">

    <div class="panel-body">
        <div class="row">
            <div class="col-md-12">

                <fieldset>

                    <h4 class="fw-bold mb-20">新增定向模板</h4>
                    <div class="form-group form-horizontal row">
                        <label class="control-label col-xs-2 col-sm-2 col-md-2 col-lg-2">模板名称</label>
                        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                            <input type="text" name="templateName" id="templateName" placeholder="输入模板名称" class="form-control">
                            <input type="hidden" name="id" id="id">
                        </div>
                    </div>
                    <hr />

                    <h4 class="fw-bold mb-20">定向条件</h4>
                    <div class="form-group col-md-12 bottom-border">
                        <label class="control-label col-md-2">地域</label>
                        <input type="hidden" name="location_types" id="location_types" value="LIVE_IN">
                        <div class="col-md-6">
                            <div class="row">
                                <div class="form-group has-feedback has-feedback-left">
                                    <input type="text" class="form-control col-md-10" id="locinput" placeholder="地域列表">
                                    <a href="javascript:void(0)" id="locinput_removeall">清空</a>
                                </div>
                                <div class="form-group has-feedback has-feedback-left">
                                    <input type="text" class="form-control input-lg" id="search_input_loc" onKeyUp="search('loc');" placeholder="搜索国家、省、市、区、商圈">
                                    <div class="form-control-feedback">
                                        <i class="icon-search4 text-size-base"></i>
                                    </div>
                                    <div id="searchloc" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                    <div id="loc" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                </div>
                            </div>

                        </div>

                    </div>
                    <div class="form-group col-md-12 bottom-border">
                        <label class="control-label col-md-2">年龄</label>
                        <div class="col-md-10">
                            <div class="row">
                                <div class="col-md-4 form-group has-feedback has-feedback-left">
                                    <select name="age_min" id="age_min" class="select">
                                        <option value="">起始年龄</option>
                                        <c:forEach var="i" begin="14" end="66" step="1">
                                            <c:choose>
                                                <c:when test="${i != 66}">
                                                    <option value="${i}">${i}岁</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${i}">${i}岁以上</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                                <span class="col-md-1" style="width: 20px">-</span>
                                <div class="col-md-4 form-group has-feedback has-feedback-left">
                                    <select id="age_max" name="age_max" class="select">
                                        <option value="">结束年龄</option>
                                        <c:forEach var="i" begin="14" end="66" step="1">
                                            <c:choose>
                                                <c:when test="${i != 66}">
                                                    <option value="${i}">${i}岁</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${i}">${i}岁以上</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-md-12 bottom-border">
                        <label class="control-label col-md-2">性别</label>
                        <div class="col-md-10">
                            <div class="row">
                                <div class="col-md-2 form-group has-feedback has-feedback-left">
                                    <label class="radio-inline">
                                        <input type="radio" name="gender" value="" class="control-success" checked="checked">
                                        全部
                                    </label>
                                </div>
                                <div class="col-md-2 form-group has-feedback has-feedback-left">
                                    <label class="radio-inline">
                                        <input type="radio" name="gender" value="MALE" class="control-success" >
                                        男
                                    </label>
                                </div>
                                <div class="col-md-2 form-group has-feedback has-feedback-left">
                                    <label class="radio-inline">
                                        <input type="radio" name="gender" value="FEMALE" class="control-success">
                                        女
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-md-12 bottom-border">
                        <label class="control-label col-md-2">兴趣/行为</label>
                        <div class="col-md-10">
                            <div class="row">
                                <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                    <label>
                                        <input type="checkbox" id="behavior_interest_switch" class="switchery" checked="checked" onclick="switchChange(this, 'behavior_interest')">
                                        全部
                                    </label>
                                </div>
                                <div id="behavior_interest" style="display: none;">
                                    <div class="form-group col-md-12 bottom-border">
                                        <label class="control-label col-md-2">兴趣</label>
                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="form-group has-feedback has-feedback-left">
                                                    <input type="text" class="form-control col-md-10" id="interestinput" placeholder="兴趣列表">
                                                    <a href="javascript:void(0)" id="interestinput_removeall">清空</a>
                                                </div>
                                                <div class="form-group has-feedback has-feedback-left">
                                                    <input type="text" class="form-control input-lg" id="search_input_interest" onKeyUp="search('interest');" placeholder="搜索兴趣">
                                                    <div class="form-control-feedback">
                                                        <i class="icon-search4 text-size-base"></i>
                                                    </div>
                                                    <div id="searchinterest" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                    <div id="interest" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="form-group col-md-12 bottom-border">
                                        <label class="control-label col-md-2">行为</label>
                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="form-group has-feedback has-feedback-left">
                                                    <input type="text" class="form-control col-md-10" id="behaviorinput" placeholder="行为列表">
                                                    <a href="javascript:void(0)" id="behaviorinput_removeall">清空</a>
                                                </div>
                                                <div class="form-group has-feedback has-feedback-left">
                                                    <input type="text" class="form-control input-lg" id="search_input_behavior" onKeyUp="search('behavior');" placeholder="搜索行为">
                                                    <div class="form-control-feedback">
                                                        <i class="icon-search4 text-size-base"></i>
                                                    </div>
                                                    <div id="searchbehavior" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                    <div id="behavior" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12 bottom-border">
                                        <label class="control-label col-md-2">行为场景</label>
                                        <div class="col-md-10">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_ALL" type="checkbox" class="control-success">
                                                            不限
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_APP" type="checkbox" class="control-success">
                                                            APP
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_ECOMMERCE" type="checkbox" class="control-success">
                                                            电商
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="scene" value="BEHAVIOR_INTEREST_SCENE_INFORMATION" type="checkbox" class="control-success">
                                                            咨询
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12 bottom-border">
                                        <label class="control-label col-md-2">行为时效性</label>
                                        <div class="col-md-10">
                                            <div class="row">
                                                <div class="col-md-10">
                                                    <div class="col-md-4 form-group has-feedback has-feedback-left" style="margin-bottom: 10px;">
                                                        <select class="form-control" id="time_window" data-width="100%">
                                                            <option value="">请选择</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_SEVEN_DAY">7 天</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_FIFTEEN_DAY">15 天</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_THIRTY_DAY">30 天</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_THREE_MONTH">3 个月</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_SIX_MONTH">6 个月</option>
                                                            <option value="BEHAVIOR_INTEREST_TIME_WINDOW_ONE_YEAR">1 年</option>
                                                        </select>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12 bottom-border">
                                        <label class="control-label col-md-2">行为强度</label>
                                        <div class="col-md-10">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="intensity"  value="BEHAVIOR_INTEREST_INTENSITY_ALL" type="checkbox" class="control-success">
                                                            不限
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                        <label>
                                                            <input name="intensity" value="BEHAVIOR_INTEREST_INTENSITY_HIGH" type="checkbox" class="control-success">
                                                            高
                                                        </label>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-md-12 bottom-border">
                        <label class="control-label col-md-2">公众号媒体类型</label>
                        <div class="col-md-8">
                            <div class="row">
                                <div class="col-md-6 form-group has-feedback has-feedback-left switchery-xs">
                                    <label>
                                        <input type="checkbox" id="wcates_switch"  class="switchery" checked="checked" onclick="switchChange(this, 'wcates_select')">
                                        全部
                                    </label>
                                </div>
                                <div id="wcates_select" style="display: none;">
                                    <div class="form-group col-md-8" style="margin-bottom:10px;">
                                        <div class="multi-select-full">
                                            <select class="multiselect-filtering" id="wcates_value" multiple="multiple">
                                                <c:forEach items="${wechatCateData}" var="obj">
                                                    <option value="${obj.id}">${obj.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div  class="panel panel-flat panel-collapsed"  style="margin-top:-20px;-webkit-box-shadow:0 0px 0px rgba(0,0,0,0);box-shadow:0 1px 1px rgba(0,0,0,0);border-color:#fff;">
                    <div class="panel-body" style="padding: 0px;">
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">学历</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="学历" data-check-name="education" checked="checked" onclick="switchChange(this,'xueli')">
                                                全部
                                            </label>
                                        </div>
                                        <div id="xueli" style="display: none" class="col-md-12">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="DOCTOR" type="checkbox" class="control-success">
                                                    博士
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="MASTER" type="checkbox" class="control-success">
                                                    硕士
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="BACHELOR" type="checkbox" class="control-success">
                                                    本科
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="SENIOR" type="checkbox" class="control-success">
                                                    专科
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="JUNIOR" type="checkbox" class="control-success">
                                                    高中
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="PRIMARY" type="checkbox" class="control-success">
                                                    初中
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="education" value="JUNIOR_COLLEGE" type="checkbox" class="control-success">
                                                    小学
                                                </label>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">婚恋状态</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" id="hlzt_switch" class="switchery" checked="checked" onchange="switchChange(this,'hlzt')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="hlzt" style="display:none;">
                                            <div id="marital_status_select">
                                                <div class="form-group col-md-6" style="margin-bottom:10px">
                                                    <input type="hidden" id="maritalStatus" name="maritalStatus" value="">
                                                    <div class="multi-select-full">
                                                        <select class="multiselect-filtering" id="marital_status_value" multiple="multiple">
                                                            <option value="SINGLE">单身</option>
                                                            <option value="IN_LOVE">恋爱</option>
                                                            <option value="NEWLY_MARRIED">新婚</option>
                                                            <option value="MARRIED">已婚</option>
                                                            <option value="PARENTING">育儿</option>
                                                            <option value="PARENTING_0">育儿（孕育中）</option>
                                                            <option value="PARENTING_0_6">育儿（宝宝 0-6 个月）</option>
                                                            <option value="PARENTING_6_12">育儿（宝宝 6-12 个月</option>
                                                            <option value="PARENTING_12_24">育儿（宝宝 1-2 岁）</option>
                                                            <option value="PARENTING_24_36">育儿（宝宝 2-3 岁）</option>
                                                            <option value="CHILD_PRE_SCHOOL">育儿（孩子 3-6 周岁）</option>
                                                            <option value="CHILD_PRIMARY_SCHOOL">育儿（孩子 6-12 周岁）</option>
                                                            <option value="CHILD_JUNIOR_SCHOOL">育儿（孩子 12-15 周岁）</option>
                                                            <option value="CHILD_HIGH_SCHOOL">育儿（孩子 15-18 周岁）</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">工作状态</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="工作状态" data-check-name="workingStatus" checked="checked" onchange="switchChange(this,'gzzt')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="gzzt" style="display:none;">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="COLLEGE_STUDENT" type="checkbox" class="control-success">
                                                    在校大学生
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="BUSINESS_USER" type="checkbox" class="control-success">
                                                    商旅用户
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="GOVERNMENT_OFFICER" type="checkbox" class="control-success">
                                                    政府公职人员
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="SCIENCE_EDUCATOR" type="checkbox" class="control-success">
                                                    科研教育者
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="IT_WORKER" type="checkbox" class="control-success">
                                                    IT 互联网工作者
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="workingStatus" value="HEALTH_CARE_WORKER" type="checkbox" class="control-success">
                                                    医护工作者
                                                </label>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">操作系统</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="操作系统" data-check-name="userOs" checked="checked" onchange="switchChange(this,'oscheck')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="oscheck" style="display: none;">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="userOs" value="IOS" type="checkbox" class="control-success">
                                                    ios系统
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="userOs" value="ANDROID" type="checkbox" class="control-success">
                                                    安卓系统
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">新设备</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="新设备" data-check-name="newDevice" checked="checked" onchange="switchChange(this,'xsb')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="xsb" style="display: none;">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="newDevice" value="IOS" type="checkbox" class="control-success">
                                                    iOS 新用户
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="newDevice" value="ANDROID" type="checkbox" class="control-success">
                                                    Android 新用户
                                                </label>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">手机价格</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-6 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="手机价格" data-check-name="devicePrice" checked="checked" onchange="switchChange(this,'phone_price_check')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="phone_price_check" style="display:none;">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="devicePrice" value="PRICE_1500_LESS" type="checkbox" class="control-success">
                                                    1500元以下
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="devicePrice" value="PRICE_1500_2500"  type="checkbox" class="control-success">
                                                    1500~2500元
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="devicePrice" value="PRICE_2500_3500" type="checkbox" class="control-success">
                                                    2500~3500元
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="devicePrice" value="PRICE_3500_4500" type="checkbox" class="control-success">
                                                    3500元~4500元
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="devicePrice" value="PRICE_4500_MORE" type="checkbox" class="control-success">
                                                    4500元以上
                                                </label>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">运营商</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="运营商" data-check-name="networkOperator" checked="checked" onchange="switchChange(this,'yls')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="yls" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkOperator" value="CTC" type="checkbox" class="control-success">
                                                    电信
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkOperator" value="CMCC" type="checkbox" class="control-success">
                                                    移动
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkOperator" value="CUC" type="checkbox" class="control-success">
                                                    联通
                                                </label>
                                            </div>


                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">联网方式</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="联网方式" data-check-name="networkType" checked="checked" onchange="switchChange(this,'lwfs')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="lwfs" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkType" value="WIFI" type="checkbox" class="control-success">
                                                    wifi
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkType" value="NET_4G" type="checkbox" class="control-success">
                                                    4G
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkType" value="NET_3G" type="checkbox" class="control-success">
                                                    3G
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkType" value="NET_2G" type="checkbox" class="control-success">
                                                    2G
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">上网场景</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="上网场景" data-check-name="networkScene" checked="checked" onchange="switchChange(this,'swcj')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="swcj" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkScene" value="PUBLIC_PLACE" type="checkbox" class="control-success">
                                                    公共场所
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkScene" value="HOME" type="checkbox" class="control-success">
                                                    家庭
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkScene" value="COMPANY" type="checkbox" class="control-success">
                                                    企业
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="networkScene" value="SCHOOL" type="checkbox" class="control-success">
                                                    学校
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">消费能力</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="消费能力" data-check-name="consumptionStatus" checked="checked" onchange="switchChange(this,'xfnl')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="xfnl" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="consumptionStatus" value="HIGH" type="checkbox" class="control-success">
                                                    高消费
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="consumptionStatus" value="LOW" type="checkbox" class="control-success">
                                                    低消费
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">居民社区价格</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery" id="sqjg_switch" checked="checked" onchange="switchChange(this,'sqjg')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="sqjg" style="display: none">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <select id="residential_community_price_min" name="residential_community_price_min" class="select">
                                                    <option value="">起始价格</option>
                                                    <option value="1">1元/m²</option>
                                                    <option value="1000">1000元/m²</option>
                                                    <option value="2000">2000元/m²</option>
                                                    <option value="3000">3000元/m²</option>
                                                    <option value="4000">4000元/m²</option>
                                                    <option value="5000">5000元/m²</option>
                                                    <option value="6000">6000元/m²</option>
                                                    <option value="7000">7000元/m²</option>
                                                    <option value="8000">8000元/m²</option>
                                                    <option value="9000">9000元/m²</option>
                                                    <option value="10000">10000元/m²</option>
                                                    <option value="20000">20000元/m²</option>
                                                    <option value="30000">30000元/m²</option>
                                                    <option value="40000">40000元/m²</option>
                                                    <option value="50000">50000元/m²</option>
                                                    <option value="60000">60000元/m²</option>
                                                    <option value="70000">70000元/m²</option>
                                                    <option value="80000">80000元/m²</option>
                                                    <option value="90000">90000元/m²</option>
                                                    <option value="100000">100000元/m²</option>
                                                </select>
                                            </div>
                                            <span class="col-md-1" style="width: 20px">-</span>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <select id="residential_community_price_max" name="residential_community_price_max" class="select">
                                                    <option value="">结束价格</option>
                                                    <option value="1000">1000元/m²</option>
                                                    <option value="2000">2000元/m²</option>
                                                    <option value="3000">3000元/m²</option>
                                                    <option value="4000">4000元/m²</option>
                                                    <option value="5000">5000元/m²</option>
                                                    <option value="6000">6000元/m²</option>
                                                    <option value="7000">7000元/m²</option>
                                                    <option value="8000">8000元/m²</option>
                                                    <option value="9000">9000元/m²</option>
                                                    <option value="10000">10000元/m²</option>
                                                    <option value="20000">20000元/m²</option>
                                                    <option value="30000">30000元/m²</option>
                                                    <option value="40000">40000元/m²</option>
                                                    <option value="50000">50000元/m²</option>
                                                    <option value="60000">60000元/m²</option>
                                                    <option value="70000">70000元/m²</option>
                                                    <option value="80000">80000元/m²</option>
                                                    <option value="90000">90000元/m²</option>
                                                    <option value="100000">100000元/m²</option>
                                                    <option value="999999999">10万以上/m²</option>
                                                </select>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">资产状态</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="资产状态" data-check-name="financialSituation" checked="checked" onchange="switchChange(this,'zczt')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="zczt" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="financialSituation" value="CAR_OWNERS" type="checkbox" class="control-success">
                                                    有车人士
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="financialSituation" value="HOME_OWNERS" type="checkbox" class="control-success">
                                                    有房人士
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">消费类型</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="消费类型" data-check-name="consumptionType" checked="checked" onchange="switchChange(this,'xflx')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="xflx" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="consumptionType" value="PAID_GOODS_VIRTUAL" type="checkbox" class="control-success">
                                                    虚拟商品
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="consumptionType" value="PAID_GOODS_REAL" type="checkbox" class="control-success">
                                                    实物商品
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">气温</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery" id="qw_switch" checked="checked" onchange="switchChange(this,'qw')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="qw" style="display: none">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <select id="temperature_min" name="temperature_min" class="form-control" data-width="100%">
                                                    <c:forEach begin="223" end="323" var="i" step="1" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${i-223-50 == 0}">
                                                                <option value="${i}" selected="selected">${i-223-50}&#8451;</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${i}">${i-223-50}&#8451;</option>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <span class="col-md-1" style="width: 20px">-</span>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <select id="temperature_max" name="temperature_max" class="form-control" data-width="100%">
                                                    <c:forEach begin="223" end="323" var="i" step="1" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${i-223-50 == 50}">
                                                                <option value="${i}" selected="selected">${i-223-50}&#8451;</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${i}">${i-223-50}&#8451;</option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">紫外线指数</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="紫外线指数" data-check-name="uvIndex" checked="checked" onchange="switchChange(this,'zwx')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="zwx" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="uvIndex" value="WEAK" type="checkbox" class="control-success">
                                                    弱
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="uvIndex" value="TEND_WEAK" type="checkbox" class="control-success">
                                                    偏弱
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="uvIndex" value="MEDIUM" type="checkbox" class="control-success">
                                                    中等
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="uvIndex" value="TEND_STRONG" type="checkbox" class="control-success">
                                                    偏强
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="uvIndex" value="STRONG" type="checkbox" class="control-success">
                                                    强
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">穿衣指数</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="穿衣指数" data-check-name="dressingIndex" checked="checked" onchange="switchChange(this,'cy')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="cy" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="FREEZING" type="checkbox" class="control-success">
                                                    寒冷
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="COLD" type="checkbox" class="control-success">
                                                    冷
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="CHILLY" type="checkbox" class="control-success">
                                                    凉
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="COOL" type="checkbox" class="control-success">
                                                    温凉
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="MILDLY_COOL" type="checkbox" class="control-success">
                                                    凉舒适
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="MILD" type="checkbox" class="control-success">
                                                    舒适
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="WARM" type="checkbox" class="control-success">
                                                    热舒适
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="dressingIndex" value="TORRIDITY" type="checkbox" class="control-success">
                                                    炎热
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">化妆指数</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="化妆指数" data-check-name="makeupIndex" checked="checked" onchange="switchChange(this,'hz')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="hz" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="makeupIndex" value="PREVENT_CRACKING" type="checkbox" class="control-success">
                                                    防龟裂
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="makeupIndex" value="MOISTURING" type="checkbox" class="control-success">
                                                    保湿
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="makeupIndex" value="OIL_CONTROL" type="checkbox" class="control-success">
                                                    控油
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="makeupIndex" value="UV_PROTECT" type="checkbox" class="control-success">
                                                    防晒
                                                </label>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">气象</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="气象" data-check-name="climate" checked="checked" onchange="switchChange(this,'qx')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="qx" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="SHINE" type="checkbox" class="control-success">
                                                    晴天
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="CLOUDY" type="checkbox" class="control-success">
                                                    阴天
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="RAINY" type="checkbox" class="control-success">
                                                    雨天
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="FOGGY" type="checkbox" class="control-success">
                                                    雾
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="SNOWY" type="checkbox" class="control-success">
                                                    雪
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="climate" value="SANDY" type="checkbox" class="control-success">
                                                    沙尘
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">空气质量</label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery switchcheck" data-name="空气质量" data-check-name="airQualityIndex" checked="checked" onchange="switchChange(this,'kqzl')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="kqzl" style="display: none">
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="GOOD" type="checkbox" class="control-success">
                                                    优
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="MODERATE" type="checkbox" class="control-success">
                                                    良
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="LIGHTLY_POLLUTED" type="checkbox" class="control-success">
                                                    轻度污染
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="MODERATELY_POLLUTED" type="checkbox" class="control-success">
                                                    中度污染
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="HEAVILY_POLLUTED" type="checkbox" class="control-success">
                                                    重度污染
                                                </label>
                                            </div>
                                            <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="airQualityIndex" value="SEVERELY_POLLUTED" type="checkbox" class="control-success">
                                                    严重污染
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--<div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">再营销
                            </label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery" id="zyx_switch" checked="checked" onchange="switchChange(this,'zyx')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="zyx" style="display: none;">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="actions" value="WECHAT_OFFICIAL_ACCOUNT_FOLLOWED" type="checkbox" class="control-success">
                                                    已关注你的公众号
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="actions" value="WECHAT_COUPON_OBTAINED" type="checkbox" class="control-success">
                                                    曾领取你的微信卡券
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="actions" value="WECHAT_OFFICIAL_ACCOUNT_AD_LIKE" type="checkbox" class="control-success">
                                                    曾对你的微信公众号广告感兴趣
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="actions" value="WECHAT_MOMENTS_AD_LIKE" type="checkbox" class="control-success">
                                                    曾对你的微信朋友圈广告感兴趣
                                                </label>
                                            </div>

                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-md-12 bottom-border">
                            <label class="control-label col-md-2">排除营销
                            </label>
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
                                            <label>
                                                <input type="checkbox" class="switchery" id = "pcyx_switch" checked="checked" onchange="switchChange(this,'pcyx')">
                                                全部
                                            </label>
                                        </div>
                                        <div class="col-md-12" id="pcyx" style="display:none;">
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="excludedActions" value="WECHAT_OFFICIAL_ACCOUNT_FOLLOWED" type="checkbox" class="control-success">
                                                    已关注你的公众号
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="excludedActions" value="WECHAT_COUPON_OBTAINED" type="checkbox" class="control-success">
                                                    曾领取你的微信卡券
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="excludedActions" value="WECHAT_OFFICIAL_ACCOUNT_AD_LIKE" type="checkbox" class="control-success">
                                                    曾对你的微信公众号广告感兴趣
                                                </label>
                                            </div>
                                            <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                <label>
                                                    <input name="excludedActions" value="WECHAT_MOMENTS_AD_LIKE" type="checkbox" class="control-success">
                                                    曾对你的微信朋友圈广告感兴趣
                                                </label>
                                            </div>

                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>--%>

                    </div>
                    <button type="button" class="btn bg-primary-700" style="float:right;" onclick="saveAdGroup()">提交</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var params = ${data}.params;
    var locData = ${locData};
    var behaviorData = ${behaviorData};
    var interestData = ${interestData};
</script>
<script type="text/javascript" src="${scripts}/pages/ad/targetingtemplate/targetingtemplate_edit.js"></script>
</body>
