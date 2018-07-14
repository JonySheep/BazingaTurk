<template>
  <div>

    <el-container style="padding-top: 0px" >
      <el-aside width="13%" style="border-right: lightgrey 1px solid;overflow: scroll;padding: 0px">

        <!--segment栏-->
        <div>
          <ul style="padding: 0px;margin: 0px">
            <li v-for="(seg,index) in segList"
                v-on:mousedown="highlightSeg(index)"
                style="display: block;margin:0px;float: left">

              <el-row style="border-top: lightgrey 1px solid;border-bottom: lightgrey 1px solid;width: 180px;">

                <el-row>
                  <el-col :span="3">
                    <i class="el-icon-star-off" :id="'starIcon'+index" style="margin-left: 5px" ></i>
                  </el-col>
                  <el-col :span="3">
                    <i class="el-icon-view" :id="'showIcon'+index" style="margin-left: 5px;cursor: pointer" v-on:click="hideSeg(index)"></i>
                  </el-col>
                </el-row>

                <div  style="text-align: center;margin: 0px" >
                  <p style="font-size: 13px;margin: 5px" :style="{color:seg.color}">{{seg.tagName}}</p>
                  <p style="font-size: 5px;color: #969896;margin-bottom: 5px">{{seg.description}}</p>
                </div>
              </el-row>
            </li>
          </ul>
        </div>
      </el-aside>

      <el-container style="margin-left:20px;margin-right: 20px ">

        <el-header style="vertical-align: middle;height: 50px;padding-left: 30px;padding-right: 30px;padding-top: 5px;">
          <el-col :span="8" style="text-align: left">
            <el-button type="text"
                       @click="lastAnnoImage"
                       style="height: 30px;border:none;color: #101010">
              <a class="el-icon-arrow-left" style="font-size: 14px;font-family: Roboto">上一张</a>
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: center">
            <el-button type="text" style="height:30px;">
              {{counter}}/{{taskSize}}
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button type="text"
                       @click="nextAnnoImage"
                       style="height: 30px;color: #101010;font-size: 14px;font-family: Roboto">
              下一张
              <i class="el-icon-arrow-right el-icon--right"></i>
            </el-button>
          </el-col>
        </el-header>


        <el-main style="padding-top: 0px">
          <!--canvas-->
          <!--<el-progress :text-inside="true" :stroke-width="18" :percentage="counter*100/taskSize" status="exception" style="margin-top: 10px"></el-progress>-->
          <div class="drawPanel" >
            <canvas
              id="canvas"
              class="CanvasPanel"
              :width=canvasWidth
              :height=canvasHeight
              :style="{opacity:config.opacity/100}"
            ></canvas>
            <img
              style="background:no-repeat center center;background-size:contain;position: absolute;z-index: 0"
              :src="'http://localhost:8080/'+taskData.imageUrl" />
          </div>
        </el-main>

      </el-container>

      <el-aside width="13%"  style="float: right;border-left: lightgrey 1px solid">
        <!--choose tag-->
        <ul class="buttonUL">
          <li v-for="t in tags" style="display: block;margin: 5px;float: left">
            <el-button width="10"
                       plain
                       icon="el-icon-edit"
                       size="mini"
                       style="color: dimgray;"
                       :style="{borderColor:t.colorHex}">{{t.name}}</el-button>
            <br/>
          </li>
        </ul>

        <div class="edit">

          <p style="color: #969896;font-size: 13px">调整透明度</p>
          <el-slider v-model="config.opacity" style="padding-left: 10px;padding-right: 10px"></el-slider>
        </div>

      </el-aside>
    </el-container>

  </div>
</template>

