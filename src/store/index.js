import { createStore } from 'vuex'

export default createStore({
  state: {
    user: null,
    ebooks: [],
    transactions: []
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
    },
    SET_EBOOKS(state, ebooks) {
      state.ebooks = ebooks
    }
  },
  actions: {
    async login({ commit }, credentials) {
      // 实际应调用API
      const mockUser = { id: 1, username: credentials.username }
      commit('SET_USER', mockUser)
    },
    async fetchEbooks({ commit }) {
      // 实际应调用API
      const mockData = [
        { id: 1, title: 'Vue.js教程', author: '张三' },
        { id: 2, title: 'Python编程', author: '李四' }
      ]
      commit('SET_EBOOKS', mockData)
    }
  }
})