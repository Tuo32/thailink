<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 200px" suffix-icon="el-icon-search"
                v-model="studyID" placeholder="Study ID">
      </el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-search"
                v-model="userID" placeholder="User ID">
      </el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-search"
                v-model="applySchool" placeholder="Apply School">
      </el-input>
      <el-select v-model="updatedOrder" style="width: 150px">
        <el-option label="ID Order" value="normal"></el-option>
        <el-option label="Updated Ascend" value="asc"></el-option>
        <el-option label="Updated Descend" value="desc"></el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">Filter</el-button>


    </div>

    <div style="margin: 10px">
      <el-button type="primary" @click="handleAdd" v-if="studyEditVisible">Add new</el-button>
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
<!--        <el-button type="danger" slot="reference"  v-if="studyDeleteVisible">Batch delete </el-button>-->
      </el-popconfirm>


    </div>
    <el-table :data="tableData"  :stripe="true"  border  style="width: 100%; " @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="studyID" label="Study ID" >
      </el-table-column>
      <el-table-column prop="userID" label="User ID" >
      </el-table-column>
      <el-table-column prop="bloodType" label="Blood Type">
      </el-table-column>
      <el-table-column prop="religion" label="Religion">
      </el-table-column>
      <el-table-column prop="formerSchool" label="Former School">
      </el-table-column>
      <el-table-column prop="applySchool" label="Apply School">
      </el-table-column>
      <el-table-column prop="fatherName" label="Father Name">
      </el-table-column>
      <el-table-column prop="motherName" label="Mother Name">
     </el-table-column>
      <el-table-column prop="status" label="Status">
      </el-table-column>

      <el-table-column prop="comment" label="Comment">
      </el-table-column>
      <el-table-column prop="modifyTime" label="Modified">
      </el-table-column>
      <el-table-column prop="updated" label="Updated">
      </el-table-column>
      <el-table-column prop="createTime" label="Created">
      </el-table-column>

      <el-table-column  width="200%">
        <template v-slot="scope" >
          <el-button  size="mini"class="ml-5" type="success" @click="handleEdit(scope.row)" style="margin-top: 5px" v-if="studyEditVisible">Edit</el-button>
          <el-popconfirm

              confirm-button-text='Sure'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure？"
              @confirm = "del(scope.row.studyID)"
          >
<!--            <el-button  size="mini" class="ml-5" type="danger" slot="reference" style="margin-top: 5px" v-if="studyDeleteVisible">Delete  </el-button>-->
          </el-popconfirm>
          <el-button  size="mini" class="ml-5" type="info" @click="fetch(scope.row.studyID)" style="margin-top: 5px; ">Details</el-button>
