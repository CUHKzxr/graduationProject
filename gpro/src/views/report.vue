<template>
  <div class="main1">
    <p class="title">RSi报告与RSS报告</p>
    <div>
      
    </div>
    <div class="tableContainer">
      <div >
        <el-table :data="rsidata" :span-method="spanMethodRsi" 
        :cell-class-name="cellClassNameRsi" 
        border=true style="width:1056px">
          <el-table-column label="RSI" width="150">
            <template slot-scope="scope">
              {{scope.row.type}}
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
        <br>
        <el-table :data=rssdata :span-method="spanMethodRss" 
        :cell-class-name="cellClassNameRss" style="width:1056px"
        border=true >
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
      </div>
    </div>
    
  </div>
</template>

<script>
import echarts from "echarts";
import {
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
      rsidata:[{
          type: "A",
          transport: "ipv4-udp",
          availability:">=96%",
          responseLatency:"<=250ms",
          correctness: "100%",
          publicationLatency:">65min",
          measurements:1000
        },
        {
          type: "A",
          transport: "ipv4-tcp",
          availability:">=96%",
          responseLatency:">500ms",
          correctness: "<100%",
          publicationLatency:">65min",
          measurements:1000
        },
        {
          type: "A",
          transport: "ipv6-udp",
          availability:">=96%",
          responseLatency:"<=250ms",
          correctness: "<100%",
          publicationLatency:"<=65min",
          measurements:1000
        },
        {
          type: "A",
          transport: "ipv6-tcp",
          availability:"<96%",
          responseLatency:"<=500ms",
          correctness: "100%",
          publicationLatency:"<=65min",
          measurements:1000
        },
        
      ],
      rssdata: [{
          time: "2022-3-27T",
          transport: "ipv4-udp",
          availability:1.0,
          responseLatency:150,
          correctness: 1.0,
          publicationLatency:35,
          measurements:1000
        },
        {
          time: "2022-3-27T",
          transport: "ipv4-tcp",
          availability:0.99998,
          responseLatency:550,
          correctness: 1.0,
          publicationLatency:34,
          measurements:1000
        },
        {
          time: "2022-3-27T",
          transport: "ipv6-udp",
          availability:0.99999,
          responseLatency:299,
          correctness: 0.99999,
          publicationLatency:34,
          measurements:1000
        },
        {
          time: "2022-3-27T",
          transport: "ipv6-tcp",
          availability:1.0,
          responseLatency:298,
          correctness: 1.0,
          publicationLatency:34,
          measurements:1000
        }
      ],
    };
  },
  mounted() {
    this.init();
    //this.resize();
    this.$nextTick(() => {
      
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
          this.chart1.resize();
        });
      });
    },
    spanMethodRsi({ row, column, rowIndex, columnIndex }) {
      if(columnIndex == 0 || columnIndex ==4 || columnIndex ==5|| columnIndex ==6){
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
      if(columnIndex == 2){
        if(this.rsidata[rowIndex].availability=='>=96%'){
          return 'row-true';
        }else if(this.rsidata[rowIndex].availability=='<96%'){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 3){
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
      }else if(columnIndex == 4){
        if(this.rsidata[rowIndex].correctness=='100%'){
          return 'row-true';
        }else if(this.rsidata[rowIndex].correctness=='<100%'){
          return 'row-false';
        }else{
          return '';
        }
      }else if(columnIndex == 5){
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

    loop() {
      this.loopId = setInterval(() => {
        //方便浏览器调试的几个变量
        //let test1 = this.chartdata;
        //let test2 = this.timedata;
        //let test3 = this.latestN;

        
      }, this.loopTime);
    },
  },
  
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
  width: 1057px;
 

}
.table{
  width:1057px;
  
}

</style>