/**
 * 基础js文件
 * Created by yanghui on 15/4/13.
 */
//上下文
var context = {};
var configData = {};
if(!isMobile()){
    $(".is_moblie_l").addClass('no_padding_left')
    $(".is_moblie_r").addClass('no_padding_right')
}
function initConfigData(data) {
    if (configData) {
        configData = data;
    }
}

$.fn.formatConfigData = function (options) {
    options = options ? options : {};
    $(this).each(function () {
        var dataSource = configData[$(this).attr("data-source")];
        if (dataSource) {
            if (this.tagName == "SELECT") {
                for (var i in dataSource) {
                    $(this).append("<option value=\"" + i + "\">" + dataSource[i] + "</option>");
                }
                var defaultValue = $(this).attr("data-defaultValue");
                if (defaultValue) {
                    $(this).val(defaultValue);
                }
                $(this).selectpicker('refresh');
            } else {
                var value = $(this).attr("data-value");
                var _transVal = dataSource[value];
                var _text = typeof _transVal == 'string' ? _transVal : _transVal.text;
                var _color = typeof _transVal == 'string' ? null : _transVal.color;
                if (options.format) {
                    $(this).append(options.format(value, _text, $(this)));
                } else {
                    $(this).text(_text);
                    _color && $(this).css('color', _color);
                }

            }
        }
    });
};
function setContext(contextPath) {
    context.contextPath = contextPath;
    context.getURL = function (url) {
        if (url != undefined && url.indexOf(this.contextPath) < 0) {
            return context.contextPath + url;
        } else {
            return url;
        }
    }
}
/**
 * 页面数据加载
 * @param url
 */
function loadData(url, data, callback) {
    if (verifyLogin()) {
        if (url.indexOf('onclick:') == 0) {
            var _func = url.substring(8);
            eval(_func);
        } else {
            $("#content").load(url, data, function () {
                if (callback) {
                    callback();
                }
                //重新加载菜单,解禁所有button,避免出现个别按钮不可点击(swal控件无法解禁)
                $("button").button("reset");
                var h1 = $('.sidebar-content').height();
                var h2 = $('.content-wrapper').height();
                if(h1 < h2){
                    $('.sidebar-content').attr('style',"height:"+h2+"px");
                }
            });
        }
    }
}
function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName)
            return unescape(temp[1]);
    }
}
/**
 * 验证是否登录，Ajax请求之前调用
 */
function verifyLogin() {
    var isLogin = true;
    $.ajax({
        url: context.getURL("verifyLogin"),
        async: false,
        timeout: 5000,
        success: function (data) {
            if (data.status != 1) {
                isLogin = false;
            }
        }, error: function (data) {
            return !timeOut(data.status);
        }
    });
    if (!isLogin) {
        window.location.href = context.getURL("");
    }
    return isLogin;
}

function timeOut(status) {
    if (status == 0) {
        $.alertWarning("网络请求超时,请检查网络");
        return false;
    }
    return true;
}
var opts = {
    title: "Over here",
    text: "Check me out. I'm in a different stack.",
    width: "100%",
    cornerclass: "no-border-radius",
    addclass: "stack-custom-bottom bg-primary",
    stack: {"dir1": "up", "dir2": "right", "spacing1": 1},
    delay:2000
};
/**
 * 错误警告
 * @param message 错误消息
 */
$.alertWarning = function (message) {
    opts.title = "操作错误";
    opts.text = (!message || message.constructor != String) ? "操作出现异常了哦,请联系管理员" : message;
    opts.addclass = "stack-custom-bottom bg-danger";
    opts.type = "error";
    new PNotify(opts);
};
/**
 * 成功警告
 * @param message 提示消息
 */
$.alertSuccess = function (message) {
    opts.title = "操作成功";
    opts.text = (!message || message.constructor != String) ? "你已经成功的完成了任务" : message;
    opts.addclass = "stack-custom-bottom bg-success";
    opts.type = "success";
    new PNotify(opts);
};
/**
 * form提交（进行二次封装）
 * @param parameter
 */
