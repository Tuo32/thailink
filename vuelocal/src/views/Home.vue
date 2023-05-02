<template>

  <div>
    <el-row>

      <el-col :span="11" style="background-color: antiquewhite" v-if="financeChartVisibility">
        <el-card style="background-color: antiquewhite; height:150px" >
          <h1>Finance Summary</h1>
          <div>Total Income: {{ this.totalIncome }} RMB</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"></div>
          <el-date-picker
              v-model="financeLineChartStartDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
          <el-date-picker
              v-model="financeLineChartEndDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
          <el-button class="ml-5" type="primary" @click="financeLineChartConfigure">Show</el-button>
        </el-card>

        <div id = "main" style="width:600px; height:400px; align-content: center "></div>
      </el-col>

      <el-col :span="11" >
        <el-card style="background-color: palegoldenrod; width: 600px; height:150px">
          <h1>Service Summary</h1>
          <div>Total orders</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">{{ this.totalCase }}</div>

        </el-card >
        <div id = "pie" style="width:600px; height:400px; background-color: palegoldenrod"></div>
      </el-col>
    </el-row>
  </div>


</template>

<script>
import * as echarts from 'echarts';
import request from "@/utils/request";

export default {
  name: "Home",


  data(){
    return{
      financeLineChartRange:"",
      tableData:[],
      total:0,
      customerID:"",
      pageNum: 1,
      pageSize: 5,
      totalIncome:"",
      totalCase:0,
      financeLineChartStartDate:this.getFirstDate(),
      financeLineChartEndDate:this.getNowFormatDate(),
      financeLineChartDates:[1,2,3],
      financeLineChartAmounts:[1,2,3],
      studyTotal:12,
      visaTotal:11,
      financeChartVisibility:false,
      storeMenus:localStorage.getItem("menus"),
    }
  },
  created() {
    // after the first login, the router is not loaded. Redirect to homepage to load the router
    if (!localStorage.getItem('reloaded')) {
      localStorage.setItem('reloaded', 'true');
      // window.location.href = 'http://' +this.$server+'/#/home';
       console.log("reloaded router")
    }
    this.financeSummary()
    if(this.storeMenus.indexOf("Finance Management") != -1){
      this.financeChartVisibility = true;
    }

  },
  mounted(){
    this.financeLineChartConfigure()
    this.pieChartConfigure()

  },
  methods:{
    pieChartConfigure(){
      request.get("http://"+this.$server+":9090/chart/pie").then(res => {
        this.studyTotal = res.studyTotal;
        this.visaTotal = res.visaTotal;
        this.totalCase = res.studyTotal + res.visaTotal

        let pieOption;
        pieOption = {
          title: {
            // text: 'Service Types',
            // subtext: 'Fake Data',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: 'Total orders',
              type: 'pie',
              radius: '80%',
              data: [
                { value: this.studyTotal, name: 'Study'},
                { value: this.visaTotal, name: 'Visa' },

              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                }
              }
            }
          ]
        };
        var pieDom = document.getElementById('pie');
        var pieChart = echarts.init(pieDom);
        pieChart.setOption(pieOption);
      })


    },
    financeLineChartConfigure(){
      request.get("http://"+this.$server+":9090/finance/chart/line?startDate=" + this.financeLineChartStartDate + "&endDate=" + this.financeLineChartEndDate ).then(res => {
        this.financeLineChartDates = res.dates
        this.financeLineChartAmounts = res.amounts


        let chartDom = document.getElementById('main');
        let myChart = echarts.init(chartDom);
        let option = {
          xAxis: {
            type: 'category',
            data:  this.financeLineChartDates
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.financeLineChartAmounts,
              itemStyle:{normal:{
                label:{
                  show:true,
                  position:'top',
                  textStyle:{
                    color:'black',
                    fontSize:8
                  }
                }
                }},
              type: 'line'
            }
          ]
        };
        myChart.setOption(option);
        this.financeSummary()
      })

    },
    getNowFormatDate() {
  var date = new Date();
  var seperator1 = "-";
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var strDate = date.getDate();
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
  }
  var currentdate = year + seperator1 + month + seperator1 + strDate;
  return currentdate;
},
    getFirstDate() {
      var date = new Date();
      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }

        strDate = "01";

      var currentdate = year + seperator1 + month + seperator1 + strDate;
      return currentdate;
    },
    financeSummary(){
      request.get("http://"+this.$server+":9090/finance/sum?startDate=" + this.financeLineChartStartDate + "&endDate=" + this.financeLineChartEndDate ).then(res => {
        this.totalIncome = res;
      })
    },
    load1() {
      request.get("http://"+this.$server+":9090/finance/chart/line?startDate=" + this.financeLineChartStartDate + "&endDate=" + this.financeLineChartEndDate ).then(res => {
       return res.dates
      })
    },
    load2() {
      request.get("http://"+this.$server+":9090/finance/chart/line?startDate=" + this.financeLineChartStartDate + "&endDate=" + this.financeLineChartEndDate ).then(res => {
        return res.amounts
      })
    },
  }

}


</script>

<style scoped>

</style>