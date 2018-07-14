<template>
    <div>
      <el-row class="tac">
        <el-col :span="4">
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo">
            <el-menu-item index="1" @click="showHome">
              <template slot="title">
                <i class="el-icon-view"></i>
                <span>首页</span>
                <div class="line"></div>
              </template>
            </el-menu-item>

            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span >管理</span>
              </template>
              <el-menu-item index="2-1" @click="showUserManage">用户管理</el-menu-item>
              <el-menu-item index="2-2" @click="showTaskManage">任务管理</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>

        <el-col :span="20">
          <home v-if="naviTo==='home'"></home>
          <user-manage v-if="naviTo==='userManage'" ></user-manage>
          <task-manage v-if="naviTo==='taskManage'" ></task-manage>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import home from './home'
  import userManage from './userManage'
  import taskManage from './taskManage'
    export default {
        name: "main-navi",

      components:{
        home,
        userManage,
        taskManage
      },

      data(){
        return{
          //default config
          naviTo:'home', //默认显示首页
        }
      },

      props:['type'],

      created(){
          this.naviTo=this.type
      },

      methods: {

        /**
        点击首页按钮显示首页
         */
        showHome:function () {
          this.$router.push('/adminPage')
        },

        /**
         点击用户管理按钮显示用户管理界面
         */
        showUserManage:function () {
          this.$router.push('/userManagePage')
        },

        showTaskManage:function () {
          this.$router.push('/taskManagePage')
        }
      }
    }
</script>

<style scoped>

  .line{
    border-bottom: lightgray 1px solid;
  }
</style>
