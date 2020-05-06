/**
 * Created by yanghui on 15/11/3.
 */
var btnDictionary = {query: "query", add: "add", edit: "edit", del: "del"};
/**
 * {
 * add {false/true}
 * query {false/true}
 * buttons {
                label: " 新增",
                icon: "icon-add",
                url: "/add"}
 * conditions {
                    type: "text",
                    defaultValue: "${query.loginName}",
                    name: "loginName",
                    label: "登录名称"
                }
 * }
 * @param options
 */
$.fn.queryTable = function (options) {
    if (options) {
        options.add = options.add == undefined ? false : options.add;
        options.query = (options.query || options.query == undefined) ? true : false;
        options.buttons = options.buttons ? options.buttons : [];
        options.conditionsRowNumber = options.conditionsRowNumber ? options.conditionsRowNumber : 4;
        var conditions_count = 0;
        if (options.conditions) {
            var conditions = options.conditions;
            for (var i in  conditions) {
                if (conditions[i].name && conditions[i].label) {
                    var type = conditions[i].type ? conditions[i].type : "text";
                    var defaultValue = conditions[i].defaultValue ? conditions[i].defaultValue : "";
                    var div = $("<div class=\"form-group\"><label>" + conditions[i].label + "</label></div>");
                    switch (type) {
                        case "number":
                            div.append("<input type=\"number\"  class=\"form-control\" name=\"" + conditions[i].name + "\" id=\"" + conditions[i].name + "\" value=\"" + defaultValue + "\"/>");
                            break;
                        case "text":
                            div.append("<input type=\"text\"  class=\"form-control\" name=\"" + conditions[i].name + "\" id=\"" + conditions[i].name + "\" value=\"" + defaultValue + "\"/>");
                            break;
                        case "date":
                            var date = $("<input type=\"text\" class=\"form-control\" name=\"" + conditions[i].name + "\" id=\"" + conditions[i].name + "\"  size=\"25px\" readonly/>");
                            div.append(date);
                            var _options = $.extend({}, {
                                autoApply: true
                            }, conditions[i].options);
                            date.daterangepicker(_options);
                            date.val(defaultValue);
                            break;
                        case "select":
                            var width = conditions[i].width ? conditions[i].width : "68%";
                            var select = $("<select name=\"" + conditions[i].name + "\" id=\"" + conditions[i].name + "\" data-width=\"" + width + "\" class=\"bootstrap-select\">");
                            select.append("<option value=\"\">全部</option>");
                            var dataSource = configData[conditions[i].dataSource];
                            if (dataSource) {
                                for (var j in dataSource) {
                                    select.append("<option value=\"" + j + "\">" + dataSource[j] + "</option>");
                                }
                            }
                            select.val(conditions[i].defaultValue);
                            div.append(select);
                            select.selectpicker('refresh');
                            break;
                    }
                    $(this).append(div);
                    conditions_count++;
                }
            }
        }
        var div = $("<div class=\"form-group\"></div>");
        if (conditions_count > 0 && options.query) {
            options.buttons.push({
                type: btnDictionary.query,
                label: " 查询",
                icon: "icon-search4"
            });
        }
        if (options.add) {
            var _label = ' 新增';
            var _icon = 'icon-add';
            var _url = getMenuURL() + '/add';
            if (typeof options.add == 'object') {
                _label = options.add.label;
                _icon = options.add.icon;
                _url = options.add.url;
            }

            options.buttons.push({
                label: _label,
                icon: _icon,
                url: _url
            });
        }
        var buttons = options.buttons;
        for (var i in  buttons) {
            var button = $("<button type=\"button\" class=\"btn bg-primary-700\"><i class=\"" + buttons[i].icon + "\"></i> " + buttons[i].label + "</button>");
            button.attr("data-url", buttons[i].url);
            button.attr("data-label", buttons[i].label);
            switch (buttons[i].type) {
                case btnDictionary.query:
                    button.click(function () {
                        $("#queryForm").query();
                    });
                    break;
                default:
                    button.click(function () {
                        loadMenuChild($(this).attr("data-url"), $(this).attr("data-label"));
                    });
            }
            div.append("&nbsp;");
            div.append(button);
        }
    }

    var formDIV = $(this).append(div);
    $(this).find(".form-group").each(function (i) {
        if (i == 0 || (i % options.conditionsRowNumber == 0)) {
            formDIV.append("<div class=\"conditionsRow\"></div>");
        }
        if ($(this).find("button>i").size() > 0) {
            $(this).appendTo(formDIV.find(".conditionsRow:first"));
        } else {
            $(this).appendTo(formDIV.find(".conditionsRow:last"));
        }
    });

};

