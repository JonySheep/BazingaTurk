<template>
  <div>
    <my-header></my-header>
    <myTasks></myTasks>
  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader.vue'
  import myTasks from '../../components/requesterTaskList.vue'
    export default {
      name: "requester-task",
      components:{
          myHeader,myTasks
      }
    }
</script>

<style>

</style>
