<template>
  <div>
    <Header></Header>

    <div style="height: 5px;background-color: rgba(252, 252, 252, 1)"> </div>

    <div class="choosePane" style="background-color: rgba(252, 252, 252, 1)">
      <el-tabs
        @tab-click="handleClick"
        style="display: inline-block">
        <el-tab-pane label="资料" name="info"></el-tab-pane>
        <el-tab-pane label="账户" name="account"></el-tab-pane>
      </el-tabs>
    </div>

    <div>
      <user-info v-if="type==='info'"></user-info>

      <user-account v-if="type==='account'"></user-account>
    </div>

  </div>
</template>

<script>
  import '../../style/css/choosePane.css'

  import Header from '../../components/header/taskPageHeader'
  import UserInfo from './userInfoPage'
  import UserAccount from './userAccountPage'

    export default {
        name: "user-info-page",

      components:{
        Header,
        UserInfo,
        UserAccount
      },

      data(){
          return{
            type:'info'
          }
      },

      methods:{

        handleClick:function (tab,event) {
          this.type=tab.name
        }
      }
    }
</script>

<style scoped>

</style>
