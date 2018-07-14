<template>
  <div>
    <p style="margin: 50px;font-size: 20px">用户管理</p>

    <el-tabs type="card" style="margin: 50px;background-color: white">
      <el-tab-pane label="发布者" @click="showReqList">
        <user-list v-bind:role="'发布者'"></user-list>
      </el-tab-pane>
      <el-tab-pane label="工作者" @click="showWorkerList">
        <user-list  v-bind:role="'工作者'"></user-list>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import userList from './userList'
    export default {
      name: "user-manage",
      components:{
        userList,
      },
      data(){
        return{
          role:"发布者",
          user:''
        }
      },

      created(){
        this.user=sessionStorage.getItem("user")
        console.log(this.user)
        if(this.user==='requester'){
          this.showReqList()
        }
        else if(this.user==='worker'){
          //this.showWorkerList()
          this.role="发布者"
        }
        else{
          this.showReqList()
        }
      },

      methods:{
        showReqList:function () {
          this.role="发布者"
        },

        showWorkerList:function () {
          console.log("!!!")
          this.role="工作者"
        }
      }
    }
</script>

<style scoped>

</style>
