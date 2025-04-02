<template>
  <div id="app">
    <NavBar />
    <main class="container">
      <router-view />
    </main>
    <footer class="app-footer">
      <p>© 2023 教育资源论坛</p>
    </footer>
  </div>
</template>

<script>
import NavBar from '/src/components/common/NavBar.vue'

export default {
  components: {
    NavBar
  }
}
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.app-footer {
  background-color: #f5f5f5;
  padding: 20px;
  text-align: center;
  margin-top: auto;
}

/* 基础样式可以放在单独的CSS文件中 */
</style>