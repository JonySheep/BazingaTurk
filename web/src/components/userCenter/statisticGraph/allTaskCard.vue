<template>
  <el-card >
    <div slot="header" >
      <span style="font-size: 15px">任务完成情况</span>
    </div>
    <div>
      <el-row>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 13px">进行中任务数</p>
          <p class="big-num">{{ongoingAssign}}</p>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 13px ">已完成任务数</p>
          <p class="big-num">{{finishedAssign}}</p>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666;font-size: 13px ">所有任务数</p>
          <p class="big-num">{{allAssign}}</p>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
  export default {
    name: "all-task-card",

    data(){
      return{
        ongoingAssign:0,
        finishedAssign:0,
        allAssign:0,
      }
    },

    created(){
      this.setOngoing()
      this.setFinished()
    },

    methods:{
      /**
       * 正在进行的任务数
       */
      setOngoing:function () {
        this.$api.get('/'+sessionStorage.getItem('role').toLowerCase()+'/'+sessionStorage.getItem('userID')+'/ongoing',null,r=>{
          this.ongoingAssign=r.data.length
          this.allAssign+=this.ongoingAssign
        })
      },


      /**
       * 完成的任务数
       */
      setFinished:function () {
        this.$api.get('/'+sessionStorage.getItem('role').toLowerCase()+'/'+sessionStorage.getItem('userID')+'/finished',null,r=>{
          this.finishedAssign=r.data.length
          this.allAssign+=this.finishedAssign
        })
      }
    }
  }
</script>

<style scoped>

  .big-num{
    font-size: 25px;
  }
</style>

