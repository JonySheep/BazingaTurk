<template>

  <div class="block">
    <el-carousel height="600px" indicator-position="outside">
      <el-carousel-item v-for="(item,index) in imgs" :key="item">
        <p style="text-align: center;margin:0px;background-color: coral;color: white">{{index+1}}</p>
        <img :src=item style="height: 590px;width: 100%;" />
      </el-carousel-item>
    </el-carousel>
  </div>

</template>

<script>
  export default {
    name: "detetion-guide",
    data(){
      return{
        imgs:[
          'server/requesterImg/detetion1.png',
          'server/requesterImg/detetion2.png',
          'server/requesterImg/detetion3.png',
        ]
      }
    }
  }
</script>
<style>

</style>
