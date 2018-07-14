<template>
  <el-card class="box-card">
    <div slot="header" >
      <span style="font-size: 18px">任务整体情况</span>
    </div>
    <div>
      <el-row>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666 ">进行中任务数</p>
          <el-button type="text" class="big-num" @click="checkTask('ongoingTask')">{{ongoingTask}}</el-button>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666 ">已完成任务数</p>
          <el-button type="text" class="big-num" @click="checkTask('finishedTask')">{{finishedTask}}</el-button>
        </el-col>
        <el-col :span="8" style="text-align: center">
          <p style="color: #666666 ">所有任务数</p>
          <el-button type="text" class="big-num" @click="checkTask('allTasks')">{{allTask}}</el-button>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "all-task-info-card",

      data(){
        return{
          ongoingTask:0,
          finishedTask:0,
          allTask:0
        }
      },

      created(){
        this.setOngoingTask()
        this.setFinishedTask()
      },

      methods:{
        setOngoingTask:function () {
          this.$api.get('/admin/tasks/ongoing',null,r=>{
            console.log(r.data)
            this.ongoingTask=r.data.length
            this.allTask+=this.ongoingTask
          })
        },

        setFinishedTask:function () {
          this.$api.get('/admin/tasks/finished',null,r=>{
            this.finishedTask=r.data.length
            this.allTask+=this.finishedTask
          })
        },


        /**
         * 跳转至查看特定任务信息
         */
        checkTask:function (task) {
          sessionStorage.setItem("task",task)
          this.$router.push('/taskManagePage')
        }
      }
    }
</script>

<style scoped>

  .box-card{
    margin: 50px;
  }

  .big-num{
    font-size: 35px;
    color:black;
  }
</style>
