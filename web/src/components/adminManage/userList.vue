<template>

  <el-card class="box-card">
    <ul style="border: lightgrey 1px solid;margin: 20px">
      <li v-for="user in Users" style="display: block">
        <el-row>
          <el-col :span="2">
            <img :src="'http://localhost:8080'+user.avatar" class="avatar" />
          </el-col>
          <el-col :span="18" class="user-list">
            <div style="margin-top: 15px;font-size: 12px;margin-bottom: 5px">{{user.name}}</div>
            <div style="font-size: 12px;color: #969896">{{user.username}}</div>
          </el-col>
          <el-col :span="4">
            <el-button size="small" type="info" plain style="margin-top: 20px">加入黑名单</el-button>
          </el-col>
        </el-row>
        <div class="line"></div>
      </li>
    </ul>
  </el-card>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "user-list",

      data(){
        return{
          Users:[]
        }
      },

      props:['role'],

      created(){
        if(this.role==="发布者"){
          this.getRequesterList()
        }
        else{
          this.getWorkerList()
        }
      },

      methods:{

        /**
         * 得到发布者列表
         */
        getRequesterList:function () {
          this.$api.get('/admin/requesters',null,r => {
            this.Users=r.data
          })
        },


        /**
         * 得到工作者列表
         */
        getWorkerList:function () {
          this.$api.get('/admin/workers',null,r => {
            this.Users=r.data
          })
        }
      }
    }
</script>

<style scoped>

  .box-card{
    margin: 50px;
  }

  .user-list{
    height: 55px;
  }

  .avatar{
    margin: 10px;
    height: 50px;
    width: 50px;
  }

  .line{
    margin-right: 40px;
    border-bottom: lightgray 1px solid;
    margin-bottom: 5px;
  }
</style>
