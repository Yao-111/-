import axios from 'axios'

// 创建axios实例
const request = axios.create({
    baseURL: 'http://localhost:9002/api',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 响应拦截器
request.interceptors.response.use(
    response => response.data,
    error => {
        console.error('Request failed:', error.response?.data);
        if (error.response?.data?.errors) {
            // 处理验证错误
            const errors = error.response.data.errors;
            const errorMessage = Object.values(errors).join('\n');
            throw new Error(errorMessage);
        } else if (error.response?.data?.message) {
            // 处理业务错误
            throw new Error(error.response.data.message);
        } else {
            // 处理其他错误
            throw new Error('请求失败，请稍后重试');
        }
    }
);

export const sendVerificationCode = (data) => {
    return request.post('/auth/send-code', data);
};

export const register = (data) => {
    return request.post('/auth/register', data);
}; 