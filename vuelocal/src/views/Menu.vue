<template>
  <div>
<!--    this page is not show during use, only kept for development purpose-->


    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <!--      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
      <!--      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="menuID" label="menuID" width="80"></el-table-column>
      <el-table-column prop="menuName" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="sortNum" label="顺序"></el-table-column>
      <el-table-column label="操作"  width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单 <i class="el-icon-plus"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.menuID)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.menuName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
  data(){
    return {
      tableData:[],
      menuName:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      email:"",
      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}


    }
  },
  created() {
    // if(this.currentUser.username!=="admin"){
    //   this.$message.warning("only admin can access user management")
    //   this.$router.push(":9090/")
    // }
    this.load()
    console.log("http://localhost/menu/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&menuName="+this.menuName)
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
      request.get("http://localhost/menu/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&menuName=" + this.menuName).then(res => {
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
      console.log("http://localhost:/user", JSON.stringify(this.form))
      request.post("http://localhost/menu", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    del(id) {
      request.delete("http://localhost/menu/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.menuID)
      request.post("http://localhost/menu/del/batch", ids).then(res => {
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
      console.log("http://localhost/menu/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&menuName=" + this.menuName)
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


<style>
.headerBg {
  background: #eee!important;
}
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>