<!--          <el-button  size="mini" type="warning" @click="updateStatus(scope.row.studyID, scope.row.status)" style="margin-top: 5px; margin-left: 5px;" v-if="statusUpdateVisible">Proceed</el-button>-->
<!--          <el-button  type="warning" @click="revertStatus(scope.row.studyID, scope.row.status)" style="margin-top: 5px; margin-left: 5px;" v-if="statusUpdateVisible">Revert</el-button>-->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to proceed？"
              @confirm = "updateStatus(scope.row.studyID, scope.row.status)"
          >
            <el-button type="warning" slot="reference" style="margin-top: 5px; margin-left: 0px;" v-if="statusUpdateVisible">Proceed</el-button>
          </el-popconfirm>

          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              confirm-button-type='danger'
              cancel-button-type='primary'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to revert？"
              @confirm = "revertStatus(scope.row.studyID, scope.row.status)"
          >
            <el-button type="warning" slot="reference" style="margin-top: 5px; margin-left: 5px;" v-if="statusUpdateVisible">Revert</el-button>
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

    <el-dialog title="Study Record Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="120px" >
        <el-form-item label="Blood Type" >
          <el-select v-model="form.bloodType" >
            <el-option label="A" value="A"></el-option>
            <el-option label="AB" value="AB"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="O" value="O"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Religion" >
          <el-select v-model="form.religion" >
            <el-option label="SDA" value="Christianity"></el-option>
            <el-option label="Buddhism" value="Buddhism"></el-option>
            <el-option label="Islam" value="Islam"></el-option>
            <el-option label="None" value="None"></el-option>
            <el-option label="Others" value="Others"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Former School" >
          <el-input v-model="form.formerSchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Apply School" >
          <el-input v-model="form.applySchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Father Name" >
          <el-input v-model="form.fatherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Mother Name" >
          <el-input v-model="form.motherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="User ID" >
          <el-input v-model="form.userID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="form.comment" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Status" >
          <div style="font-size: xx-small">{{form.status}}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Sure</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Study Record Fetch Information" :visible.sync="dialogFormVisible2" width="30%">
      <el-form label-width="150px" >
        <el-form-item label="Service Code" >
          {{join.serviceID + "study" + join.studyID}}
        </el-form-item>
        Customer: <br></br>
        <el-form-item label="Customer ID" >
          <el-input v-model="join.customerID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Name" >
          <el-input v-model="join.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Gender" >
          <el-input v-model="join.gender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birth Date" >
          <el-input v-model="join.birthDate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birth Place" >
          <el-input v-model="join.birthPlace" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Passport" >
          <el-input v-model="join.passport" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address" >
          <el-input v-model="join.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Postcode" >
          <el-input v-model="join.postcode" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone" >
          <el-input v-model="join.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email" >
          <el-input v-model="join.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="join.customerComment" autocomplete="off"></el-input>
        </el-form-item>

        Service: <br></br>
        <el-form-item label="Service ID" >
          <el-input v-model="join.serviceID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Price" >
          <el-input v-model="join.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Paid" >
          <el-input v-model="join.paid" autocomplete="off"></el-input>
        </el-form-item>

        Study: <br></br>
        <el-form-item label="Study ID" >
          <el-input v-model="join.studyID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Name" >
          <el-input v-model="join.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Gender" >
          <el-input v-model="join.gender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Passport" >
          <el-input v-model="join.passport" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birth Date" >
          <el-input v-model="join.birthDate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birth Place" >
          <el-input v-model="join.birthPlace" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address" >
          <el-input v-model="join.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Post Code" >
          <el-input v-model="join.postcode" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone" >
          <el-input v-model="join.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email" >
          <el-input v-model="join.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Study ID" >
          <el-input v-model="join.studyID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Blood Type" >
          <el-input v-model="join.bloodType" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Religion" >
          <el-input v-model="join.religion" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Former School" >
          <el-input v-model="join.formerSchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Apply School" >
          <el-input v-model="join.applySchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Father Name" >
          <el-input v-model="join.fatherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Mother Name" >
          <el-input v-model="join.motherName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Study Comment" >
          <el-input v-model="join.studyComment" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>


      <div slot="footer" class="dialog-footer">
        <el-popconfirm
            class="ml-5"
            confirm-button-text='Yes'
            cancel-button-text='No'
            confirm-button-type='danger'
            cancel-button-type='primary'
            icon="el-icon-info"
            icon-color="red"
            title="Are you sure？"
            @confirm = "history(join.serviceID)"
        >
          <el-button type="danger" slot="reference" >Archive </el-button>
        </el-popconfirm>
        <el-upload :action="'http://' + this.server + ':9090/file/upload?serviceID='+this.join.serviceID" :show-file-list="false"
                   :on-success="handleFileUploadSuccess" :on-error="uploadError" :headers="this.headerObj" style="display: inline-block">
          <el-button type="primary" class="ml-5">Upload File</el-button>
        </el-upload>
        <el-button @click="dialogFormVisible2 = false" type="primary"  class="ml-5">Close</el-button>
      </div>
    </el-dialog>


<!--    add new form -->
    <el-dialog title="New Study Record Information" :visible.sync="dialogFormVisible3" width="30%">
      <el-form label-width="120px" >
        <el-form-item label="Blood Type" >
