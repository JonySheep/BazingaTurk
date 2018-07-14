<template>
  <div>
    <my-header></my-header>
    <myTasks></myTasks>
  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader.vue'
  import myTasks from '../../components/taskList.vue'
  import myTaskType from '../../components/taskType.vue'
    export default {
      components: { myHeader, myTasks ,myTaskType},
    }
</script>

<style scoped>

</style>
