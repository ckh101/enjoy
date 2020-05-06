<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../include/config.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${webTitle}</title>
	<link href="https://fonts.loli.net/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
	<link href="${css}/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
	<link href="${css}/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/core.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/components.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/colors.min.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/layer/theme/default/layer.css" rel="stylesheet" type="text/css">
	<link href="${scripts}/plugins/validation/bootstrapValidator.min.css" rel="stylesheet" type="text/css">
	<link href="${css}/common_a.css" rel="stylesheet" type="text/css">
	<link href="${css}/index.css" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="${scripts}/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/tags/tagsinput.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/loaders/blockui.min.js"></script>
	<script type="text/javascript" src="${scripts}/core/libraries/jquery_ui/interactions.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/ui/moment/moment.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/pickers/daterangepicker.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/selects/select2.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/uniform.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/switch.min.js"></script>
	<script type="text/javascript" src="${scripts}/plugins/forms/styling/switchery.min.js"></script>

    <script type="text/javascript" src="${scripts}/plugins/forms/wizards/stepy.min.js"></script>
    <script type="text/javascript" src="${scripts}/core/libraries/jasny_bootstrap.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/validation/validate.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.3/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.3/js/plugins/piexif.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/fileinput.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/themes/fa/theme.min.js"></script>
    <script type="text/javascript" src="${scripts}/plugins/uploaders/locales/zh.js"></script>
	<link href="${scripts}/plugins/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
	<script src="${scripts}/plugins/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
	<script src="${scripts}/plugins/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
	<script src="${scripts}/plugins/ztree/jquery.ztree.exhide.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${scripts}/core/app.js"></script>
	<script type="text/javascript" src="${scripts}/pages/form_select2.js"></script>
	<script type="text/javascript" src="${scripts}/pages/form_checkboxes_radios.js"></script>
	<script type="text/javascript" src="https://unpkg.com/qiniu-js@2.5.4/dist/qiniu.min.js"></script>
    <script type="text/javascript" src="${scripts}/pages/picker_date.js"></script>
    <style>
        .file-preview{
            width: 100%;
            height: 100%;
        }
        .file-preview-frame{
            height: 100%;
            max-height: 800px;
        }
    </style>
	<script type="text/javascript">
		var treeData = ${treeData};
		var dataMap = {};
		var searchKeyWord="";
		$(function () {
			initTree("tree", treeData);
			initTreeInput("tree");
			initFileInput("datapackage","datapackageurl","data-package-upload-btn","smartad/datapackage");
            // Stepy callbacks
            $(".stepy-callbacks").stepy({
                titleClick: true,
                nextLabel:"下一步<i class=\"icon-arrow-right14 position-right\"></i>",
                backLabel:"<i class=\"icon-arrow-left13 position-left\"></i>上一步",
                validate:true,
                legend:false,
                block: true,
                transition:'fade',
                duration:150,
                next: function(index) {
                    alert('Going to step: ' + index);
                    if (!$(".stepy-callbacks").validate(validate)) {
                        return false
                    }
                },
                back: function(index) {
                    alert('Returning to step: ' + index);
                },
                finish: function() {
                    alert('Submit canceled.');
                    return false;
                }
            });


            // Initialize validation
            var validate = {
                ignore: 'input[type=hidden], .select2-input',
                errorClass: 'validation-error-label',
                successClass: 'validation-valid-label',
                highlight: function(element, errorClass) {
                    $(element).removeClass(errorClass);
                },
                unhighlight: function(element, errorClass) {
                    $(element).removeClass(errorClass);
                },
                errorPlacement: function(error, element) {
                    if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container') ) {
                        if(element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
                            error.appendTo( element.parent().parent().parent().parent() );
                        }
                        else {
                            error.appendTo( element.parent().parent().parent().parent().parent() );
                        }
                    }
                    else if (element.parents('div').hasClass('checkbox') || element.parents('div').hasClass('radio')) {
                        error.appendTo( element.parent().parent().parent() );
                    }
                    else if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
                        error.appendTo( element.parent().parent() );
                    }
                    else if (element.parent().hasClass('uploader') || element.parents().hasClass('input-group')) {
                        error.appendTo( element.parent().parent() );
                    }
                    else {
                        error.insertAfter(element);
                    }
                },
                rules: {
                    email: {
                        email: true
                    }
                }
            }



            // Initialize plugins
            // ------------------------------

            // Apply "Back" and "Next" button styling
            $('.stepy-step').find('.button-next').addClass('btn btn-primary');
            $('.stepy-step').find('.button-back').addClass('btn btn-default');
		});
		function initTree(treeId, treeData){
			$("#search"+treeId).hide();
			var mapId = treeId;
			var setting = {
				callback: {
					onCheck: function (event,treeId, treeNode) {
						$.fn.zTree.getZTreeObj(mapId).expandNode(treeNode, true);
						initMap(mapId);
					}
				},
				check: {
					enable: true
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				view:{
					showIcon: false,
					showLine: false
				}
			};
			$.fn.zTree.init($("#"+treeId), setting, treeData);

		}
		function getValidNode(treeId,node){
			var map = dataMap[treeId];
			var parent = node.getParentNode();
			if(parent == null||parent.check_Child_State != 2){
				if(node.isParent){
					if(node.check_Child_State == 2){
						if(map.get(node.id)==null){
							map.set(node.id, node);
						}
					}
				}else{
					if(map.get(node.id)==null){
						map.set(node.id, node);
					}
				}

			}else{
				getValidNode(treeId, parent);
			}
		}
		function initMap(mapId){
			var map = dataMap[mapId];
			if(map == null){
				map = new Map();
				dataMap[mapId] = map;
			}else{
				map.clear();
			}

			var array = $.fn.zTree.getZTreeObj(mapId).getCheckedNodes();
			if(array.length > 0){
				for(var i = 0; i < array.length; i++){
					getValidNode(mapId, array[i]);
				}
			}
			$("#"+mapId+"input").tagsinput('removeAll');
			for(var [key,value] of map){
				$("#"+mapId+"input").tagsinput('add', { id: value.id, text: value.name });
			}

		}
		function initTreeInput(treeId){
			$("#"+treeId+"input").tagsinput({
				itemValue: 'id',
				itemText: 'text'
			});
			$(".bootstrap-tagsinput input").attr("readonly","readonly");
			$("#"+treeId+"input").on('itemRemoved', function(event) {
				var id = event.item.id;
				var tree = $.fn.zTree.getZTreeObj(treeId);
				var node = tree.getNodesByParam("id", id)[0];
				tree.checkNode(node,false, true, false);
				var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
				if(searchtree != null){
					var searchnode = searchtree.getNodesByParam("id", id)[0];
					if(searchnode != null){
						searchtree.checkNode(searchnode,false, true, false);
					}
				}

				var map = dataMap[treeId];
				map.delete(id);
			});
			$("#"+treeId+"input"+"_removeall").click(function(){
				$("#"+treeId+"input").tagsinput('removeAll');
				var tree = $.fn.zTree.getZTreeObj(treeId);
				tree.checkAllNodes(false);
				var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
				if(searchtree != null){
					searchtree.checkAllNodes(false);
				}
				var map = dataMap[treeId];
				if(map != null){
					map.clear();
				}
			});
		}
		function search(treeId){
			var tree = $.fn.zTree.getZTreeObj(treeId);
			var searchtree = $.fn.zTree.getZTreeObj("search"+treeId);
			var keyword = $("#search").val();
			var setting = {
				callback: {
					onCheck: function (event,treeId, treeNode) {
						var node = tree.getNodesByParam("id",treeNode.id)[0];
						tree.checkNode(node,treeNode.checked, true, true);
					}
				},
				check: {
					enable: true
				},
				data: {
					simpleData: {
						enable: true
					},
					key:{
						name:"fullname"
					}
				},
				view:{
					showIcon: false,
					showLine: false

				}
			};

			if(keyword.length > 0){
				searchKeyWord = keyword;
				$("#"+treeId).hide();
				$("#search"+treeId).show();
				var showNodes = tree.getNodesByFilter(filter);

				if(showNodes != null && showNodes.length > 0) {
					if(searchtree != null){
						searchtree.destroy();
					}
					var nodes = [];
					for(var i = 0;i< showNodes.length;i++){
						if(showNodes[i].name != showNodes[i].pName){
							showNodes[i].fullname = showNodes[i].pName+"-"+showNodes[i].name;
						}else{
							showNodes[i].fullname = showNodes[i].name;
						}
					}
					$.fn.zTree.init($("#searchtree"), setting, showNodes);
				}else{
					$("#search"+treeId).hide();
				}
			}else{
				$("#"+treeId).show();
				$("#search"+treeId).hide();
			}
		}
		function filter(node) {
			return (node.name.indexOf(searchKeyWord)>-1||node.pName.indexOf(searchKeyWord)>-1);
		}
		function initFileInput(fileId,fileInputId,uploadbtn,filepath){
			$("#"+fileId).fileinput({
                language : 'zh',
                maxFileSize:120,
                showCaption:false,
                showCancel:false,
                showPreview:true,
                maxImageWidth:960,
                maxImageHeight:344,
                minImageWidth:960,
                minImageHeight:344,
                msgImageWidthSmall:"图片宽必须为960",
                msgImageWidthLarge:"图片宽必须为960",
                msgImageHeightLarge:"图片高必须为344",
                msgImageHeightSmall:"图片高必须为344",
                browseLabel: '选择文件',
				showCaption:false,
                removeFromPreviewOnError:true,
                autoReplace:true,
                fileActionSettings:{showZoom:false,indicatorNew:""},
                showUploadedThumbs:false,
                frameClass:"",
                dropZoneEnabled:false,
				browseClass: 'btn btn-primary',
				removeLabel: '清空',
				browseIcon: '<i class="icon-plus22 position-left"></i> ',
				uploadIcon: '<i class="icon-file-upload position-left"></i> ',
				uploadLabel: '上传',
				uploadClass:'btn btn-default kv-fileinput-upload '+uploadbtn,
				removeClass: 'btn btn-danger btn-icon',
				removeIcon: '<i class="icon-cancel-square"></i> ',
				maxFilesNum: 1,
				allowedFileExtensions: ["zip","jpg","png"]

			});
			$("."+uploadbtn).on("click",function(){
				var files = $("#"+fileId)[0].files;
				if(files.length > 0){
					$("#"+fileInputId).siblings(".progress").show();
					updateLoad(files[0],fileInputId,filepath);
				}
			});
			$("#"+fileId).on("filecleared",function(event, data, msg){
				$("#"+fileInputId).val("");
				$("#"+fileInputId).siblings(".progress").children(".progress-bar").css("width","0").children("span").text("0% completed");
				$("#"+fileInputId).siblings(".progress").hide();
			});

		}
		function updateLoad(file,fileInput,filepath){
			var key = filepath+"/"+file.name;
			$.ajax({url: "${source}/admin/user/getQiNiuToken", success: function(res){
					var token = res.token;
					var domain = res.domain;
					var config = {
						useCdnDomain: true,
						disableStatisticsReport: false,
						retryCount: 6,
						region: qiniu.region.z0
					};
					var putExtra = {
						fname: "",
						params: {},
						mimeType: null
					};
					// 设置next,error,complete对应的操作，分别处理相应的进度信息，错误信息，以及完成后的操作
					var error = function(err) {
						alert("上传出错")
					};
					var complete = function(res) {
						$("#"+fileInput).val(domain+"/"+res.key);
					};
					var next = function(response) {
						var total = response.total;
						var percent = Math.round(total.percent);
						$("#"+fileInput).siblings(".progress").children(".progress-bar").css("width",percent+"%").children("span").text(percent+"% completed");
					};
					var subObject = {
						next: next,
						error: error,
						complete: complete
					};
					// 调用sdk上传接口获得相应的observable，控制上传和暂停
					var observable = qiniu.upload(file, key, token, putExtra, config);
					var subscription = observable.subscribe(subObject);

			}})
		}
		function changeDateType(){
            var dateType = $("input[name='dateType']:checked").val();
            if("custom_time" == dateType){
                $("#datetime").show();
            }else{
                $("#datetime").hide();
            }
        }
        function switchChange(obj,toggleId){
		    if($(obj).is(':checked')){
		        $("#"+toggleId).hide();
            }else{
                $("#"+toggleId).show();
            }
        }
	</script>

</head>

<body>
	<!-- Page container -->
	<div class="page-container">
		<!-- Page content -->
		<div class="page-content">
			<!-- Main content -->
			<div class="content-wrapper">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-flat">
							<div class="panel-heading">
								<h6 class="panel-title">广告20190430</h6>
								<div class="heading-elements">
									<ul class="icons-list">
										<li><a data-action="collapse"></a></li>
										<li><a data-action="close"></a></li>
									</ul>
								</div>
							</div>
							<div class="panel-body">
								<div class="col-md-8">
                                    <fieldset>
                                        <legend class="text-bold">投放时间</legend>
                                        <div class="form-group col-lg-12 bottom-border">
                                            <label class="control-label col-lg-2">类型</label>
                                            <div class="col-lg-10">
                                                <div class="row">
                                                    <div class="col-md-12">

                                                        <div class="col-md-2 form-group has-feedback has-feedback-left">
                                                            <label class="radio-inline">
                                                                <input type="radio" name="dateType" checked value="all_day" class="control-success" onchange="changeDateType()" >
                                                                全天投放
                                                            </label>
                                                        </div>
                                                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                            <label class="radio-inline">
                                                                <input type="radio" name="dateType" value="custom_time" class="control-success" onchange="changeDateType()">
                                                                自定义时段
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group col-lg-12 bottom-border">
                                            <label class="control-label col-lg-2">投放日期</label>
                                            <div class="col-lg-10" style="margin-bottom:15px">
                                                <div class="row form-inline">
                                                    <div class="col-md-12 form-group has-feedback has-feedback-left">
                                                        <div class="col-md-10 input-group">
                                                            <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                                            <input type="text" class="form-control col-md-8 daterange-basic">
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group  col-lg-12" id="datetime" style="display: none;">
                                            <label class="col-lg-2 control-label">每天投放时段</label>
                                            <div class="col-lg-10">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                            <div class="input-group">
                                                                <span class="input-group-addon"><i class="icon-alarm"></i></span>
                                                                <select class="select col-xs-6 no-padding">
                                                                    <option value="00:00">00:00</option>
                                                                    <option value="01:00">01:00</option>
                                                                    <option value="02:00">02:00</option>
                                                                    <option value="03:00">03:00</option>
                                                                    <option value="04:00">04:00</option>
                                                                    <option value="05:00">05:00</option>
                                                                    <option value="06:00">06:00</option>
                                                                    <option value="07:00">07:00</option>
                                                                    <option value="08:00">08:00</option>
                                                                    <option value="09:00">09:00</option>
                                                                    <option value="10:00">10:00</option>
                                                                    <option value="11:00">11:00</option>
                                                                    <option value="12:00">12:00</option>
                                                                    <option value="13:00">13:00</option>
                                                                    <option value="14:00">14:00</option>
                                                                    <option value="15:00">15:00</option>
                                                                    <option value="16:00">16:00</option>
                                                                    <option value="17:00">17:00</option>
                                                                    <option value="18:00">18:00</option>
                                                                    <option value="19:00">19:00</option>
                                                                    <option value="20:00">20:00</option>
                                                                    <option value="21:00">21:00</option>
                                                                    <option value="22:00">22:00</option>
                                                                    <option value="23:00">23:00</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <span class="col-md-1" style="width: 20px">-</span>
                                                        <div class="col-md-4 form-group has-feedback has-feedback-left">
                                                            <div class="input-group">
                                                                <span class="input-group-addon"><i class="icon-alarm"></i></span>
                                                                <select class="select col-xs-6 no-padding">
                                                                    <option value="00:00">00:00</option>
                                                                    <option value="01:00">01:00</option>
                                                                    <option value="02:00">02:00</option>
                                                                    <option value="03:00">03:00</option>
                                                                    <option value="04:00">04:00</option>
                                                                    <option value="05:00">05:00</option>
                                                                    <option value="06:00">06:00</option>
                                                                    <option value="07:00">07:00</option>
                                                                    <option value="08:00">08:00</option>
                                                                    <option value="09:00">09:00</option>
                                                                    <option value="10:00">10:00</option>
                                                                    <option value="11:00">11:00</option>
                                                                    <option value="12:00">12:00</option>
                                                                    <option value="13:00">13:00</option>
                                                                    <option value="14:00">14:00</option>
                                                                    <option value="15:00">15:00</option>
                                                                    <option value="16:00">16:00</option>
                                                                    <option value="17:00">17:00</option>
                                                                    <option value="18:00">18:00</option>
                                                                    <option value="19:00">19:00</option>
                                                                    <option value="20:00">20:00</option>
                                                                    <option value="21:00">21:00</option>
                                                                    <option value="22:00">22:00</option>
                                                                    <option value="23:00">23:00</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
									<fieldset>
										<legend class="text-bold">定向条件</legend>
										<div class="form-group col-lg-12 bottom-border">
											<label class="control-label col-lg-2">地域</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="form-group has-feedback has-feedback-left">
															<input type="text" class="form-control col-md-10" id="treeinput" placeholder="地域列表">
															<a href="javascript:void(0)" id="treeinput_removeall">清空</a>
														</div>
														<div class="form-group has-feedback has-feedback-left">
															<input type="text" class="form-control input-lg" id="search" onKeyUp="search('tree');" placeholder="搜索国家、省、市、区、商圈">
															<div class="form-control-feedback">
																<i class="icon-search4 text-size-base"></i>
															</div>
															<div id="searchtree" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
															<div id="tree" class="ztree" style="margin-bottom: 20px;margin-top:20px;"></div>
														</div>
													</div>
												</div>

											</div>

										</div>
										<div class="form-group col-lg-12 bottom-border">
											<label class="control-label col-lg-2">年龄</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<select class="select">
																<option>14</option>
															</select>
														</div>
														<span class="col-md-1" style="width: 20px">-</span>
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<select class="select">
																<option>65</option>
															</select>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group col-lg-12 bottom-border">
											<label class="control-label col-lg-2">性别</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="control-success" checked="checked">
																全部
															</label>
														</div>
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="control-success" >
																男
															</label>
														</div>
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="control-success">
																女
															</label>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group col-lg-12 bottom-border">
											<label class="control-label col-lg-2">兴趣/行为</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="styled" checked="checked">
																全部
															</label>
														</div>
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="styled" >
																男
															</label>
														</div>
														<div class="col-md-2 form-group has-feedback has-feedback-left">
															<label class="radio-inline">
																<input type="radio" name="gender" class="styled">
																女
															</label>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group col-lg-12 bottom-border">
											<label class="control-label col-lg-2">公众号媒体类型</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
															<label>
																<input type="checkbox"  class="switchery" checked="checked" onclick="switchChange(this)">
																全部
															</label>
														</div>
													</div>
												</div>

											</div>
										</div>
										<div class="form-group  col-lg-12">
											<label class="col-lg-2 control-label">数据包</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-md-12">
														<div class="form-group has-feedback has-feedback-left">
															<input type="text" class="form-control col-md-10" id="datapackageurl" name="datapackageurl" placeholder="数据包链接" style="margin-bottom: 10px">
															<input type="file" class="file-input-extensions" id="datapackage">
															<div class="progress col-md-12" style="padding-left: 0px;padding-right: 0px;display: none;">
																<div class="progress-bar bg-teal" style="width: 0;">
																	<span>0% Complete</span>
																</div>
															</div>
														</div>

													</div>

												</div>
											</div>
										</div>
									</fieldset>
									<div  class="panel panel-flat panel-collapsed"  style="margin-top:-20px;-webkit-box-shadow:0 0px 0px rgba(0,0,0,0);box-shadow:0 1px 1px rgba(0,0,0,0);border-color:#fff;">
										<div class="panel-heading">
											<h6 class="panel-title">更多定向信息</h6>
											<div class="heading-elements">
												<ul class="icons-list">
													<li><a data-action="collapse" class="rotate-180"></a></li>
												</ul>
											</div>
										</div>
										<div class="panel-body" style="padding: 0px;">
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">学历</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																	<label>
																		<input type="checkbox" class="switchery" checked="checked" onclick="switchChange(this,'xueli')">
																		全部
																	</label>
															</div>

														</div>
													</div>
													<div class="row" id="xueli">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	博士
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	硕士
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	本科
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	专科
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	高中
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	初中
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	小学
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">婚恋状态</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	单身
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	新婚
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	已婚
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	育儿
																</label>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">操作系统</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	ios系统
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	安卓系统
																</label>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">手机价格</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-6 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	1500元以下
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	1500~2500元
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	2500~3500元
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	3500元~4500元
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	4500元以上
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">手机品牌</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">运营商</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	电信
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	移动
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	联通
																</label>
															</div>


														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">联网方式</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	wifi
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	4G
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	3G
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	2G
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">付费用户
												</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	游戏付费
																</label>
															</div>
															<div class="col-md-2 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	电商付费
																</label>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">再营销
												</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	已关注你的公众号
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	曾领取你的微信卡券
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	曾对你的微信广告感兴趣
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12 bottom-border">
												<label class="control-label col-lg-2">排除营销
												</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	已关注你的公众号
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	曾领取你的微信卡券
																</label>
															</div>
															<div class="col-md-4 form-group has-feedback has-feedback-left">
																<label>
																	<input type="checkbox" class="control-success" checked="checked">
																	曾对你的微信广告感兴趣
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>
											<div class="form-group col-lg-12">
												<label class="control-label col-lg-2">自定义人群</label>
												<div class="col-lg-10">
													<div class="row">
														<div class="col-md-12">
															<div class="col-md-4 form-group has-feedback has-feedback-left switchery-xs">
																<label>
																	<input type="checkbox" class="switchery" checked="checked">
																	全部
																</label>
															</div>

														</div>
													</div>
												</div>
											</div>


										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="panel panel-flat">
										<div class="panel-heading">
											<h6 class="panel-title"><i class="icon-files-empty position-left"></i>覆盖人群预估</h6>
											<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
										</div>
										<table class="table table-borderless table-xs content-group-sm">
											<tbody>
											<tr>
												<td><i class="icon-file-check position-left"></i> 预估最大覆盖用户数:</td>
												<td class="text-right"><span class="pull-right"><a href="#">11111111</a></span></td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 预估日覆盖用户数最大值:</td>
												<td class="text-right">12 May, 2015</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 预估日曝光量最大值:</td>
												<td class="text-right">25 Feb, 2015</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 出价下限:</td>
												<td class="text-right">29</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 出价上限:</td>
												<td class="text-right"><a href="#">Winnie</a></td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 建议出价:</td>
												<td class="text-right">Published</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 预估日覆盖用户数:</td>
												<td class="text-right">Published</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 预估日曝光量:</td>
												<td class="text-right">Published</td>
											</tr>
											<tr>
												<td><i class="icon-file-check position-left"></i> 调整的定向:</td>
												<td class="text-right">Published</td>
											</tr>
											<!--
											<tr>
												<td><i class="icon-file-check position-left"></i> oCPA 广告建议:</td>
												<td class="text-right">Published</td>
											</tr>
											-->
											</tbody>
										</table>

									</div>
								</div>

							</div>
						</div>

					</div>
					<div class="col-md-12">
						<div class="panel panel-flat">
							<div class="panel-heading">
								<h6 class="panel-title">Regular <small>Small tag</small></h6>
								<div class="heading-elements">
									<ul class="icons-list">
										<li><a data-action="collapse"></a></li>
										<li><a data-action="close"></a></li>
									</ul>
								</div>
							</div>

							<div class="panel-body">
								Regular text with subtitle
							</div>
						</div>
					</div>
                    <div class="col-md-12">
                        <div class="panel panel-white">
                            <div class="panel-heading">
                                <h6 class="panel-title">新建投放</h6>
                                <div class="heading-elements">
                                    <ul class="icons-list">
                                        <li><a data-action="collapse"></a></li>
                                        <li><a data-action="close"></a></li>
                                    </ul>
                                </div>
                            </div>

                            <form class="stepy-callbacks" action="#">
                                <fieldset title="1">
                                    <legend class="text-semibold">推广计划</legend>

                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label>推广计划类型</label>
                                                <select name="location" data-placeholder="Select position" class="select">
                                                    <option value="CAMPAIGN_TYPE_NORMAL">微信公众号广告</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label>推广目标：</label>
                                                <select name="position" data-placeholder="Select position" class="select">
                                                    <option value="PROMOTED_OBJECT_TYPE_LINK_WECHAT">网页(微信)</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>计划日限额:</label>
                                                <input type="text" name="name" class="form-control">
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Email address:</label>
                                                <input type="email" name="email" class="form-control" placeholder="your@email.com">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Phone #:</label>
                                                <input type="text" name="tel" class="form-control" placeholder="+99-99-9999-9999" data-mask="+99-99-9999-9999">
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <label>Date of birth:</label>
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <div class="form-group">
                                                        <select name="birth-month" data-placeholder="Month" class="select">
                                                            <option></option>
                                                            <option value="1">January</option>
                                                            <option value="2">February</option>
                                                            <option value="3">March</option>
                                                            <option value="4">April</option>
                                                            <option value="5">May</option>
                                                            <option value="6">June</option>
                                                            <option value="7">July</option>
                                                            <option value="8">August</option>
                                                            <option value="9">September</option>
                                                            <option value="10">October</option>
                                                            <option value="11">November</option>
                                                            <option value="12">December</option>
                                                        </select>
                                                    </div>
                                                </div>

                                                <div class="col-md-4">
                                                    <div class="form-group">
                                                        <select name="birth-day" data-placeholder="Day" class="select">
                                                            <option></option>
                                                            <option value="1">1</option>
                                                            <option value="2">2</option>
                                                            <option value="3">3</option>
                                                            <option value="4">4</option>
                                                            <option value="5">5</option>
                                                            <option value="6">6</option>
                                                            <option value="7">7</option>
                                                            <option value="8">8</option>
                                                            <option value="9">9</option>
                                                            <option value="...">...</option>
                                                            <option value="31">31</option>
                                                        </select>
                                                    </div>
                                                </div>

                                                <div class="col-md-4">
                                                    <div class="form-group">
                                                        <select name="birth-year" data-placeholder="Year" class="select">
                                                            <option></option>
                                                            <option value="1">1980</option>
                                                            <option value="2">1981</option>
                                                            <option value="3">1982</option>
                                                            <option value="4">1983</option>
                                                            <option value="5">1984</option>
                                                            <option value="6">1985</option>
                                                            <option value="7">1986</option>
                                                            <option value="8">1987</option>
                                                            <option value="9">1988</option>
                                                            <option value="10">1989</option>
                                                            <option value="11">1990</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset title="2">
                                    <legend class="text-semibold">定向条件</legend>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>University:</label>
                                                <input type="text" name="university" placeholder="University name" class="form-control">
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Country:</label>
                                                <select name="university-country" data-placeholder="Choose a Country..." class="select">
                                                    <option></option>
                                                    <option value="1">United States</option>
                                                    <option value="2">France</option>
                                                    <option value="3">Germany</option>
                                                    <option value="4">Spain</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Degree level:</label>
                                                <input type="text" name="degree-level" placeholder="Bachelor, Master etc." class="form-control">
                                            </div>

                                            <div class="form-group">
                                                <label>Specialization:</label>
                                                <input type="text" name="specialization" placeholder="Design, Development etc." class="form-control">
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>From:</label>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="education-from-month" data-placeholder="Month" class="select">
                                                                    <option></option>
                                                                    <option value="January">January</option>
                                                                    <option value="...">...</option>
                                                                    <option value="December">December</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="education-from-year" data-placeholder="Year" class="select">
                                                                    <option></option>
                                                                    <option value="1995">1995</option>
                                                                    <option value="...">...</option>
                                                                    <option value="1980">1980</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="col-md-6">
                                                    <label>To:</label>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="education-to-month" data-placeholder="Month" class="select">
                                                                    <option></option>
                                                                    <option value="January">January</option>
                                                                    <option value="...">...</option>
                                                                    <option value="December">December</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="education-to-year" data-placeholder="Year" class="select">
                                                                    <option></option>
                                                                    <option value="1995">1995</option>
                                                                    <option value="...">...</option>
                                                                    <option value="1980">1980</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label>Language of education:</label>
                                                <input type="text" name="education-language" placeholder="English, German etc." class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset title="3">
                                    <legend class="text-semibold">广告创意</legend>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Company:</label>
                                                <input type="text" name="experience-company" placeholder="Company name" class="form-control">
                                            </div>

                                            <div class="form-group">
                                                <label>Position:</label>
                                                <input type="text" name="experience-position" placeholder="Company name" class="form-control">
                                            </div>

                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>From:</label>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="experience-from-month" data-placeholder="Month" class="select">
                                                                    <option></option>
                                                                    <option value="January">January</option>
                                                                    <option value="...">...</option>
                                                                    <option value="December">December</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="experience-from-year" data-placeholder="Year" class="select">
                                                                    <option></option>
                                                                    <option value="1995">1995</option>
                                                                    <option value="...">...</option>
                                                                    <option value="1980">1980</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="col-md-6">
                                                    <label>To:</label>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="experience-to-month" data-placeholder="Month" class="select">
                                                                    <option></option>
                                                                    <option value="January">January</option>
                                                                    <option value="...">...</option>
                                                                    <option value="December">December</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <div class="form-group">
                                                                <select name="experience-to-year" data-placeholder="Year" class="select">
                                                                    <option></option>
                                                                    <option value="1995">1995</option>
                                                                    <option value="...">...</option>
                                                                    <option value="1980">1980</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Brief description:</label>
                                                <textarea name="experience-description" rows="4" cols="4" placeholder="Tasks and responsibilities" class="form-control"></textarea>
                                            </div>

                                            <div class="form-group">
                                                <label>Recommendations:</label>
                                                <input name="recommendations" type="file" class="file-styled">
                                                <span class="help-block">Accepted formats: pdf, doc. Max file size 2Mb</span>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <fieldset title="4">
                                    <legend class="text-semibold">广告</legend>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Applicant resume:</label>
                                                <input type="file" name="resume" class="file-styled">
                                                <span class="help-block">Accepted formats: pdf, doc. Max file size 2Mb</span>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Where did you find us?</label>
                                                <select name="source" data-placeholder="Choose an option..." class="select-simple">
                                                    <option></option>
                                                    <option value="monster">Monster.com</option>
                                                    <option value="linkedin">LinkedIn</option>
                                                    <option value="google">Google</option>
                                                    <option value="adwords">Google AdWords</option>
                                                    <option value="other">Other source</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Availability:</label>
                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="availability" class="styled">
                                                        Immediately
                                                    </label>
                                                </div>

                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="availability" class="styled">
                                                        1 - 2 weeks
                                                    </label>
                                                </div>

                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="availability" class="styled">
                                                        3 - 4 weeks
                                                    </label>
                                                </div>

                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="availability" class="styled">
                                                        More than 1 month
                                                    </label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Additional information:</label>
                                                <textarea name="additional-info" rows="5" cols="5" placeholder="If you want to add any info, do it here." class="form-control"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <button type="submit" class="btn btn-primary stepy-finish">Submit <i class="icon-check position-right"></i></button>
                            </form>
                        </div>
                    </div>
				</div>
				<!-- /panel titles -->
			</div>
			<!-- /main content -->

		</div>
		<!-- /page content -->
	</div>
	<!-- /page container -->
</body>

</html>
