/**
 * 基于h+的PNotify notifications和bootstrap的modal组件，实现任务提交和完成通知
 * @author zoulr
 * @date 2019/08/26
 */
(function($){
    $('body').on('click', '.task-details', function() {
        var _detail = $(this).data('detail');
        if (!_detail) {
            return;
        }

        var _taskId = this.getAttribute('task-id');
        var _taskDetail = _detail[_taskId];
        if (!_taskDetail) {
            return;
        }

        showDetail(_taskDetail, _taskId);
    });

    var _modal = `
        <div id="taskDetailModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="table-responsive pre-scrollable">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>广告ID</th>
                                        <th>广告名称</th>
                                        <th>操作结果</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    $('body').append(_modal);
    var _detailModal = $('#taskDetailModal');
    const _taskIdItemName = "smartad-task-id-list";
    var _singleInstance;

    function pushTaskId(_taskId) {
        var _arr = getTaskIds();
        if (_arr.indexOf(_taskId) == -1) {
            _arr.push(_taskId);
            sessionStorage.setItem(_taskIdItemName, JSON.stringify(_arr));
        }
    }

    function getTaskIds() {
        var _itemValue = sessionStorage.getItem(_taskIdItemName);
        var _arr;
        if (_itemValue == null) {
            _arr = [];
        } else {
            _arr = JSON.parse(_itemValue);
        }

        return _arr;
    }

    function deleteTaskId(_taskId) {
        var _arr = getTaskIds();
        var _index = _arr.indexOf(_taskId);
        if (_index == -1) {
            return;
        }

        if (_arr.length == 1) {
            _arr = [];
        } else {
            delete _arr[_index];
        }

        sessionStorage.setItem(_taskIdItemName, JSON.stringify(_arr));
    }

    function showDetail(_taskDetail, _taskId) {
        var _tbody = _detailModal.find('tbody');
        _tbody.empty();

        var _html = '';
        var _msgList = _taskDetail.msgList;
        for (var i in _msgList) {
            _html += '<tr>';
            _html += '<td>' + _msgList[i].adGroupId + '</td>';
            _html += '<td>' + _msgList[i].name + '</td>';
            _html += '<td>' + _msgList[i].msg + '</td>';
            _html += '</tr>';
        }

        _tbody.html(_html);

        _detailModal.find('table').siblings().remove();
        _detailModal.find('table').parent().css('overflow-y', 'auto');
        _detailModal.modal('show');
    }

    function notify(_component, _type, _taskType, _autoHide, _msg) {
        var _title;
        var _addclass;
        switch (_type) {
            case 1:
                _title = getTaskName(_taskType) + ' 任务已提交';
                _addclass = 'bg-slate-400';
                break;
            case 2:
                _title = getTaskName(_taskType) + ' 任务已完成';
                _addclass = 'bg-primary';
                break;
        }

        var _notify = new PNotify({
            title: _title,
            text: _msg,
            addclass: _addclass,
            hide: _autoHide,
            buttons: {
                sticker: false
            }
        });

        if (_type == 2) {
            _notify.elem.find('.task-details').data('detail', _component.completeDetail);
        }
    }

    function getTaskName(_taskType) {
        if (_taskType == 'adCopy') {
            return '广告复制';
        }

        return '未定义任务名';
    }

    function addTask(_taskType, _taskId) {
        pushTaskId(_taskId);
        notify(this, 1, _taskType,true, '系统正在处理您的任务……');
    }

    function complete(_component, _taskId, _detail) {
        deleteTaskId(_taskId);
        _component.completeDetail[_taskId] = _detail;

        var _succeedMsg = '总' + _detail.total + '条，成功' + _detail.succedCnt + '条，失败' + (_detail.total - _detail.succedCnt) + '条。'
            + '<span class="task-details" style="text-decoration:underline;cursor:pointer" task-id="' + _taskId + '"><i class="icon-list-ordered"></i>查看详细</span>';

        notify(_component, 2, _detail.taskType, false, _succeedMsg);
    }

    function query(_component) {
        if (!_component.options.active) {
            return;
        }

        var unCompleteIds = getTaskIds().slice();

        for (var i = 0; i < unCompleteIds.length; i++) {
            doQuery(_component, unCompleteIds[i]);
        }

        setTimeout(function() {
            query(_component);
        }, _component.options.interval || 4000);
    }

    function doQuery(_component, _id) {
        $.ajax({
            url : _component.options.queryUrl,
            data : {taskId : _id},
            success : function(ret) {
                if (ret.status < 0) {
                    deleteTaskId(_id);
                    return;
                }

                if (ret.status == 0) {
                    complete(_component, _id, ret.data);
                }
            }
        });
    }

    function taskmonitor(_opt) {
        if (_opt.queryUrl == null) {
            alert("任务监视组件初始化失败，请检查配置！");
            return;
        }

        this.unCompleteIds = [];
        this.completeDetail = {};

        this.options = _opt;
        this.addTask = addTask;
        this.active = function() {
            this.options.active = true;
            query(this);
            return this;
        };
    }

    $.taskmonitor = function(options){
        var _opts = $.extend({}, $.taskmonitor.defaults, options);
        if (_singleInstance) {
            _singleInstance.options = _opts;
        } else {
            _singleInstance = new taskmonitor(_opts);
        }

        return _singleInstance;
    };

    $.taskmonitor.defaults = {
        queryUrl:null,
        interval:4000, // 轮询时间，单位ms，建议大于4秒
    };
})(jQuery);