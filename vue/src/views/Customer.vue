<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="customerID" placeholder="Customer ID">
      </el-input>
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="name" placeholder="Customer Name">
      </el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>

    </div>

    <div style="margin: 10px">
      <el-button v-if="customerEditVisible" type="primary" @click="handleAdd">Add new</el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Yes'
          cancel-button-text='No'
          confirm-button-type='danger'
          cancel-button-type='primary'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure？"
          @confirm = "delBatch"
      >
        <el-button  v-if="customerDeleteVisible" type="danger" slot="reference"  >Batch delete </el-button>
      </el-popconfirm>
      <el-button class="ml-5" v-if="customerEditVisible" type="warning" @click="privacy">Privacy</el-button>


    </div>
    <el-table :data="tableData"   stripe  border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="customerID" label="ID" >
      </el-table-column>
      <el-table-column prop="name" label="name" >
      </el-table-column>
      <el-table-column prop="gender" label="gender">
      </el-table-column>
      <el-table-column prop="passport" label="passport">
      </el-table-column>
      <el-table-column prop="birthDate" label="birthDate">
      </el-table-column>
      <el-table-column prop="birthPlace" label="birthPlace">
      </el-table-column>
      <el-table-column prop="address" label="address">
      </el-table-column>
      <el-table-column prop="postcode" label="postcode">
      </el-table-column>
      <el-table-column prop="phone" label="phone">
      </el-table-column>
      <el-table-column prop="email" label="email">
      </el-table-column>


      <el-table-column>
        <template v-slot="scope">
            <el-button v-if="customerEditVisible" type="success" style="margin-top: 5px" @click="handleEdit(scope.row)">Edit </el-button>
          <el-popconfirm
              v-if="customerDeleteVisible"
              confirm-button-text='Sure'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete？"
              @confirm = "del(scope.row.customerID)"
          >
            <el-button type="danger" slot="reference" style="margin-top: 5px" v-if="customerDeleteVisible">Delete </el-button>
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

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" >
        <el-form-item label="Name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Gender" >
          <el-radio v-model="form.gender" label="Male">Male</el-radio>
          <el-radio v-model="form.gender" label="Female">Female</el-radio>
        </el-form-item>
        <el-form-item label="Passport" >
          <el-input v-model="form.passport" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birth Date" >
          <el-date-picker
              v-model="form.birthDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="Select date">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Birth Place" >
          <el-input v-model="form.birthPlace" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address" >
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Post Code" >
          <el-input v-model="form.postcode" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone" >
          <el-input v-model="form.phone" autocomplete="off" placeholder="example: +86 12345667"></el-input>
        </el-form-item>
        <el-form-item label="Email" >
          <el-input v-model="form.email" autocomplete="off" placeholder="example: 12345@gmail.com"></el-input>
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
import arr from "core-js/internals/array-iteration";




export default {
  name: "CustomerStudy",
  data(){
    return {
      tableData:[],
      name:"",
      customerID:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      email:"",
      storeMenus:localStorage.getItem("menus"),
      customerEditVisible:false,
      customerDeleteVisible:false,
      user: localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")) : null


    }
  },
  created() {
   if(this.storeMenus.indexOf("Customer Edit") != -1){
     this.customerEditVisible = true;
   }
    if(this.storeMenus.indexOf("Customer Delete") != -1){
      this.customerDeleteVisible = true;
    }
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

    privacy(){
      let ids = this.multipleSelection.map(v => v.customerID)
      request.post("http://"+this.$server+"/api/customer/clear?token="+this.user.token, ids).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("successful")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
      this.load()
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
      request.get("http://"+this.$server+"/api/customer/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&customerID=" + this.customerID + "&name=" + this.name + "&token=" + this.user.token).then(res => {
        this.tableData = res.records
        this.total = res.total
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    save() {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.form.email)) {
        this.$message.error("the email is invalid")
        return false;
      }

      const phoneRegex = /^\+\d{2,3}\s\d*$/;
      if (!phoneRegex.test(this.form.phone)) {
        this.$message.error("the phone is invalid")
        return false;
      }

      request.post("http://"+this.$server+"/api/customer?token=" + this.user.token, this.form).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("save success")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    del(id) {
      request.delete("http://"+this.$server+"/api/customer/delete?id=" + id + "&token=" + this.user.token).then(res => {
        console.log(res.code)
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.customerID)
      request.post("http://"+this.$server+"/api/customer/del/batch?token=" + this.user.token, ids).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
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