<template>
  <div>
    <my-header></my-header>

    <div style="height: 5px"> </div>

    <el-row>
      <el-col :span="4">
        <el-menu
          default-active="1"
          class="el-menu-vertical-demo">
          <el-menu-item index="1" @click="showUser">
            <i class="el-icon-menu"></i>
            <span slot="title">个人排名</span>
          </el-menu-item>
          <el-menu-item index="2"  @click="showTask">
            <i class="el-icon-menu"></i>
            <span slot="title">任务完成情况</span>
          </el-menu-item>
          <el-menu-item index="3"  @click="showReward">
            <i class="el-icon-menu"></i>
            <span slot="title">任务报酬</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <user-statistic v-if="showType==='user'" ></user-statistic>
        <user-task-statistic v-if="showType==='task'"></user-task-statistic>
        <user-reward v-if="showType==='reward'"></user-reward>
      </el-col>
    </el-row>
  </div>

</template>

<script>
  import myHeader from '../../components/header/taskPageHeader'
  import userStatistic from './userRankStatisticPage'
  import userTaskStatistic from './userTaskStatisticPage'
  import userReward from './userRewardPage'
    export default {
        name: "user-rank-page",
      components:{
        myHeader,
        userTaskStatistic,
        userStatistic,
        userReward
      },

      data(){
        return{
          showType:'user'
        }
      },

      methods:{
        showUser:function () {
          this.showType='user'
        },

        showTask:function () {
          this.showType='task'
        },

        showReward:function () {
          this.showType='reward'
        }
      }
    }
</script>

<style scoped>

</style>
