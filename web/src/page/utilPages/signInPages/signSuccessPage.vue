<template>
  <div >
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="Box">
      <p style="margin-top: 30px;font-size: 23px;">账户激活成功</p>
      <p>
        <span style="font-size: 13px">欢迎来到</span>
        <span style="color: coral;font-size: 20px">bazingaTurk</span>
      </p>
      <p style="font-size: 13px">最实用的众包标注平台</p>

      <el-button style="margin-top:50px;margin-right: 20px" type="primary" size="small" @click="toReqLogin">发布者点此登录</el-button>
      <el-button style="margin-left: 20px" type="primary" size="small" @click="toWorkerLogin">工作者点此登录</el-button>
    </div>
  </div>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "activation-page",

      methods:{
        toReqLogin:function () {
          this.$router.push('/requesterLogin')
        },
        toWorkerLogin:function () {
          this.$router.push('/workerLogin')
        }
      },
    }
</script>

<style scoped>
  .Box{
    margin: auto;
    width: 400px;
    height: 300px;
    border: darkgray 1px solid ;
    border-radius: 20px;
    text-align: center;
  }
</style>
