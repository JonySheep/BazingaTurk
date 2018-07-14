<template>

  <el-card class="box-card">
    <ul style="border: lightgrey 1px solid;margin: 20px">
      <li v-for="task in taskList" style="display: block">
        <el-row style="height: 50px">
          <el-col :span="6">
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px">{{task.taskName}}</div>
          </el-col>
          <el-col :span="6">
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px">{{task.taskType}}</div>
          </el-col>
          <el-col :span="6">
            <div style="margin-top: 15px;font-size: 13px;color: #969896;margin-bottom: 5px">{{task.createTime}}</div>
          </el-col>
          <el-col :span="6">
            <el-button size="mini" type="danger" plain style="margin-top: 10px">详情</el-button>
            <el-button size="mini" type="success" plain style="margin-top: 10px">移除</el-button>
          </el-col>
        </el-row>
        <div class="line"></div>
      </li>
    </ul>
  </el-card>
</template>

<script>
    export default {
        name: "task-list",

      data(){
        return{
          taskList:[]
        }
      },

      props:['state'],

      created(){
        if(this.state==='进行中'){
          this.getOngoingTaskList()
        }
        else{
          this.getFinishedTaskList()
        }
      },

      methods:{
        getOngoingTaskList:function () {
          this.$api.get('/admin/tasks/ongoing',null,r=>{
            this.taskList=r.data
          })
        },

        getFinishedTaskList:function () {
          this.$api.get('/admin/tasks/finished',null,r=>{
            this.taskList=r.data
          })
        }
      }
    }
</script>

<style scoped>

  .line{
    margin-right: 40px;
    border-bottom: lightgray 1px solid;
    margin-bottom: 5px;
  }

  .box-card{
    margin: 50px;
  }

</style>