<!--          <el-input v-model="form.bloodType" autocomplete="off"></el-input>-->
          <el-select v-model="form.bloodType" >
            <el-option label="A" value="A"></el-option>
            <el-option label="AB" value="AB"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="O" value="O"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Religion" >
          <el-select v-model="form.religion" >
            <el-option label="SDA" value="Christianity"></el-option>
            <el-option label="Buddhism" value="Buddhism"></el-option>
            <el-option label="Islam" value="Islam"></el-option>
            <el-option label="None" value="None"></el-option>
            <el-option label="Others" value="Others"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Former School" >
          <el-input v-model="form.formerSchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Apply School" >
          <el-input v-model="form.applySchool" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Father Name" >
          <el-input v-model="form.fatherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Mother Name" >
          <el-input v-model="form.motherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="User ID" >
          <el-input v-model="form.userID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="form.comment" autocomplete="off"></el-input>
        </el-form-item>
        Service Information: <br></br>
        <div style="background-color: bisque">
          <el-form-item label="CustomerID" >
            <el-input v-model="form.customerID" autocomplete="off"></el-input>
          </el-form-item>
          <el-input
              style="width: 120px"
              placeholder="Name"
              prefix-icon="el-icon-search"
              v-model="customerName">
          </el-input>
          <el-button @click="loadCustomer" type="primary">Filter</el-button>
