<template>
  <div id="dailyReward" style="height: 400px;width:1000px;margin: 60px"></div>
</template>

<script>
  export default {
    name: "daily-reward",

    data(){
      return{
        data:[]
      }
    },

    mounted(){

      // 基于准备好的dom，初始化echarts实例
      var echarts=require('echarts')
      var myChart = echarts.init(document.getElementById('dailyReward'));

      var dateList = []
      var valueList = []

      let _this=this
      this.$api.get('/worker/'+sessionStorage.getItem("userID")+'/rewards',null,r=>{
        console.log(r.data)
        for(let i=0;i<r.data.length;i++){
          var each=[]
          each=r.data[i].split(':')
          dateList.push(each[0])
          valueList.push(each[1])
        }


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
            text: '日报酬变化',
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
      })


    }
  }
</script>

<style scoped>

</style>
