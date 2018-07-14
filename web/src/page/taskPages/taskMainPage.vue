<template>
  <div>
    <my-header @showRequesterTaskManage="showRequesterTaskManage"
               @showTaskManage="showTaskManage"
               @showRequesterTaskDone="showRequesterTaskDone"
               @showTaskDone="showTaskDone"
               @showTaskLobby="showTaskLobby"
               @showTaskConfig="showTaskConfig"></my-header>

    <task-manage v-if="taskPage==='taskManage'"></task-manage>
    <task-lobby v-if="taskPage==='taskLobby'"></task-lobby>
    <requester-task-manage v-if="taskPage==='requesterTaskManage'"></requester-task-manage>
    <task-done v-if="taskPage==='taskDone'"></task-done>
    <requester-task-done v-if="taskPage==='requesterTaskDone'"></requester-task-done>
    <task-config v-if="taskPage==='taskConfig'"></task-config>
  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader'
  import taskManage from '../../components/taskList'
  import taskLobby from '../../components/taskLobbyList'
  import requesterTaskManage from '../../components/requesterTaskList'
  import taskDone from '../../components/taskDoneList'
  import requesterTaskDone from '../../components/requesterTaskDoneList'
  import taskConfig from '../../components/releaseConfig'
    export default {
        name: "task-main-page",
      components:{
        taskLobby,
        taskManage,
        myHeader,
        requesterTaskManage,
        taskDone,
        requesterTaskDone,
        taskConfig
      },

      data(){
        return{
          taskPage:'taskLobby'
        }
      },

      methods:{
        showTaskManage:function () {
          this.taskPage='taskManage'
        },

        showTaskLobby:function () {
          this.taskPage='taskLobby'
        },

        showRequesterTaskManage:function(){
          this.taskPage='requesterTaskManage'
        },

        showTaskDone:function(){
          this.taskPage='taskDone'
        },

        showRequesterTaskDone:function(){
          this.taskPage='requesterTaskDone'
        },

        showTaskConfig:function(){
          this.taskPage='taskConfig'
        }
      }
    }
</script>

<style scoped>

</style>
