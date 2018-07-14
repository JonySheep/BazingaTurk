<template>
  <div>
    <el-row class="headerBackground">
      <el-col :span="4" style="height: 50px">
        <p style="margin-left: 20px">
          <span style="color: coral;font-size: 20px">NJU </span>
          <span style="color:dimgray;font-size: 20px">bazinga turk</span>
        </p>
      </el-col>
      <el-col :span="18">
        <el-tag color="white" type="danger" style="margin-top: 10px">{{taskName}}</el-tag>
      </el-col>
      <el-col :span="1">
        <el-button plain style="color: coral;margin-top: 10px" size="small" @click="quitTask">退出任务</el-button>
      </el-col>
    </el-row>
    <div class="line"></div>
  </div>
</template>

<script>
  import '../../style/css/headerBackground.css'

    export default {

      //data from api
      props:['taskName'],

      methods:{

        quitTask:function () {
          this.$router.push('/taskCenter')
        }
      }

    }
</script>

<style scoped>
.line{
  border-bottom: darkgray 1px solid;
}
</style>
