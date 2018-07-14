<template>
    <div>
      <my-navi></my-navi>
      <my-homepic></my-homepic>
    </div>
</template>

<script>
  import MyHomepic from '../../components/homePic'
  import MyNavi from "../../components/header/homePageHeader";
    export default {
      components: {
        MyNavi,MyHomepic
      },
    }
</script>

<style scoped>

</style>
