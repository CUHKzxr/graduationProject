<template>
  <div class="main1">
    <p class="title">根服务器Ipv4延迟统计</p>
    <div class="container">
      <div ref="chart1" :style="{ width, height }"></div>
    </div>
    <p class="title">根服务器Ipv6延迟统计</p>
    <div class="container">
      <div ref="chart2" :style="{ width, height }"></div>
    </div>
    <p class="title">根服务数据查询</p>
    <el-form ref="form" :model="searchConditions" label-width="80px" style="border:solid">
      <el-row>
        <el-col span="8">
          <div  > 
            <el-date-picker
              v-model="searchConditions.timestamprange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-ddTHH:mm:SSZ">
            </el-date-picker>
          </div>
        </el-col>
        <el-col span="16">
          <div  >
            
            可用节点：
            <!-- TODO: 添加全选框 -->
            <el-checkbox :indeterminate="searchConditions.isIndeterminate0" v-model="searchConditions.checkAll0" @change="handleCheckAllChange0">全选</el-checkbox>
            <el-checkbox-group v-model="searchConditions.checkedProviders" @change="handleCheckedProviders">
              <el-checkbox v-for="provider in searchConditions.providers" :label="provider" :key="provider">{{provider}}</el-checkbox>
            </el-checkbox-group>
          </div>
          <div  >
            <el-checkbox :indeterminate="searchConditions.isIndeterminate1" v-model="searchConditions.checkAll1" @change="handleCheckAllChange1">全选</el-checkbox>
            <el-checkbox-group v-model="searchConditions.checkedNameList">
              <el-checkbox label="A" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="B" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="C" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="D" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="E" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="F" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="G" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="H" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="I" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="J" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="K" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="L" @change="handleCheckedNameList"></el-checkbox>
              <el-checkbox label="M" @change="handleCheckedNameList"></el-checkbox>
            </el-checkbox-group>
          </div>
        </el-col>
      </el-row>
      <el-button type="primary" @click="onSubmit">查找</el-button>
    </el-form>
    <div class="container">
      
    </div>
      <el-table :data="tabledata" height="650" width="150%">
        <el-table-column type="expand">
          
          <template slot-scope="props">
            <el-row>
              <el-col span="8">
                <el-table :data="props.row.path_ipv4" width="400">
                  <el-table-column label="序号" width="170" type="index"></el-table-column>
                  <el-table-column
                  label="Ipv4地址" width="170"
                  >
                    <template slot-scope="scope">
                      <span >{{scope.row}}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
              <el-col span="8">
                <el-table :data="props.row.path_ipv6" width="400">
                  <el-table-column label="序号" width="170" type="index"></el-table-column>
                  <el-table-column
                  label="Ipv6地址" width="300"
                  >
                    <template slot-scope="scope">
                      <span >{{scope.row}}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column
        label="时间戳" width="170"
         >
          <template slot-scope="scope">
            <span >{{scope.row.timestamp}}</span>
          </template>
        </el-table-column>
        <el-table-column
        label="探测节点"
         >
          <template slot-scope="scope">
            <span >{{scope.row.provider}}</span>
          </template>
        </el-table-column>
        <el-table-column
        label="类型" width="30"
         >
          <template slot-scope="scope">
            <span >{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column
        label="服务器标识名" width="250"
         >
          <template slot-scope="scope">
            <span >{{scope.row.identification}}</span>
          </template>
        </el-table-column>
        <el-table-column label="查询延迟">
          <el-table-column label="Ipv4">
            <el-table-column label="UDP" width="60"> 
              <template slot-scope="scope">
                {{scope.row.query_latency_ipv4_udp}}
              </template>
            </el-table-column>
            <el-table-column label="TCP" width="60">
              <template slot-scope="scope">
                {{scope.row.query_latency_ipv4_tcp}}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="Ipv6">
            <el-table-column label="UDP" width="60">
              <template slot-scope="scope">
                {{scope.row.query_latency_ipv6_udp}}
              </template>
            </el-table-column>
            <el-table-column label="TCP" width="60">
              <template slot-scope="scope">
                {{scope.row.query_latency_ipv6_tcp}}
              </template>
            </el-table-column>
          </el-table-column>
        </el-table-column>
        <el-table-column
        label="状态" width="100"
         >
          <template slot-scope="scope">
            <span >{{scope.row.status}}</span>
          </template>
        </el-table-column>
        <el-table-column label="服务器地址">
          <el-table-column
          label="Ipv4" width="120"
           >
            <template slot-scope="scope">
              <span >{{scope.row.source_ip_ipv4}}</span>
            </template>
          </el-table-column>
           <el-table-column
          label="Ipv6" width="280"
           >
            <template slot-scope="scope">
              <span >{{scope.row.source_ip_ipv6}}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="参考延迟">
          <el-table-column label="Ipv4">
            <el-table-column
            label="1" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv4_0}}</span>
              </template>
            </el-table-column>
             <el-table-column
            label="2" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv4_1}}</span>
              </template>
            </el-table-column>
             <el-table-column
            label="3" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv4_2}}</span>
              </template>
            </el-table-column>
            
          </el-table-column>
          <el-table-column label="Ipv6">
            <el-table-column
            label="1" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv6_0}}</span>
              </template>
            </el-table-column>
             <el-table-column
            label="2" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv6_1}}</span>
              </template>
            </el-table-column>
             <el-table-column
            label="3" width="50"
             >
              <template slot-scope="scope">
                <span >{{scope.row.refer_latency_ipv6_2}}</span>
              </template>
            </el-table-column>
          </el-table-column>
        </el-table-column>
        <el-table-column
        label="正确性"
         >
          <template slot-scope="scope">
            <span >{{scope.row.correctness}}</span>
          </template>
        </el-table-column>
      </el-table>
  </div>
