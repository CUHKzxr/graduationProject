export function parseIpv4String(number){
  if (number==0){
    return '* * *';
  }
  var ip = [];
  for (var i = 0; i <4; i++) {
    ip[i] = number & 0xff;
    number = number >> 8;
  }
  
  return ip.join(".");			    
}
export function parseIpv6String(bytes){
  if(bytes=="* * *" || bytes=="* * * "){
    return '* * *';
  }
  var r="";
  for (var i = 0; i <14; i+=2) {
    r+=bytes[i].toString(16)+bytes[i+1].toString(16)+":";
  }
  r+=bytes[14].toString(16)+bytes[15].toString(16);
}