$.fn.formSubmit = function (parameter) {
    if (!verifyLogin()) {
        return;
    }
    $("button").button("loading");
    parameter = parameter || {};
    parameter.type = $(this).attr("method");
    parameter.dataType = $(this).attr("dataType");
    if (parameter.dataType == undefined) {
        parameter.dataType = "json";
    }
    if (parameter.type == undefined) {
        parameter.type = "post";
    }
    parameter.url = parameter.url ? context.getURL(parameter.url) : context.getURL($(this).attr("action"));
    //回调
    parameter.successCallback = parameter.success;
    parameter.errorCallback = parameter.error;
    parameter.error = function (data) {
        $("button").button("reset");
        if (data.responseText) {
            try {
                if (parameter.errorCallback != undefined) {
                    parameter.errorCallback(data);
                }
                var resultData = $.parseJSON(data.responseText);
                $.alertWarning(resultData.msg);
            } catch (e) {
                $.alertWarning("亲亲，休息一下重试或者刷新一下浏览器！");
            }

        } else {
            $.alertWarning();
        }
    };

    parameter.success = function (data) {
        //$("button").button("reset");
        if (parameter.dataType == "json") {
            
            if (data.status == 1) {
                $.alertSuccess();
                if (parameter.successCallback != undefined) {
                    parameter.successCallback(data);
                }
            } else {
                $.alertWarning(data.msg);
                if (parameter.errorCallback != undefined) {
                    parameter.errorCallback(data);
                }
            }
        } else {
            $("#content").html(data);
        }

    };
    $(this).ajaxSubmit(parameter)
};
/**
 * 加载数据，依赖当前对象进行信息提示
 * @param parameter aja提交参数，基础jquery
 * */
$.loadAjax = function (parameter) {
    if (!verifyLogin()) {
        return;
    }
    if (typeof(parameter) == "object" && Object.prototype.toString.call(parameter).toLowerCase() == "[object object]" && !parameter.length) {
        parameter.dataType = "json";
        if (parameter.type == undefined) {
            parameter.type = "post";
        }
        parameter.url = context.getURL(parameter.url);
        parameter.successBind = parameter.success;
        parameter.errorBind = parameter.error;
        parameter.error = function (data) {
            var resultData = {status: 0, msg: "亲亲，休息一下重试或者刷新一下浏览器！"};
            try {
                //resultData = $.parseJSON(data.responseText);
                resultData = data;
            } catch (e) {
            }
            if (parameter.errorBind) {
                parameter.errorBind(resultData);
            } else {
                if (resultData) {
                    $.alertWarning(resultData.msg);
                } else {
                    $.alertWarning();
                }
            }
        };
        parameter.success = function (data) {
            if (typeof(data) == "boolean") {
                if (!data) {
                    $.alertWarning("操作失败,请稍侯再试!");
                } else {
                    if (parameter.successBind) {
                        parameter.successBind(data);
                    }
                }
            } else {
                if (data.status == 1) {
                    if (parameter.successBind) {
                        parameter.successBind(data);
                    }
                } else {
                    if (parameter.errorBind) {
                        parameter.errorBind(data);
                    } else {
                        $.alertWarning(data.msg);
                    }
                }
            }
        };
        $.ajax(parameter);
    }
};

$.fn.query = function (currentPage) {
    $(".currentPage").val(currentPage ? currentPage : 1);
    $(this).setQueryParam();
    var action = $(this).attr("action");
    if (action == "" || undefined == action) {
        $(this).attr("action", getMenuURL());
    }
    $(this).formSubmit();
};

$.fn.refresh = function () {
    $(this).formSubmit({url: getMenuURL()});
};

