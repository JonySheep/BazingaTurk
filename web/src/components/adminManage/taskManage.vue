<template>
  <div>
    <p style="margin: 50px;font-size: 20px">任务管理</p>

    <el-tabs type="card" style="margin: 50px;background-color: white">
      <el-tab-pane label="进行中" @click="showOngoing">
        <task-list v-bind:state="'进行中'"></task-list>
      </el-tab-pane>
      <el-tab-pane label="已完成" @click="showFinished">
        <task-list  v-bind:state="'已完成'"></task-list>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import taskList from './taskList'
  export default {
    name: "task-manage",
    components:{
      taskList,
    },
    data(){
      return{
        state:"进行中"
      }
    },

    created(){
      this.user=sessionStorage.getItem("task")
      if(this.user==='ongoingTask'){
        this.showOngoing()
      }
      else if(this.user==='finishedTask'){
        this.showFinished()
      }
      else{
        this.showReqList()
      }
    },

    methods:{
      showOngoing:function () {
        this.state="进行中"
      },

      showFinished:function () {
        this.state="已完成"
      }
    }
  }
</script>

<style scoped>


</style>
