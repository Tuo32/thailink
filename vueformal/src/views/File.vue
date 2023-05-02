<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 200px" suffix-icon="el-icon-search"
                v-model="serviceID" placeholder="Service ID">
      </el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-search"
                v-model="name" placeholder="File Name">
      </el-input>
      <el-button class="ml-5" type="primary" @click="load">Filter</el-button>
    </div>
    <div slot="tip" class="el-upload__tip">Only pdf files, Please input the service id which the file is belong to </div>
    <div style="">

      <el-input style="width: 100px"
                v-model="uploadServiceID" placeholder="Service ID">
      </el-input>
      <el-upload :action="'https://' + this.server + '/file/upload?serviceID='+this.uploadServiceID" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" :on-error="uploadError" style="display: inline-block" v-if="fileUploadVisible"
                 :headers="this.headerObj"
      >
        <el-button type="primary" class="ml-5">Upload</el-button>
      </el-upload>

    </div>

    <el-table :data="tableData" stripe border style="width: 100%" @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="fileID" label="File ID" >
      </el-table-column>
      <el-table-column prop="serviceID" label="Service ID" >
      </el-table-column>
      <el-table-column prop="name" label="Name">
      </el-table-column>
<!--      <el-table-column prop="type" label="Type">-->
<!--      </el-table-column>-->
      <el-table-column prop="size" label="Size(kb)">
      </el-table-column>


      <el-table-column>
        <template v-slot="scope">
          <el-button type="success" slot="reference" style="margin-top: 5px" @click="donwload(scope.row.name)" >Download  </el-button>
          <el-popconfirm
              confirm-button-text='Sure'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure？"
              @confirm = "del(scope.row.fileID)"
          >
            <el-button class="ml-5" type="danger" slot="reference" style="margin-top: 5px" v-if="fileDeleteVisible" >Delete  </el-button>
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
  </div>
</template>

<script>
import request from "@/utils/request";
import axios from "axios";

export default {
  name: "File",
  data(){
    return {
      fileList: [],
      tableData:[],
      name:"",
      total: 0,
      form:{},
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible:false,
      email:"",
      storeMenus:localStorage.getItem("menus"),
      fileUploadVisible:false,
      fileDeleteVisible:false,
      serviceID:"",
      uploadServiceID:"",
      server:this.$server,
      headerObj:{
        'token': JSON.parse(localStorage.getItem("user")).token
      }
    }
  },
  created() {
    if(this.storeMenus.indexOf("File Upload") != -1){
      this.fileUploadVisible = true;
    }
    if(this.storeMenus.indexOf("File Delete") != -1){
      this.fileDeleteVisible = true;
    }
    // console.log(this.headerObj)
    this.load()
  },
  methods: {
    uploadError(){
      this.$message.error("Upload failed, the serviceID may not exist, or the file name is duplicate")
    },
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
      request.get("https://"+this.$server+"/file/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&name=" + this.name + "&serviceID=" + this.serviceID).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    del(fileID) {
      request.delete("https://"+this.$server+"/file/" + fileID).then(res => {
        if (res) {
          this.$message.success("Deleted successfully ")
          this.load()
        } else {
          this.$message.error("Fail")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.fileID)
      request.post("https://"+this.$server+"/file/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("Batch deleted successfully")
          this.load()
        } else {
          this.$message.error("Fail")
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
    handleFileUploadSuccess(res) {
      console.log(res)
      this.$message.success("上传成功")
      this.load()
    },
    donwload(url){
      axios({
        method: 'get',
        url: 'https://'+this.$server+'/file/download',
        responseType: 'blob',
        headers:this.headerObj,
        params: {
          fileUUID: url
        }
      }).then(response => {
        const fileName = response.config.params.fileUUID
        const blob = new Blob([response.data], { type: 'application/octet-stream' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = decodeURIComponent(fileName)
        a.click()
        window.URL.revokeObjectURL(url)
      })
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