<template>
    <div>
      <SearchBar @search="handleSearch" />
      <div v-for="ebook in filteredEbooks" :key="ebook.id" class="ebook-item">
        <h3>{{ ebook.title }}</h3>
        <p>作者: {{ ebook.author }}</p>
        <router-link :to="`/ebooks/${ebook.id}`">查看详情</router-link>
      </div>
    </div>
  </template>
  
  <script>
  import SearchBar from '/src/components/common/SearchBar.vue'
  
  export default {
    components: { SearchBar },
    computed: {
      filteredEbooks() {
        return this.$store.state.ebooks.filter(ebook => 
          ebook.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          ebook.author.toLowerCase().includes(this.searchQuery.toLowerCase())
        )
      }
    },
    data() {
      return {
        searchQuery: ''
      }
    },
    methods: {
      handleSearch(query) {
        this.searchQuery = query
      }
    },
    created() {
      this.$store.dispatch('fetchEbooks')
    }
  }
  </script>