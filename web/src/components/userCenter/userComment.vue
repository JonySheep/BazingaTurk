<template>
  <el-container style="margin-left: 300px;margin-right: 300px;margin-top: 50px">

    <el-col :span="8" >
      <el-card  shadow="hover" style="width: 200px;margin-bottom: 20px;">
        <div>
          <span style="font-size: 15px;color: dimgray">工作效率</span>
          <p style="text-align: center;">
            <span style="color: coral;font-size: 40px;margin: 5px">{{eff}}</span>
            <span style="font-size: 15px;color: dimgray">元/小时</span>
          </p>
        </div>
      </el-card>

    </el-col>

    <el-col :span="8">
      <el-card  shadow="hover" style="width: 200px;">
        <div>
          <span style="font-size: 15px;color: dimgray">标注准确率</span>
          <p style="text-align: center;">
            <span style="color:red;font-size: 40px;margin: 5px">{{accuracy}}</span>
            <span style="font-size: 15px;color: dimgray">%</span>
          </p>
        </div>
      </el-card>
    </el-col>

    <el-col :span="8">
      <el-card  shadow="hover" style="width: 200px;">
        <div>
          <span style="font-size: 15px;color: dimgray">总奖励</span>
          <p style="text-align: center;">
            <span style="color: gold;font-size: 40px;margin: 5px">{{reward}}</span>
            <span style="font-size: 15px;color: dimgray">¥</span>
          </p>
        </div>
      </el-card>
    </el-col>

  </el-container>
</template>

<script>
    export default {
        name: "user-comment",
      data(){
        return{
          eff:0.0,
          accuracy:0.0,
          reward:0.0,
        }
      },

      created(){
        this.setWorkerInfo()
      },

      methods:{
        setWorkerInfo:function () {
          let _this=this
          this.$api.get('/worker/'+sessionStorage.getItem('userID'),null,r=>{
            _this.eff=parseFloat(Number(r.data.efficiency).toFixed(2))
            _this.accuracy=parseFloat(Number(r.data.accuracy*100).toFixed(2))
            _this.reward=parseFloat(Number(r.data.reward).toFixed(2))
          })
        }
      }
    }
</script>

<style scoped>

</style>
