<template>
  <div>
    <task-header v-bind:taskName="name"></task-header>
    <segment-panel v-bind:name="name"
                   v-bind:state="state"
                   v-bind:tags="tags"
                   v-bind:assignId="assignId"
                   v-bind:workerId="workerId"
                   v-bind:taskId="taskId"></segment-panel>
  </div>
</template>

<script>
  import segmentPanel from '../../components/segmentPanel'
  import TaskHeader from "../../components/header/annoPageHeader"
    export default {
      data(){
        return{

          //task Info
          taskId:'',
          assignId:'',
          workerId:'',
          state:'', //是开始还是查看

          taskInfo:{},

          name:'',
          assignSize:0,
          progress:1,

          tags:{}, //不这样用就会报错qwq我也没办法
        }
      },

      created(){
        this.taskId=this.$route.params.taskId
        this.assignId=this.$route.params. assignmentId
        this.workerId=this.$route.params.workerId
        this.state=this.$route.params.state

        this.getTaskName()
        this.getTaskInfo()
      },

      methods:{

        /**
         * 得到任务的名字和标签
         */
        getTaskName:function () {
          let _this=this
          this.$api.get('/task/'+this.taskId+'/brief',
            null, r => {
              _this.name=r.data.taskName
          })
        },

        getTaskInfo:function () {
          let _this=this
          this.$api.get('/task/'+this.taskId+'/attributes',
            null, r => {
              _this.tags=r.data[0].tags
            })
        },


      },
      components: {
        "task-header":TaskHeader,
        "segment-panel":segmentPanel
      },
    }
</script>

<style scoped>

</style>
