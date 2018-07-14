<template>
  <el-container>
    <el-header>
      <task-header v-bind:taskName="taskName"></task-header>
    </el-header>
    <el-main>
      <el-row :gutter="15">
        <el-col :span="16">
          <div id="img-panel">
            <div id="img-panel-background" v-bind:width=w v-bind:height=h
                 v-bind:style="{width:w,height:h}"
                 style="position: absolute">
              <div id="img-area-self" v-bind:style="{width:w,height:h,backgroundImage:'url('+pic+')'}"
                   style="background:no-repeat center center;background-size:contain;position: absolute;z-index: 1">
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="tags">
            <div style="float:right">
              <div v-for="item in attrCopy">
            <span>
              <p style="color:coral;font-family: Arial">{{item.name}}</p>
            <el-select v-model=item.res placeholder="请选择" >
              <el-option v-for="i in item.tags"
                         :key="i.name"
                         :label="i.name"
                         :value="i.name">
              </el-option>
            </el-select>
              </span>
              </div>
            </div>
          </div>
        </el-col>

      </el-row>

      <el-row class="foot">
        <el-col :span="24">
          <el-button id="nextButton" type="danger" height="20" round v-on:click="nextData">下一张</el-button>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import TaskHeader from "../../components/header/annoPageHeader"
    export default {
      components: {
        ElMain,
        ElContainer,
        "task-header":TaskHeader
      },
        name: "whole-classification",
      data:function(){
        var pic='';
        var w=0;
        var h=0;
        var attrCopy={};
        var id='';
        var count=0;
        var size=0;
        return{
          pic,w,h,attrCopy,id,count,size
        }
      },
      created() {
        this.id=this.$route.params.id;
        this.getTaskName(this.id);
        this.getTaskTags(this.id);
        this.getFirstImage(this.id);
      },

      methods:{
        getTaskName(taskid){

        },
        getTaskTags(taskid){

        },
        getNextData(){

        },
        getFirstImage(taskid){

        },
        postData(){

        }
      }

    }
</script>

<style>
  #img-panel{
    position: relative;
    width: 100%;
    height:650px;

    border: lightgray 2px solid;
    overflow: auto;
  }
  .tags{
    background-color: transparent;
    min-height:650px;
    width:25%;
    overflow: scroll;
    position: absolute;
  }
</style>