<!--          <el-button @click="setCurrent()">取消选择</el-button>-->
          <el-table
              ref="singleTable"
              :data="tableDataCustomer"
              highlight-current-row
              @current-change="handleCurrentChangeCustomer"
              style="width: 100%">
            <el-table-column prop="customerID" label="ID" >
            </el-table-column>
            <el-table-column prop="name" label="Name" >
            </el-table-column>
            <el-table-column prop="passport" label="Passport" >
            </el-table-column>
          </el-table>
        </div>
        <el-form-item label="Price" >
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible3 = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Study",

  data(){
    return {
      //array to contain customer table data
      tableDataCustomer:[],
      //array to contain the study table data
      tableData:[],
      // filter params
      studyID:"",
      applySchool:"",
      userID:0,
      updatedOrder:"normal",
      //array for study table check box
      multipleSelection:[],
      //create form obj
      form:{},
      //array to hold fetched data
      join:[],

      //amount of study table records
      total: 0,

      //page number and size
      pageNum: 1,
      pageSize: 5,

      //this is used to check the url and permission available
      storeMenus:localStorage.getItem("menus"),

      //visibility of dialog forms
      dialogFormVisible:false,
      statusUpdateVisible:false,
      studyDeleteVisible:false,
      studyEditVisible:false,
      studyViewAllVisible:false,
      dialogFormVisible2:false,
      dialogFormVisible3:false,


      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      customerID:"",
      customerName:"",

      currentRow: null,
      status:"",
      pageNumCustomer: 1,
      pageSizeCustomer: 5,

      email:"",

      server:this.$server,

      //the header used for upload file
      headerObj:{
        'token': JSON.parse(localStorage.getItem("user")).token
      },




    }
  },
  created() {
    this.userID = this.currentUser.id

    if(this.storeMenus.indexOf("Study Status Update") !== -1){
      this.statusUpdateVisible = true;
    }
    if(this.storeMenus.indexOf("Study Delete") !== -1){
      this.studyDeleteVisible = true;
    }
    if(this.storeMenus.indexOf("Study Edit") !== -1){
      this.studyEditVisible = true;
    }



    this.load()
  },
  methods: {
    //this is a method to fold the left menu, but this feature is not implemented yet
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
    //add new study record
    handleAdd() {
      this.loadCustomer()
      this.dialogFormVisible3 = true
      this.form = {
        userID: this.currentUser.id,
        customerID: 0,
      }
    },
    //open edit form
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    //load the study table
    load() {
      this.$notify.closeAll()
      request.get("http://"+this.$server+":9090/study/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&studyID=" + this.studyID + "&userID="+ this.userID + "&applySchool="+ this.applySchool + "&updatedOrder=" + this.updatedOrder).then(async res => {
        this.tableData = res.records
        this.total = res.total

        for (const item of res.records) {
          let d1 = new Date()
          // console.log(d1.toJSON().substring(0, 10), item.updated.substring(0, 10))
          if (this.calculateDays(d1.toJSON().substring(0, 10), item.updated.substring(0, 10)) > 10) {
            await this.$notify({
              type: 'warning',
              title: '',
              message: "One case have not been updated for more than 10 days, case id:" + item.studyID,
              position: 'bottom-left',
            })
          }
        }

      })
    },
    // fetch the related customer, service and study record together
    fetch(studyID){
      this.dialogFormVisible2 = true
      request.get("http://"+this.$server+":9090/study/study-service?studyID="+studyID).then(res =>{
            console.log(res)
            this.join = res
          }
      )
    },
    calculateDays(date1,date2){
      let d1 = Date.parse(date1)
      let d2 = Date.parse(date2)
      let days = (d1 - d2)/(1*24*60*60*1000);
      return days
    },
    updateStatus(studyID, status){
      request.post("http://"+this.$server+":9090/study/statusUpdate?studyID=" + studyID + "&status=" + status + "&requesterID=" + this.currentUser.id ).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("status proceeded")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    revertStatus(studyID, status){
      request.post("http://"+this.$server+":9090/study/statusRevert?studyID=" + studyID + "&status=" + status + "&requesterID=" + this.currentUser.id).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("status reverted")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    //archive the service
    history(serviceID) {
      let ids = this.multipleSelection.map(v => v.serviceID)
      request.post("http://"+this.$server+":9090/service/history?requesterID="+this.currentUser.id, serviceID).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("moved to history successfully")
          this.dialogFormVisible2 = false
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
    },
    //study table check box
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    //save a new created study record
    save() {
      request.post("http://"+this.$server+":9090/study/save?requesterID="+this.currentUser.id, this.form).then(res => {
        console.log(res)
        if (res.code == 'CODE_200') {
          this.$message.success("save success")
          this.dialogFormVisible = false
          this.dialogFormVisible2 = false
          this.dialogFormVisible3 = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    //delete a record, but delete is disable now
    del(id) {
      request.delete("http://"+this.$server+":9090/study/del?id=" + id + "&requesterID=" +this.currentUser.id).then(res => {
        if (res) {
          this.$message.success("succeed")
          this.load()
        } else {
          this.$message.error("fail")
        }
      })
    },
    //delete multiple records
    delBatch() {
      let ids = this.multipleSelection.map(v => v.studyID)
      request.post("http://"+this.$server+":9090/study/del/batch?requesterID="+this.currentUser.id, ids).then(res => {
        if (res) {
          this.$message.success("succeed")
          this.load()
        } else {
          this.$message.error("fail")
        }
      })
    },
    //change the volume of study table
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.username = ""
      this.load()
    },
    //change page
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.username = ""
      this.load()
    },
  //methods for the customer table
    loadCustomer() {
      request.get("http://"+this.$server+":9090/customer/page?pageNum=" + this.pageNumCustomer + "&pageSize=" + this.pageSizeCustomer + "&customerID=" + this.customerID + "&name=" + this.customerName + "&status=" + this.status ).then(res => {
        this.tableDataCustomer = res.records
      })
    },
    handleCurrentChangeCustomer(val) {
      this.currentRow = val;
      this.form.customerID = this.currentRow.customerID;
    },
    handleCommand(command) {
      this.$message('click on item ' + command);
    },
    handleFileUploadSuccess(res) {
      console.log(res)
      this.$message.success("Upload succeed")
      this.load()
    },
    //method for upload file
    uploadError(){
      this.$message.error("Upload fail, the serviceID may not exist, or the file name is duplicate")
    },

  }

}
</script>

<style  scoped>

.button-cell {
  background: red;
  color: red;
}

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