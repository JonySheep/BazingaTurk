<template>
  <div>
    <el-container>
      <el-header id="header" style="height: 50px">
        <task-header v-bind:taskName="taskName"></task-header>
      </el-header>
      <el-container style="padding-top: 0">
        <el-aside id="left-side" width="13%" style="border: lightgray 1px solid">
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
                下一张
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
                     draggable=false/>
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
                <div v-for="item in attrCopy">
                  <p style="color:#101010;font-size: 13px">{{item.name}}</p>
                  <el-select v-model=item.res placeholder="请选择" style="height: 30px"
                             filterable
                             clearable
                             size="mini"
                             default-first-option
                             allow-create>
                    <el-option v-for="i in item.tags"
                               :key="i.name"
                               :label="i.name"
                               :value="i.name">
                    </el-option>
                  </el-select>
                </div>
              </div>
            </div>
            <div style="height:40px;line-height: 40px;text-align:center;font-size: 14px;border-bottom: lightgray 1px solid">
              <span>描述</span>
            </div>
            <div style="height: 200px;text-align: center">
              {{description}}
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
    name: "part-classification-done-check",
    data: function () {
      return{
        /* 图片的src */
        pic: '',
        /* 任务的大小 */
        size: 0,
        /* 当前图片的位置 */
        count: 1,
        /* 当前拥有标签的总数 */
        labelTotal: 0,

        /* 此任务的名称 */
        taskName: '',
        task: {},
        assign:{},

        /* 由路由分配的id号 */
        taskId: '',
        assignmentId:'',
        workerId:'',


        /* 查看图层时选中的图层 */
        selected: -1,

        /* 获取的图片信息以及返回的图片信息对象 */
        res: {},

        currentLayer: {
          newLabel:{},
          attrs:[],
          color:'#F49108',
          description:'',
          isExist:true
        },
        attrCopy:[],
        layerList:[],
        description:'无'
      }
    },
    created() {
      this.taskId = this.$route.params.taskId;
      this.assignmentId=this.$route.params.assignmentId;
      this.workerId=this.$route.params.workerId;
      this.getTaskInfo(this.taskId);
      this.getTaskName(this.taskId);
      this.getAssignment(this.taskId,this.assignmentId,this.workerId);
    },
    destroyed(){

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

      /**
       * 获取该任务的标签维度与图片总数
       * @param taskid
       */
      getTaskInfo: function (taskid) {
        this.$api.get('task/' + taskid + '/attributes', null, r => {
          this.attrCopy = r.data;
          for(let i = 0;i<r.data.length;i++){
            this.attrCopy.attrs[i].res = ""
          }
          console.log(this.attrCopy)
        })
      },

      getAssignment:function(taskId,assignmentId,workerId){
        this.$api.get('assignment/'+assignmentId,null,r=>{
          this.assign = r.data
          this.count = 1
          this.size = r.data.size
          this.$api.get('assignment/'+this.assignmentId+'/'+this.count,null,re=>{
            this.res = re.data
            this.pic = 'http://localhost:8080/' + re.data.imageUrl

            //TODO 拷贝信息到layerList
            this.labelTotal = 0

            for(let i=0;i<this.res.boxes.length;i++){
              let tempLayer={
                newLabel:{},
                attrs:[],
                color:'#F49108',
                description:'',
                isExist:true
              };
              tempLayer.newLabel.xpos = this.res.boxes[i].xpos;
              tempLayer.newLabel.ypos = this.res.boxes[i].ypos;
              tempLayer.newLabel.width = this.res.boxes[i].width;
              tempLayer.newLabel.height = this.res.boxes[i].height;
              tempLayer.color = this.res.boxes[i].colorHex;
              for(let j=0;j<this.res.boxes[i].tags.length;j++){
                let tempTag = Object.assign({},this.res.boxes[i].tags[j])
                tempLayer.attrs.push(tempTag)
              }
              this.layerList.push(tempLayer)
            }
            //console.log(this.layerList)
            console.log(this.res.boxes)
            for(let i=0;i<this.res.boxes.length;i++){
              let ab = document.createElement("div")
              ab.id = "label_"+this.labelTotal
              ab.className = "box"
              ab.style.zIndex = 2
              ab.style.left = this.res.boxes[i].xpos+'px'
              ab.style.top = this.res.boxes[i].ypos+'px'
              ab.style.width = this.res.boxes[i].width+'px'
              ab.style.height = this.res.boxes[i].height+'px'
              ab.style.borderColor = this.res.boxes[i].colorHex
              let temp = document.getElementById("img-panel-background")
              temp.appendChild(ab)
              this.labelTotal++
              this.addLayer()
            }
          })
        })
      },

      /**
       * 获取图片并加载
       * @param taskid
       * @param imageID
       */
      getTaskImage: function (assignmentId, imageId) {
        this.$api.get('assignment/'+ assignmentId +'/'+ imageId, null, r=>{
          this.res = r.data;
          let picUrl = 'http://localhost:8080/' + r.data.imageUrl;
          //console.log(this.pic)
          this.pic = picUrl;

          //TODO 拷贝信息到layerList
          this.labelTotal = 0
          for(let i=0;i<this.res.boxes.length;i++){
            let tempLayer={
              newLabel:{},
              attrs:[],
              color:'#F49108',
              description:'',
              isExist:true
            }
            tempLayer.newLabel.xpos = this.res.boxes[i].xpos;
            tempLayer.newLabel.ypos = this.res.boxes[i].ypos;
            tempLayer.newLabel.width = this.res.boxes[i].width;
            tempLayer.newLabel.height = this.res.boxes[i].height;
            tempLayer.color = this.res.boxes[i].colorHex;
            for(let j=0;j<this.res.boxes[i].tags.length;j++){
              let tempTag = Object.assign({},this.res.boxes[i].tags[j])
              tempLayer.attrs.push(tempTag)
            }
            this.layerList.push(tempLayer)
          }
          //console.log(this.layerList)
          console.log(this.res.boxes)
          for(let i=0;i<this.res.boxes.length;i++){
            let ab = document.createElement("div")
            ab.id = "label_"+this.labelTotal
            ab.className = "box"
            ab.style.zIndex = 2
            ab.style.left = this.res.boxes[i].xpos+'px'
            ab.style.top = this.res.boxes[i].ypos+'px'
            ab.style.width = this.res.boxes[i].width+'px'
            ab.style.height = this.res.boxes[i].height+'px'
            ab.style.borderColor = this.res.boxes[i].colorHex
            let temp = document.getElementById("img-panel-background")
            temp.appendChild(ab)
            this.labelTotal++
            this.addLayer()
          }
        })
      },

      addLayer:function () {
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
        layerName.innerHTML = "图层" + (this.labelTotal);
        layerName.style.color = this.layerList[this.labelTotal-1].color;
        layerName.onclick = this.onlayerNameclick;

      },

      getLastData: function () {
        if(this.count === 1){
          this.$message({
            message:"已经是第一张了！",
            type: 'warning'
          })
        }else{
            this.clearAll()
            this.count--;
            this.getTaskImage(this.assignmentId, this.count)
        }
      },

      getNextData: function () {
          if (this.count < this.size) {
            this.clearAll()
            this.count++
            this.getTaskImage(this.assignmentId, this.count)
          } else {
            this.$confirm('已经是最后一张，是否退出？', '提示', {
              confirmButtonText: '退出',
              cancelButtonText: '取消',
              type: 'info'
            }).then(() => {
              this.$router.push('/taskCenter');
            }).catch(() => {
            })
          }
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

        this.currentLayer = {
          newLabel:{},
          attrs:[],
          color:'#F49108',
          description:'',
          isExist:true
        }

        this.layerList = []
      },

      onseenclick:function (e) {
        var seen = e.target
        let num = seen.parentNode.id.split("_")[1]
        var ab = document.getElementById("label_"+num)
        if(seen.isDisabled == true){
          seen.isDisabled= false
          seen.style.color = 'black'
          ab.style.width = this.layerList[num].newLabel.width+'px'
          ab.style.height = this.layerList[num].newLabel.height+'px'
          ab.style.borderColor = this.layerList[num].color
        }else{
          seen.isDisabled= true
          seen.style.color = 'lightgray'
          ab.style.width = 0+'px'
          ab.style.height = 0+'px'
          ab.style.borderColor = 'transparent'
        }
      },

      onlayerNameclick:function (e) {
          var layerName = e.target
          let num = layerName.parentNode.id.split("_")[1]
          this.selected = num
          var ab = document.getElementById("label_" + num)
          this.currentLayer = this.layerList[num]
          console.log(this.currentLayer)
          for (let i = 0; i < this.attrCopy.length; i++) {
            this.attrCopy[i].res = this.currentLayer.attrs[i].name
          }
      },

    }
  }
</script>

<style>
  #header{
    padding-left: 1px;
    padding-right:1px;
    padding-top: 0px;
  }
  #img-panel {
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
  .tags{
    background-color: transparent;
    /*min-height:600px;*/
    overflow: auto;
    position: absolute;
    padding-left: 17px;
    padding-right:10px;
    width:123px;
  }
  .foot{
    height:50px;
    margin-left: 22%;
    margin-top:15px;
  }
</style>
