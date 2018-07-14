<template>
  <div>
    <admin-header ></admin-header>
    <main-navi :type="'home'"></main-navi>
  </div>
</template>

<script>
  import adminHeader from '../../components/header/adminPageHeader'
  import mainNavi from '../../components/adminManage/mainNavi'
    export default {
      components: {
        adminHeader,
        mainNavi,
      },
      name: "admin-main-pages",

    }
</script>

<style scoped>

</style>
