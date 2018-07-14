<template>
  <div>
    <my-header></my-header>
    <myTasks></myTasks>
  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader.vue'
  import myTasks from '../../components/requesterTaskDoneList.vue'
    export default {
      name: "requester-task-done",
      components:{
          myHeader,myTasks
      }
    }
</script>

<style scoped>

</style>
