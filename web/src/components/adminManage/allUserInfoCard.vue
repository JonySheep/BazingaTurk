<template>
    <el-card class="box-card">
      <div slot="header" >
        <span style="font-size: 18px">用户整体情况</span>
      </div>
      <div>
        <el-row>
          <el-col :span="8" style="text-align: center">
            <p style="color: #666666 ">发布者人数</p>
            <el-button type="text" class="big-num" @click="checkUser('requester')">{{reqLength}}</el-button>
          </el-col>
          <el-col :span="8" style="text-align: center">
            <p style="color: #666666 ">工作者人数</p>
            <el-button type="text" class="big-num" @click="checkUser('worker')">{{workerLength}}</el-button>
          </el-col>
          <el-col :span="8" style="text-align: center">
            <p style="color: #666666 ">总用户数</p>
            <el-button type="text" class="big-num" @click="checkUser('allUsers')">{{allUserLength}}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>
</template>

<script>
    export default {
        name: "all-user-info-card",

      data(){
          return{
            reqLength:0,
            workerLength:0,
            allUserLength:0
          }
      },

      created(){
        this.setReqListLength()
        this.setWorkerListLength()
      },

      methods:{

        /**
         * 得到发起者总数
         */
        setReqListLength:function () {
          this.$api.get('/admin/requesters',null,r => {
            this.reqLength=r.data.length
            this.allUserLength+=r.data.length
          })
        },

        /**
         * 得到工作者总数
         */
        setWorkerListLength:function () {
          this.$api.get('/admin/workers',null,r => {
            this.workerLength=r.data.length
            this.allUserLength+=r.data.length
          })
        },


        /**
         * 查看特定用户信息
         * @param user
         */
        checkUser:function (user) {
          sessionStorage.setItem("user",user)
          this.$router.push('/userManagePage')
        }
      }
    }
</script>

<style scoped>
  .box-card{
    margin: 50px;
  }

  .big-num{
    font-size: 35px;
    color: black;
  }
</style>
