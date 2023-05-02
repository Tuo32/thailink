<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="roleIDSearch" placeholder="Role ID">
      </el-input>
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="roleName" placeholder="Role Name">
      </el-input>
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
    </div >

    <el-table :data="tableData"   stripe  border style="width: 100%; " @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="roleID" label="roleID" >
      </el-table-column>
      <el-table-column prop="roleName" label="roleName" >
      </el-table-column>
      <el-table-column prop="description" label="description">
      </el-table-column>

      <el-table-column>
        <template v-slot="scope">
          <el-button  type="primary" slot="reference" style="margin-top: 5px" @click="selectMenu(scope.row.roleID)">Authority </el-button>
          <el-button type="success" style="margin-top: 5px" @click="handleEdit(scope.row)">Edit</el-button>

          <el-popconfirm style="margin-left: 10px;"
              confirm-button-text='Sure'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure？"
              @confirm = "del(scope.row.roleID)"
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

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" >
        <el-form-item label="roleName" >
          <el-input v-model="form.roleName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="description" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

    <el-dialog title="authority management" :visible.sync="menuDialogVisible" width="30%">
      <el-tree
          :data="menuData"
          show-checkbox
          :default-checked-keys="checks"
          default-expand-all
          node-key="id"
          ref="tree"
      >
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveRoleMenu">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Role",
  data(){

    return {
      props: {
        children: 'zones'
      },
      tableData:[],
      roleIDSearch:"",
      roleName:"",
      total: 0,
      form:{},
      roleID:"",
      checks: [],
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      menuDialogVisible:false,
      expands:[],
      email:"",
      menuData: [
        {
        id: 1,
        label: 'User Management',
      },
        {
          id: 2,
          label: 'Role Management',
        },
        {
        id: 9,
        label: 'File Management',
        children: [{
          id: 3,
          label: 'File Browse'
        }, {
          id: 13,
          label: 'File Upload'
        }, {
          id: 14,
          label: 'File Delete'
        }]
      },
        {
          id:7,
          label: 'Customer',
          children: [
            {
              id: 4,
              label: 'Customer Browse'
            },
            {
            id: 6,
            label: 'Customer Edit'
            },
            {
              id: 8,
              label: 'Customer Delete'
            }]
        },
        {
          id: 5,
          label: 'Finance Management',
        },
        {
          id:15,
          label: 'Study Management',
          children: [
            {
              id: 26,
              label: 'Study Status Update'
            },
            {
              id: 16,
              label: 'Study Browse'
            },
            {
              id: 32,
              label: 'Study View All'
            },
            {
              id: 24,
              label: 'Study Edit'
            }]
        },
        {
          id:18,
          label: 'Visa Management',
          children: [
            {
              id: 21,
              label: 'Visa Status Update'
            },
            {
              id: 17,
              label: 'Visa Browse'
            },
            {
              id: 23,
              label: 'Visa Edit'
            },
            {
              id: 28,
              label: 'Visa View All'
            },
          ]
        },
        {
          id:29,
          label: 'Service Management',
          children: [
            {
              id: 30,
              label: 'Service Browse'
            },
            {
              id: 31,
              label: 'Service Edit'
            }]
        },

      ],

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
    selectMenu(roleID){
      this.menuDialogVisible = true
      this.roleID = roleID;
      //get the current menu authority and set as default checked in the check list
      this.request.get("http://"+this.$server+"/api/role/roleMenu/" + roleID).then(res => {
        this.checks = res.data
      })
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    load() {
      request.get("http://"+this.$server+"/api/role/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&roleID=" + this.roleIDSearch + "&roleName=" + this.roleName).then(res => {
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
      request.post("http://"+this.$server+"/api/role", this.form).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("saved successfully")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    saveRoleMenu(){
      console.log(this.roleID)
     this.request.post("http://"+this.$server+"/api/role/roleMenu/" + this.roleID,this.$refs.tree.getCheckedKeys()).then(res => {
       if (res.code == 'CODE_200') {
         this.$message.success("saved successfully")
         this.dialogFormVisible = false
         this.load()
       }else{
         this.$message.error(res.msg)
       }
     })
      this.menuDialogVisible = false
    },
    del(id) {
      request.delete("http://"+this.$server+"/api/role/" + id).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.roleID)
      request.post("http://"+this.$server+"/api/role/del/batch", ids).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("deleted successfully")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
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