<template>
    <div>
      <myTaskHeader v-bind:taskName="name"></myTaskHeader>
      <segment-check v-bind:tags="tags"
                     v-bind:name="name"
                     v-bind:assignId="assignmentId"
                     v-bind:workerId="workerId"
                     v-bind:taskId="taskId"></segment-check>
    </div>
</template>

<script>
    import ElContainer from "element-ui/packages/container/src/main";
    import myTaskHeader from "../../components/header/annoPageHeader"
    import SegmentCheck from "../../components/segmentCheck"
    import ElHeader from "element-ui/packages/header/src/main";

    export default {
      components: {
        SegmentCheck,
        ElHeader,
        myTaskHeader,
        ElContainer},

      name: "segment-check-page",

      data(){
        return{
          //task Info
          taskId:'',
          assignmentId:'',
          workerId:'',
          name:'',
          tags:{}, //不这样用就会报错qwq我也没办法
        }
      },

      created(){
        this.taskId=this.$route.params.taskId
        this.assignmentId=this.$route.params.assignmentId
        this.workerId=this.$route.params.workerId

        this.getTaskInfo()
        this.getTaskName()
      },

      methods: {

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

      }
    }
</script>

<style scoped>

</style>
