<template>
  <div>
    <all-task-card class="box-card"></all-task-card>
    <!--<daily-finish-task></daily-finish-task>-->
  </div>
</template>

<script>
  import allTaskCard from '../../components/userCenter/statisticGraph/allTaskCard'
  import dailyFinishTask from '../../components/userCenter/statisticGraph/dailyFinishTask'
    export default {
        name: "user-statistic",
      components:{
        dailyFinishTask,
        allTaskCard,
      }
    }
</script>

<style scoped>
  .box-card{
    margin-top: 50px;
    margin-right: 150px;
    margin-left: 150px;
  }
</style>
