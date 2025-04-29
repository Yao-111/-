import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 全局样式
import '/src/assets/base.css'

const app = createApp(App)

// 注入路由和状态管理
app.use(router)
app.use(store)

app.mount('#app')