$.fn.paginator = function (currentPage, totalPage, form) {
    var pageInput = $("<input  name=\"pageNumber\" class='currentPage'  type=\"hidden\">");
    pageInput.val(currentPage ? currentPage : 1);
    $(form).prepend(pageInput);
    var pagination = $("<div class='text-center' style='margin-top:10px'><ul class='pagination-flat pagination-sm twbs-flat'></ul></div>");
    $(this).after(pagination);
    var c = currentPage;
    var tp = totalPage;
    $('.twbs-flat').twbsPagination({
        totalPages: tp,
        visiblePages: tp>10?10:tp,
        currentPage:c,
        startPage:c,
        first: "首页",
        last: "尾页",
        prev: '<',
        next: '>',
        initiateStartPageClick:false,
        initiateStartPageClick:false,
        onPageClick: function (event, page) {
            $('.twbs-content-flat').text('Page ' + page);
            $(form).query(page);
        }
    });
};


function isPhoneNumber(tel) {
    var reg =/^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
    return reg.test(tel);
}

function isMobile() {
    var sUserAgent = navigator.userAgent.toLowerCase();
    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    var bIsAndroid = sUserAgent.match(/android/i) == "android";
    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
    if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
        return true
    }else {
        return false
    }
}
function initFileInput(fileId,fileInputId,uploadbtn,filepath){
    $("#"+fileId).fileinput({
        browseLabel: '选择文件',
        showCaption:false,
        browseClass: 'btn btn-primary',
        removeLabel: '清空',
        browseIcon: '<i class="icon-plus22 position-left"></i> ',
        uploadIcon: '<i class="icon-file-upload position-left"></i> ',
        uploadLabel: '上传',
        uploadClass:'btn btn-default kv-fileinput-upload '+uploadbtn,
        removeClass: 'btn btn-danger btn-icon',
        removeIcon: '<i class="icon-cancel-square"></i> ',
        maxFilesNum: 1,
        allowedFileExtensions: ["zip"]
    });
    $("."+uploadbtn).on("click",function(event){
        event.preventDefault();
        var files = $("#"+fileId)[0].files;
        if(files.length > 0){
            $("#"+fileInputId).siblings(".progress").show();
            updateLoad(files[0],fileInputId,filepath);
        }
        return false;
    });
    $("#"+fileId).on("filecleared",function(event, data, msg){
        $("#"+fileInputId).val("");
        $("#"+fileInputId).siblings(".progress").children(".progress-bar").css("width","0").children("span").text("0% completed");
        $("#"+fileInputId).siblings(".progress").hide();
    });

}
function updateLoad(file,fileInput,filepath){
    var key = filepath+"/"+file.name;
    $.ajax({url: "/smartad-web/admin/user/getQiNiuToken", success: function(res){
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


/**
 * 乘法函数，用来得到精确的乘法结果
 * 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function accMul(arg1, arg2) {
    var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
    try { m += s1.split(".")[1].length } catch (e) { }
    try { m += s2.split(".")[1].length } catch (e) { }
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

/**
 * 除法函数，用来得到精确的除法结果
 * 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function accDiv(arg1, arg2) {
    var t1 = 0, t2 = 0, r1, r2;
    try { t1 = arg1.toString().split(".")[1].length } catch (e) { }
    try { t2 = arg2.toString().split(".")[1].length } catch (e) { }
    with (Math) {
        r1 = Number(arg1.toString().replace(".", ""));
        r2 = Number(arg2.toString().replace(".", ""));
        return (r1 / r2) * pow(10, t2 - t1);
    }
}


function changeTab(pageTitle, pageMenu, menuURL){
    loadMenu(pageTitle, pageMenu, menuURL, true);
}

function fillFormData($formEL, obj) {
    $.each(obj, function(index, item) {
        $formEL.find("[name=" + index + "]").val(item);
    });
}
function dateMinus(date1,date2){//date1:小日期   date2:大日期
    var sdate = new Date(date1);
    var now = new Date(date2);
    var days = now.getTime() - sdate.getTime();
    var day = parseInt(days / (1000 * 60 * 60 * 24));
    return day;
}