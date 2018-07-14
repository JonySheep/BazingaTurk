<template >
  <div style="margin-left: 200px;margin-right: 200px;text-align: center">
    <div id="main" style="height: 1000px;width: 1000px"></div>
  </div>

</template>

<script>
    export default {
        name: "star",

      data(){
        return{
          value:'5',
        }
      },

      mounted(){

// 基于准备好的dom，初始化echarts实例
        var echarts=require('echarts')
        var myChart = echarts.init(document.getElementById('main'));
// 绘制图表
        // 模拟数据

        var option = {
          tooltip : {},
          visualMap: {
            min: 0,
            max: 10000,
            type: 'piecewise',
            orient: 'horizontal',
            left: 'center',
            top: 65,
            textStyle: {
              color: '#000'
            }
          },
          calendar: {
            top: 120,
            left: 30,
            right: 30,
            cellSize: ['auto', 14],
            range: '2016',
            itemStyle: {
              normal: {borderWidth: 0.5},
            },
            yearLabel: {show: false},

          },

          heatmap:{
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'red' // 0% 处的颜色
                }, {
                  offset: 1, color: 'blue' // 100% 处的颜色
                }],
                globalCoord: false // 缺省为 false
              },
            },
          },

          series: {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            data: this.getVirtulData(echarts,2016)
          }
        };

        myChart.setOption(option);
      },

      methods:{
        getVirtulData(echarts,year){
      year = year || '2017';
      var date = +echarts.number.parseDate(year + '-01-01');
      var end = +echarts.number.parseDate(year + '-12-31');
      var dayTime = 3600 * 24 * 1000;
      var data = [];
      for (var time = date; time <= end; time += dayTime) {
        data.push([
          echarts.format.formatTime('yyyy-MM-dd', time),
          Math.floor(Math.random() * 10000)
        ]);
      }
      return data;
    }
      }

    }
</script>

<style scoped>

</style>
