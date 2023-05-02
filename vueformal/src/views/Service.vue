<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="serviceID" placeholder="Service ID">
      </el-input>
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="customerIDSearch" placeholder="Customer ID">
      </el-input>
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="serviceType" placeholder="Service Type">
      </el-input>
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="infoID" placeholder="Info ID">
      </el-input>
      <el-button class="ml-5" type="primary" @click="load">Filter</el-button>
    </div>

    <el-table :data="tableData"   stripe  border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="serviceID" label="Service Order ID" >
      </el-table-column>
      <el-table-column prop="customerID" label="Customer ID" >
      </el-table-column>
      <el-table-column prop="infoID" label="Information ID">
      </el-table-column>
      <el-table-column prop="serviceType" label="Service Type">
      </el-table-column>
      <el-table-column prop="price" label="Price">
      </el-table-column>
      <el-table-column prop="paid" label="Paid">
      </el-table-column>


      <el-table-column>
        <template v-slot="scope">
          <el-popconfirm

              confirm-button-text='Sure'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete？"
              @confirm = "del(scope.row.serviceID)"
          >
          </el-popconfirm>
        </template>
      </el-table-column>


    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Service Record Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" >
        <el-form-item label="Customer ID" >
          <el-input v-model="form.customerID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Info ID" >
          <el-input v-model="form.infoID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Service Type" >
          <el-input v-model="form.serviceType" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Price" >
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Paid" >
          <div style="font-size: xx-small">{{form.paid}}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Sure</el-button>
      </div>
    </el-dialog>


  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "Service",
  data(){
    return {
      tableData:[],
      customerIDSearch:"",
      serviceType:"",
      infoID:"",

      serviceID:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      email:"",
      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},


    }
  },
  created() {
    this.load()
  },
  methods: {
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    load() {
      request.get("https://"+this.$server+"/service/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&serviceID=" + this.serviceID + "&customerID=" + this.customerIDSearch + "&serviceType=" + this.serviceType + "&infoID=" + this.infoID).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    save() {
      request.post("https://"+this.$server+"/service/save?requesterID="+this.currentUser.id, this.form).then(res => {
        console.log(res.code)
        if (res.code == 'CODE_200') {
          this.$message.success("saved successfully")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    // update() {
    //   request.post("https://localhost/service/update", this.form).then(res => {
    //     console.log(res.code)
    //     if (res.code == 'CODE_200') {
    //       this.$message.success("saved successfully")
    //       this.dialogFormVisible = false
    //       this.load()
    //     }else{
    //       this.$message.error(res.msg)
    //     }
    //   })
    // },
    del(id) {
      request.delete("https://"+this.$server+"/service/del?id=" + id + "&requesterID=" +this.currentUser.id).then(res => {
       console.log(res)
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.serviceID)
      request.post("https://"+this.$server+"/service/del/batch?requesterID="+this.currentUser.id, ids).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    //archive the service
    // history() {
    //   let ids = this.multipleSelection.map(v => v.serviceID)
    //   request.post("https://"+this.$server+"/service/history?requesterID="+this.currentUser.id, ids).then(res => {
    //     if (res.code == 'CODE_200') {
    //       this.$message.success("moved to history successfully")
    //       this.load()
    //     }else{
    //       this.$message.error(res.code+" "+res.msg)
    //     }
    //   })
    // },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.username = ""
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.username = ""
      this.load()
    },



  }


}
</script>

<style  scoped>

/deep/ .el-table__header-wrapper th {
  background-color: #f0f1ff;
}
/deep/.el-table__body-wrapper::-webkit-scrollbar {
  width: 15px !important;
  height: 15px !important;
  background-color: white;
}
/deep/.el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 0;
  width: 10px !important;
  height: 10px !important;
  background: grey;
}
/deep/.el-table__body-wrapper::-webkit-scrollbar-track {
  box-shadow: none;
  border-radius: 0;
  background: #fff;
}
/deep/.el-table .cell {
  word-break: keep-all;
}



</style>