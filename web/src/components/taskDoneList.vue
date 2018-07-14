<template>
    <div>
    <!--<div style="margin-top: 15px;">-->
      <!--<el-input-->
        <!--size="small"-->
        <!--placeholder="请输入内容"-->
        <!--suffix-icon="el-icon-search"-->
        <!--v-model="searchInfo"-->
        <!--style="padding-left: 50px;width:85%"></el-input>-->
      <!--<el-button type="text" style="border: none">-->
        <!--<a class="ali-icon2-filter" style="margin-left: 10px;color: grey">筛选</a>-->
      <!--</el-button>-->
    <!--</div>-->
    <div style="width: 100%">
      <el-table
        :data="tasks"
        stripe
        style="width:100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="任务描述">
                <span>{{props.row.description}}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="requesterName"
          label="任务发起者"
          sortable
          width="180">
        </el-table-column>
        <el-table-column
          prop="taskName"
          label="任务名称"
          sortable
          width="180">
        </el-table-column>
        <el-table-column
          prop="taskType"
          :formatter="taskTypeFormat"
          label="任务类型"
          sortable
          width="160">
        </el-table-column>
        <el-table-column
          prop="partitionNo"
          label="分组序号"
          sortable
          width="130">
        </el-table-column>
        <el-table-column
          prop="size"
          label="任务大小"
          sortable
          width="130">
        </el-table-column>
        <el-table-column
          prop="assignmentReward"
          label="积分奖励"
          sortable
          width="130">
        </el-table-column>
        <el-table-column
          prop="deadline"
          label="截止时间"
          sortable
          :formatter="dateFormat"
          width="200">
        </el-table-column>
        <el-table-column
          prop="status"
          label="评估结果"
          width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status==='APPROVED'" type="success" disable-transitions size="medium">已通过</el-tag>
            <el-tag v-if="scope.row.status==='REJECTED'" type="danger" disable-transitions size="medium"> 已拒绝</el-tag>
            <el-tag v-if="scope.row.status==='CHECKING'" type="warning" disable-transitions size="medium">审核中</el-tag>
          </template>
        </el-table-column>

        <el-table-column width="177">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openTask(scope.row.taskType,scope.row.taskId,scope.row.assignmentId,scope.row.workerId)">查看</el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    </div>
</template>

<script>
    export default {
      name: "task-done",
      data(){
        return{
          tasks:[],
          searchInfo:'',
          task:{}
        }
      },
      created(){
        this.getConfiguration();
        console.log(this.tasks)
      },
      methods:{
        getConfiguration:function(){
          let userId=sessionStorage.getItem("userID");
          this.$api.get('worker/'+userId+'/finished',null,r=>{
            this.tasks = r.data
          });
        },

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

        dateFormat:function(row,column,cellValue,index){
          return cellValue;
        },

        taskTypeFormat:function(row,column,cellValue,index){
          switch(cellValue){
            case "CLASSIFICATION":
              return "局部分类标注任务";
            case "DETECTION":
              return "检测任务";
            case "SEGMENTATION":
              return "区域划分任务";
          }
        }
      }
    }
</script>

<style>
  .line{
    border-bottom: dimgray 1px solid;
  }
</style>
