<template>
    <el-container style="min-height: 100vh; border: 1px solid #eee">
      <el-aside  style="width:250px; background-color: rgb(238, 241, 246)">
        <Aside :menu="user.menus"></Aside>
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid black;">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="isCollapse"></Header>
        </el-header>

        <el-main>
<!--          current page's sub-router is in router-view-->
            <router-view />

        </el-main>
      </el-container>
    </el-container>

</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import request from "@/utils/request";
import Aside from "@/components/Aside";
import Header from "@/components/Header";



export default {
  name: 'HomeView',
  components: {
    Header,
    Aside,
    HelloWorld
  },
  data(){
    return {
      tableData:[],
      username:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      isCollapse:false,
      dialogFormVisible:false,
      collapseBtnClass: 'el-icon-s-fold',
      sideWidth: 200,
      logoTextShow:true,
      input: '',
      user: localStorage.getItem("user")

    }
  },
  created() {
    this.load()

  },
  methods: {
    collapse(){
      this.isCollapse = !this.isCollapse
      if(this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else{
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    load(){
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    save(){
      request.post("http://"+this.$server+"/api/user", this.form).then(res =>{
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    del(id){
      request.delete("http://"+this.$server+"/api/user/" + id).then(res => {if(res){this.$message.success("删除成功")
      this.load()}
      else{
        this.$message.error("删除失败")
      }})
    },
    delBatch(){
      let ids = this.multipleSelection.map( v => v.id)
      request.post("http://"+this.$server+"/api/user/del/batch", ids).then(res => {if(res){this.$message.success("批量删除成功")
        this.load()}
      else{
        this.$message.error("批量删除失败")
      }})
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.username=""
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.username=""
      this.load()
    },
  }
}




</script>

<style>
.headerBg{
  background-color: #ccc;
}
</style>
