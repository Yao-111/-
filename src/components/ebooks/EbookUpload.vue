<template>
    <div class="upload-form">
      <h2>上传电子教材</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>教材名称</label>
          <input v-model="form.title" required>
        </div>
        <div class="form-group">
          <label>文件上传</label>
          <input type="file" @change="handleFileUpload" accept=".pdf,.docx" required>
        </div>
        <button type="submit">提交</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        form: {
          title: '',
          file: null
        }
      }
    },
    methods: {
      handleFileUpload(e) {
        this.form.file = e.target.files[0]
      },
      async handleSubmit() {
        const formData = new FormData()
        formData.append('title', this.form.title)
        formData.append('file', this.form.file)
        
        try {
          await this.$store.dispatch('uploadEbook', formData)
          alert('上传成功')
          this.$router.push('/ebooks')
        } catch (error) {
          alert('上传失败')
        }
      }
    }
  }
  </script>