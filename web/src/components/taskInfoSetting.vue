<template>
    <div style="height:800px;overflow: scroll">
      <el-form ref="form" :model="form"
               label-position="left"
               label-width="120px"
               style="padding-left:130px;padding-top:10px">
        <el-form-item label="任务标题">
          <el-input
            size="small"
            style="width: 400px"
            v-model="form.taskName"></el-input>
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input
            type="textarea"
            placeholder="请输入内容"
            style="width: 400px"
            v-model="form.description"
            :rows="5">
          </el-input>
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select
            clearable
            size="small"
            v-model="form.taskType"
            placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编辑属性和标签">
          <el-button style="border: none" @click="addNewTag">
            <a class="el-icon-circle-plus">增加标签</a>
          </el-button>
          <div class="block" id="segmentation"
               style="width:400px;"
               v-if="form.taskType==='SEGMENTATION'">
            <div id="inputArea" style="height:50px">
            <el-col :span="14">
              <el-input
                :disabled="segmentationNotAddable"
                v-model="segmentationInput"
                size="small"></el-input>
            </el-col>
            <el-col :span="2" style="padding-left: 8px"><p></p></el-col>
            <el-col :span="2">
              <el-color-picker
                style="padding-top: 3px"
                :disabled="segmentationNotAddable"
                v-model="segmentationColorPic"
                size="small"></el-color-picker>
            </el-col>
            <el-col :span="2" style="padding-left: 8px"><p></p></el-col>
            <el-col :span="4">
              <el-button
                style="float:right;margin-top:3px"
                :disabled="segmentationNotAddable"
                size="small" plain
                @click="segmentationTagConfirm"
                type="warning">确认</el-button>
            </el-col>
            </div>
            <div id="tagArea" style="min-height:50px;
            border:lightgray 1px solid;
            border-radius: 2px 2px 2px 2px">
              <el-tag
                closable
                @close="segmentationTagClose(segTag)"
                v-for="segTag in segmentationTags"
                :key="segTag.name"
                style="margin-left: 2px"
                :style="{color: segTag.colorHex}">
                {{segTag.name}}
              </el-tag>
            </div>
          </div>
          <div class="block" id="classification"
               style="width: 400px"
               v-if="form.taskType==='CLASSIFICATION'">
            <div id="addArea" style="height: 50px">
              <el-col :span="18">
                <el-input
                  :disabled="classificationNotAddable"
                  v-model="classificationInput"
                  size="small"></el-input>
              </el-col>
              <el-col :span="2" style="padding-left: 6px"><p></p></el-col>
              <el-col :span="4">
                <el-button
                  style="float: right;margin-top:3px"
                  :disabled="classificationNotAddable"
                  size="small" plain
                  @click="classificationTagConfirm"
                  type="warning">确认</el-button>
              </el-col>
            </div>
            <div id="treeArea" style="min-height:100px;
            border:lightgray 1px solid;
            border-radius: 2px 2px 2px 2px">
            <el-tree
              :data="classificationTags"
              node-key="name"
              default-expand-all
              :expand-on-click-node="false">
              <span class="custom-tree-node" slot-scope="{node,data}">
                <span v-if="node.level===1">{{data.name}}</span>
                <span v-if="!data.inputVisible && node.level===2">
                  {{data.name}}
                </span>
                <span>
                    <el-popover
                      v-if="node.level===1"
                      trigger="click"
                      placement="top">
                      <el-input
                        style="width: 150px"
                        size="small"
                        @keyup.enter.native="append(node,data)"
                        v-model="classificationChild">
                      </el-input>
                      <el-button
                        slot="reference"
                        v-if="node.level===1"
                        type="text"
                        size="mini">
                     Append
                   </el-button>
                    </el-popover>

                  <el-button
                     type="text"
                     @click="()=>remove(node,data)"
                     size="mini">
                    Delete
                  </el-button>
                </span>
              </span>
            </el-tree>
            </div>
          </div>
          <div class="block" id="detection"
               v-if="form.taskType==='DETECTION'">
            <el-input v-if="detectionInputVisible"
                      style="width:200px"
                      size="small"
                      @keyup.enter.native="detectionHandleInput"
                      @blur="detectionHandleInput"
                      v-model="detectionTag.name"></el-input>
            <el-tag v-if="detectionTagVisible"
                    v-for="decTag in detectionTags"
                    :key="decTag.name"
                    closable
                    style="margin-left: 2px"
                    @close="detectionTagClose(decTag)">{{decTag.name}}</el-tag>
          </div>
        </el-form-item>
        <el-form-item label="图片集">
          <el-upload drag
                     action=""
                     :before-upload="judgeIsZip"
                     :http-request="submitFile"
                     :limit=1>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class='el-upload__tip' slot="tip">只能上传zip文件夹</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="子任务个数">
          <el-input-number
            v-model="form.partTimes"
            :min="1" size="small"></el-input-number>
        </el-form-item>
        <el-form-item label="需要份数">
          <el-input-number
            v-model="form.assignTimes"
            :min="1" size="small"></el-input-number>
        </el-form-item>
        <el-form-item label="单份奖金">
          <el-input-number
            v-model="form.assignmentReward"
            :step="0.01"
            :min="0" size="small"></el-input-number>
        </el-form-item>
        <el-form-item label="单元限制时间">
          <el-input-number
            :step="0.01"
            v-model="form.unitLimitMinutes"
            :min="0" size="small"></el-input-number>
        </el-form-item>
      </el-form>
      <el-button type="success"
                 style="margin-left:250px;margin-top:10px"
                 @click="submitTask">发布任务</el-button>
    </div>
