<template>
    <div class="login-form">
      <h2>用户登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="username" type="text" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="password" type="password" required>
        </div>
        <button type="submit">登录</button>
        <router-link to="/register">注册新用户</router-link>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: '',
        password: ''
      }
    },
    methods: {
      async handleLogin() {
        try {
          // 实际应调用API
          await this.$store.dispatch('login', {
            username: this.username,
            password: this.password
          });
          this.$router.push('/');
        } catch (error) {
          alert('登录失败');
        }
      }
    }
  }
  </script>