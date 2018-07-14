<template>
  <div>
    <el-row>
      <el-col :span="4" >
        <el-button type="text" style="float: right;margin-right: 50px">
          <span style="color: coral; font-size: 20px">NJU </span>
          <span style="color: slategray; font-size: 20px;">bazinga turk</span>
        </el-button>
      </el-col>
      <el-col :span="10" style="float: left">
        <el-button class="headerbutton" type="text">首页</el-button>
        <el-button class="headerbutton" type="text">费用&回报</el-button>
        <el-button class="headerbutton" type="text">关于我们</el-button>
        <el-button class="headerbutton" type="text">联系我们</el-button>
      </el-col>
      <el-col :span="10">
        <div style="float: right;margin-right: 30px">
          <el-button type="danger" plain size="small" style="margin-top: 5px" @click="requesterLogin">发布者登录</el-button>
          <el-button type="danger" plain size="small" @click="workerLogin">工作者登录</el-button>
        </div>
      </el-col>
    </el-row>
    <div class="line"></div>
  </div>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: 'navi',

      methods:
        {
          requesterLogin:function() {
            this.$router.push('/requesterLogin')
          },

          workerLogin:function () {
            this.$router.push('workerLogin')
          },
        }
    }


</script>

<style scoped>
.headerbutton{
  font-weight: normal;
  font-size: 15px;
  margin-top: 3px;
  color: dimgray;
  margin-left: 5px;
  margin-right: 30px;
}
  .line{
    border-bottom: darkgray solid 1px;
  }
</style>