<script>

  import ElButton from "element-ui/packages/button/src/button";
  import ElInput from "element-ui/packages/input/src/input";
  import ElContainer from "element-ui/packages/container/src/main";

  export default {

    components: {
      ElContainer,
      ElInput,
      ElButton},
    data(){

      return{
        thisCanvas:{},
        context:{},
        isStart:true,
        taskData:{},

        canvasWidth:0,
        canvasHeight:0,

        config: {   //配置画笔属性
          lineWidth: 2,
          lineColor: "",
          opacity:70,
        },

        disable:false,  //是否可进行标注

        counter:1 ,//当前数据编号

        taskSize:0,

        isLast:false, //是否最后一张图片

        assign:{},  //当前assignmen

        segList:[], //segVO的列表

        isClick:false, //鼠标是否被按下 （是否已标记锚点）

        //当前锚点坐标
        currentOffsetX:0,
        currentOffsetY:0,

        //起始点锚点坐标
        startOffsetX:0,
        startOffsetY:0,

        currentTag:'', //当前选中的标签
        currentPointerList:[], //当前对象的坐标列表
        description:"", //当前对象的描述

        isFinishSeg:false, //是否结束一个区域的标注（图形已闭合）

        isEditMode:false, //是否处于可编辑模式
        isChosen:false, //是否选中进行编辑的锚点
        chosenSeg:0,  //记录选中的点的位置
        chosenPointer:0,

        ishighLight:-1, //是否有高亮元素，没有为-1，有则为index

        isLast:false, //是否是查看的最后一张图片
      }
    },

    props:['name','tags','taskId','assignId','workerId','state'],

    created(){

      this.isEdit=this.$route.params.isEdit
      if(this.isEdit===1){
        this.editImgID=this.$route.params.dataID
        this.editMode(this.editImgID)
      }

    },

    mounted(){

      this.thisCanvas=document.getElementById("canvas");
      this.context=this.thisCanvas.getContext("2d");

      this.initialize()
      this.setCanvasStyle()

      //设置当前页面标题
      document.title=this.name+" | Bazinga Turk"

    },


    methods:{

      /**
       * 设置当前作业
       */
      initialize:function(){
        let _this=this
        this.$api.get('/assignment/'+this.assignId,null,r=>{
          _this.taskSize=r.data.size
        })

        this.getImage()
      },

      /**
       * 设置绘画配置
       */
      setCanvasStyle:function() {
        this.context.strokeStyle = this.config.lineColor
        this.context.fillStyle=this.config.lineColor
      },


      /**
       * 点击下一张图片的响应方法
       */
      nextAnnoImage:function(){

        if(this.counter===this.taskSize){
          //confirm
          this.$message.warning('已是最后一张图片')
          return
        }

        this.counter+=1
        this.getImage()
      },


      /**
       * 点击上一张图片的响应方法
       */
      lastAnnoImage:function () {

        if(this.counter===1){
          this.$message.warning('已是第一张图片了哟')
          return
        }

        this.counter-=1

        this.getImage()
      },


      /**
       * 清空画布
       */
      clearCanvas:function () {
        this.context.clearRect(0,0,this.canvasWidth,this.canvasHeight)
      },


      /**
       * 通过api传输得到对应索引的图片
       */
      getImage:function(){

        //initial!
        this.isFinishSeg=false
        this.isChosen=false
        this.ishighLight=-1
        this.isStart=true

        let imgID=this.counter

        let _this=this
        this.$api.get('/assignment/'+this.assignId+'/'+imgID,null, r => {

          console.log('/assignment/'+this.assignId+'/'+imgID)
          console.log(r.data)

          _this.taskData=r.data

          let img=new Image()
          img.src='http://localhost:8080'+_this.taskData.imageUrl

          img.onload=function () {
            _this.canvasWidth=img.naturalWidth
            _this.canvasHeight=img.naturalHeight

            //将左边的标签栏装上当前标签
            _this.segList=r.data.segList
          }

          setTimeout(function(){

            _this.redraw(r.data.segList)

          },100)

        })

      },




      /**
       * 重新绘制canvas
       */
      redraw:function(segList){
        this.clearCanvas()

        if(segList.length===0){
          return
        }

        for(let i=segList.length-1;i>=0;i--){

          //若不可见，则跳过
          if(!segList[i].isShow){
            continue
          }

          let pointers=segList[i].pointerList
          this.context.strokeStyle=segList[i].color

          let j=0
          for(j=0;j<pointers.length;j++){

            //画圆点
            this.context.beginPath()
            this.context.arc(pointers[j].x,pointers[j].y,3,0,360,false)

            this.context.fillStyle="#ffffff"
            this.context.fill()
            this.context.stroke()

            if(j!==0){
              //画线
              this.context.beginPath()
              this.context.moveTo(pointers[j-1].x,pointers[j-1].y)
              this.context.lineTo(pointers[j].x,pointers[j].y)
              this.context.stroke()
              this.context.closePath()
            }

          }

          //将最后一个点与第一个点连接起来
          this.context.beginPath()
          this.context.moveTo(pointers[j-1].x,pointers[j-1].y)
          this.context.lineTo(pointers[0].x,pointers[0].y)
          this.context.stroke()
          this.context.closePath()

          this.fillColor(pointers,segList[i].color)
        }
      },


      /**
       *  给指定区域填充颜色
       */
      fillColor:function (list,color) {
        this.context.beginPath()

        for(let i=0;i<list.length;i++){
          this.context.lineTo(list[i].x,list[i].y)
        }

        this.context.closePath()
        this.context.fillStyle=color
        this.context.strokeStyle=color
        this.context.fill()
        this.context.stroke()
      },


      /**
       * 高亮指定的区域！（加粗）
       */
      highlightSeg:function (index) {

        let highlight=document.getElementById('starIcon'+index)

        //有高亮元素
        if(this.ishighLight!==-1){

          //在有元素高亮时，点击其他元素
          if(index!==this.ishighLight){
            //原来的变黑
            let lastStar=document.getElementById('starIcon'+this.ishighLight)
            lastStar.style.color="black"
          }
          else{
            highlight.style.color="black"
            this.redraw(this.segList)
            this.ishighLight=-1
            return
          }

        }


        highlight.style.color="#FF7F50"
        this.ishighLight=index
        this.redraw(this.segList)

        let pointers=this.segList[index].pointerList
        this.context.strokeStyle="#ffffff"

        //加上模糊特效
        this.context.shadowBlur=10
        this.context.shadowColor="#ffffff"

        let j=0
        for(;j<pointers.length;j++){
          //画圆点
          this.context.beginPath()
          this.context.arc(pointers[j].x,pointers[j].y,3,0,360,false)

          this.context.fillStyle="#ffffff"
          this.context.fill()
          this.context.stroke()

          if(j!==0){
            //画线
            this.context.beginPath()
            this.context.moveTo(pointers[j-1].x,pointers[j-1].y)
            this.context.lineTo(pointers[j].x,pointers[j].y)
            this.context.stroke()
            this.context.closePath()
          }
        }


        //将最后一个点与第一个点连接起来
        this.context.beginPath()
        this.context.moveTo(pointers[j-1].x,pointers[j-1].y)
        this.context.lineTo(pointers[0].x,pointers[0].y)
        this.context.stroke()
        this.context.closePath()

        //取消模糊特效
        this.context.shadowBlur=0
      },


      /**
       *隐藏指定位置的划分
       * @param index
       */
      hideSeg:function (index) {
        let isShow=this.segList[index].isShow
        let eyeIcon=document.getElementById('showIcon'+index)

        if(isShow){
          this.segList[index].isShow=0
          eyeIcon.style.color="lightgray"
        }
        else{
          this.segList[index].isShow=1
          eyeIcon.style.color="black"
        }

        this.redraw(this.segList)
      },

    },

  }
</script>

<style>

  .drawPanel{
    width: 100%;
    padding-bottom: 62.5%;
    position: relative;
    border: lightgray 1px solid;
    overflow: scroll;
  }

  #canvas{
    position: absolute;
    z-index: 1;
  }

  .nextButton{
    color: coral;
    margin-top: 10px;

  }

  #finishButton{
    color: coral;
    margin-top: 10px;
  }

  .edit{
    border-bottom: lightgrey 1px solid;
    text-align: left;
    padding: 10px;
    padding-bottom: 20px;
  }

  .buttonUL{
    height:350px;
    overflow: scroll;
    margin-top: 10px;
    border-bottom: darkgray 1px solid;
    margin-bottom: 0px;
  }

  .confirmButton{
    margin-top: 20px;
    width: 60px;
    height: 30px;
  }
</style>
