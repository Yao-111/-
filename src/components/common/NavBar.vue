<template>
    <nav class="navbar">
      <div class="brand">
        <router-link to="/">教材论坛</router-link>
      </div>
      <div class="nav-links">
        <router-link to="/ebooks">电子教材</router-link>
        <router-link to="/transactions">二手交易</router-link>
        <template v-if="!user">
          <router-link to="/login">登录</router-link>
          <router-link to="/register">注册</router-link>
        </template>
        <template v-else>
          <router-link to="/profile">{{ user.username }}</router-link>
          <a href="#" @click.prevent="logout">退出</a>
        </template>
      </div>
    </nav>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  
  export default {
    computed: {
      ...mapState(['user'])
    },
    methods: {
      logout() {
        this.$store.commit('SET_USER', null)
        this.$router.push('/login')
      }
    }
  }
  </script>
  
  <style scoped>
  .navbar {
    background-color: #2c3e50;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .brand a {
    color: white;
    font-size: 1.5rem;
    text-decoration: none;
  }
  
  .nav-links {
    display: flex;
    gap: 1.5rem;
  }
  
  .nav-links a {
    color: white;
    text-decoration: none;
  }
  
  .nav-links a:hover {
    color: #42b983;
  }
  </style>