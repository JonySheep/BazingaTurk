<template>
  <div>
    <manage_header></manage_header>

      <div style="height: 5px"> </div>

      <el-row>
        <el-col :span="4">
          <el-menu
            style="height:800px">
            <template v-for="item in partitionList">
            <el-menu-item @click="showPartition(item)" :index="no(item.no)">
              <i class="el-icon-menu"></i>
              <span slot="title">分组{{item.no}}</span>
            </el-menu-item>
            </template>
          </el-menu>
        </el-col>
        <el-col :span="20" style="padding-left: 10px;padding-right: 10px;padding-top: 10px;text-align: center">
          <el-table :data="assignmentList" stripe style="width: 100%;text-align: left">
            <el-table-column prop="userName" label="参与工人" width="160"></el-table-column>
            <el-table-column prop="low" label="开始图片序号" width="110"></el-table-column>
            <el-table-column prop="high" label="结束图片序号" width="110"></el-table-column>
            <el-table-column prop="size" label="任务图片张数" width="110"></el-table-column>
            <el-table-column prop="progress" label="进度" width="100"></el-table-column>
            <el-table-column prop="deadline" label="截止时间" width="200"></el-table-column>
            <el-table-column prop="status" label="任务状态" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status==='ONGOING'" type="info" size="medium">进行中</el-tag>
                <el-tag v-if="scope.row.status==='CHECKING'" type="warning" size="medium">待审核</el-tag>
                <el-tag v-if="scope.row.status==='APPROVED'" type="success" size="medium">已通过</el-tag>
                <el-tag v-if="scope.row.status==='REJECTED'" type="danger" size="medium">已拒绝</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="审核操作" width="160">
              <template slot-scope="scope">
                <el-button v-if="scope.row.status==='CHECKING'"
                           @click="reject(scope.row.assignmentId,scope.row.index)"
                           plain type="danger" size="mini">拒绝</el-button>
                <el-button v-if="scope.row.status==='CHECKING'"
                           @click="approve(scope.row.assignmentId,scope.row.index)"
                           plain type="success" size="mini">通过</el-button>
              </template>
            </el-table-column>

            <el-table-column prop="accuracy" label="准确率" width="100"></el-table-column>

          </el-table>

          <el-button type="success" plain
                     v-if="assignmentList.length===assignTimes"
                     @click="integrate"
                     style="margin-top: 10px" >答案整合</el-button>

        </el-col>
      </el-row>

  </div>
</template>

<script>
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import ElHeader from "element-ui/packages/header/src/main";
  import manage_header from "../../components/header/taskPageHeader"
    export default {
      name: "task-partition-list",
      components:{
        ElContainer,ElMain,ElHeader,manage_header
      },
      data(){
        return{
          taskId:'',
          taskType: '',
          partitionList:[],
          assignmentList:[],
          partitionId:'',
          assignTimes:0,
        }
      },
      created(){
        this.taskId=this.$route.params.taskId;
        this.getPartitionList(this.taskId);
      },
      methods:{
        no:function (number) {
          return number+''
        },

        /**
         * 获得分组列表
         * @param taskId
         */
        getPartitionList: function (taskId) {
          this.$api.get('task/'+taskId+'/partitions',null,r=>{
            this.$api.get('task/'+taskId+'/brief',null, res =>{
              this.partitionList=r.data;
              this.assignTimes=res.data.assignTimes
              this.taskType = res.data.taskType
            })
          })

        },

        /**
         * 展示提交的任务
         * @param partition
         */
        showPartition:function(partition){
          this.assignmentList=[];
          for(let i=0; i<partition.assignmentVOs.length; i++){
            let temp = Object.assign({}, partition.assignmentVOs[i])
            this.$api.get('user/'+temp.workerId,null,r=>{
              temp.low = partition.low
              temp.high = partition.high
              temp.userName = r.data.username
              temp.accuracy = 0
              this.assignmentList.push(temp)
              this.partitionId = partition.partitionId
              console.log(this.assignmentList)
            })
          }
        },


        /**
         * 审核通过
         */
        approve:function (assignmentId) {
          this.$api.get('/assignment/'+assignmentId+'/approve',null,r=>{
            this.$message({
              type: 'success',
              message: '通过成功'
            });

          })
        },


        /**
         * 审核拒绝
         */
        reject:function (assignmentId,index) {

          this.$api.get('/assignment/'+assignmentId+'/reject',null,r=>{
            this.$message({
              type: 'success',
              message: '拒绝成功'
            });

          })
        },

        integrate: function () {
          let _this=this



          if(this.partitionId === ''){
            this.$message({
              type: 'danger',
              message: '请先选择分组！'
            });
          }
          else {
            this.$api.get('task/' + this.taskId + '/' + this.partitionId + '/' + this.taskType + '/integration', null, r => {
              if (r.data === "SUCCESS") {
                this.$message({
                  type: 'success',
                  message: '整合成功'
                });

                _this.setAccuracy()
              }
            })
          }
        },



        setAccuracy:function () {
          for(let i=0;i<this.assignmentList.length;i++){
            this.$api.get('/assignment/'+this.assignmentList[i].assignmentId,null,r=>{
              this.assignmentList[i].accuracy=parseFloat(Number(r.data.accuracy).toFixed(2))
            })
          }

        }
      }
    }
</script>

<style>

</style>
