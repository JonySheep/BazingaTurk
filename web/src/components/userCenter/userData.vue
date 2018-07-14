<template>
  <el-container style="margin-left: 250px;margin-right: 250px;margin-top: 50px">

    <el-col :span="8">
      <el-card  shadow="hover" style="width: 200px;">
        <div>
          <span style="font-size: 15px;color: dimgray">连续登录天数</span>
          <p style="text-align: center;">
            <span style="color: coral;font-size: 40px;margin: 5px">{{userInfo.continuousLoginDays}}</span>
            <span style="font-size: 15px;color: dimgray">天</span>
          </p>
        </div>
      </el-card>
    </el-col>

    <el-col :span="16" style="text-align: center">
      <all-task></all-task>
    </el-col>

  </el-container>
</template>

<script>
  import allTask from './statisticGraph/allTaskCard'
  import ElContainer from "element-ui/packages/container/src/main";
    export default {
      name: "user-data",
      components:{
        ElContainer,
        allTask,
      },

      data(){
        return{
          userInfo:{}
        }
      },

      created(){
        this.setInfo()
      },

      methods:{
        setInfo:function () {
          let _this=this
          this.$api.get('/user/'+sessionStorage.getItem('userID'),null,r=>{
            _this.userInfo=r.data
          })
        }
      }
    }
</script>

<style scoped>

</style>
