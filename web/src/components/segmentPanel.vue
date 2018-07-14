<template>
  <div>

    <el-container style="padding-top: 0px" >
      <el-aside width="13%" style="border-right: lightgrey 1px solid;overflow: scroll;padding: 0px">

        <!--segment栏-->
        <div>
          <ul style="padding: 0px;margin: 0px">
            <li v-for="(seg,index) in segList"
                v-dragging="{ item: seg, list: segList, group: 'seg' }"
                :key="index"
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
                  <el-col :span="15"><p> </p></el-col>
                  <el-col :span="3">
                    <i class="el-icon-circle-close-outline"  style="cursor: pointer" v-on:click="deleteSeg(index)"></i>
                  </el-col>
                </el-row>

                <div  style="text-align: center;margin: 0px" >
                  <p style="font-size: 13px;margin: 5px" :style="{color:seg.color}">{{seg.tagName}}</p>
                  <el-row style="margin-bottom: 5px">
                    <el-col :span="3">
                      <p> </p>
                    </el-col>
                    <el-col :span="18">
                      <p style="font-size: 5px;color: #969896;margin: 0px">{{seg.description}}</p>
                    </el-col>
                    <el-col :span="3" style="float: right">
                      <el-tooltip content="添加描述" placement="top" effect="light" >
                        <el-popover
                          placement="right"
                          trigger="click">
                          <el-input
                            style="font-size: 13px;width: 100%;margin: auto"
                            type="textarea"
                            :rows="4"
                            :placeholder=seg.description
                            v-model="description">
                          </el-input>
                          <el-button type="success" plain size="small" class="confirmButton" v-on:click="confirmDescript(index)">确认</el-button>
                          <i class="el-icon-edit-outline" slot="reference" style="cursor: pointer;float: right;margin-right: 5px" ></i>
                        </el-popover>
                      </el-tooltip>
                    </el-col>
                  </el-row>
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
          <div v-on:mousedown="beginPath"
               class="drawPanel" >
            <canvas
              id="canvas"
              class="CanvasPanel"
              :width=canvasWidth
              :height=canvasHeight
              :style="{opacity:config.opacity/100}"
              v-on:mousedown="canvasDown"
              v-on:mousemove="canvasMove"
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
                       v-on:click="setTag(t.colorHex,t.name)"
                       style="color: dimgray;"
                       :style="{borderColor:t.colorHex}">{{t.name}}</el-button>
            <br/>
          </li>
        </ul>

        <div class="edit">
          <p style="color: #969896;font-size: 13px">切换至编辑模式</p>
          <el-switch
            style="display: block"
            v-model="isEditMode"
            change="toEditMode"
            active-color="#FF7F50"
            inactive-color="lightgray">
          </el-switch>

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
            maxCounter:1,

            currentProgress:0,
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

        //拖拽后的响应方法
        this.$dragging.$on('dragend', () => {
          this.redraw(this.segList)

          //所有的star都变黑
          for(let i=0;i<this.segList.length;i++){
            let star=document.getElementById('starIcon'+i)
            star.style.color='black'
          }
          this.ishighLight=-1
        })
      },

      destroyed(){
        this.saveCanvas()
      },


      methods:{

        /**
         * 设置当前作业
         */
        initialize:function(){
          let _this=this
          this.$api.get('/assignment/'+this.assignId,null,r=>{

            //若是开始任务，从progress开始
            if(this.state==='doTask'){
              _this.taskSize=r.data.size

              if(r.data.progress===0){
                this.$api.get('/assignment/'+this.assignId+'/begin',null)
                this.$api.get('/assignment/'+this.assignId+'/progress',null)
                _this.counter=1
              }
              else{
                _this.counter=r.data.progress
              }

              this.getImage()
            }
            //若是查看任务，从第一张开始
            else{
              _this.taskSize=r.data.progress
              _this.counter=1
              this.getImage()
            }

          })

        },

        /**
         * 设置绘画配置
         */
        setCanvasStyle:function() {
          if(this.config.lineColor===""){
            this.$message({
              message: '请先选择标签再进行标注哦∠( ᐛ 」∠)＿',
              type: 'warning'
            });
          }
          else{
            this.context.strokeStyle = this.config.lineColor
            this.context.fillStyle=this.config.lineColor
          }
        },


        /**
         * 鼠标移动的响应方法
         */
        canvasMove:function (event) {
          this.thisCanvas.style.cursor='crosshair'

          // offset是canvas距离顶部以及左边的距离
          const canvasX = event.offsetX - event.target.offsetLeft
          const canvasY = event.offsetY - event.target.offsetTop

          if(this.isEditMode){
            this.findPointer(canvasX,canvasY)
          }
          else{
            if(!this.isStart){
              this.findStartPointer(canvasX,canvasY)
            }
          }
        },


        /**
         * mouse down
         * @param event actionEvent
         */
        canvasDown:function (event) {

          this.context.lineWidth=2

          if(this.disable){
            return
          }

          if(this.config.lineColor===""){
            this.$message({
              message: '请先选择标签!',
              type: 'error'
            });
            return
          }

          this.canvasMoveUse = true

          // offset是canvas距离顶部以及左边的距离
          const canvasX = event.offsetX - event.target.offsetLeft
          const canvasY = event.offsetY - event.target.offsetTop

          //如果是编辑模式
          if(this.isEditMode){
            this.thisCanvas.style.cursor='pointer'

            //未有选中的点时
            if(!this.isChosen){
              let i=0
              for(;i<this.segList.length;i++){
                let pointers=this.segList[i].pointerList
                for(let j=0;j<pointers.length;j++){
                  //若找到了对应的点
                  if(canvasX>=pointers[j].x-2 && canvasX<=pointers[j].x+2
                    && canvasY>=pointers[j].y-2 && canvasY<=pointers[j].y+2){

                    this.chosenSeg=i
                    this.chosenPointer=j

                    //将该点高亮
                    this.context.beginPath()
                    this.context.arc(canvasX,canvasY,3,0,360,false)

                    this.context.strokeStyle="#ffffff"
                    this.context.stroke()

                    this.isChosen=true
                    break
                  }
                }
              }
            }
            //已有选中点，再点击界面，将其重画
            else{
              //重新赋值
              this.segList[this.chosenSeg].pointerList[this.chosenPointer].x=canvasX
              this.segList[this.chosenSeg].pointerList[this.chosenPointer].y=canvasY

              this.thisCanvas.style.cursor='crosshair'

              //重画
              this.redraw(this.segList)
              this.isChosen=false
            }

            return
          }

          //鼠标点击则在当前位置显示锚点，并记录当前坐标
          this.context.beginPath()
          this.context.arc(canvasX,canvasY,3,0,360,false)

          this.context.fillStyle="#ffffff"
          this.context.fill()
          this.context.strokeStyle=this.config.lineColor
          this.context.stroke()


          // 判断是否是起始点
          if(!this.isStart){

            //this.context.beginPath()
            this.context.moveTo(this.currentOffsetX,this.currentOffsetY);

            //判断是否闭合
            if(canvasX>=this.startOffsetX-2 && canvasX<=this.startOffsetX+2
            && canvasY>=this.startOffsetY-2 && canvasY<=this.startOffsetY+2){

              //若此图形闭合,则将其加入右侧列表

              this.context.lineTo(this.startOffsetX,this.startOffsetY)

              this.context.stroke()
              this.context.closePath()

              this.fillColor(this.currentPointerList,this.config.lineColor)
              this.confirmSeg()

              return
            }
            else{
              this.context.lineTo(canvasX,canvasY)
              this.context.stroke()
            }
          }
          else{
            this.startOffsetX=canvasX
            this.startOffsetY=canvasY
            this.isStart=false
          }

          this.currentOffsetX=canvasX
          this.currentOffsetY=canvasY

          //将当前的坐标加入列表
          let Pointer={}
          Pointer.x=canvasX
          Pointer.y=canvasY
          this.currentPointerList.push(Pointer)
        },


        /**
         *重新初始化一些全局变量
         */
        iniData:function () {
          //重新开始一次新的划分
          this.isStart=true
          this.isFinishSeg=false
          this.disable=false
          this.description=" "
          this.currentPointerList=[]
        },


        /**
         * 开始绘图的准备
         * @param event
         */
        beginPath:function (event) {
          const canvas = document.querySelector('#canvas')
          if (event.target !== canvas) {
            this.context.beginPath()
          }
        },


        // 当前选中的tag
        setTag:function (color,name) {
          this.config.lineColor = color
          this.currentTag=name
        },

        /**
         * 保存当前图层，并将数据传给服务器记录
         */
        saveCanvas:function () {

          this.drawSavingPic(this.segList)

          let canvasUrl=this.thisCanvas.toDataURL(this.counter+'/png',1.0)

          let canvasFile=this.convertBase64UrlToBlob(canvasUrl,'png')

          let tempList=[]
          for(let j=0;j<this.segList.length;j++){
            if(tempList.length===0){
              tempList.push(this.segList[j].color)
            }
            else{
              let findSame=false
              for(let k=0;k<tempList.length;k++){
                if(tempList[k]===this.segList[j].color){
                  findSame=true
                  break
                }
              }
              if(!findSame){
                tempList.push(this.segList[j].color)
              }
            }
          }
          //利用post向后端发送数据
          this.taskData.segList=this.segList
          this.taskData.colorSize=tempList.length
          this.postAnnoImage(canvasFile,this.counter,this.taskData)
        },



        /**
         * 将base64的url转换为blob的方法
         */
        convertBase64UrlToBlob:function(urlData,type){
          var bytes=window.atob(urlData.split(',')[1]);        //去掉url的头，并转换为byte
          //处理异常,将ascii码小于0的转换为大于0
          var ab = new ArrayBuffer(bytes.length);
          var ia = new Uint8Array(ab);
          for (var i = 0; i < bytes.length; i++) {
            ia[i] = bytes.charCodeAt(i);
          }
          return new Blob( [ab] , {type : (this.counter)+'.'+type});
        },


        /**
         * 点击下一张图片的响应方法
         */
        nextAnnoImage:function(){

          let _this=this

          if(this.state ==='checkTask'){

            this.$api.get('/assignment/'+this.assignId,null,r=>{
              //若在查看进行中任务，则只能查看至progress的！

              if(_this.counter===r.data.progress){
                this.$message.warning('你上次只做到这张哦～继续去标注吧！')
                this.isLast=true
              }
            })

            if(!this.isLast){
              this.saveCanvas()

              this.counter+=1
              this.getImage()
            }

          }

          else{
            if(this.counter===this.taskSize){
              //confirm
              this.$confirm('确认提交？提交后不可修改', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {

                _this.saveCanvas()
                //删除指定位置元素
                _this.submit()

                //确认删除
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });


              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消提交'
                });
              });

              return
            }

            if(this.counter>this.maxCounter){
              this.maxCounter=this.counter
              this.$api.get('/assignment/'+this.assignId+'/progress',null)
            }
            this.saveCanvas()

            this.counter+=1
            this.getImage()


          }


        },


        /**
         * 点击上一张图片的响应方法
         */
        lastAnnoImage:function () {

          if(this.counter===1){
            this.$message.warning('已是第一张图片了哟')
            return
          }

          this.saveCanvas()

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
          this.isEditMode=false
          this.isChosen=false
          this.ishighLight=-1
          this.isStart=true

          let imgID=this.counter

          let _this=this
          this.$api.get('/assignment/'+this.assignId+'/'+imgID,null, r => {

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
         * 通过post向后端传输数据
         * @param taskid
         * @param imgid
         */
        postAnnoImage:function (file,imgID,taskData) {
          let form =new FormData()

          taskData.layerUrl=''

          form.append("anno",JSON.stringify(taskData))

          form.append("layer",file)

          this.$api.post('/assignment/'+this.assignId+'/'+imgID,form)
        },


        /**
         * 编辑模式
         */
        editMode:function (imgID) {
          this.$api.get('tasks/'+this.taskID+'/segmentation/resources/'+imgID,null, r => {

            let image=new Image()
            image.src="http://localhost:8080/"+r.data.segLayerUrl

            this.context.drawImage(image,0,0,900,550)
            let button=document.getElementById('finishButton')
            button.style.display="block"
            document.getElementById('nextButton').style.display="none"
          })
        },



        /**
         * 提交当前任务的方法
         */
        submit:function () {

          this.$api.get('/assignment/'+this.assignId+'/progress',null,r=>{
            if(r.success){
              this.$api.get('/assignment/'+this.assignId+'/submit',null)
                this.$message({
                  type: 'success',
                  message: '任务提交成功!'
                });
            }
          })

          //this.assign.status="CHECKING"
          this.$router.push('/taskCenter')

        },


        /**
         * 点击确认按钮，确认添加一个区域划分
         */
        confirmSeg:function () {
          //创建一个seg并将其加入list中
          let newSeg={}
          newSeg.tagName=this.currentTag
          newSeg.pointerList=this.currentPointerList
          newSeg.color=this.config.lineColor
          newSeg.isShow=1

          this.isStart=true
          this.disable=true

          this.segList.push(newSeg)
          this.iniData()
        },


        /**
         * 确认描述
         */
        confirmDescript:function (index) {
          this.segList[index].description=this.description
          this.description=""
        },


        /**
         * 点击删除按钮，删除此区域划分
         */
        deleteSeg:function (index) {

          let _this=this

          //confirm
          this.$confirm('确认删除此图层？删除后不可恢复', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            //删除指定位置元素
            _this.segList.splice(index,1)
            //重新绘图
            _this.redraw(this.segList)

            //确认删除
            this.$message({
              type: 'success',
              message: '删除成功!'
            });


          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });

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


        /**
         * 画出最终存储的图片，不要画点啦
         */
        drawSavingPic:function  (segList) {

          let _this=this
          //
          // this.thisCanvas.width=sessionStorage.getItem('width')
          // this.thisCanvas.height=sessionStorage.getItem('height')

          //setTimeout(function () {
            _this.clearCanvas()

            if(segList.length===0){
              return
            }

            for(let i=segList.length-1;i>=0;i--){
              let pointers=segList[i].pointerList
              _this.fillColor(pointers,segList[i].color)
            }

            _this.thisCanvas.style.opacity=1
          //},50)

        },


        /**
         * 鼠标所在位置是否是画布上的某区域的点，若是，则鼠标变为pointer
         * @param x
         * @param y
         */
        findPointer:function (x,y) {
          for(let i=0;i<this.segList.length;i++){
            let pointers=this.segList[i].pointerList
            for(let j=0;j<pointers.length;j++){
              if(x>=pointers[j].x-2 && x<=pointers[j].x+2 && y>=pointers[j].y-2 && y<=pointers[j].y+2){
                this.thisCanvas.style.cursor='pointer'
                return
              }
            }
          }
        },


        /**
         * 若鼠标所在位置是起始点，则变为pointer，表示可闭合
         * @param x
         * @param y
         */
        findStartPointer:function (x,y) {
          let pointer=this.currentPointerList[0]
          if(x>=pointer.x-2 && x<=pointer.x+2 && y>=pointer.y-2 && y<=pointer.y+2){
            this.thisCanvas.style.cursor='pointer'
            return
          }
        }

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
    cursor: crosshair;
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
