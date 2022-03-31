/**关于散点图的接口 */
import axios from '../utils/axios'

export function getLatestDemoChartData(data) {
  return axios({
    method: 'post',
    url: '/showdata/updatelocal',
    data
  })
}
export function getSearchData(data) {
  return axios({
    method: 'post',
    url: '/showdata/search',
    data
  })
}
export function getLatestDemoChartDataTest(requestdata) {
  var testtime ='2022-01-02T13:01:34Z'
  var latestN=requestdata.latestN
  var testtimestamp=changeTimestamp(testtime, requestdata.testtimestamp)
  var data=[];
  var time=[];
  if(latestN>1){
    for (var i=0; i<latestN;i++){
      time.push(changeTimestamp(testtimestamp,i));
      data.push(generateRandomData(time[i]))
      data.push(generateRandomData(time[i]))
      data.push(generateRandomData(time[i]))
      data.push(generateRandomData(time[i]))
    }
  }else if (latestN==1){
      time.push(changeTimestamp(testtimestamp,15));
      data.push(generateRandomData(time[0]))
      data.push(generateRandomData(time[0]))
      data.push(generateRandomData(time[0]))
      data.push(generateRandomData(time[0]))
  }
  
  return {
    code: '200',
    data: data,
    time: time

  };
}

export function changeTimestamp(time, num) {
  var year = parseInt(time.substring(0, 4))
  var month = parseInt(time.substring(5, 7))
  var day = parseInt(time.substring(8, 10))
  var hour = parseInt(time.substring(11, 13))
  var minute = parseInt(time.substring(14, 16))
  var second = parseInt(time.substring(17, 19))
  second += num;
  minute += Math.floor(second / 60)
  second =second % 60
  second = second >=0 ? second :second+60
  hour += Math.floor(minute / 60)
  minute = minute % 60
  minute = minute >=0 ? minute:minute+60
  day +=Math.floor(hour/24)
  hour = hour % 24
  hour = hour>=0? hour:hour+24
  day=day-1
  month+=Math.floor(day/30)
  day =day % 30
  day = day>=0 ? day+1:day+31
    
  if(month >= 10){
    month = '' + month
  }else{
    month = '0'+month
  }
  if(day>=10){
    day=''+day
  }else{
    day='0'+day
  }
  if(hour>=10){
    hour=''+hour
  }else{
    hour='0'+hour
  }
  if(minute>=10){
    minute=''+minute
  }else{
    minute='0'+minute
  }
  if(second>=10){
    second=''+second
  }else{
    second='0'+second
  }
  var r=''+year+'-'+month+'-'+day+'T'+hour+':'+minute+':'+second+'Z'
  console.log(r)
  return r
}

function generateRandomData(time) {
  return {
    time: time,
    indentification: randomNum(0, 4) + '-xxxx',
    value1: randomNum(0, 30),
    value2: randomNum(0, 300)
  }
}


function randomNum(minNum, maxNum) {
  switch (arguments.length) {
    case 1:
      return parseInt(Math.random() * minNum + 1, 10);

    case 2:
      return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);

    default:
      return 0;

  }
} 