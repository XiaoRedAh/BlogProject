<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      萧的博客后台管理系统
    </div>
    <div>
      不知道放啥，就先放个hexo搭建的博客吧：<a href="https://xiaoredah.github.io/">https://xiaoredah.github.io/</a>   
    </div>
    <div>
      啦啦啦啦啦水一水首页
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
