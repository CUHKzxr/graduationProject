//import Vue from 'vue'
import axios from 'axios'

// const vueInstance = new Vue()
// let loadingInstance
const axiosInstance = axios.create({
  baseURL: '/api/',
  timeout: 20000 // 请求超时时间： 20s
})

// 请求拦截
axiosInstance.interceptors.request.use((config) => {
  // loadingInstance = vueInstance.$loading({
  //   text: '正在加载...'
  // })
  const sessionID = localStorage.getItem('sessionid')
  if (sessionID) {
    config.headers.sessionID = sessionID
  }
  return config
}, err => {
  console.log('axios request err', err)
})

// 响应拦截
axiosInstance.interceptors.response.use((res) => {
  console.log('axios response success', res)
  return res.data
}, err => {
  console.log('axios response err', err)
  
})

export default axiosInstance