</template>

<script>
import echarts from "echarts";
import {
  getLatestDemoChartDataTest,
  getSearchData,
  getLatestDemoChartData,
  changeTimestamp
} from "@/api/demochart";
import {
  parseIpv4String,
  parseIpv6String
} from "@/utils/ip";
//import refreshCache from "@/api/index";
const nameList=['A','B','C','D','E','F','G','H','I','J','K','L','M'];
export default {
  
  components: {},
  data() {
    return {
      height: "",
      width: "",
      chartdata: [],
      timedata: [],
      latestN: 16,
      loopTime: 3000,
      testtimestamp: "2022-03-27T09:20:54Z", //测试用的时间戳
      timestamp: "2022-03-27T09:20:54Z",
      loopId: "",
      identifications: [],
      searchConditions:{
        providers:["0"],
        checkedProviders:[],
        timestamprange:[],
        checkedNameList:[],
        isIndeterminate0:false,
        isIndeterminate1:false,
        isCheckAll0:false,
        isCheckAll1:false,
      },
      tabledata: [],
     
    };
  },
  mounted() {
    this.init();
    this.resize();
    //this.refreshCache()
    this.$nextTick(() => {
      this.initEchart("chart1");
      this.initEchart("chart2");
      this.setEchart1("chart1", this.timedata, this.chartdata);
      this.setEchart2("chart2", this.timedata, this.chartdata);
      //this.demoSetEchart("chart1");
      //this.getLatestNdata(this.latestN)
      if (!this.loopId ) {
        this.loop();
      }
    });
  },
  methods: {
    init() {
      this.width = window.innerWidth - 20 + "px";
      this.height = window.innerHeight - 50 + "px";
    },
    resize() {
      window.addEventListener("resize", () => {
        this.init();
        this.$nextTick(() => {
          this.chart1.resize();
        });
      });
    },
    initEchart(chartName) {
      this[chartName] = echarts.init(this.$refs[chartName]);
    },
    async onSubmit(){
      const resdata = await getSearchData({
        data: this.searchConditions
      });
      this.tabledata=resdata.data;
      for(let i = 0; i < this.tabledata.length; i++){
        this.tabledata[i].source_ip_ipv4=parseIpv4String(this.tabledata[i].source_ip_ipv4);
        //this.tabledata[i].source_ip_ipv6=parseIpv6String(this.tabledata[i].source_ip_ipv6);
        for(let j = 0; j < this.tabledata[i].path_ipv4.length;j++){
          this.tabledata[i].path_ipv4[j]=parseIpv4String(this.tabledata[i].path_ipv4[j]);
        }
        /*
        for(let k= 0;k<this.tabledata[i].path_ipv6.length;k++){
          this.tabledata[i].path_ipv6[k]=parseIpv6String(this.tabledata[i].path_ipv6[k]);
        }
        */
      }
      
      let test0=this.tabledata;
    },
    async getAlternativeProviders(){
      // TODO
    },
    async refreshCache() {
      //await refreshCache();
    },
    async getLatestNdata(latestN) {
      const resdata = await getLatestDemoChartData({
        latestN: latestN,
        //timestamp: this.testtimestamp,
      });

      if (!resdata || resdata.code !== "200") {
        return;
      }
      let flag = false;
      let identificationFlag = false;
      let reslatesttime = resdata.time[resdata.time.length - 1];
      if (this.timedata.length > 0) {
        let latesttime = this.timedata[this.timedata.length - 1];
        let d1 = new Date(latesttime),
          d2 = new Date(reslatesttime);
        if (d1 >= d2) {
          flag = false;
        } else {
          flag = true;
        }
      } else {
        flag = true;
      }
      if (flag) {
        //true:需要更新数据
        if (latestN == 1) {
          //仅获取最新数据
          this.timedata.shift();
          this.timedata.push(reslatesttime);

          //方便浏览器调试的几个变量
          //let test1 = this.chartdata;
          //let test2 = this.timedata;

          for (let i = 0; i < resdata.data.length; i++) {
            this.chartdata.shift();
            if (
              !this.identifications.includes(resdata.data[i].identification)
            ) {
              identificationFlag = true;
              this.identifications.push(resdata.data[i].identification);
            }
          }
          for (let i = 0; i < resdata.data.length; i++) {
            this.chartdata.push(resdata.data[i]);
          }
          /*
          this.chartdata.shift();
          this.chartdata.shift();
          this.chartdata.shift();
          this.chartdata.shift();
          this.chartdata.push(resdata.data[0]);
          this.chartdata.push(resdata.data[1]);
          this.chartdata.push(resdata.data[2]);
          this.chartdata.push(resdata.data[3]);
          */
        } else {
          this.timedata = resdata.time;
          this.chartdata = resdata.data;
          for (let i = 0; i < resdata.data.length; i++) {
            let newid=resdata.data[i].identification;
            if (!this.identifications.includes(newid)) {
              identificationFlag = true;
              this.identifications.push(resdata.data[i].identification);
            }
          }
        }
        let testidentifications = this.identifications;
        let test1 = this.chartdata;
        let test2 = this.timedata;
        if (identificationFlag) {
          this.identifications.sort();
          this.identifications.reverse();
          this.updateEchart("chart1", this.timedata,this.chartdata,this.identifications);
          this.updateEchart("chart2", this.timedata,this.chartdata,this.identifications);
        } else {
          this.updateEchart("chart1", this.timedata, this.chartdata);
          this.updateEchart("chart2", this.timedata, this.chartdata);
        }
      }

      //
      this.testtimestamp = changeTimestamp(this.testtimestamp, 300)
      console.log(this.testtimestamp)

      if (!this.loopId) {
        this.loop();
      }
      //updateEchart('chart1',resdata.time,resdata.data)
    },
    setEchart1(chartName, time, data) {
      const option = {
        grid:{
          left: 200
        },
        tooltip:{
          trigger:'item',
          formatter:(params)=>{
            var str="identification:  "+params.value[params.dimensionNames[params.encode.y[0]]];
            str+="</br>time:  "+params.value[params.dimensionNames[params.encode.x[0]]];
            return str;
          },
          axisPointer:{
            type: 'corss',

          }
        },
        xAxis: [
          {
            type: "time",
            position: "top",
          },
        ],
        yAxis: [
          {
            type: "category",
          },
        ],
        visualMap: [
          {
            type: "piecewise",
            dimensions: 2,
            inRange: {
              color: ["green", "orange", "red"],
            },
            min: 0,
            max: 460,
            maxOpen: true
          },
        ],
        dataset: {
          dimensions: [
            "time",
            "identification",
            "ipv4_latency_min",
            "ipv6_latency_min",
          ],
          source: data
        },
        series: [
          {
            name: "chartname",
            type: "scatter",
            encode: {
              x: 0,
              y: 1,
            },
          },
        ],
      };
      this[chartName].setOption(option);
    },
    setEchart2(chartName, time, data) {
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "time",
            position: "top",
          },
        ],
        yAxis: [
          {
            type: "category",
          },
        ],
        visualMap: [
          {
            type: "piecewise",
            dimensions: 3,
            inRange: {
              color: ["green", "orange", "red"],
            },
            min: 0,
            max: 460,
            maxOpen: true
          },
        ],
        dataset: {
          dimensions: [
            "time",
            "identification",
            "ipv4_latency_min",
            "ipv6_latency_min",
          ],
          source: data
        },
        series: [
          {
            name: "chartname",
            type: "scatter",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          },
        ],
      };
      this[chartName].setOption(option);
    },
    updateEchart(chartName, time, data, identifications) {
      if (typeof identifications == "undefined") {
        this[chartName].setOption({
          //**
          xAxis: {
            data: time,
          },
          //*/
          dataset: {
            source: data,
          },
        });
      } else {
        this[chartName].setOption({
          //**
          xAxis: {
            data: time,
          },
          //*/
          dataset: {
            source: data,
          },
          yAxis: {
            data: identifications,
          },
        });
      }
    },
    loop() {
      this.loopId = setInterval(() => {
        //方便浏览器调试的几个变量
        //let test1 = this.chartdata;
        //let test2 = this.timedata;
        //let test3 = this.latestN;

        this.getLatestNdata(
          this.chartdata.length === this.latestN ? 1 : this.latestN
        );
        this.getAlternativeProviders();
      }, this.loopTime);
      this.getAlternativeProviders();
    },

    demoSetEchart(chartName) {
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "time",
            //scale:true
            //boundaryGap: true
            data: [
              "2022-02-27T09:20:54Z",
              "2022-02-27T09:25:50Z",
              "2022-02-27T09:30:37Z",
              "2022-02-27T09:35:38Z",
              "2022-02-27T09:40:36Z",
              "2022-02-27T09:45:04Z",
              "2022-02-27T09:50:55Z",
              "2022-02-27T09:55:56Z",
              "2022-02-27T10:00:43Z",
              "2022-02-27T10:05:46Z",
              "2022-02-27T10:10:08Z",
              "2022-02-27T10:15:23Z",
              "2022-02-27T10:20:33Z",
              "2022-02-27T10:25:44Z",
              "2022-02-27T10:31:02Z",
              "2022-02-27T10:35:55Z",
            ],
          },
        ],
        yAxis: [
          {
            type: "category",
            //scale: true
            /*
            data:[
              
            ]
            */
          },
        ],
        visualMap: [
          {
            type: "piecewise",
            dimensions: 3,
            inRange: {
              symbolSize: [5, 30],
            },
            orient: "horizontal",
            min: 0,
            max: 460,
            maxOpen: true
          },
          {
            type: "piecewise",
            dimensions: 2,
            inRange: {
              color: ["green", "orange", "red"],
            },
            min: 0,
            max: 460,
            maxOpen: true
          },
        ],
        dataset: {
          dimensions: [
            "time",
            "identification",
            "ipv4_latency_min",
            "ipv6_latency_min",
          ],
          source:[]
        },
        series: [
          {
            name: "chartname",
            type: "scatter",
            encode: {
              x: 0,
              y: 1,
            },
          },
        ],
      };
      this[chartName].setOption(option);
    },
    handleCheckedProviders(val){
      this.searchConditions.isCheckAll0=val.length===this.searchConditions.providers.length;
      this.searchConditions.isIndeterminate0=this.searchConditions.checkedProviders.length>0
        && this.searchConditions.checkedProviders.length<this.searchConditions.providers.length;
      
    },
    handleCheckedNameList(val){
      this.searchConditions.isCheckAll1=val.length===13;
      this.searchConditions.isIndeterminate1=this.searchConditions.checkedNameList.length>0
        &&this.searchConditions.checkedNameList.length<13;
    },
    handleCheckAllChange0(val){
      this.searchConditions.checkedProviders=val?this.searchConditions.providers:[];
      this.searchConditions.isIndeterminate0=false;
    },
    handleCheckAllChange1(val){
      this.searchConditions.checkedNameList=val?nameList:[];
      this.searchConditions.isIndeterminate1=false;
    }
  },
  
};
</script>
<style lang="scss" scoped>
.main {
  max-width: 1680px;
  justify-content: center;
  margin: 0 auto;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 0;
}
.tip {
  display: flex;
  margin-top: 16px;
  .item {
    display: flex;
    align-items: center;
    margin-right: 16px;
  }
  .block {
    width: 30px;
    height: 20px;
    margin-right: 5px;
    &.block1 {
      background-color: rgb(69, 139, 0);
    }
    &.block2 {
      background-color: rgb(255, 215, 0);
    }
    &.block3 {
      background-color: rgb(219, 12, 147);
    }
    &.block4 {
      background-color: rgb(238, 102, 102);
    }
    &.block5 {
      background-color: rgb(233, 233, 233);
    }
  }
}
.title {
  font-size: 36px;
  text-align: center;
}
.div-chart {
  text-align: center;
}
.container {
  // color: white;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}
.label {
  font-size: 24px;
}

</style>