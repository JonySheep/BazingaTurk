<template>
  <el-container style="margin:0px;padding:0px">
    <el-header style="margin:0px;padding:0px">
      <manage_header></manage_header>
    </el-header>
    <el-container>
      <el-main>
        <div style="padding-top: 30px;padding-left: 68px;padding-right: 68px;text-align: center">

          <div style="border: lightgrey 1px solid;margin-bottom: 20px">
            <p style="color: dimgray;">图片预览</p>
            <el-carousel :interval="4000" type="card" height="300px" style="width: 1000px;margin: auto">
              <el-carousel-item v-for="item in previewList" :key="item">
                <img :src=item style="height: 100%;width: 100%;" />
              </el-carousel-item>
            </el-carousel>
          </div>

          <div style="border:lightgray 1px solid;height: 600px">
            <p style="color: dimgray;">操作指引</p>
            <seg-guide v-if="taskType==='SEGMENTATION'"></seg-guide>
            <part-guide v-if="taskType==='CLASSIFICATION'"></part-guide>
            <det-guide v-if="taskType==='DETECTION'"></det-guide>
          </div>
        </div>
      </el-main>

      <el-footer style="padding-top: 20px;text-align: center">
        <el-button id="receipt" type="danger"
                   height="20" round
                   v-if="isWorker"
                   @click="receipt">接受</el-button>
        <el-button id="cancel" type="danger" height="20" round @click="cancel">返回</el-button>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import ElHeader from "element-ui/packages/header/src/main";
  import manage_header from "../../components/header/taskPageHeader"

  import segGuide from '../../components/taskGuide/segGuide'
  import detGuide from '../../components/taskGuide/detetionGuide'
  import partGuide from '../../components/taskGuide/partGuide'

    export default {
      components:{
        ElHeader,
        ElMain,
        ElContainer,
        manage_header,
        segGuide,
        detGuide,
        partGuide
      },
      name: "task-detail",

      data(){
        return{
          userId:'',
          taskId:'',
          isWorker:false,
          taskType:'',
          previewList:'',
        }
      },
      created(){
        this.taskId=this.$route.params.id;
        this.userId=sessionStorage.getItem("userID");
        this.setType()
        this.setPreview()
      },
      mounted(){
        this.judgeIsWorker()
      },
      destroyed(){
        console.log('destroyed')
      },
      methods:{
        judgeIsWorker:function(){
          if(sessionStorage.getItem("role")==='Worker'){
            this.isWorker=true;
          }
        },

        receipt:function(){
          this.$api.get('/assignment/'+this.taskId+'/'+this.userId+'/accept',null,r=>{
            console.log(r.data)
            if(r.data==='SUCCESS'){
              this.$router.push('/taskCenter');
            }
          })
        },
        cancel:function(){
          this.$router.push('/taskCenter');
        },


        setType:function () {
          this.$api.get('/task/'+this.taskId+'/brief',null,r=>{
            this.taskType=r.data.taskType
          })
        },

        /**
         * 获得预览图片
         */
        setPreview:function () {
          this.$api.get('/task/'+this.taskId+'/preview',null,r=>{
            this.previewList=r.data
          })
        }
      }
    }
</script>

<style>

</style>
