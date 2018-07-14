<template>
  <el-card class="box-card">
    <div slot="header" >
      <span style="font-size: 15px">用户排名</span>
    </div>
    <div>
      <el-row>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 13px">月排名</p>
          <p class="big-num">{{rank}}</p>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 17px;margin: 0px">日排名</p>
          <p style="font-size: 40px;margin: 19px">{{rank}}</p>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 15px ">季度排名</p>
          <p class="big-num">{{rank}}</p>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
  export default {
    name: "user-rank-card",
    data(){
      return{
        rank:0,
      }
    },

    created(){
      this.setRank()
    },

    methods:{
      setRank:function () {
        this.$api.get('/worker/'+sessionStorage.getItem('userID')+'/rank',null,r=>{
          this.rank=r.data
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
    font-size: 25px;
  }
</style>

