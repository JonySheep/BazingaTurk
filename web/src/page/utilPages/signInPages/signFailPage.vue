<template>
  <div>
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="Box">
      <p style="margin-top: 30px;font-size: 23px;">激活失败</p>
      <p>
        <span style="font-size: 13px">感谢您注册</span>
        <span style="color: coral;font-size: 20px">bazingaTurk</span>
      </p>
      <p style="font-size: 13px">您的激活网址已过期，请尝试重新注册</p>

      <el-button style="margin-top:30px;" type="primary" size="small" @click="toHome">回到首页</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "sign-fail-page",

      methods:{
        toHome:function () {
          this.$router.push('/')
        }
      }
    }
</script>

<style scoped>
  .Box{
    margin: auto;
    width: 400px;
    height: 250px;
    border: darkgray 1px solid ;
    border-radius: 20px;
    text-align: center;
  }
</style>
