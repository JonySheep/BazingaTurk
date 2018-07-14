<template>
  <div>
    <div style="margin-top: 15px;">
      <el-input
        size="small"
        placeholder="请输入内容"
        suffix-icon="el-icon-search"
        v-model="searchInfo"
        style="padding-left: 50px;width:85%"></el-input>
      <el-button type="text" style="border: none">
        <a class="ali-icon2-filter" style="margin-left: 10px;color: grey">筛选</a>
      </el-button>

    </div>

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
          prop="size"
          label="任务总数"
          sortable
          width="160">
        </el-table-column>
        <el-table-column
          prop="partTimes"
          label="任务分组数"
          sortable
          width="160">
        </el-table-column>
        <el-table-column
          prop="deadline"
          label="发布时间"
          sortable
          :formatter="dateFormat"
          width="200">
        </el-table-column>
        <el-table-column
          label="需要的总花费"
          width="160"
          sortable>
          <template slot-scope="scope">
            {{total(scope.row.assignmentReward,scope.row.partTimes,scope.row.assignTimes)}}
          </template>
        </el-table-column>
        <el-table-column
          width="180">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openTask(scope.row.taskId)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
    export default {
      name: "requester-task-done",
      data(){
        return{
          tasks:[],
          searchInfo:''
        }
      },
      computed:{
        total:function (assignmentReward,partTimes,assignTimes) {
          return assignmentReward*partTimes*assignTimes;
        }
      },
      created(){
        this.getConfiguration()
      },
      methods:{
        getConfiguration:function(){
          let userId=sessionStorage.getItem("userID");
          this.$api.get('/requester/'+userId+'/finished',null,r=>{
            console.log(r.data);
            this.tasks=r.data;
          })
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
        },
        dateFormat: function (row,column,cellValue,index) {
          //TODO 确定时间格式 #zora
          return cellValue;
        },
        openTask:function (taskId) {
          //TODO 与进行中的查看界面不同 #zora
          this.$router.push('/taskPatition/'+taskId);
        }
      }
    }
</script>

<style scoped>

</style>
