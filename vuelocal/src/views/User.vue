<template>
  <div>
  <div style="padding:10px 0">
    <el-input style="width: 150px" suffix-icon="el-icon-search"
              v-model="userID" placeholder="UserID">
    </el-input>
    <el-input style="width: 150px" suffix-icon="el-icon-search"
              v-model="username" placeholder="Username">
    </el-input>
    <el-input style="width: 150px" suffix-icon="el-icon-search"
              v-model="name" placeholder="Name">
    </el-input>

<!--    this dropdown is dynamically readed and generated from role table-->
    <el-select v-model="roleID" style="width: 150px">
      <el-option label="All" value=""></el-option>
      <el-option v-for="item in tableDataRoleSearch" :key="item.roleID" :label="item.roleName" :value="item.roleID"></el-option>
    </el-select>


    <el-button class="ml-5" type="primary" @click="load">Filter</el-button>

  </div>

  <div style="margin: 10px">
    <el-button type="primary" @click="handleAdd">Add new</el-button>
    <el-popconfirm
        class="ml-5"
        confirm-button-text='Sure'
        cancel-button-text='No'
        confirm-button-type='danger'
        cancel-button-type='primary'
        icon="el-icon-info"
        icon-color="red"
        title="Are you sure？"
        @confirm = "delBatch"
    >
      <el-button type="danger" slot="reference"  >Batch delete </el-button>
    </el-popconfirm>


  </div>
  <el-table :data="tableData" stripe  border  @selection-change="handleSelectionChange">>
    <el-table-column
        type="selection"
        width="55">
    </el-table-column>
    <el-table-column prop="userID" label="User ID" >
    </el-table-column>
    <el-table-column prop="username" label="Username" >
    </el-table-column>
    <el-table-column prop="password" label="Password" >
    </el-table-column>
    <el-table-column prop="name" label="Name" >
    </el-table-column>
    <el-table-column prop="email" label="Email">
    </el-table-column>
    <el-table-column prop="phone" label="Phone">
    </el-table-column>
    <el-table-column prop="address" label="Address">
    </el-table-column>
    <el-table-column prop="roleID" label="RoleID">
    </el-table-column>]
    <el-table-column prop="modifyTime" label="Modified">
    </el-table-column>
    <el-table-column prop="createTime" label="Created">
    </el-table-column>

    <el-table-column>
      <template v-slot="scope">
        <el-button type="success" style="margin-top: 5px" @click="handleEdit(scope.row)">Edit</el-button>

        <el-popconfirm
            confirm-button-text='Sure'
            cancel-button-text='No'
            confirm-button-type='danger'
            cancel-button-type='primary'
            icon="el-icon-info"
            icon-color="red"
            title="Are you sure？"
            @confirm = "del(scope.row.userID)"
        >
          <el-button type="danger" slot="reference" style="margin-top: 5px">Delete </el-button>
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

  <el-dialog title="用户信息 User Info" :visible.sync="dialogFormVisible" width="50%">
    <el-form label-width="100px" >
      <el-form-item label="Username" >
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="Password" >
        <el-input v-model="form.password" autocomplete="off"></el-input>
        <el-button  type="danger" @click="encrypt">Encrypt Password</el-button>
      </el-form-item>
      <el-form-item label="Name" >
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="Email" >
        <el-input v-model="form.email" autocomplete="off" placeholder="example: 12345@gmail.com"></el-input>
      </el-form-item>
      <el-form-item label="Phone" >
        <el-input v-model="form.phone" autocomplete="off" placeholder="example: +86 12345667"></el-input>
      </el-form-item>
      <el-form-item label="Address" >
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="RoleID" >
        <div style="background-color: bisque">
            <el-input v-model="form.roleID" autocomplete="off"></el-input>
          <el-input
              style="width: 120px"
              placeholder="Name"
              prefix-icon="el-icon-search"
              v-model="roleName">
          </el-input>
          <el-button @click="loadRole" type="primary">Filter</el-button>
          <el-table
              ref="singleTable"
              :data="tableDataRole"
              highlight-current-row
              @current-change="handleCurrentChangeRole"
              style="width: 100%">
            <el-table-column prop="roleID" label="Role ID" >
            </el-table-column>
            <el-table-column prop="roleName" label="Role Name" >
            </el-table-column>
            <el-table-column prop="description" label="Description" >
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">Cancel</el-button>
      <el-button type="primary" @click="save">Save</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import md5 from 'js-md5';

export default {
  name: "User",
  data(){
    return {
      tableData:[],
      //filter parameter
      userID:"",
      username:"",
      name:"",
      roleID:"",
      tableDataRoleSearch:[],


      total: 0,
      form:{
        roleID: 0,
      },
      encryptedPasswd:"",
      multipleSelection:[],

      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      email:"",
      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},


      //variables for the role table in 'add new dialog'
      tableDataRole:[],
      roleIDAdd:"",
      roleName:"",
      userIDAdd:0,
      currentRow: null,
      pageNumCustomer: 1,
      pageSizeCustomer: 5,

    }
  },
  created() {
    this.load()
    this.loadRoleForFilter()
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
      this.loadRole()
      this.dialogFormVisible = true
      this.form = {
        roleID: 0,
      }
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    load() {
      request.get("http://"+this.$server+":9090/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&userID=" + this.userID + "&username=" + this.username+"&name="+this.name+"&roleID="+this.roleID ).then(res => {
        this.tableData = res.records
        this.total = res.total
      })


    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    encrypt(){
      this.form.password = md5(this.form.password)
    },
    save() {
      // let encryptedPasswd = md5(this.form.password)
      // this.form.password = encryptedPasswd
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

      request.post("http://"+this.$server+":9090/user/save?requesterID="+this.currentUser.id , this.form).then(res => {
        console.log(res)
        if (res.code == 'CODE_200') {
          this.$message.success("saved successfully")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
      this.load()
    },
    del(id) {
      request.delete("http://"+this.$server+":9090/user/del?id=" + id + "&requesterID=" +this.currentUser.id + "&token=" + this.currentUser.token).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.userID)
      request.post("http://"+this.$server+":9090/user/del/batch?requesterID="+this.currentUser.id + "&token=" + this.currentUser.token, ids).then(res => {
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
    },

    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChangeRole(val) {
      this.currentRow = val;
      this.form.roleID = this.currentRow.roleID;
    },
    //load data from role table
    loadRole() {
      request.get("http://"+this.$server+":9090/role/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&roleID=" + this.roleIDAdd + "&roleName=" + this.roleName).then(res => {
        this.tableDataRole = res.records
      })
    },
    //load the data from role table to role filter
    loadRoleForFilter(){
      request.get("http://"+this.$server+":9090/role/all").then(res=>{
        this.tableDataRoleSearch = res
        console.log(this.tableDataRoleSearch)
          }
      )
    }


}
}
</script>

<style>

.headerBg{
  background-color: #ccc;
}
/deep/.el-table .cell {
  word-break: keep-all;
}
</style>
