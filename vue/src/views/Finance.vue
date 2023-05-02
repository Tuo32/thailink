<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="serviceID" placeholder="Service ID">
      </el-input>
      <el-date-picker
          v-model="createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="Select Date"
          style="width: 150px"
      >
      </el-date-picker>
      <el-button class="ml-5" type="primary" @click="load">Filter</el-button>

    </div>

    <div style="margin: 10px">
      <el-button type="primary" @click="handleAdd">新增 add new</el-button>
<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text='确定'-->
<!--          cancel-button-text='不'-->
<!--          confirm-button-type='danger'-->
<!--          cancel-button-type='primary'-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="您确定批量删除吗？"-->
<!--          @confirm = "delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference"  >批量删除 </el-button>-->
<!--      </el-popconfirm>-->
      <el-button type="primary" @click="generateReceipt()" class="ml-5">Receipt</el-button>

      <!--  receipt form-->

<!--      <el-dialog-->
<!--          title="提示"-->
<!--          :visible.sync="receiptFormVisibility"-->
<!--          width="30%"-->
<!--        >-->
<!--        <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" class="demo-dynamic" :visible.sync="receiptFormVisibility">-->
<!--          <el-form-item-->
<!--              prop="customerName"-->
<!--              label="Customer Name"-->
<!--          >-->
<!--            <el-input style="" v-model="dynamicValidateForm.customerName"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item >-->
<!--            <el-col :span="10">-->
<!--              <el-form-item-->
<!--                  v-for="(description, index) in dynamicValidateForm.descriptions"-->
<!--                  :label="'Description' + index"-->
<!--                  :key="description.key"-->
<!--                  :prop="'descriptions.' + index + '.value'"-->
<!--              >-->
<!--                <el-input v-model="description.value" ></el-input>  <el-button icon="el-icon-edit" circle></el-button>-->
<!--              </el-form-item>-->
<!--            </el-col>-->

<!--            <el-col :span="10">-->

<!--              <el-form-item-->
<!--                  v-for="(amount, index) in dynamicValidateForm.amounts"-->
<!--                  :label="'Amount' + index"-->
<!--                  :key="amount.key"-->
<!--                  :prop="'amounts.' + index + '.value'"-->
<!--              >-->
<!--                <el-input v-model="amount.value"></el-input><el-button @click.prevent="removeLine();">Remove</el-button>-->
<!--              </el-form-item>-->
<!--            </el-col>-->
<!--          </el-form-item>-->

<!--          <el-form-item style=" width: 400px; ">-->

<!--            <el-button @click="addLine">Add New Item</el-button>-->
<!--            <el-button type="primary" @click="generateReceipt()">Generate</el-button>-->
<!--            <el-button type="warning" @click="resetForm()">Reset</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->

<!--      </el-dialog>-->











    </div>
    <el-table :data="tableData"   stripe  border style="width: 100%" @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="financeID" label="Finance ID" >
      </el-table-column>
      <el-table-column prop="serviceID" label="ServiceID" >
      </el-table-column>
      <el-table-column prop="description" label="Description">
      </el-table-column>
      <el-table-column prop="amount" label="Amount">
      </el-table-column>
<!--      <el-table-column prop="status" label="Status">-->
<!--      </el-table-column>-->
      <el-table-column prop="createTime" label="Create Time">
      </el-table-column>


      <el-table-column>
        <template v-slot="scope">
<!--          <el-button class="ml-5" type="success" @click="handleEdit(scope.row)" style="margin-top: 5px">Edit</el-button>-->
<!--          <el-button type="warning" slot="reference"  @click="confirm(scope.row)"> Confirm </el-button>-->
          <el-popconfirm

              confirm-button-text='确定'
              cancel-button-text='不'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm = "del(scope.row.financeID)"
          >

<!--            <el-button class="ml-5" type="danger" slot="reference" style="margin-top: 5px">Delete  </el-button>-->

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

    <el-dialog title="Finance Record Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" >
<!--        <el-form-item label="Finance ID" >-->
<!--          <el-input v-model="form.financeID" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="ServiceID" >
          <el-input v-model="form.serviceID" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="Customer ID" >-->
<!--          <el-input v-model="form.customerID" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

        <el-form-item label="Description" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Amount" >
          <el-input v-model="form.amount" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Finance",
  data(){
    return {
      tableData:[],
      serviceID:"",
      createTime:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,

      email:"",
      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      status:"",

      //RECEIPT FORM
      receiptFormVisibility: false,

      dynamicValidateForm: {
        descriptions: [{
          value: ''
        }],
        amounts: [{
          value: ''
        }],
       customerName: ''
      },

      lineCount:0,

      receiptForm: {
        customerName: "",
        descriptions: [],
        amounts: []
      }
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
    handleReceipt() {
      this.receiptFormVisible = true
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    load() {
      request.get("http://"+this.$server+"/api/finance/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&serviceID=" +this.serviceID + "&createTime=" +this.createTime ).then(res => {
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
      request.post("http://"+this.$server+"/api/finance/save?requesterID=" + this.currentUser.id, this.form).then(res => {
        console.log(res.code)
        if (res.code == 'CODE_200') {
          this.$message.success("saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    update() {
      request.post("http://"+this.$server+"/api/finance/update", this.form).then(res => {
        console.log(res.code)
        if (res.code == 'CODE_200') {
          this.$message.success("saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      request.delete("http://"+this.$server+"/api/finance/del?id=" + id + "&requesterID=" + this.currentUser.id).then(res => {

        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        } else {
          this.$message.error(res.code + " " + res.msg)
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.financeID)
      request.post("http://"+this.$server+"/api/finance/del/batch?requesterID=" + this.currentUser.id, ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
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


    //receipt form
    generateReceipt() {
      let ids = this.multipleSelection.map(v => v.financeID)
      request.post("http://"+this.$server+"/api/pdf/generate-receipt?requesterID=" + this.currentUser.id, ids)
          .then(res => {
            console.log("receipt", res.fileName);
            // this.download(res.fileName);
            if (res) {
              this.$message.success("receipt created")
              this.load()
            } else {
              this.$message.error("receipt failed")
            }
          })
          .catch(error => {
            console.error(error);
          });

      // this.receiptFormVisible = false;
      // this.resetForm();

    },
    resetForm() {
      //reset the receipt form
      this.dynamicValidateForm = {
        descriptions: [{
          value: ''
        }],
        amounts: [{
          value: ''
        }],
      }

      this.receiptForm = {
        customerName: "",
            descriptions: [],
            amounts: []
      }
    },
    removeLine() {
      if (this.lineCount !== -1) {
        this.dynamicValidateForm.descriptions.splice(this.lineCount, 1)
        this.dynamicValidateForm.amounts.splice(this.lineCount, 1)
      }
      this.lineCount--

    },
    addLine() {
      this.lineCount++
      this.dynamicValidateForm.descriptions.push({
        value: '',
        key: Date.now()
      });

      this.dynamicValidateForm.amounts.push({
        value: '',
        key: Date.now()
      });
    },
    download(fileName){
      request.get("http://"+this.$server+"/api/pdf/" + fileName).then(res => {
        if (res) {
          // window.open(res)
          this.$message.success("download success")
          this.load()
        } else {
          this.$message.error("download fail")
        }
      })
    }
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