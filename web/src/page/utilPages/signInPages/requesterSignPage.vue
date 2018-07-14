<template>
  <req-sign v-bind:role="'Requester'"></req-sign>
</template>

<script>
  import ReqSign from '../../../components/signIn'
    export default {
        name: "requester-sign-page",
      components:{ReqSign}
    }
</script>

<style scoped>

</style>
