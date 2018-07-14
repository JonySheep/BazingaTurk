<template>
  <el-card class="box-card">

    <el-radio-group v-model="state" size="small" @change="change">
      <el-radio-button label="进行中" ></el-radio-button>
      <el-radio-button label="已完成"  ></el-radio-button>
    </el-radio-group>

    <ul style="border: lightgrey 1px solid;margin: 20px">
      <li v-for="task in taskList" style="display: block">
        <el-row style="height: 50px">
          <el-col :span="6">
            <div style="margin-top: 15px;font-size: 13px;color: #969896;margin-bottom: 5px" v-if="role==='Worker'">{{task.requesterName}}</div>
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px" v-if="role==='Requester'">{{task.createTime}}</div>
          </el-col>
          <el-col :span="5">
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px">{{task.taskName}}</div>
          </el-col>
          <el-col :span="5">
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px">{{task.taskType}}</div>
          </el-col>
          <el-col :span="4">
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px" v-if="role==='Worker'">{{task.progress}}/{{task.size}}</div>
            <div style="margin-top: 15px;font-size: 13px;margin-bottom: 5px" v-if="role==='Requester'">{{task.assignmentReward}}¥</div>


          </el-col>
          <el-col :span="3">
            <el-button plain type="danger" size="mini"
                       v-if="role==='Worker' && state==='进行中'"
                       @click="beginTask(task.taskType,task.taskId,task.assignmentId,userId)"
                       style="margin-top: 10px">继续</el-button>

            <el-button plain type="success" size="mini"
                       v-if="role==='Worker' && state==='已完成'"
                       @click="openTask(task.taskType,task.taskId,task.assignmentId,userId)"
                       style="margin-top: 10px">查看</el-button>
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
      name: "user-projext",
      data(){
        return{
          role:'',
          userId:'',
          taskList:[],
          state:'进行中',
        }
      },

      created(){
        this.role=sessionStorage.getItem('role')
        this.userId=sessionStorage.getItem('userID')
        if(this.state==='进行中'){
          this.getOngoingTaskList()
        }
        else{
          this.getFinishedTaskList()
        }
      },

      methods:{
        /**
         * 得到进行中任务列表
         */
        getOngoingTaskList:function () {
          let _this=this
          this.$api.get('/'+this.role.toLowerCase()+'/'+sessionStorage.getItem('userID')+'/ongoing',null,r=>{
            console.log(r.data)
            _this.taskList=r.data
          })
        },

        /**
         * 得到已完成用户列表
         */
        getFinishedTaskList:function () {
          let _this=this
          this.$api.get('/'+this.role.toLowerCase()+'/'+sessionStorage.getItem('userID')+'/finished',null,r=>{
            _this.taskList=r.data
          })
        },


        /**
         * 选择进行中/已完成
         */
        change:function () {
          if(this.state==='进行中'){
            this.getOngoingTaskList()
          }
          else{
            this.getFinishedTaskList()
          }
        },


        /**
         * 点击进行继续标注
         * @param taskType
         * @param taskId
         * @param assignmentId
         * @param workerId
         */
        beginTask:function (taskType,taskId,assignmentId,workerId) {
          if (taskType === "CLASSIFICATION") {
            this.$router.push('/partClassification/' + 'doTask' + '/' +taskId + '/' + assignmentId + '/' + workerId)
          }
          if (taskType === "DETECTION") {
            this.$router.push('/detection/' + 'doTask' + '/' + taskId + '/' + assignmentId + '/' + workerId)
          }
          if (taskType === "SEGMENTATION") {
            this.$router.push('/segment/'  + 'doTask' + '/' + taskId + '/' + assignmentId + '/' + workerId)
          }
        },


        /**
         * 点击查看已完成任务
         * @param taskType
         * @param taskId
         * @param assignmentId
         * @param workerId
         */
        openTask:function (taskType,taskId,assignmentId,workerId) {
          if(taskType==="CLASSIFICATION"){
            this.$router.push('/partClassificationDoneCheck/'+taskId+'/'+assignmentId+'/'+workerId)
          }
          if(taskType==="SEGMENTATION"){
            this.$router.push('/segmentCheck/'+taskId+'/'+assignmentId+'/'+workerId)
          }
          if(taskType==="DETECTION"){
            this.$router.push('/detectionDoneCheck/'+taskId+'/'+assignmentId+'/'+workerId)
          }
        },
      }
    }
</script>

<style scoped>

  .line{
    margin-right: 40px;
    border-bottom: lightgray 1px solid;
    margin-bottom: 5px;
  }

  .box-card{
    margin-top: 50px;
    margin-left: 250px;
    margin-right: 250px;
  }

</style>
