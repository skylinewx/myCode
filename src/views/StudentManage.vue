<comment>
    # 组件注释
</comment>
<template>
    <div class="StudentManage">
      {{testInfo}}
    </div>
</template>

<script>
export default {
  name: 'StudentManage',
  components: {},
  data () {
    return {
      loading: true,
      error: false,
      testInfo: '',
      students: []
    }
  },
  props: {},
  watch: {
    loading () {
      if (this.loading === false) {
        this.$Loading.finish()
      }
    },
    error () {
      if (this.error === true) {
        this.$Loading.error()
      }
    }
  },
  methods: {
    start () {
      this.$Loading.start()
    },
    end () {
      this.$Loading.finish()
    }
  },
  computed: {},
  created () {
    this.$Loading.start()
  },
  mounted () {
    this.$http.get('https://api.coindesk.com/v1/bpi/currentprice.json')
      .then(response => {
        this.testInfo = response
        this.loading = false
        this.error = false
      })
      .catch(error => {
        console.log(error)
        this.loading = false
        this.error = true
      })
  },
  destroyed () {
  }
}
</script>

<style lang="scss" scoped>
</style>
