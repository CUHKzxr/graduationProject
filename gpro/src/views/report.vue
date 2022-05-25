<template>
  <div class="main1">
    <p class="title">RSi报告与RSS报告</p>
    <div>
      <el-form ref="form" :model="searchConditions" label-width="80px" style="border:solid">
        <el-date-picker
              v-model="searchConditions.timestamprange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-ddTHH:mm:SSZ">
        </el-date-picker>
        
        <el-button type="primary" @click="onSubmit">查找</el-button>
      </el-form>
    </div>
    <div>
      <el-tabs value="latency" stretch:true>
        <el-tab-pane label="延迟" name="latency">
          <el-tabs value="latencyIpv4" stretch:true>
            <el-tab-pane label="IPv4" name="latencyIpv4">
              <div ref="latencyIpv4Chart" :style="{ width, height }"></div>
            </el-tab-pane>
            <el-tab-pane label="IPv6" name="latencyIpv6">
              <div ref="latencyIpv6Chart" :style="{ width, height }"></div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
        <el-tab-pane label="可用性" name="avaliability">
          <el-tabs value="availabilityIpv4" stretch:true>
            <el-tab-pane label="IPv4" name="availabilityIpv4">
              <div ref="availabilityIpv4Chart" :style="{ width, height }"></div>
            </el-tab-pane>
            <el-tab-pane label="IPv6" name="avaliabilityIpv6">
              <div ref="availabilityIpv6Chart" :style="{ width, height }"></div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
        <el-tab-pane label="正确性" name="correctness">
          <div ref="correctnessChart" :style="{ width, height }"></div>
        </el-tab-pane>
        <el-tab-pane label="根区发布延迟" name="publicationLatency">
          <div ref="publicationLatencyChart" :style="{ width, height }"></div>
        </el-tab-pane>
      </el-tabs>
    </div>

    
    <div class="tableContainer">
      <div >
        <el-table :data="rsiTableData" :span-method="spanMethodRsi" 
        :cell-class-name="cellClassNameRsi" 
        border:true style="width:1206px">
          <el-table-column label="Time" width="150">
            <template slot-scope="scope">
              {{scope.row.time}}
            </template>
          </el-table-column>
          <el-table-column label="RSI" width="150">
            <template slot-scope="scope">
              {{scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column label="Tansport" width="150">
            <template slot-scope="scope">
              {{scope.row.transport}}
            </template>
          </el-table-column>
          <el-table-column label="Time: 2022-3-27T" >
            <el-table-column label="Availability" width="150">  
              <template slot-scope="scope">
                {{scope.row.availability}}
              </template>
            </el-table-column>
            <el-table-column label="Response Latency" width="150">
              <template slot-scope="scope">
                {{scope.row.responseLatency}}
              </template>
            </el-table-column>
            <el-table-column label="Correctness" width="150">
              <template slot-scope="scope">
                {{scope.row.correctness}}
              </template>
            </el-table-column>
            <el-table-column label="Publication Latency" width="155">
              <template slot-scope="scope">
                {{scope.row.publicationLatency}}
              </template>
            </el-table-column>
            <el-table-column label="Measuremnets" width="150"> 
              <template slot-scope="scope">
                {{scope.row.measurements}}
              </template>
            </el-table-column>
          </el-table-column>
        </el-table>
        <br>
        <el-pagination
          @size-change="handleSizeChangeRsi" 
          @current-change="handleCurrentChangeRsi" 
          :page-size="paginationRsi.pageSize" 
          :current-page="paginationRsi.currentPage"
          :total="paginationRsi.total"
          :page-sizes="[12, 20, 40, 120]"  
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <br>
        <el-table :data=rssTableData :span-method="spanMethodRss" 
        :cell-class-name="cellClassNameRss" style="width:1056px"
        border:true >
          <el-table-column label="Time" width="150">
            <template slot-scope="scope">
              {{scope.row.time}}
            </template>
          </el-table-column>
          <el-table-column label="Transport" width="150">
            <template slot-scope="scope">
              {{scope.row.transport}}
            </template>
          </el-table-column>
          <el-table-column label="Availability" width="150">
            <template slot-scope="scope">
              {{(scope.row.availability*100).toFixed(3)+"%"}}
            </template>
          </el-table-column>
          <el-table-column label="Response Latency" width="150">
            <template slot-scope="scope">
              {{scope.row.responseLatency+"ms"}}
            </template>
          </el-table-column>
          <el-table-column label="Correctness" width="150">
            <template slot-scope="scope">
              {{(scope.row.correctness*100).toFixed(3)+"%"}}
            </template>
          </el-table-column>
          <el-table-column label="Publication Latency" width="155">
            <template slot-scope="scope">
              {{scope.row.publicationLatency+"min"}}
            </template>
          </el-table-column>
          <el-table-column label="Measuremnets" width="150">
            <template slot-scope="scope">
              {{scope.row.measurements}}
            </template>
          </el-table-column>
        </el-table>
        <br>
        <el-pagination
          @size-change="handleSizeChangeRss" 
          @current-change="handleCurrentChangeRss" 
          :page-size="paginationRss.pageSize" 
          :current-page="paginationRss.currentPage"
          :total="paginationRss.total"
          :page-sizes="[12, 20, 40, 120]"  
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>
    </div>
    
  </div>
</template>

<script>
import echarts from "echarts";
import {
  getReport
} from "@/api/report";
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
      searchConditions:{
        timestamprange:[]
      },
      showConditions:{
        isIndeterminate1:false,
        isCheckAll1:false,
        checkedNameList:[]
      },
      rsidata:[],
      rssdata: [],
      rsiTableData:[],
      rssTableData:[],
      latencyIpv4Chart:'',
      latencyIpv6Chart:'',
      availabilityIpv4Chart:'',
      availabilityIpv6Chart:'',
      correctnessChart:'',
      publicationLatencyChart:'',
      paginationRsi:{
        pageSize:12,
        currentPage:1,
        total:0
      },
      paginationRss:{
        pageSize:12,
        currentPage:1,
        total:0
      },
      testOption:{}
    };
  },
  mounted() {
    this.init();
    this.resize();
    this.$nextTick(() => {
      this.initEchart("latencyIpv4Chart");
      this.initEchart("latencyIpv6Chart");
      this.initEchart("availabilityIpv4Chart");
      this.initEchart("availabilityIpv6Chart");
      this.initEchart("correctnessChart");
      this.initEchart("publicationLatencyChart");
      if (false&&!this.loopId ) {
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
          this.latencyIpv4Chart.resize();
          this.latencyIpv6Chart.resize();
          this.availabilityIpv4Chart.resize();
          this.availabilityIpv6Chart.resize();
          this.correctnessChart.resize();
          this.publicationLatencyChart.resize();
        });
      });
    },
    initEchart(chartName) {
      this[chartName] = echarts.init(this.$refs[chartName]);
    },
    
    async onSubmit(){
      const resdata = await getReport({
        data:this.searchConditions
      });
      this.rsidata=[];
      this.rssdata=[];
      for(let rsi of resdata.rsi){
        let publicationLatency=rsi.publication_latency.value==1?"<=65min":">65min";
        let correctness=rsi.correctness.value==1?"100%":"<100%";
        this.rsidata.push({
          time:rsi.time,
          name:rsi.name,
          transport: "ipv4-udp",
          availability:rsi.ipv4_udp_availability.value==1?">=96%":"<96%",
          responseLatency:rsi.ipv4_udp_latency.value==1?"<=250ms":">250ms",
          correctness: correctness,
          publicationLatency:publicationLatency,
          measurements:rsi.ipv4_udp_availability.measurements
        });
        this.rsidata.push({
          time:rsi.time,
          name:rsi.name,
          transport: "ipv4-tcp",
          availability:rsi.ipv4_tcp_availability.value==1?">=96%":"<96%",
          responseLatency:rsi.ipv4_tcp_latency.value==1?"<=500ms":">500ms",
          correctness: correctness,
          publicationLatency:publicationLatency,
          measurements:rsi.ipv4_tcp_availability.measurements
        });
        this.rsidata.push({
          time:rsi.time,
          name:rsi.name,
          transport: "ipv6-udp",
          availability:rsi.ipv6_udp_availability.value==1?">=96%":"<96%",
          responseLatency:rsi.ipv6_udp_latency.value==1?"<=250ms":">250ms",
          correctness: correctness,
          publicationLatency:publicationLatency,
          measurements:rsi.ipv6_udp_availability.measurements
        });
        this.rsidata.push({
          time:rsi.time,
          name:rsi.name,
          transport: "ipv6-tcp",
          availability:rsi.ipv6_tcp_availability.value==1?">=96%":"<96%",
          responseLatency:rsi.ipv6_tcp_latency.value==1?"<=500ms":">500ms",
          correctness: correctness,
          publicationLatency:publicationLatency,
          measurements:rsi.ipv6_tcp_availability.measurements
        });
      }
      //let time=this.searchConditions.timestamprange[0]+"--"+this.searchConditions.timestamprange[1];
      for(let rss of resdata.rss){
        this.rssdata.push({
          time: rss.timestamp,
          transport: "ipv4-udp",
          availability:rss.availabilityIpv4Udp,
          responseLatency:rss.latencyIpv4Udp,
          correctness: rss.correctness,
          publicationLatency:rss.publicationLatency,
          measurements:rss.ipv4UdpAvailabilityMeasurements
        });
        this.rssdata.push({
          time: rss.timestamp,
          transport: "ipv4-tcp",
          availability:rss.availabilityIpv4Tcp,
          responseLatency:rss.latencyIpv4Tcp,
          correctness: rss.correctness,
          publicationLatency:rss.publicationLatency,
          measurements:rss.ipv4UdpAvailabilityMeasurements
        });
        this.rssdata.push({
          time: rss.timestamp,
          transport: "ipv6-udp",
          availability:rss.availabilityIpv6Udp,
          responseLatency:rss.latencyIpv6Udp,
          correctness: rss.correctness,
          publicationLatency:rss.publicationLatency,
          measurements:rss.ipv4UdpAvailabilityMeasurements
        });
        this.rssdata.push({
          time: rss.timestamp,
          transport: "ipv6-tcp",
          availability:rss.availabilityIpv6Tcp,
          responseLatency:rss.latencyIpv6Tcp,
          correctness: rss.correctness,
          publicationLatency:rss.publicationLatency,
          measurements:rss.ipv4UdpAvailabilityMeasurements
        });

      }
      this.setLatencyIpv4Chart(resdata.rss);
      this.setLatencyIpv6Chart(resdata.rss);
      this.setAvailabilityIpv4Chart(resdata.rss);
      this.setAvailabilityIpv6Chart(resdata.rss);
      this.setCorrectnessChart(resdata.rss);
      this.setPublicationLatencyChart(resdata.rss)

      this.paginationRsi.total=this.rsidata.length;
      this.paginationRss.total=this.rssdata.length;
      this.handleSizeChangeRsi(this.paginationRsi.pageSize);
      this.handleSizeChangeRss(this.paginationRss.pageSize);
    },
    handleCheckedNameList(val){
      this.showConditions.isCheckAll1=val.length===13;
      this.showConditions.isIndeterminate1=this.showConditions.checkedNameList.length>0
        &&this.showConditions.checkedNameList.length<13;
    },
    handleCheckAllChange1(val){
      this.showConditions.checkedNameList=val?nameList:[];
      this.showConditions.isIndeterminate1=false;
    },
    spanMethodRsi({ row, column, rowIndex, columnIndex }) {
      if(columnIndex ==0 || columnIndex == 1 || columnIndex ==5 || columnIndex ==6|| columnIndex ==7){
        if(rowIndex%4 == 0){
          return [4,1];
        }else{
        return [0,0];
        } 
      }
    },
    spanMethodRss({ row, column, rowIndex, columnIndex }) {
      return this.spanMethodRsi({ row, column, rowIndex, columnIndex });
    },
    cellClassNameRsi({row, column, rowIndex, columnIndex}){
      if(columnIndex == 3){
        if(this.rsidata[rowIndex].availability=='>=96%'){
          return 'row-true';
        }else if(this.rsidata[rowIndex].availability=='<96%'){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 4){
        if(rowIndex %2 == 0){
          if(this.rsidata[rowIndex].responseLatency=='<=250ms'){
            return 'row-true';
          }else if(this.rsidata[rowIndex].responseLatency=='>250ms'){
            return 'row-false';
          }else {
            return '';
          }
        }else{
          if(this.rsidata[rowIndex].responseLatency=='<=500ms'){
            return 'row-true';
          }else if(this.rsidata[rowIndex].responseLatency=='>500ms'){
            return 'row-false';
          }else{
            return '';
          }
        }
      }else if(columnIndex == 5){
        if(this.rsidata[rowIndex].correctness=='100%'){
          return 'row-true';
        }else if(this.rsidata[rowIndex].correctness=='<100%'){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 6){
        if(this.rsidata[rowIndex].publicationLatency=='<=65min'){
          return 'row-true';
        }else if(this.rsidata[rowIndex].publicationLatency=='>65min'){
          return 'row-false';
        }else{
          return '';
        }
      }
    },
    cellClassNameRss({row, column, rowIndex, columnIndex}){
      if(columnIndex == 2){
        if(this.rssdata[rowIndex].availability>=0.99999){
          return 'row-true';
        }else if(this.rssdata[rowIndex].availability>=0){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 3){
        if(rowIndex %2 == 0){
          if(this.rssdata[rowIndex].responseLatency<=150&&this.rssdata[rowIndex].responseLatency>=0){
            return 'row-true';
          }else if(this.rssdata[rowIndex].responseLatency>150){
            return 'row-false';
          }else {
            return '';
          }
        }else{
          if(this.rssdata[rowIndex].responseLatency<=300&&this.rssdata[rowIndex].responseLatency>=0){
            return 'row-true';
          }else if(this.rssdata[rowIndex].responseLatency>=300){
            return 'row-false';
          }else{
            return '';
          }
        }
      }else if(columnIndex == 4){
        if(this.rssdata[rowIndex].correctness==1){
          return 'row-true';
        }else if(this.rssdata[rowIndex].correctness<1&&this.rssdata[rowIndex].correctness>=0){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 5){
        if(this.rssdata[rowIndex].publicationLatency<=65&&this.rssdata[rowIndex].publicationLatency>=0){
          return 'row-true';
        }else if(this.rssdata[rowIndex].publicationLatency>=65){
          return 'row-false';
        }else{
          return '';
        }
      }
    },
    //currentPage starts at 1
    getRsiTableData(pageSize, currentPage){
      this.rsiTableData=this.rsidata.slice((currentPage-1)*pageSize,currentPage*pageSize);
    },
    getRssTableData(pageSize, currentPage){
      this.rssTableData=this.rssdata.slice((currentPage-1)*pageSize,currentPage*pageSize);
    },
    handleSizeChangeRsi(newPageSize){
      this.paginationRsi.pageSize=newPageSize;
      this.paginationRsi.currentPage=1;
      this.getRsiTableData(newPageSize, 1);
    },
    handleCurrentChangeRsi(newCurrentPage){
      this.paginationRsi.currentPage=newCurrentPage;
      this.getRsiTableData(this.paginationRsi.pageSize,newCurrentPage);
    },
    handleSizeChangeRss(newPageSize){
      this.paginationRss.pageSize=newPageSize;
      this.paginationRss.currentPage=1;
      this.getRssTableData(newPageSize, 1);
    },
    handleCurrentChangeRss(newCurrentPage){
      this.paginationRss.currentPage=newCurrentPage;
      this.getRssTableData(this.paginationRss.pageSize,newCurrentPage);
    },
    setLatencyIpv4Chart(data){ 
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "latencyIpv4Udp",
            "latencyIpv4Tcp",
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          },
          {
            name: "tcp",
            type: "line",
            encode: {
              x: 0,
              y: 2,
            },
            label: {
              show: true,
            }
          }
        ],
      };
      this["latencyIpv4Chart"].setOption(option);
    },
    setLatencyIpv6Chart(data){ 
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "latencyIpv6Udp",
            "latencyIpv6Tcp",
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          },
          {
            name: "tcp",
            type: "line",
            encode: {
              x: 0,
              y: 2,
            },
            label: {
              show: true,
            }
          }
        ],
      };
      this["latencyIpv6Chart"].setOption(option);
    },
    setAvailabilityIpv4Chart(data){
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "availabilityIpv4Udp",
            "availabilityIpv4Tcp",
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          },
          {
            name: "tcp",
            type: "line",
            encode: {
              x: 0,
              y: 2,
            },
            label: {
              show: true,
            }
          }
        ],
      };
      this.testOption=option;
      this["availabilityIpv4Chart"].setOption(option);
    },
    setAvailabilityIpv6Chart(data){
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "availabilityIpv6Udp",
            "availabilityIpv6Tcp",
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          },
          {
            name: "tcp",
            type: "line",
            encode: {
              x: 0,
              y: 2,
            },
            label: {
              show: true,
            }
          }
        ],
      };
      this.testOption=option;
      this["availabilityIpv6Chart"].setOption(option);
    },
    setCorrectnessChart(data){
      
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "correctness"
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          }],
      };
      this.testOption=option;
      this["correctnessChart"].setOption(option);
    },
    setPublicationLatencyChart(data){
      
      const option = {
        grid:{
          left: 200
        },
        xAxis: [
          {
            type: "category",
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        dataset: {
          dimensions: [
            "timestamp",
            "publicationLatency"
          ],
          source: data
        },
        series: [
          {
            name: "udp",
            type: "line",
            encode: {
              x: 0,
              y: 1,
            },
            label: {
              show: true,
            }
          }],
      };
      this.testOption=option;
      this["publicationLatencyChart"].setOption(option);
    }


  }
  
};
</script>
<style lang="scss" scoped>
@import url('../css/common.css');
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
.tableContainer {
  margin: 0 auto;
  width: 1207px;
 

}
.table{
  width:1057px;
  
}

</style>