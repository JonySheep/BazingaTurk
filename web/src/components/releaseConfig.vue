<template>
  <div>
    <div>
      <div style="height: 5px"></div>
      <el-row>
        <el-col :span="4">
          <el-menu
            style="height: 800px"
            default-active="1"
            class="el-menu-vertical-demo">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-setting"></i>
                <span>任务</span>
              </template>
              <el-menu-item index="1-1" @click="setTaskInfo">任务信息</el-menu-item>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-info"></i>
                <span>指引</span>
              </template>
              <el-menu-item index="2-1" @click="setTaskGuide">任务指引</el-menu-item>
              <el-menu-item index="2-2" @click="setTaskRequest">工作者要求</el-menu-item>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-tickets"></i>
                <span>规则</span>
              </template>
              <el-menu-item index="3-1" @click="setTaskAward">奖励规则</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>
        <el-col :span="20">
          <task-info-setting v-if="routeTo==='taskInfo'"></task-info-setting>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import taskInfoSetting from "../components/taskInfoSetting"
  import ElRow from "element-ui/packages/row/src/row";
  import ElContainer from "element-ui/packages/container/src/main";
    export default {
      name: "release-config",
      components:{
        ElContainer,
        ElRow,
        taskInfoSetting
      },
      data(){
        return{
          routeTo:''
        }
      },
      methods:{
        setTaskInfo:function(){
          this.routeTo='taskInfo';
        },
        setTaskGuide:function(){
          this.routeTo='taskGuide';
        },
        setTaskRequest:function(){
          this.routeTo='taskRequest';
        },
        setTaskAward:function(){
          this.routeTo='taskAward';
        },
      }
    }
</script>

<style>

</style>
