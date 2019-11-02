$(function(){
    loadCustomerGc();
})

function loadCustomerGc(){
    $.ajax({
        type:"post",
        url:ctx+"/customer/queryCustomerGC",
        dataType:"json",
        success:function (data) {
            if(data.code == 200){
                var x = data.levels;
                var y = data.counts;
                var myChart = echarts.init(document.getElementById("main"));
                var option = {
                    title: {
                        text: '客户构成分析图'
                    },
                    tooltip: {},
                    legend: {
                        data:['数量']
                    },
                    xAxis: {
                        data: x
                    },
                    yAxis: {},
                    series: [{
                        name: '数量',
                        type: 'bar',
                        data: y
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }


        }
    })
}