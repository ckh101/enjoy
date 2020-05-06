/* ------------------------------------------------------------------------------
*
*  # Date and time pickers
*
*  Specific JS code additions for picker_date.html page
*
*  Version: 1.0
*  Latest update: Aug 1, 2015
*
* ---------------------------------------------------------------------------- */

$(function() {

    // Date range picker
    // ------------------------------

    // Basic initialization
    $('.daterange-basic').daterangepicker({
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        locale:{
            format: 'YYYY/MM/DD',
            applyLabel: "确定",
            cancelLabel: "取消",
            startLabel: "起始时间",
            endLabel: "结束时间",
            daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
            monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }


    });

    // Single picker
    $('.daterange-single').daterangepicker({
        singleDatePicker: true
    });

});
