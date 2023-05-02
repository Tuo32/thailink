<template>
  <div>
    <div style="padding:10px 0">
      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="visaID" placeholder="Visa ID">
      </el-input>

      <el-select v-model="searchVisaType" style="width: 150px">
        <el-option label="All" value=""></el-option>
        <el-option label="Tourist visa" value="Tourist visa"></el-option>
        <el-option label="Business visa" value="Business visa"></el-option>
        <el-option label="Education visa" value="Education visa"></el-option>
        <el-option label="Retirement visa" value="Retirement visa"></el-option>
        <el-option label="Work visa" value="Work visa"></el-option>
        <el-option label="Elite visa" value="Elite visa"></el-option>
        <el-option label="Non-Immigrant O" value="Non-Immigrant O"></el-option>
      </el-select>

      <el-input style="width: 150px" suffix-icon="el-icon-search"
                v-model="userIDSearch" placeholder="User ID">
      </el-input>

      <el-select v-model="updatedOrder" style="width: 150px">
        <el-option label="ID Order" value="normal"></el-option>
        <el-option label="Updated Ascend" value="asc"></el-option>
        <el-option label="Updated Descend" value="desc"></el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">Filter</el-button>
    </div>

    <div style="margin: 10px">
      <el-button type="primary" @click="handleAdd " v-if="visaEditVisible">Add new</el-button>

    </div>

<!--    // table-->
    <el-table :data="tableData" :fit="true" :stripe="true"  border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="visaID" label="Visa ID" >
      </el-table-column>
      <el-table-column prop="userID" label="User ID">
      </el-table-column>
      <el-table-column prop="visaType" label="Visa Type">
      </el-table-column>
      <el-table-column prop="status" label="Status">
      </el-table-column>
      <el-table-column prop="comment" label="Comment">
      </el-table-column>
      <el-table-column prop="updated" label="Updated">
      </el-table-column>
      <el-table-column prop="createTime" label="Created">
      </el-table-column>
      <el-table-column width="200%">
        <template v-slot="scope">
          <el-button  type="success" @click="handleEdit(scope.row)" style="margin-top: 5px; margin-right: 5px" v-if="visaEditVisible">Edit</el-button>
          <el-button  type="info" @click="fetch(scope.row.visaID)" style="margin-top: 5px; ">Details</el-button>
                <el-popconfirm
                    class="ml-5"
                    confirm-button-text='Yes'
                    cancel-button-text='No'
                    confirm-button-type='danger'
                    cancel-button-type='primary'
                    icon="el-icon-info"
                    icon-color="red"
                    title="Are you sure to proceed？"
                    @confirm = "updateStatus(scope.row.visaID, scope.row.status)"
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
              @confirm = "revertStatus(scope.row.visaID, scope.row.status)"
          >
            <el-button type="warning" slot="reference" style="margin-top: 5px; margin-left: 5px;" v-if="statusUpdateVisible">Revert</el-button>
          </el-popconfirm>
<!--          <el-button  type="warning" @click="revertStatus(scope.row.visaID, scope.row.status)" style="margin-top: 5px; margin-left: 5px;" v-if="statusUpdateVisible">Revert</el-button>-->

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

<!--    //add new form, i make it differ from edit form because this step the user-->
<!--    //need to input data for service table record too-->
    <el-dialog title="Visa New Record Information" :visible.sync="dialogFormVisible3" width="30%">
      <el-form label-width="100px" >
        <el-form-item label="Visa Type" >
          <el-select v-model="form.visaType" >
            <el-option label="Tourist visa" value="Tourist visa"></el-option>
            <el-option label="Business visa" value="Business visa"></el-option>
            <el-option label="Education visa" value="Education visa"></el-option>
            <el-option label="Retirement visa" value="Retirement visa"></el-option>
            <el-option label="Work visa" value="Work visa"></el-option>
            <el-option label="Elite visa" value="Elite visa"></el-option>
            <el-option label="Non-Immigrant O" value="Non-Immigrant O"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="form.comment" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="User ID" >
          <el-input v-model="form.userID" autocomplete="off"></el-input>
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

        <br></br>
        <el-form-item label="Price" >
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible3 = false">Close</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Visa Record Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" >

        <el-form-item label="Visa Type" >
          {{this.form.visaType}}
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="form.comment" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Status">
          <div style="font-size: xx-small">{{form.status}}</div>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

<!--    the form will fetch all information from customer, service, also visa infomation together for the select case-->
    <el-dialog title="Visa Record Fetch Information" :visible.sync="dialogFormVisible2" width="30%">
      <el-form label-width="120px" >
        <el-form-item label="Service Code" >
          {{join.serviceID + "visa" + join.visaID}}
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
        Visa: <br></br>
        <el-form-item label="Visa ID" >
          <el-input v-model="join.visaID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Visa Type" >
          <el-input v-model="join.visaType" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Comment" >
          <el-input v-model="join.visaComment" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Status" >
          <el-input v-model="join.visaStatus" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Visa Updated" >
          <el-input v-model="join.visaUpdated" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Visa Created" >
          <el-input v-model="join.visaCreated" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

