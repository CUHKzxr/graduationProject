import axios from '../utils/axios'

export function getLatestMeasuringPointsData(data){
  return axios({
    method: 'post',
    url: '/measuringPoints/getData',
    data
  })
}

export function add(data){
  return axios({
    method: 'post',
    url: '/measuringPoints/add',
    data
  })
}

export function edit(data){
  return axios({
    method: 'post',
    url: '/measuringPoints/edit',
    data
  })
}

export function del(data){
  return axios({
    method: 'post',
    url: '/measuringPoints/del',
    data
  })
}