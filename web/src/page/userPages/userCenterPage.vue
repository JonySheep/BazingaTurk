<template>
  <div>

    <my-header v-bind:isUserCenter="isUserCenter"></my-header>

    <div class="userInfoDiv">

      <div style="height: 10px"></div>

      <el-row type="flex">
        <el-col :span="4">
          <button class="rank" @click="showRank" v-if="userInfo.role==='Worker'">全区排名：#{{rank}}</button>
        </el-col>

        <el-col :span="16">
          <div class="info">
            <img :src=userInfo.avatar class="avatar" />

            <p style="font-size: 20px">{{userInfo.name}}</p>
            <div>
              <a class="el-icon-message"></a>
              <a style="font-size: 13px">{{userInfo.publicEmail}}</a>
            </div>
          </div>
        </el-col>

        <el-col :span="4">
          <el-row>
            <el-col :span="5">
              <p> </p>
            </el-col>
            <el-col :span="19">
              <button class="logoutButton" @click="logout"></button>
              <button class="chatButton"></button>
              <button class="editButton" @click="showInfo"></button>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>

    <div class="choosePane">
      <el-tabs  @tab-click="handleClick" style="display: inline-block">
        <el-tab-pane label="个人" name="user" ></el-tab-pane>
        <el-tab-pane label="项目" name="project" ></el-tab-pane>
        <el-tab-pane label="评估" name="comment" v-if="userInfo.role==='Worker'"></el-tab-pane>
      </el-tabs>
    </div>

    <div>
      <user-data v-if="showType==='user'"></user-data>
      <user-project v-if="showType==='project'"></user-project>
      <user-comment v-if="showType==='comment'"></user-comment>
    </div>

  </div>
</template>

<script>
  import myHeader from '../../components/header/taskPageHeader'
  import UserData from '../../components/userCenter/userData'
  import ElButton from "element-ui/packages/button/src/button";
  import '../../style/css/choosePane.css'
  import UserProject from '../../components/userCenter/userProject'
  import UserComment from '../../components/userCenter/userComment'

    export default {
        name: "user-center-page",
      components:{
        ElButton,
        UserData,
        UserProject,
        UserComment,
        myHeader,
      },
      data(){
        return {
          isUserCenter:true,
          showType:'user',
          userInfo:{},
          rank:0,
        }
      },

      mounted(){
        this.setUserInfo()
        if(sessionStorage.getItem('role')==='Worker'){
          this.setRank()
        }
      },

      methods: {

        /**
         *获得用户排名
         */
        setRank: function () {
          this.$api.get('/worker/'+sessionStorage.getItem('userID')+'/rank',null,r=>{
            this.rank=r.data
          })
        },

        handleClick:function (tab,event) {
          this.showType=tab.name
        },


        showInfo: function () {
          this.$router.push('/userInfo')
        },


        /**
         * 显示用户信息
         * @param userId
         */
        setUserInfo: function () {
          var _this = this
          this.$api.get('/user/'+sessionStorage.getItem("userID"), null, r => {
            _this.userInfo = r.data
            console.log(r.data)
          })
        },


        /**
         * 登出的响应方法
         */
        logout:function () {

          let _this=this

          this.$confirm('您确定要登出吗, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            sessionStorage.setItem("userId","")

            //登出
            this.$api.get('/logout')

            _this.$router.push('/')

            this.$message({
              type: 'success',
              message: '已退出登录!'
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消登出'
            });
          });

        },


        /**
         * 显示个人排名统计信息的方法
         */
        showRank:function () {
          this.$router.push('/userRank')
        }
      }
    }
</script>

<style scoped>

  .userInfoDiv{
    height: 270px;
    width: 100%;
    background-color: #f6f6f6;
  }

  .info{
    width: 500px;
    height: 100%;
    margin: auto;
    text-align: center;
  }

  .avatar{

    margin-top: 30px;
    width: 100px;
    height: 100px;
    border-radius: 200px;
    display: inline-block;
  }

  .rank{
    border: lightgrey 1px solid;
    border-radius: 3px;
    margin-top: 10px;
    margin-left: 20px;
    background-color: white;
    text-align: center;
    cursor: pointer;
    width: 100px;
  }

  .logoutButton{
    background: url("../../assets/icon/登出.png") white center no-repeat;
    width: 30px;
    height: 30px;
    border: lightgrey 1px solid;
    border-radius: 3px;
    margin: 10px;
    cursor: pointer;
    outline: none;
   }

  .chatButton{
    background: url("../../assets/icon/聊天.png") white center no-repeat;
    width: 30px;
    height: 30px;
    border: lightgrey 1px solid;
    border-radius: 3px;
    margin: 10px;
    cursor: pointer;
    outline: none;
  }

  .editButton{
    background: url("../../assets/icon/编辑.png") white center no-repeat;
    width: 30px;
    height: 30px;
    border: lightgrey 1px solid;
    border-radius: 3px;
    margin: 10px;
    cursor: pointer;
    outline: none;
  }

</style>
