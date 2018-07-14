<template>
    <div>
      <el-row class="headerBackground">
        <el-col :span="4">
          <div style="padding-top: 10px;padding-left: 20px">
            <span style="color: coral;font-size: 20px">NJU </span>
            <span style="color:dimgray;font-size: 20px">bazinga turk</span>
          </div>
        </el-col>
        <el-col :span="19">
          <p> </p>
        </el-col>
      </el-row>
      <div class="line"></div>
    </div>
</template>

<script>
  import '../../style/css/headerBackground.css'
  import '../../style/css/headAvatar.css'

    export default {
        name: "admin-page-header"
    }
</script>

<style scoped>

  .line{
    border-bottom: lightgray 1px solid;
  }
</style>
