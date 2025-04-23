import axios from 'axios'

const baseURL = 'http://localhost:9002/api'

// 创建axios实例
const request = axios.create({
    baseURL,
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        return Promise.reject(error)
    }
)

// 发送验证码
export const sendVerificationCode = (data) => {
    return request({
        url: '/auth/send-code',
        method: 'post',
        data
    })
}

// 用户注册
export const register = (data) => {
    return request({
        url: '/auth/register',
        method: 'post',
        data
    })
}

// 用户登录
export const login = (data) => {
    return request({
        url: '/auth/login',
        method: 'post',
        data
    })
}

// 获取用户信息
export const getUserInfo = () => {
    return request({
        url: '/auth/user-info',
        method: 'get'
    })
}

// 退出登录
export const logout = () => {
    return request({
        url: '/auth/logout',
        method: 'post'
    })
}

// 修改密码
export const changePassword = (data) => {
    return request({
        url: '/auth/change-password',
        method: 'post',
        data
    })
}