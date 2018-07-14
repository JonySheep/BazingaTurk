<template>
  <div>
    <admin-header ></admin-header>
    <main-navi :type="'userManage'"></main-navi>
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
    name: "user-manage-pages",

  }
</script>

<style scoped>

</style>
