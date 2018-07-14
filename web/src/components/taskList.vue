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
  <div>
    <el-table
      :data="tasks"
      stripe
      style="width: 100%">
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
        width="100">
      </el-table-column>
      <el-table-column
        prop="size"
        label="任务大小"
        sortable
        width="100">
      </el-table-column>
      <el-table-column
        prop="assignmentReward"
        label="积分奖励"
        sortable
        width="100">
      </el-table-column>
      <el-table-column
        prop="deadline"
        label="截止时间"
        sortable
        :formatter="dateFormat"
        width="190">
      </el-table-column>
      <el-table-column width="200">
        <template slot-scope="scope">
          <el-progress :percentage=calPercentage(scope.row.progress,scope.row.size)></el-progress>
        </template>
      </el-table-column>
      <el-table-column width="77">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="openTask(scope.row.taskType,scope.row.taskId,scope.row.assignmentId,scope.row.workerId,scope.row.progress)">
            查看</el-button>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="beginTask(scope.row.taskType,scope.row.taskId,scope.row.assignmentId,scope.row.workerId)">
            开始</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  </div>
</template>

<script>
  import ElButton from "element-ui/packages/button/src/button";

  export default {
    components: {ElButton},
    data() {
      return {
        tasks:[],
        searchInfo:'',
      }
    },
    created(){
      this.getConfiguration();
    },

    methods: {
      calPercentage: function (progress, size) {
        let res = ((progress/size)*100).toFixed(3);
        return res
      },

      getConfiguration:function(){
        let userId=sessionStorage.getItem("userID");
        this.$api.get('worker/'+userId+'/ongoing',null,r=>{
          this.tasks = r.data
          console.log(r.data);
        });
      },

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

      openTask:function (taskType,taskId,assignmentId,workerId,progress) {
        if(progress===0){
          this.$message({
            message:"你还没有开始过该任务哦！",
            type: 'warning'
          })
        }else {
          if (taskType === "CLASSIFICATION") {
            this.$router.push('/partClassification/' + 'checkTask' + '/'+ taskId + '/' + assignmentId + '/' + workerId)
          }
          if (taskType === "SEGMENTATION") {
            this.$router.push('/segment/' + 'checkTask' + '/' + taskId + '/' + assignmentId + '/' + workerId)
          }
          if (taskType === "DETECTION") {
            this.$router.push('/detection/' + 'checkTask' + '/'+ taskId + '/' + assignmentId + '/' + workerId)
          }
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
