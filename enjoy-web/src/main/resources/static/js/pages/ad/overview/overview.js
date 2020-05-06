var  changeFundType = function () {
    $("#balance").text($("#fundType").val());
    $("#cost").text($("#fundType").find("option:selected").data("cost"));
    var status = $("#fundType").find("option:selected").data("status");
    if(status == 'FUND_STATUS_NORMAL'){
        $("#fundStatus").text("有效");
    }else if(status == 'FUND_STATUS_NOT_ENOUGH'){
        $("#fundStatus").text("余额不足");
    }else if(status == 'FUND_STATUS_FROZEN'){
        $("#fundStatus").text("资金冻结");
    }
}
var last7DaysViewCountLine = function(){
    if(Array.isArray(last7DayViewCount)){
        if(last7DayViewCount.length > 0){
            $("#c3-line-chart").addClass("has-fixed-height");
            $("#no_data").hide();
            var date = [];
            var viewcount = [];
            var clickcount=[];
            for(var i in last7DayViewCount){
                date[i] = last7DayViewCount[i].date;
                viewcount[i] = last7DayViewCount[i].view_count;
                clickcount[i] = last7DayViewCount[i].valid_click_count;
            }
            var chart = echarts.init(document.getElementById("c3-line-chart"));
            var option = {
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['曝光量','点击量']
                },
                color:['#19ff9c', '#26a69a'],
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    data: date
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name:'曝光量',
                        type:'line',
                        data:viewcount,
                        itemStyle : {
                            normal : {
                                lineStyle:{
                                    color:'#19ff9c'
                                }
                            }
                        }

                    },
                    {
                        name:'点击量',
                        type:'line',
                        data:clickcount,
                        itemStyle : {
                            normal : {
                                lineStyle:{
                                    color:'#26a69a'
                                }
                            }
                        }
                    }
                ]
            };
            chart.setOption(option);
        }else{
            $("#c3-line-chart").removeClass("has-fixed-height");
            $("#no_data").show();
        }

    }
}
