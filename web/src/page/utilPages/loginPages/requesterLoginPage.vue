<template>
  <login v-bind:role="'Requester'"></login>
</template>

<script>
  import Login from '../../../components/login'
  export default {
    name: "worker-login",
    components:{Login},
  }
</script>

<style scoped>

</style>
