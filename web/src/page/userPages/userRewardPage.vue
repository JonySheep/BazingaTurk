<template>
  <div>
    <reward-card></reward-card>
    <daily-reward></daily-reward>
  </div>
</template>

<script>
  import rewardCard from '../../components/userCenter/rewardCard'
  import dailyReward from '../../components/userCenter/statisticGraph/dailyReward'
    export default {
        name: "user-reward-page",

      components:{
        rewardCard,
        dailyReward
      },

    }
</script>

<style scoped>

</style>
