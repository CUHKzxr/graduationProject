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
        <el-table :data="pagedTableData" 
        border:true >
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
        <el-pagination
          @size-change="handleSizeChange" 
          @current-change="handleCurrentChange" 
          :page-size="pagination.pageSize" 
          :current-page="pagination.currentPage"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"  
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <br>
        <el-button size='mini' @click="addDialogVisible = true">
          新节点
        </el-button>
        <el-dialog
          title="添加节点"
          :visible.sync="addDialogVisible"
          width="30%">
          <el-form ref="addForm" :model="newPoint" label-width="100px">
           <el-form-item label="节点名">
              <el-input v-model="newPoint.provider"></el-input>
            </el-form-item>
            <el-form-item label="位置或说明">
              <el-input v-model="newPoint.location"></el-input>
            </el-form-item>
            <el-form-item label="IPv4地址">
              <el-input v-model="newPoint.IPv4Address"></el-input>
            </el-form-item>
            <el-form-item label="IPv6地址">
              <el-input v-model="newPoint.IPv6Address"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="addDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="onSubmitAdd">新 建</el-button>
          </span>
        </el-dialog>
        <el-dialog
          title="编辑节点"
          :visible.sync="editDialogVisible"
          width="30%">
          <el-form ref="editForm" :model="newPoint" label-width="100px">
            <el-form-item label="节点名">
              {{this.newPoint.provider}}
            </el-form-item>
            <el-form-item label="位置或说明">
              <el-input v-model="newPoint.location"></el-input>
            </el-form-item>
            <el-form-item label="IPv4地址">
              <el-input v-model="newPoint.IPv4Address"></el-input>
            </el-form-item>
            <el-form-item label="IPv6地址">
              <el-input v-model="newPoint.IPv6Address"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="onSubmitEdit">编 辑</el-button>
          </span>
        </el-dialog>
        <br>
      </div>
    </div>
    
  </div>
</template>

