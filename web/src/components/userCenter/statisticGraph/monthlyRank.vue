<template>
  <div id="monthlyRank" style="height: 400px;width:500px;margin-left: 100px"></div>
</template>

<script>
  export default {
    name: "monthly-rank",

    mounted(){

      // 基于准备好的dom，初始化echarts实例
      var echarts=require('echarts')
      var myChart = echarts.init(document.getElementById('monthlyRank'));


      var data = [
        ["2018-01",5],
        ["2018-02",6],
        ["2018-03",5],
        ["2018-04",3],
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
          text: '月排名变化',
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
            color:'#ff8713'
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
