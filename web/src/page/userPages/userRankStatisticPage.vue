<template>
  <div>
    <user-rank-card></user-rank-card>
    <!--<daily-rank></daily-rank>-->
    <!--<monthly-rank></monthly-rank>-->
  </div>
</template>

<script>
  import dailyRank from '../../components/userCenter/statisticGraph/dailyRank'
  import monthlyRank from '../../components/userCenter/statisticGraph/monthlyRank'
  import userRankCard from '../../components/userCenter/statisticGraph/userRankCard'
    export default {
        name: "user-rank-statistic-page",
      components:{
        userRankCard,
        dailyRank,
        monthlyRank
      },
    }
</script>

<style scoped>

</style>
