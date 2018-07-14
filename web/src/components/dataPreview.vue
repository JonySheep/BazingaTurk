<template>

  <div class="preBorder">
    <ul style="white-space: nowrap;margin: 10px">
      <li v-for="(img,index) in imgList" class="preview">
        <img v-bind:src=img style="width: 100%;height: 100%" @click="chooseData(index)">
      </li>
    </ul>
  </div>

</template>

<script>
    export default {
      name: "data-preview",

      data(){
        return{
          imgList:[],
          info:{}
        }
      },

      created(){
        this.getImgList()
      },

      props:['taskId','partitionId','workerId','counter','firstIndex','taskSize','taskType'],

      methods:{

        /**
         * 得到数据url列表
         */
        getImgList:function () {

          let annoURL='/assignment/'+this.taskId+'/'+this.partitionId+'/'+this.workerId+'/segmentation/resources/'

          let list=[]
          for(let i=0;i<this.taskSize;i++){
            console.log(i)
            this.$api.get(annoURL+(this.firstIndex+i),null, r => {
              list.push('http://localhost:8080/'+r.data.imageUrl)
            })
          }

          this.setImgList(list)
        },


        /**
         * 封装imgList的赋值方法
         * @param list
         */
        setImgList:function (list) {
          this.imgList=list
        },


        /**
         * 点击某张图片的响应方法
         * @param imgUrl
         */
        chooseData:function (index) {
          this.$emit("changeCounter",index)
        }
      }
    }
</script>

<style scoped>

  .preBorder{
    border: darkgray 1px solid;
    height: 100px;
    overflow: scroll;

  }
  .preview{
    display: inline-block;
    width: 80px;
    height:80px;
    margin-right: 20px;
    margin-bottom: 10px;
    border: darkgray 1px solid;
  }
</style>
