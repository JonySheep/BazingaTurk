<template>
  <div>
    <div class="headerNavi">
      <el-row style="padding: 5px;border-bottom: lightgrey 1px solid">
        <el-col :span="7"><p> </p></el-col>
        <el-col :span="2">
          <el-button round size="mini" class="taskButton" @click="showAllTasks">全部任务</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round size="mini" class="taskButton" @click="showRecommandTasks" v-if="userRole==='Worker'">为你推荐</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round size="mini" class="taskButton" @click="showHotTasks">热门任务TOP5</el-button>
        </el-col>
      </el-row>
    </div>
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
<div style="margin-top: 5px">
  <el-table
    :data=config
    style="width:100%"
    stripe
    height="700">
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
      type="index">
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
      prop="size"
      label="总任务大小"
      sortable
      width="130">
    </el-table-column>
    <el-table-column
      prop="partTimes"
      label="子任务个数"
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
      prop="createTime"
      label="创建时间"
      sortable
      :formatter="dateFormat"
      width="200">
    </el-table-column>
    <el-table-column width="36">
      <template slot-scope="scope">
        <a class="ali-icon-mima" v-show="qualified(scope.row.level)"></a>
      </template>
    </el-table-column>
    <el-table-column>
      <template slot-scope="scope">
        <el-button size="mini" @click="handleDetail(scope.row.taskId)"
                   :disabled="qualified(scope.row.level)"
                   type="danger">详情</el-button>
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
      name: "task-lobby-list",
      data:function(){
          var config=[];
          return{
            config,
            searchInfo:'',
            task:{},
            userRole:'',
          }
      },
      created(){
        this.userRole=sessionStorage.getItem('role')
          this.getConfiguration();
      },
      methods:{

          //TODO 设置权限
        qualified:function(level){
            return false;
        },
        handleDetail:function(taskid){
          this.$api.get('/task/'+taskid+'/view',null)
          this.$router.push('/taskLobby/'+taskid);
        },
        getConfiguration:function(){
          //TODO 新增的属性
          this.$api.get('/lobby/',null,r=>{
            //this.task = r.data.taskList
            console.log(r.data)
            this.config=r.data
          })

        },
        dateFormat:function(row,column,cellValue,index){
          return cellValue
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

        /**
         * 点击按钮展示全部任务
         */
        showAllTasks:function () {
          this.getConfiguration();
        },

        /**
         * 点击按钮展示推荐的任务
         */
        showRecommandTasks:function () {
          this.$api.get('/recommend/content/'+sessionStorage.getItem('userID'),null,r=>{
            if(r.data.length<=10){
              this.config=r.data
            }
            else{
              //取前10位
              let tempList=[]
              for(let i=0;i<10;i++){
                tempList.push(r.data[i])
              }
              this.config=tempList
            }

          })
        },

        /**
         * 点击按钮展示最热任务
         */
        showHotTasks:function () {
          this.$api.get('/recommend/heat',null,r=>{
            if(r.data.length<=5){
              this.config=r.data
            }
            else{
              //取前5位
              let tempList=[]
              for(let i=0;i<5;i++){
                tempList.push(r.data[i])
              }
              this.config=tempList
            }

          })
        }
      }
    }
</script>

<style>
  .table-expand {
    font-size: 0;
  }
  .table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .headerNavi{

    height: 40px;
  }
  .taskButton{
    color: dimgray;
    font-size: 10px;
    padding: 0px;
  }
</style>
