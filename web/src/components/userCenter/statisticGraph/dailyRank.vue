
<template>
  <div id="dailyRank" style="height: 400px;width:1000px;margin: 60px"></div>
</template>

<script>
    export default {
        name: "daily-rank",

      mounted(){

        // 基于准备好的dom，初始化echarts实例
        var echarts=require('echarts')
        var myChart = echarts.init(document.getElementById('dailyRank'));


        var data = [
          ["2018-04-05",3],
          ["2018-04-06",1],
          ["2018-04-07",5],
          ["2018-04-08",3],
          ["2018-04-09",2],
          ["2018-04-10",4],
          ["2018-04-11",6],
          ["2018-04-12",5],
          ["2018-04-13",10],
          ["2018-04-14",5],
          ["2018-04-15",2],
          ["2018-04-16",8],
          ["2018-04-17",1],
          ["2018-04-18",6],
          ["2018-04-19",12],
          ["2018-04-20",6],
          ["2018-04-21",7],
          ["2018-04-22",4],
          ["2018-04-23",7],
          ["2018-04-24",5],
        ]

        var dateList = data.map(function (item) {
          return item[0];
        });
        var valueList = data.map(function (item) {
          return item[1];
        });

        var option = {

          // Make gradient line here
          visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 400
          },

          title: {
            left: 'center',
            text: '日排名变化',
            textStyle:{
              color:"#696969",
              fontWeight:'lighter',
            },
          },

          tooltip: {
            trigger: 'axis',

          },

          xAxis: {
            data: dateList
          },

          yAxis: {
            splitLine: {show: true}
          },


          grid: {
            bottom: '30%'
          },

          textStyle:{
            color:'#696969',
            fontWeight:'lighter',
          },

          series: {
            type: 'line',
            lineStyle:{
              color:'#FFD700'
            },
            showSymbol: false,
            data: valueList
          },
        };

        myChart.setOption(option)
      }
    }
</script>

<style scoped>

</style>
