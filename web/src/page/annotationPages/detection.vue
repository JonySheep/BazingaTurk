<template>
  <div>
    <el-container>
      <el-header id="header" style="height: 50px">
        <task-header v-bind:taskName="taskName"></task-header>
      </el-header>
      <el-container style="padding-top: 0">
        <el-aside id="left-side" width="13%" style="border: lightgray 1px solid">
          <div style="height:55px;line-height: 40px;text-align:center;font-size: 14px;border-bottom: lightgray 1px solid">
            <el-col :span="12" style="padding-top: 15px">
              <span style="color: #101010">边框颜色</span>
            </el-col>
            <el-col :span="12" style="padding-top: 10px">
              <el-color-picker v-model="currentLayer.tag.colorHex"></el-color-picker>
            </el-col>
          </div>
          <div style="height:40px;line-height: 40px;text-align:center;font-size: 14px;border-bottom: lightgray 1px solid">
            <p style="color: #101010">图层</p>
          </div>
          <div id="layer" style="overflow: auto;max-height: 560px"></div>
        </el-aside>
        <el-container style="text-align: center">

          <el-header style="vertical-align: middle;height: 50px;padding-left: 30px;padding-right: 30px;padding-top: 20px;">
            <el-col :span="8" style="text-align: left">
              <el-button type="text"
                         @click="getLastData"
                         style="height: 30px;border:none;color: #101010">
                <a class="el-icon-arrow-left" style="font-size: 14px;font-family: Roboto">上一张</a>
              </el-button>
            </el-col>
            <el-col :span="8" style="text-align: center">
              <el-button type="text" style="height:30px;">
                {{count}}/{{size}}
              </el-button>
            </el-col>
            <el-col :span="8" style="text-align: right">
              <el-button type="text"
                         @click="getNextData"
                         style="height: 30px;border:none;color: #101010;font-size: 14px;font-family: Roboto">
                {{nextMessage}}
                <i class="el-icon-arrow-right el-icon--right"></i>
              </el-button>
            </el-col>
          </el-header>
          <el-main style="padding-top:5px">
            <!--图片区域-->
            <div id="img-panel">
              <div id="img-panel-background"
                   style="position: absolute">
                <img id="img-area-self" v-bind:src=pic
                     style="background:no-repeat center center;background-size:contain;position: absolute;z-index: 1;"
                     draggable="false"
                     @mousedown="mouse_down"
                     @mousemove="mouse_move"
                     @mouseup="mouse_up"/>
              </div>
            </div>
          </el-main>

        </el-container>
        <el-aside style="width: 13%; border: lightgray 1px solid">
          <div>
            <div style="height:40px;line-height: 40px;text-align:center;font-size: 14px;border-bottom: lightgray 1px solid">
              <p>属性</p>
            </div>
            <div style="height:350px; border-bottom: lightgray 1px solid">
              <div  class="tags" >
                <ul class="buttonUL">
                  <li v-for="t in tag" style="display: block;margin: 5px;float: left">
                    <div>
                      <el-col :span="22">
                        <el-button width="10"
                                   plain
                                   icon="el-icon-edit"
                                   size="mini"
                                   v-on:click="setName(t.name)">{{t.name}}</el-button>
                      </el-col>
                      <el-col :span="2" style="padding-top: 5px;color: lightgreen">
                        <i class="el-icon-circle-check" v-if="currentLayer.tag.name === t.name"></i>
                      </el-col>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <div style="height:40px;line-height: 40px;text-align:center;font-size: 14px;border-bottom: lightgray 1px solid">
              <span>描述</span>
            </div>
            <div style="height: 100px">
              <el-input
                style="padding-top: 5px"
                type="textarea"
                :disabled="!isRightVisible"
                autosize
                placeholder="请输入内容"
                v-model="currentLayer.description">
              </el-input>
            </div>
            <div style="padding-top:20px;text-align: center">
              <el-button  size="mini" type="danger" round @click="confirmInfo"
                          :disabled="!isRightVisible">{{buttonMessage}}</el-button>
            </div>
          </div>
        </el-aside>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import TaskHeader from "../../components/header/annoPageHeader"
  import ElAside from "element-ui/packages/aside/src/main";
    export default {
      components:{
        ElAside,
        ElMain,
        ElContainer,
        "task-header": TaskHeader
      },
      name: "detection",
      data:function () {
        return{
          /* 图片的src */
          pic: '',
          /* 任务的大小 */
          size: 0,
          /* 当前图片的位置 */
          count: 1,
          /* 当前拥有标签的总数 */
          labelTotal: 0,
          max:1,

          /* 拖拽一个标签的起始位置 */
          startX: 0,
          startY: 0,

          /* 记录鼠标是否进行操作 */
          isMouseDown: false,
          isMouseMove: false,

          /* 此任务的名称 */
          taskName: '',
          task: {},
          assign:{},

          /* 由路由分配的id号 */
          taskId: '',
          assignmentId:'',
          workerId:'',
          state:'',

          /* 属性栏下的按钮 */
          buttonMessage: '确认',
          /* 查看图层时选中的图层 */
          selected: -1,

          /* 获取的图片信息以及返回的图片信息对象 */
          res: {},

          creatable: false,
          isRightVisible: false,
          confirm: true,

          currentLayer:{
            newLabel:{},
            tag:{name:'',colorHex:'#F49108'},
            description:'',
            isExist:true
          },
          layerList:[],

          tag:[],
          nextMessage: '下一张',
        }
      },
      created() {
        this.state = this.$route.params.state;
        this.taskId = this.$route.params.taskId;
        this.assignmentId=this.$route.params.assignmentId;
        this.workerId=this.$route.params.workerId;
        this.getTaskInfo(this.taskId);
        this.getTaskName(this.taskId);
        this.getAssignment(this.taskId,this.assignmentId,this.workerId);
      },
      destroyed(){
        this.integrateRes()
        this.postTaskImage(this.assignmentId, this.count)
      },
      methods:{
        /**
         * 获取任务名称
         * @param taskid
         */
        getTaskName: function (taskid) {
          this.$api.get('task/' + taskid + '/brief', null, r => {
            this.taskName = r.data.taskName;
            this.task = r.data;
          })
        },

        getTaskInfo: function (taskid) {
          this.$api.get('task/'+taskid+'/attributes',null,r=> {
            this.tag=r.data[0].tags;
          })
        },

        getAssignment: function (taskId, assignmentId, workerId) {
          this.$api.get('assignment/'+assignmentId,null,r=>{

            //记录任务开始时间
            if(r.data.progress===0){
              this.$api.get('/assignment/'+assignmentId+'/begin',null)
            }
            this.assign = r.data
            if(this.state === 'doTask'){
              this.count = r.data.progress+1
              this.max = this.count
              this.size  = r.data.size
              if(this.count === this.size){
                this.nextMessage = '提交'
              }
            }else if(this.state === 'checkTask'){
              this.count = 1
              this.size = r.data.progress
            }
            this.$api.get('assignment/'+this.assignmentId+'/'+this.count,null ,re=>{
              //console.log(re.data)
              this.res = re.data
              this.pic = 'http://localhost:8080/' + re.data.imageUrl
              //console.log('assignment/'+this.assignmentId+'/'+this.count)
              this.labelTotal = 0
              console.log(this.res.boxes)

              for(let i=0;i<this.res.boxes.length;i++){
                let tempLayer = {
                  newLabel:{},
                  tag:{name:'',colorHex:'#F49108'},
                  description:'',
                  isExist:true
                }
                tempLayer.newLabel.xpos = this.res.boxes[i].xpos;
                tempLayer.newLabel.ypos = this.res.boxes[i].ypos;
                tempLayer.newLabel.width = this.res.boxes[i].width;
                tempLayer.newLabel.height = this.res.boxes[i].height;
                tempLayer.tag = Object.assign({}, this.res.boxes[i].tags[0]);
                this.layerList.push(tempLayer);
              }
              for(let i =0; i<this.res.boxes.length; i++){
                console.log(this.layerList)
                let ab = document.createElement("div")
                ab.id = "label_"+this.labelTotal
                ab.className = "box"
                ab.style.zIndex = 2
                ab.style.left = this.res.boxes[i].xpos+'px'
                ab.style.top = this.res.boxes[i].ypos+'px'
                ab.style.width = this.res.boxes[i].width+'px'
                ab.style.height = this.res.boxes[i].height+'px'
                ab.style.borderColor = this.res.boxes[i].tags[0].colorHex
                let temp = document.getElementById("img-panel-background")
                temp.appendChild(ab)
                this.labelTotal++
                this.addLayer()
              }
            })
          })
        },

        setName :function(name){
          if(this.confirm){
            this.creatable = true
            this.currentLayer.tag.name = name
          }else{
            this.$message({
              message:'有未确认的标注',
              type:'info'
            })
          }
        },

        mouse_down: function (e) {
          if(this.creatable){
            this.isMouseDown = true
            this.startX = e.offsetX
            this.startY = e.offsetY

            var active_box = document.createElement('div')
            active_box.id = "label_" + this.labelTotal
            active_box.className = "box"
            active_box.onmousemove = this.boxmove
            active_box.onmouseup = this.boxup
            active_box.style.borderColor = this.currentLayer.tag.colorHex
            var temp = document.getElementById("img-panel-background")
            temp.appendChild(active_box)
            active_box = null
          }else{
            this.$message({
              message:'请先选择标签！',
              type:'warning'
            })
          }
        },

        boxmove: function (e) {
          if(this.isMouseDown){
            //console.log('boxmove')
            var ab = document.getElementById("label_"+this.labelTotal)
            if(ab !== null){
              let thisX = e.offsetX
              let thisY = e.offsetY
              thisX = thisX + ab.offsetLeft
              thisY = thisY + ab.offsetTop
              //console.log(thisX, thisY)
              this.drawRectangle(this.startX, this.startY, thisX, thisY)
            }
          }
        },

        boxup: function (e) {
          if(this.isMouseDown && this.isMouseMove) {
            var ab = document.getElementById("label_" + this.labelTotal)
            if(ab !== null){
              if(ab.offsetHeight<3 || ab.offsetWidth<3){
                var parent = document.getElementById("img-panel-background")
                parent.removeChild(ab)
              }else{
                this.currentLayer.newLabel.xpos = ab.offsetLeft
                this.currentLayer.newLabel.ypos = ab.offsetTop
                this.currentLayer.newLabel.width = ab.offsetWidth
                this.currentLayer.newLabel.height = ab.offsetHeight
                this.isRightVisible = true
                this.creatable = false
                this.confirm = false
                this.labelTotal++
              }
              this.isMouseDown = false
              this.isMouseMove = false
            }
            //console.log('boxup')
          }
        },

        mouse_move: function (e) {
          if(this.isMouseDown){
            //console.log('move')
            this.isMouseMove = true
            var ab = document.getElementById("label_"+this.labelTotal)
            if(ab !== null){
              let thisX = e.offsetX
              let thisY = e.offsetY
              this.drawRectangle(this.startX, this.startY, thisX, thisY)
            }
          }
        },

        drawRectangle: function (startX, startY, endX, endY) {
          let width = endX - startX
          let height = endY - startY
          let top = startY
          let left = startX
          if(width < 0){
            width = 0 - width
            left = endX
          }
          if(height < 0){
            height = 0- height
            top = endY
          }
          var ab = document.getElementById("label_"+ this.labelTotal)
          ab.style.left = left+'px'
          ab.style.top = top+'px'
          ab.style.width = width+'px'
          ab.style.height = height+'px'
          ab.style.zIndex = 2
        },

        mouse_up: function (e){
          if(this.isMouseDown && this.isMouseMove) {
            var ab = document.getElementById("label_"+this.labelTotal)
            if(ab !== null){
              if(ab.offsetHeight<3 || ab.offsetWidth<3){
                var parent = document.getElementById("img-panel-background")
                parent.removeChild(ab)

              }else{
                this.currentLayer.newLabel.xpos = ab.offsetLeft
                this.currentLayer.newLabel.ypos = ab.offsetTop
                this.currentLayer.newLabel.width = ab.offsetWidth
                this.currentLayer.newLabel.height = ab.offsetHeight
                this.labelTotal++
                this.isRightVisible = true
                this.creatable = false
                this.confirm = false
              }
              this.isMouseDown = false
              this.isMouseMove = false
              //console.log('up')
            }
          }
        },

        confirmInfo: function () {
          this.confirm = true
          if(this.buttonMessage === '确认'){
            let layer = Object.assign({}, this.currentLayer)
            layer.tag = Object.assign({}, this.currentLayer.tag)
            //console.log(layer)
            this.layerList.push(layer)
            this.addLayer()
          }else if(this.buttonMessage === '保存修改'){
            this.layerList[this.selected]=Object.assign({},this.currentLayer)
            console.log(this.layerList[this.selected])
            this.buttonMessage = '确认'
          }
          this.currentLayer.description = ''
          this.currentLayer.tag.name = ''
          this.currentLayer.tag.colorHex = '#F49108'
          this.currentLayer.newLabel = {}

          this.isRightVisible = false
        },

        getLastData: function () {
          if(this.count === 1){
            this.$message({
              message:"已经是第一张了！",
              type: 'warning'
            })
          }else{
            if(!this.confirm){
              this.$message({
                message:"有未确定的标注！",
                type: 'warning'
              })
            }else{
              this.integrateRes()
              this.postTaskImage(this.assignmentId, this.count)
              this.clearAll()
              this.count--;
              this.getTaskImage(this.assignmentId, this.count)
              this.nextMessage = '下一张'
            }
          }
        },

        getNextData: function () {
          if(!this.confirm){
            this.$message({
              message:"有未确定的标注！",
              type: 'warning'
            })
          }else {
            this.integrateRes()
            if (this.count < this.size) {
              this.postTaskImage(this.assignmentId, this.count)
              if(this.state === 'doTask'){
                if(this.count === this.max){
                  this.addProgress(this.assignmentId)
                  this.max++;
                }
              }
              this.clearAll()
              this.count++
              this.getTaskImage(this.assignmentId, this.count)
              //console.log(this.res)
              if (this.count === this.size) {
                if(this.state === 'doTask'){
                  this.nextMessage = '提交'
                }else{
                  this.nextMessage = '保存修改'
                }
              }
            } else {
              //TODO 选择提交还是暂存
              if(this.state === 'doTask'){
                this.$confirm('此任务已完成，是否提交？','提示',{
                  confirmButtonText:'提交',
                  cancelButtonText:'暂存',
                  type: 'info'
                }).then(()=>{
                  this.postTaskImage(this.assignmentId, this.count)
                  //this.addProgress(this.assignmentId)
                  this.submit(this.assignmentId)
                  this.$router.push('/taskCenter');
                }).catch(()=>{
                  this.postTaskImage(this.assignmentId, this.count)
                  this.$router.push('/taskCenter')
                })
              }else{
                this.$confirm('已保存修改，是否退出？','提示',{
                  confirmButtonText:'确认',
                  cancelButtonText:'取消',
                  type: 'info'
                }).then(()=>{
                  this.postTaskImage(this.assignmentId, this.count)
                  this.$router.push('/taskCenter');
                }).catch(()=>{
                  this.postTaskImage(this.assignmentId, this.count)
                })
              }
            }
          }
        },

        addProgress: function (assignmentId) {
          this.$api.get('assignment/'+assignmentId+'/progress',null,r=>{})
        },

        integrateRes: function () {
          this.res.boxes = []
          for(let i=0; i<this.layerList.length; i++){
            if(this.layerList[i].isExist){
              //TODO 加上description储存
              let box = {}
              box.xpos = this.layerList[i].newLabel.xpos
              box.ypos = this.layerList[i].newLabel.ypos
              box.width = this.layerList[i].newLabel.width
              box.height = this.layerList[i].newLabel.height
              box.colorHex = "#FFFFFF"
              box.tags = []
              let tag = Object.assign({},this.layerList[i].tag)
              box.tags.push(tag)
              this.res.boxes.push(box)
            }
          }
          console.log(this.res)
        },

        clearAll: function () {
          let panelDiv = document.getElementById("img-panel-background")
          let left = document.getElementById("layer")
          for(let i=0; i<this.labelTotal; i++){
            let ab = document.getElementById("label_"+i)
            if(ab !== null){
              panelDiv.removeChild(ab)
            }
            let layer = document.getElementById("layer_"+i)
            if(layer !== null){
              left.removeChild(layer)
            }
          }

          this.labelTotal = 0
          this.selected = -1
          this.creatable = false
          this.isRightVisible = false
          this.confirm = true

          this.currentLayer = {
            newLabel:{},
            tag:{name:'',colorHex:'#F49108'},
            description:'',
            isExist:true
          }

          this.layerList = []
        },

        submit: function (assignmentId) {
          this.$api.get('assignment/'+assignmentId+'/progress',null,r=>{
            if(r.success){
              this.$api.get('assignment/'+assignmentId+'/submit',null,res=>{

              })
            }
          })
        },

        postTaskImage: function (assignmentId, imageId) {
          let formData = new FormData()
          let temp = JSON.stringify(this.res)
          formData.append('anno',temp)
          this.$api.post('assignment/'+ assignmentId +'/'+ imageId, formData)
        },

        getTaskImage: function (assignmentId, imageId) {
          this.$api.get('assignment/'+ assignmentId +'/'+ imageId, null, r=>{
            this.res = r.data
            this.pic = 'http://localhost:8080/' + r.data.imageUrl

            this.labelTotal = 0
            console.log(this.res.boxes)
            //TODO 拷贝信息到layerList
            for(let i=0;i<this.res.boxes.length;i++){
              let tempLayer = {
                newLabel:{},
                tag:{name:'',colorHex:'#F49108'},
                description:'',
                isExist:true
              }
              tempLayer.newLabel.xpos = this.res.boxes[i].xpos;
              tempLayer.newLabel.ypos = this.res.boxes[i].ypos;
              tempLayer.newLabel.width = this.res.boxes[i].width;
              tempLayer.newLabel.height = this.res.boxes[i].height;
              tempLayer.tag = Object.assign({}, this.res.boxes[i].tags[0]);
              this.layerList.push(tempLayer);
            }
            for(let i =0; i<this.res.boxes.length; i++){
              let ab = document.createElement("div")
              ab.id = "label_"+this.labelTotal
              ab.className = "box"
              ab.style.zIndex = 2
              ab.style.left = this.res.boxes[i].xpos+'px'
              ab.style.top = this.res.boxes[i].ypos+'px'
              ab.style.width = this.res.boxes[i].width+'px'
              ab.style.height = this.res.boxes[i].height+'px'
              ab.style.borderColor = this.res.boxes[i].tags[0].colorHex
              let temp = document.getElementById("img-panel-background")
              temp.appendChild(ab)
              this.labelTotal++
              this.addLayer()
            }
          })
        },

        addLayer: function () {
          var layer = document.createElement('div')
          layer.id = "layer_" + (this.labelTotal-1)
          layer.style.height = "40px";
          layer.style.lineHeight = "40px";
          layer.style.fontSize = "14px";
          layer.style.fontFamily = "Roboto";
          layer.style.borderBottom = "lightgray 1px solid";
          var temp = document.getElementById("layer")
          console.log(temp.style)
          document.getElementById("layer").appendChild(layer);

          var seen = document.createElement("i");
          seen.className = "el-icon-view";
          seen.style.width = "30px";
          seen.style.color = "black";
          seen.style.paddingLeft = "5px";
          seen.style.paddingTop = "13px";
          seen.style.float = "left";
          seen.style.cursor = "pointer";
          layer.appendChild(seen);
          seen.onclick = this.onseenclick;

          var layerName = document.createElement("el-button");
          layerName.type = "text";
          layerName.style.cursor = "pointer";
          layerName.style.fontFamily = "Roboto";
          layerName.style.fontSize = "14px";
          layerName.style.marginLeft = "22px";
          layerName.style.width = "35px";
          layer.appendChild(layerName);
          layerName.innerHTML = this.layerList[this.labelTotal-1].tag.name;
          layerName.style.color = this.layerList[this.labelTotal-1].tag.colorHex;
          layerName.onclick = this.onlayerNameclick;

          var remove = document.createElement("a");
          remove.className = "el-icon-remove";
          remove.style.float = "right";
          remove.style.paddingRight = "5px";
          remove.style.paddingTop = "13px";
          remove.style.cursor = "pointer";
          layer.appendChild(remove);
          remove.onclick = this.onremoveclick;
        },

        onseenclick: function (e) {
          var seen = e.target
          let num = seen.parentNode.id.split("_")[1]
          var ab = document.getElementById("label_"+num)
          if(seen.isDisabled == true){
            seen.isDisabled= false
            seen.style.color = 'black'
            ab.style.width = this.layerList[num].newLabel.width+'px'
            ab.style.height = this.layerList[num].newLabel.height+'px'
            ab.style.borderColor = this.layerList[num].tag.colorHex
            console.log('on')
          }else{
            seen.isDisabled= true
            seen.style.color = 'lightgray'
            ab.style.width = 0+'px'
            ab.style.height = 0+'px'
            ab.style.borderColor = 'transparent'
            console.log('off')
          }
        },

        onlayerNameclick: function (e) {
          if(this.confirm) {
            var layerName = e.target
            let num = layerName.parentNode.id.split("_")[1]
            this.selected = num
            var ab = document.getElementById("label_" + num)
            this.currentLayer = Object.assign({},this.layerList[num])
            this.currentLayer.tag = Object.assign({}, this.layerList[num].tag)
            //console.log(this.currentLayer)
            //console.log(this.layerList[num])
            this.isRightVisible = true
            this.confirm = false
            this.buttonMessage = '保存修改'
          }else{
            this.$message({
              message:"有未确定的标注！",
              type: 'info'
            })
          }
        },

        onremoveclick: function (e) {
          if(this.confirm){
            this.$confirm('确认删除么','提示',{
              confirmButtonText:'确定',
              cancelButtonText:'取消',
              type: 'warning'
            }).then(()=>{
              var remove = e.target
              let num = remove.parentNode.id.split("_")[1]
              var ab = document.getElementById("label_"+num)
              console.log(remove.parentNode.id)
              remove.parentNode.parentNode.removeChild(remove.parentNode)
              let i =document.getElementById('layer').style
              console.log(i)
              ab.parentNode.removeChild(ab)
              this.layerList[num].isExist = false
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
            }).catch(()=>{
              this.$message({
                type: 'info',
                message: '已取消删除'
              })
            })
          }else{
            this.$message({
              message:"有未确定的标注！",
              type: 'info'
            })
          }
        }
      }
    }
</script>

<style>
  #header{
    padding-left: 1px;
    padding-right:1px;
    padding-top: 0px;
  }
  #img-panel{
    position: relative;
    width: 100%;
    padding-bottom: 62.5%;
    border: lightgray 2px solid;
    overflow: auto;
  }
  .box{
    /*background:mistyrose;*/
    border:1px solid;
    width: 0px;
    height: 0px;
    position: absolute;
    cursor: move;
    /*opacity: 0.5;*/
  }
</style>
