import axios from '../utils/axios'

export function getReport(data) {
  return axios({
    method: 'post',
    url: '/report/getReport',
    data
  })
}

export function getReports(data) {
  return axios({
    method: 'post',
    url: '/report/getReport',
    data
  })
}