<!--upload documents for this case-->
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
          <el-button type="danger" slot="reference" v-if="visaEditVisible" >Archive </el-button>
        </el-popconfirm>
        <el-upload :action="'https://' + this.server + '/file/upload?serviceID='+this.join.serviceID" :show-file-list="false"
                   :on-success="handleFileUploadSuccess" :on-error="uploadError"  :headers="this.headerObj"
                   style="display: inline-block">
          <el-button type="primary" class="ml-5" v-if="visaEditVisible">Upload File</el-button>
        </el-upload>
        <el-button @click="dialogFormVisible2 = false" type="primary"  class="ml-5">Close</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";



export default {
  name: "Visa",
  data(){
    return {
      tableData:[],
      join:[],

      customerIDSearch:1,
      // filter parameters
      visaID:"",
      userIDSearch:"",
      updatedOrder:"normal",

      total: 0,

      form:{
      },

      form2:{},

      storeMenus:localStorage.getItem("menus"),
      multipleSelection:[],
      pageNum: 1,
      pageSize: 5,

      //control the visibility of forms
      dialogFormVisible:false,
      dialogFormVisible2:false,
      dialogFormVisible3:false,

      searchVisaType:"",
      email:"",

      //these are used to verify the authority of user
      statusUpdateVisible:false,
      visaEditVisible:false,
      visaViewAllVisible:false,


      currentUser:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      //the header used for upload file
      headerObj:{
        'token': JSON.parse(localStorage.getItem("user")).token
      },




//variables for the customer table in 'add new dialog'
      tableDataCustomer:[],
      customerID:"",
      customerName:"",
      userID:0,
      currentRow: null,
      status:"",
      pageNumCustomer: 1,
      pageSizeCustomer: 5,
      server:this.$server
    }
  },
  created() {


    if(this.storeMenus.indexOf("Visa Status Update") !== -1){
      this.statusUpdateVisible = true;
    }

    if(this.storeMenus.indexOf("Visa Edit") != -1){
      this.visaEditVisible = true;
    }

      this.userIDSearch = this.currentUser.id;


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

    updateStatus(visaID, status){
      request.post("https://"+this.$server+"/visa/statusUpdate?visaID=" + visaID + "&status=" + status + "&requesterID=" + this.currentUser.id).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("status proceeded")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    revertStatus(visaID, status){
      request.post("https://"+this.$server+"/visa/statusRevert?visaID=" + visaID + "&status=" + status + "&requesterID=" + this.currentUser.id).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("status reverted")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.loadCustomer()
      this.dialogFormVisible3 = true
      this.form = {
        userID: this.currentUser.id,
        customerID: 0,
      }
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    fetch(visaID){
      this.dialogFormVisible2 = true
      request.get("https://"+this.$server+"/visa/visa-service?visaID="+visaID).then(res =>{
            console.log(res)
          this.join = res
      }
      )
    },
     load() {
      this.$notify.closeAll()
      request.get("https://"+this.$server+"/visa/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&visaID=" + this.visaID + "&visaType="+ this.searchVisaType + "&userID="+ this.userIDSearch + "&updatedOrder=" + this.updatedOrder).then(async res => {
        this.tableData = res.records
        this.total = res.total
        this.form.userID = this.currentUser.id
        //find the cases that have not been updated for too long
        for (const item of res.records) {
          let d1 = new Date()
          console.log("dates" +this.calculateDays(d1.toJSON().substring(0,10), item.updated.substring(0, 10)) )
          if (this.calculateDays(d1.toJSON().substring(0,10), item.updated.substring(0, 10)) > 10) {
            await this.$notify({
              type: 'warning',
              duration: 15000,
              title: '',
              message: "One case have not been updated for more than 10 days, case id:" + item.visaID,
              position: 'bottom-left'
            })
          }
        }
      })

    },
    calculateDays(date1,date2){
      let d1 = Date.parse(date1)
      let d2 = Date.parse(date2)
      let days = (d1 - d2)/(1*24*60*60*1000);
      return days
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    save() {
      console.log("https://"+this.$server+"/visa/save?requesterID="+this.currentUser.id, this.form)

      request.post("https://"+this.$server+"/visa/save?requesterID="+this.currentUser.id, this.form).then(res => {
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
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.username = ""
      this.load()
    },
//archive the service
    history(serviceID) {
      let ids = this.multipleSelection.map(v => v.serviceID)
      request.post("https://"+this.$server+"/service/history?requesterID="+this.currentUser.id, serviceID).then(res => {
        if (res.code == 'CODE_200') {
          this.$message.success("moved to history successfully")
          this.dialogFormVisible2 = false
          this.load()
        }else{
          this.$message.error(res.code+" "+res.msg)
        }
      })
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
    handleCurrentChangeCustomer(val) {
      this.currentRow = val;
      this.form.customerID = this.currentRow.customerID;
    },
    loadCustomer() {
      request.get("https://"+this.$server+"/customer/page?pageNum=" + this.pageNumCustomer + "&pageSize=" + this.pageSizeCustomer + "&customerID=" + this.customerID + "&name=" + this.customerName + "&status=" + this.status ).then(res => {
        this.tableDataCustomer = res.records
      })
    },
    handleFileUploadSuccess(res) {
      console.log(res)
      this.$message.success("Upload succeed")
      this.load()
    },
    uploadError(){
      this.$message.error("Upload fail, the serviceID may not exist, or the file name is duplicate")
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