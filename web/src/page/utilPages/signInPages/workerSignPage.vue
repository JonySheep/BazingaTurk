<template>
  <worker-sign v-bind:role="'Worker'"></worker-sign>
</template>

<script>
  import WorkerSign from '../../../components/signIn'
  export default {
    name: "requester-sign-page",
    components:{WorkerSign}
  }
</script>

<style scoped>

</style>
