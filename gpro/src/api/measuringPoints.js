import axios from '../utils/axios'
export function submitEdit(data){
  return axios({
    method: 'post',
    url: '/',
    data
  })
}

export function submitDelete(data){
  return axios({
    method: 'post',
    url: '/',
    data
  })
}

export function getLatestMeasuringPointsData(data){
  return axios({
    method: 'post',
    url: '/measuringPoints/getData',
    data
  })
}