<script>
import echarts from "echarts";
import {
  getLatestMeasuringPointsData,
  add,edit,del
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
      addDialogVisible:false,
      editDialogVisible:false,
      loopTime: 3000,
      newPoint:{
        provider:'',
        localtion:'',
        IPv4Address:'',
        IPv6Address:'',
        
      },
      measuringPointsData:[],
      pagedTableData:[],
      pagination:{
        currentPage:1,
        pageSize:10,
        total:0
      },
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
      this.width = window.innerWidth - 100 + "px";
      this.height = window.innerHeight - 200 + "px";
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
      this.measuringPointsData=resdata.data;
      let i=0;
      for(;i<this.measuringPointsData.length;i++) {
        this.measuringPointsData[i].ipv4=parseIpv4String(this.measuringPointsData[i].ipv4);
      }
      this.pagination.total=this.measuringPointsData.length;
      this.handleSizeChange(this.pagination.pageSize);
      
    },
    checkPoint(){
      if((this.newPoint.provider.match(/[^_\w\d]/)!=null)
            ||(this.newPoint.location.match(/[^_\w\d]/)!=null)){
        this.$message({
          message: '名称和描述中包含非法字符',
          type: 'warning'
        });
        return false;
      }
      let ipv4Nums=this.newPoint.IPv4Address.split(".");
      if(ipv4Nums.length!=4){
        this.$message({
          message: 'ipv4地址格式有误',
          type: 'warning'
        });
        return false;
      }
      for(let temp of ipv4Nums){
        if(temp.match(/[^\d]/)!=null){
          this.$message({
            message: 'ipv4地址格式有误',
            type: 'warning'
          });
          return false;
        }
        let i=parseInt(temp);
        if( i > 255 || i < 0 ){
          this.$message({
            message: 'ipv4地址格式有误',
            type: 'warning'
          });
          return false;
        }
      }
      let ipv6Nums=this.newPoint.IPv6Address.split(':');
      for(let temp of ipv6Nums){
        if(temp.match(/[^a-fA-F\d]/)!=null && temp!=""){
          this.$message({
            message: 'ipv6地址格式有误',
            type: 'warning'
          });
          return false;
        }
      }
      this.newPoint.IPv6Address=this.tranSimIpv6ToFullIpv6(this.newPoint.IPv6Address);
      return true;
    },
    checkResult(message){
      if(message=='success'){
        this.$message({
          message: '修改成功',
          type: 'success'
        });
      }else if(message=='existed'){
        this.$message({
          message: '节点已存在',
          type: 'error'
        });
      }else if(message=="error"){
        this.$message({
          message: '服务器错误！',
          type: 'error'
        });
      }else if(message=="none"){
        this.$message({
          message: '节点不存在！',
          type: 'error'
        });
      }else if(message=="nothing changed"){
        this.$message({
          message: '无变化',
          type: 'warning'
        });
      }
    },
    async onSubmitAdd(){
      if(this.checkPoint()){
        const resdata = await add({
          point:this.newPoint
        });
        let message=resdata.result;
        this.checkResult(message);
        this.addDialogVisible=false;
        this.getMeasuringPointsData();
      }
      
    },
    async onSubmitEdit(){
      if(this.checkPoint()){
        const resdata = await edit({
          point:this.newPoint
        });
        let message=resdata.result;
        this.checkResult(message);
        this.editDialogVisible=false;
        this.getMeasuringPointsData();
      }
      
    },
    handleEdit(index, row) {
      let point=this.measuringPointsData[index];
      this.newPoint.provider=point.identification;
      this.newPoint.location=point.location;
      this.newPoint.IPv4Address=point.ipv4;
      this.newPoint.IPv6Address=point.ipv6;
      this.editDialogVisible=true;
    },
    async handleDelete(index, row) {
      let point=this.measuringPointsData[index];
      this.newPoint.provider=point.identification;
      this.newPoint.location=point.location;
      this.newPoint.IPv4Address=point.ipv4;
      this.newPoint.IPv6Address=point.ipv6;
      const resdata = await del({
          point:this.newPoint
        });
        let message=resdata.result;
        this.checkResult(message);
        this.getMeasuringPointsData();
    },
    getTableData(pageSize, currentPage){
      this.pagedTableData=this.measuringPointsData.slice((currentPage-1)*pageSize,currentPage*pageSize);
    },
    handleSizeChange(newPageSize){
      this.pagination.pageSize=newPageSize;
      this.pagination.currentPage=1;
      this.getTableData(newPageSize, 1);
    },
    handleCurrentChange(newCurrentPage){
      this.pagination.currentPage=newCurrentPage;
      this.getTableData(this.pagination.pageSize,newCurrentPage);
    },
    tranSimIpv6ToFullIpv6(simpeIpv6){
      simpeIpv6 = simpeIpv6.toUpperCase()
      if(simpeIpv6 == "::"){
        return "0000:0000:0000:0000:0000:0000:0000:0000";
      }
      let arr = ["0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000"]
      if(simpeIpv6.startsWith("::")){
        let tmpArr = simpeIpv6.substring(2).split(":")
        for(let i=0;i<tmpArr.length;i++){
          arr[i+8-tmpArr.length] = ('0000'+tmpArr[i]).slice(-4)
        }
      }else if(simpeIpv6.endsWith("::")){
        let tmpArr = simpeIpv6.substring(0, simpeIpv6.length - 2).split(":");
        for(let i=0;i<tmpArr.length;i++){
          arr[i] = ('0000'+tmpArr[i]).slice(-4)
        }
      }else if(simpeIpv6.indexOf("::")>=0){
        let tmpArr = simpeIpv6.split("::");
        let tmpArr0 = tmpArr[0].split(":");
        for(let i=0;i<tmpArr0.length;i++){
          arr[i] = ('0000'+tmpArr0[i]).slice(-4)
        }
        let tmpArr1 = tmpArr[1].split(":");
        for(let i=0;i<tmpArr1.length;i++){
          arr[i+8-tmpArr1.length] = ('0000'+tmpArr1[i]).slice(-4)
        }
      }else{
        let tmpArr = simpeIpv6.split(":");
        for(let i=0;i<tmpArr.length;i++){
          arr[i+8-tmpArr.length] = ('0000'+tmpArr[i]).slice(-4)
        }
      }
      return arr.join(":")
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
    
    
  }
  .title {
    font-size: 36px;
    text-align: center;
  }
  .div-chart {
    text-align: center;
  }
  .container {
    color: white;
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