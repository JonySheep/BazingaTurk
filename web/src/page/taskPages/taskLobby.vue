<template>
    <div>
      <manage_header></manage_header>
      <task-lobby-list></task-lobby-list>
    </div>
</template>

<script>
  import ElContainer from "element-ui/packages/container/src/main";
  import ElMain from "element-ui/packages/main/src/main";
  import ElHeader from "element-ui/packages/header/src/main";
  import manage_header from "../../components/header/taskPageHeader"
  import taskLobbyList from "../../components/taskLobbyList"
    export default {
    components:{
      ElHeader,
      ElMain,
      ElContainer,
      manage_header,
      taskLobbyList
    },
        name: "task-lobby"
    }
</script>

<style>

</style>
