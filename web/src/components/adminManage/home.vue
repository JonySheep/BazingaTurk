<template>
  <div>
    <all-user></all-user>
    <all-task></all-task>
  </div>
</template>

<script>
  import allUser from './allUserInfoCard'
  import allTask from './allTaskInfoCard'
    export default {
        name: "home",
      components:{
        allUser,
        allTask
      }
    }
</script>

<style scoped>

</style>