</template>

<script>
    import ElPopover from "element-ui/packages/popover/src/main";

    export default {
      components: {ElPopover},
      name: "task-info-setting",
      data(){
        return {
          form: {
            taskName: '',
            taskType: '',
            requesterId:sessionStorage.getItem("userID"),
            description:'',
            attributes:[],
            partTimes:0,
            assignTimes:0,
            assignmentReward:0.0,
            unitLimitMinutes:0.0
          },

          formData:new FormData(),

          options:[
            {
              value:'CLASSIFICATION',
              label:'局部分类标注任务'
            },
            {
              value:'DETECTION',
              label:'检测任务'
            },
            {
              value:'SEGMENTATION',
              label:'区域划分任务'
            }
          ],
          detectionInputVisible:false,
          detectionTagVisible:false,
          detectionTags:[],
          detectionTag:{
            name:'',
            colorHex:''
          },

          segmentationInput:'',
          segmentationColorPic:'#FF9C40',
          segmentationNotAddable:true,
          segmentationTags:[],
          segmentationTag:{
            name:'',
            colorHex:''
          },

          classificationTags:[],
          classificationInput:'',
          classificationNotAddable:true,
          classificationInputVisible:false,
          classificationChild:'',

          isInputVisible:false
        }
      },
      methods:{
        addNewTag:function(){
          if(this.form.taskType=='CLASSIFICATION'){
            this.classificationNotAddable=false;
          }else if(this.form.taskType=='SEGMENTATION'){
            this.segmentationNotAddable=false;
          }else if(this.form.taskType=='DETECTION'){
            this.detectionInputVisible=true;
          }
        },

        detectionHandleInput:function(){
          if(this.detectionTag!=='') {
            this.detectionInputVisible = false;
            this.detectionTagVisible = true;
            let tempTag = Object.assign({},this.detectionTag)
            this.detectionTags.push(tempTag)
            this.detectionTag.name = ''
          }
        },

        detectionTagClose:function(tag){
          this.detectionTags.splice(this.detectionTags.indexOf(tag),1)
        },

        segmentationTagClose:function(tag){
          this.segmentationTags.splice(this.segmentationTags.indexOf(tag),1);
          console.log(this.segmentationTags)
        },

        segmentationTagConfirm:function(){
          this.segmentationNotAddable=true;
          this.segmentationTag.name=this.segmentationInput;
          this.segmentationTag.colorHex=this.segmentationColorPic;
          let tempTag=Object.assign({},this.segmentationTag);
          this.segmentationTags.push(tempTag);
          this.segmentationTag.name='';
          this.segmentationTag.colorHex='';
          this.segmentationInput='';
          this.segmentationColorPic='#FF9C40';
          console.log(this.segmentationTags)
        },

        classificationTagConfirm:function(){
          this.classificationNotAddable=true;
          let tempTag={};
          tempTag.inputVisible=false;
          tempTag.name=this.classificationInput;
          tempTag.children=[];
          this.classificationTags.push(tempTag);
          this.classificationInput='';
        },

        append:function(node,data){
          console.log(node.level)
          data.inputVisible=true;
          console.log(data.name+":"+data.inputVisible)
          let newChild={};
          newChild.inputVisible=false;
          newChild.name=this.classificationChild;
          newChild.children=[];
          data.children.push(newChild);
          this.classificationChild='';
          this.isInputVisible=false;
        },

        modifyAppendStatus:function(data){
          data.inputVisible=false;
          console.log(data.name+":"+data.inputVisible)
        },

        remove:function(node,data){
          if(node.level===1) {
            console.log(node.parent);
            let parent = node.parent;
            let children = parent.data;
            console.log(children.length);
            let index = children.findIndex(d => d.name === data.name);
            children.splice(index, 1);
          }else if(node.level===2){
            let parent=node.parent;
            let children=parent.data.children;
            let index=children.findIndex(d => d.name===data.name);
            children.splice(index,1);
          }
        },

        classificationChildAdd:function(node,data){
          let parent=node.parent;
          let children=parent.data.children;
          let index=children.findIndex(d => d.name==='');
          children[index].inputVisible=false;
          children[index].name=this.classificationChild;
          this.classificationChild='';
        },

        submitFile:function(item){
          this.formData.append('file', item.file);
        },

        judgeIsZip:function(file){
          let fileType=file.name.substring(file.name.lastIndexOf('.')+1);
          let isUpload=(fileType==='zip');
          if(!isUpload){
            this.$message({
              message:'上传文件夹只能是zip格式！',
              type:'warning'
            })
          }
          return isUpload;
        },

        submitTask:function(){
          console.log(this.formData.get('file'))
          if(this.formData.get('file')!=null && this.formData.get('task')!=null){
            this.$message({
              message: '后台正在处理',
              type: 'info'
            })
          }else {

            let parentElement = {};
            let tags = [];
            let childElement = {};
            switch (this.form.taskType) {
              case 'CLASSIFICATION':
                console.log(this.classificationTags)
                for (let i = 0; i < this.classificationTags.length; i++) {
                  tags = [];
                  parentElement.name = this.classificationTags[i].name;
                  for (let j = 0; j < this.classificationTags[i].children.length; j++) {
                    childElement.name = this.classificationTags[i].children[j].name;
                    childElement.colorHex = '';
                    let tempChild = Object.assign({}, childElement);
                    tags.push(tempChild);
                  }
                  parentElement.tags = tags;
                  let tempParent = Object.assign({}, parentElement);
                  this.form.attributes.push(tempParent);
                }
                console.log(JSON.stringify(this.form.attributes))
                break;
              case 'DETECTION':
                parentElement.name = this.form.taskName;
                parentElement.tags = this.detectionTags;
                this.form.attributes.push(parentElement);
                break;
              case 'SEGMENTATION':
                parentElement.name = this.form.taskName;
                parentElement.tags = this.segmentationTags;
                this.form.attributes.push(parentElement);
                break;
            }

            this.form.assignmentReward = parseFloat(this.form.assignmentReward.toString()).toFixed(2);
            this.form.assignTimes = parseFloat(this.form.assignTimes.toString()).toFixed(2);

            //this.formData.append('file', this.file)
            this.formData.append('task', JSON.stringify(this.form));
            console.log(this.form)


            this.$api.post('/task/create', this.formData, r => {
              if (r.data === 'SUCCESS') {
                this.$router.push('/taskCenter');
                this.$message({
                  type: 'success',
                  message: '发布成功!'
                });
              }
            });
          }
        }
      }
    }
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
    height:26px;
  }
</style>
