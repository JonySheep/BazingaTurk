<template>
  <el-card class="box-card">
    <div slot="header" >
      <span style="font-size: 15px">个人任务报酬</span>
    </div>
    <div>
      <el-row>
        <el-col :span="12" style="text-align: center">
          <span class="icon-icon-test" ></span>
          <span style="color: #666666;font-size: 15px">   今日到账报酬</span>
          <p class="big-num">{{rewardDaily}} ¥</p>
        </el-col>
        <el-col :span="12" style="text-align: center">
          <span class="icon-icon-test" ></span>
          <span style="color: #666666;font-size: 15px ">   总报酬</span>
          <p class="big-num">{{reward}} ¥</p>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
  import '../../assets/rewardIcon/iconfont.css'
  export default {
    name: "reward-card",

    data(){
      return{
        reward:0.0,
        rewardDaily:0.0
      }
    },

    created(){
      this.setUserInfo()
    },

    methods:{

      /**s
       * 得到用户信息
       */
      setUserInfo:function () {
        var _this = this
        this.$api.get('/worker/'+sessionStorage.getItem("userID"), null, r => {

          _this.reward=parseFloat(Number(r.data.reward).toFixed(3))
        })

        this.$api.get('/worker/'+sessionStorage.getItem("userID")+'/reword',null,r=>{
          _this.rewardDaily=parseFloat(Number(r.data).toFixed(3))
        })
      }
    }
  }
</script>

<style scoped>

  .box-card{
    margin-top: 50px;
    margin-right: 150px;
    margin-left: 150px;
  }

  .big-num{
    font-size: 30px;
    color: gold;
  }

</style>