$.fn.tableBtn = function (options) {
    options.del = options.del == undefined ? false : options.del;
    options.edit = options.edit == undefined ? false : options.edit;
    options.buttons = options.buttons ? options.buttons : [];
    options.index = options.index ? options.index : 0;
    if (options.del) {
        options.buttons.push({
            type: btnDictionary.del,
            label: " 删除",
            icon: "icon-trash"
        });
    }
    if (options.edit) {
        var _url = "/edit/";
        if (typeof options.edit == 'object') {
            options.edit.url && (_url = options.edit.url);
        }
        options.buttons.push({
            label: " 编辑",
            icon: "icon-pencil7",
            url: _url
        });
    }
    $(this).find("tr").each(function (i) {
        var row = $(this);
        if (i > options.index && options.buttons.length > 0) {
            row.find("td:last").each(function () {
                var html, menu = row.find(".icons-list > .dropdown > .dropdown-menu"), key = $(this).attr("data-key"), isExist = menu.size() == 0;
                if (isExist) {
                    html = $("<ul class=\"icons-list\"><li class=\"dropdown\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"icon-menu9\"></i></a><ul class=\"dropdown-menu dropdown-menu-right\"> </ul></li></ul>");
                    menu = html.find(".dropdown-menu");
                }
                var buttons = options.buttons;
                for (var i in  buttons) {
                    var li = $("<li><a href=\"#\"><i class=\"" + buttons[i].icon + "\"></i> " + buttons[i].label + "</a></li>");
                    li.data("data-url", buttons[i].url);
                    li.data("data-native-url", buttons[i].nativeURL != undefined ? buttons[i].nativeURL : true);
                    li.data("data-label", buttons[i].label);
                    li.data("data-keyName", buttons[i].keyName || '');
                    if (buttons[i].param) {
                        for (var j in buttons[i].param) {
                            var value = $(this).attr(buttons[i].param[j]);
                            if (value) {
                                buttons[i].param[j] = $(this).attr(buttons[i].param[j]);
                            }
                        }
                        li.data("data-param", buttons[i].param);
                    }
                    switch (buttons[i].type) {
                        case btnDictionary.del:
                            li.click(function () {
                                $.deleteConfirm({
                                    url: getMenuURL() + "/delete/" + key,
                                    success: function () {
                                        row.remove();
                                    }
                                });
                            });
                            break;
                        default:
                            li.click(function () {
                                var data = {}, url;
                                if ($(this).data("data-param")) {
                                    data = $(this).data("data-param");
                                }

                                var _dataUrl = $(this).data("data-url");
                                if (!$(this).data("data-native-url") || _dataUrl.indexOf("onclick:") == 0){
                                    url =  _dataUrl;
                                } else {
                                    url =  getMenuURL() + _dataUrl;
                                }

                                var _keyName = $(this).data("data-keyName");
                                if (url.indexOf('onclick:') == 0) {
                                    eval(url.substring(8));
                                    return;
                                }
                                var _url = url + (_keyName ? ('?' + encodeURIComponent(_keyName) + '=') : '/') + encodeURIComponent(key);
                                loadMenuChild(_url, $(this).data("data-label"), data);
                            });
                    }
                    menu.prepend(li);
                }
                if (isExist) {
                    $(this).append(html);
                }
            });
        }
    });
    if (options.pagination) {
        if (options.pagination.totalPage == 0) {
            options.pagination.totalPage = 1;
        }
        $(this).paginator(options.pagination.currentPage, options.pagination.totalPage, options.pagination.form);
    }
};


$.deleteConfirm = function (options) {
    Swal.fire({
        title: "确定删除所选的记录?",
        text: "你的数据被删除后将无法恢复!",
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 删除",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            $.loadAjax({
                url: options.url,
                type: "delete",
                error: function (request) {
                    Swal.fire({
                        type: 'error',
                        title: "错误",
                        text: request.msg
                    })

                },
                success: function (request) {
                    if (options.success) {
                        options.success();
                    }
                    Swal.fire({
                        position: 'top-end',
                        title: "成功!",
                        text: "数据已被成功删除.",
                        showConfirmButton: false,
                        timer: 1500
                    })
                }
            });
        }
    });

};