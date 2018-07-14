<template>
  <div>
    <my-header></my-header>
    <myTasks></myTasks>
  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader.vue'
  import myTasks from '../../components/taskDoneList.vue'
    export default {
      name: "task-done",
      components:{
          myHeader,myTasks
      }
    }
</script>

<style scoped>

</style>
