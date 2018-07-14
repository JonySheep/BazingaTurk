<template>
  <div>
    <el-row class="headerBackground">
      <el-col :span="4">
        <p style="margin-left: 20px;margin-top: 12px">
          <span style="color: coral;font-size: 20px">NJU </span>
          <span style="color:dimgray;font-size: 20px">bazinga turk</span>
        </p>
      </el-col>
      <el-col :span="15">
        <!--<div>-->
          <!--<el-button class="topButton" type="text" @click="routeToManage" >进行中</el-button>-->
          <!--<el-button class="topButton" type="text"  >已完成</el-button>-->
          <!--<el-button class="topButton" type="text"  @click="routeToLobby" >任务大厅</el-button>-->
        <!--</div>-->

        <el-menu class="nav-menu"
                 mode="horizontal"
                 active-text-color="coral"
                 text-color="dimgray"
                 background-color="rgba(252, 252, 252, 1)">
          <el-menu-item index="1" style="height: 53px" @click="routeToManage">进行中</el-menu-item>
          <el-menu-item index="2" style="height: 53px" @click="routeToDone">已完成</el-menu-item>
          <el-menu-item index="3" style="height: 53px" @click="routeToLobby">任务大厅</el-menu-item>
          <el-menu-item index="4" style="height: 53px" v-if="isRequester" @click="routeToRelease">发布任务</el-menu-item>
        </el-menu>
      </el-col>

      <el-col :span="4">

        <p> </p>

      </el-col>

      <!--个人头像-->
      <el-col :span="1">
        <img :src=userAvatar class="headAvatar" @click="routeToUserCenter"/>
      </el-col>
    </el-row>
    <div class="line"></div>
  </div>
</template>

<script>
  import '../../style/css/headerBackground.css'
  import '../../style/css/headAvatar.css'
    export default {
        name: "data-manage-header",
      data() {
        return {
          searchInput:'',
          userAvatar: '',

          //是否发起者
          isRequester:false,

        }
      },

      props:['taskPage'],

      mounted(){
        this.userAvatar=sessionStorage.getItem("avatar")
        if(sessionStorage.getItem("role")==='Requester'){
          this.isRequester=true
        }
      },

      methods:{

        /**
         * 跳转至个人中心界面
         */
        routeToUserCenter:function () {
          this.$router.push('/userCenter')
        },


        /**
         * 跳转至任务大厅界面
         */
        routeToLobby:function () {
          this.$router.push('/taskCenter')
          this.$emit('showTaskLobby')
        },


        /**
         * 跳转至个人任务管理界面
         */
        routeToManage:function () {
          if(!this.isRequester) {
            this.$router.push('/taskCenter')
            this.$emit('showTaskManage')
          }else{
            this.$router.push('/taskCenter')
            this.$emit('showRequesterTaskManage')
          }
        },

        /**
         * 跳转至个人已完成任务界面
         */
        routeToDone:function(){
          if(!this.isRequester){
            this.$router.push('/taskCenter')
            this.$emit('showTaskDone')
          }else{
            this.$router.push('/taskCenter')
            this.$emit('showRequesterTaskDone')
          }
        },

        /**
         * 跳转至上传任务界面
         */
        routeToRelease:function(){
          this.$router.push('/taskCenter')
          this.$emit('showTaskConfig')
        },

      }
    }
</script>

<style scoped>

  .topButton{
    margin-top: 7px;
    color: dimgray;
    font-size: 14px;
    margin-right: 10px;
  }
  .line{
    border-bottom: lightgrey 1px solid;
  }

  .nav-menu{
    margin-top: -3px;
  }
</style>
