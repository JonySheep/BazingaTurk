<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" active-text-color="#f25f5c">
    <el-tab-pane label="进行中" name="first"></el-tab-pane>
    <el-tab-pane label="已完成" name="second"></el-tab-pane>
    <el-tab-pane label="可参与" name="third"></el-tab-pane>
  </el-tabs>
</template>
<script>
  export default {
    data() {
      return {
        activeName: 'second'
      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
</script>
