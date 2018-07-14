<template>
  <div>
    <el-header style="padding:0px;margin:0px">
    <manage_header></manage_header>
    </el-header>
    <el-main style="padding:0px;margin:0px">
    <release_config></release_config>
    </el-main>
  </div>
</template>

<script>
  import manage_header from "../../components/header/taskPageHeader";
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import ElHeader from "element-ui/packages/header/src/main";
  import release_config from "../../components/releaseConfig"
    export default {
      name: "task-config",
      components:{
        manage_header,
        ElContainer,
        ElMain,
        ElHeader,
        release_config
      },
      data(){
        return{
        }
      },
      methods:{
        submitFile(item){
          let formData = new FormData();
          formData.append('file', item.file);
          this.$api.post( '/tasks/upload', formData);
        },
      }
    }
</script>

<style scoped>

</style>
