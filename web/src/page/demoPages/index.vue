<template>
  <div>
    <myHeader></myHeader>

    <div class="article_list">
      <ul>
        <li v-for="i in list">
          <time v-text="$utils.goodTime(i.create_at)"></time>
          <router-link :to="'/content/' + i.id">
            {{ i.title }}
          </router-link>

        </li>
        <li>
          {{classification}}
        </li>

        <li>
          {{detection}}
        </li>

        <li>
          {{segmentation}}
        </li>
      </ul>
    </div>

    <myFooter></myFooter>
  </div>
</template>


<script>
  import myHeader from '../../components/header.vue'
  import myFooter from '../../components/footer.vue'
  export default {
    components: { myHeader, myFooter },
    data () {
      return {
        classification:null,
        detection:null,
        segmentation:null,
      }
    },
    created () {
      this.getData()
    },
    methods: {
      getData () {
        this.$api.get('/tasks/001/classification/resources/001', null, r => {
          this.classification = r
        }),
        this.$api.get('/tasks/002/detection/resources/001', null, r => {
          this.detection = r
        }),
        this.$api.get('/tasks/003/segmentation/resources/001', null, r => {
          this.segmentation = r
        })
      }
    }
  }
</script>

<style scoped>

</style>
