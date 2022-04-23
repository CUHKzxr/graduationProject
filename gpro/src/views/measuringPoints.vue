<template>
  <div class="main1">
    <p class="title">测量节点</p>
    <div>
      <el-button size='mini' @click="getMeasuringPointsData">
        刷新
      </el-button>
    </div>
    <div class="tableContainer">
      <div >
        <el-table :data="measuringPointsData" 
        border=true >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table :data="props.row.tasks">
                <el-table-column label="ID" width="100">
                  <template slot-scope="scope">
                    {{scope.row.id}}
                  </template>
                </el-table-column>
                <el-table-column label="efficient" width="100">
                  <template slot-scope="scope">
                    {{scope.row.effective}}
                  </template>
                </el-table-column>
                <el-table-column label="开始时间" width="150">
                  <template slot-scope="scope">
                    {{scope.row.startTimestamp}}
                  </template>
                </el-table-column>
                <el-table-column label="结束时间" width="150">
                  <template slot-scope="scope">
                    {{scope.row.endTimestamp}}
                  </template>
                </el-table-column>
                <el-table-column label="描述" width="300">
                  <template slot-scope="scope">
                    {{scope.row.text}}
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column label="名称" >
            <template slot-scope="scope">
              {{scope.row.identification}}
            </template>
          </el-table-column>
          <el-table-column label="物理位置" >
            <template slot-scope="scope">
              {{scope.row.location}}
            </template>
          </el-table-column>
          <el-table-column label="IPv4地址" >
            <template slot-scope="scope">
              {{scope.row.ipv4}}
            </template>
          </el-table-column>
          <el-table-column label="IPv6地址" >
            <template slot-scope="scope">
              {{scope.row.ipv6}}
            </template>
          </el-table-column>
          <el-table-column label="状态" >
            <template slot-scope="scope">
              {{scope.row.status}}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <br>
        <br>
      </div>
    </div>
    
  </div>
</template>

<script>
import echarts from "echarts";
import {
  submitEdit,
  submitDelete,
  getLatestMeasuringPointsData
} from "@/api/measuringPoints";
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
      loopTime: 3000,
      measuringPointsData:[
        {
          identification:"testpoint1",
          location:"testlocation",
          ipv4:"test",
          ipv6:"test",
          status:"testStatus",
          currentTasks:"test"
        },
        {
          identification:"testpoint2",
          location:"testlocation",
          ipv4:"test",
          ipv6:"test",
          status:"testStatus",
          currentTasks:"test"
        },
        {
          identification:"testpoint3",
          location:"testlocation",
          ipv4:"test",
          ipv6:"test",
          status:"testStatus",
          currentTasks:"test"
        }

      ],
      formData:{},
    };
  },
  mounted() {
    this.init();
    //this.resize();
    this.$nextTick(() => {
      this.getMeasuringPointsData();
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


    async getMeasuringPointsData(){
      const resdata = await getLatestMeasuringPointsData({

      });
      let test=resdata.data;
      this.measuringPointsData=resdata.data;
    },

    handleEdit(index, row) {
      console.log(index, row);
    },


    handleDelete(index, row) {
      console.log(index, row);
    },

    loop() {
      this.loopId = setInterval(() => {
        //方便浏览器调试的几个变量
        this.getMeasuringPointsData();
        
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