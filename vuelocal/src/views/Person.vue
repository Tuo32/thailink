<template>
  <div>
    <el-card style="width: 500px;">
    <el-form label-width="100px" >
      <el-form-item label="username:" >
       {{user.name}}
      </el-form-item>
      <el-form-item label="email:" >
        {{user.email}}
      </el-form-item>
      <el-form-item label="phone:" >
        {{user.phone}}
      </el-form-item>
      <el-form-item label="address:" >
        {{user.address}}
      </el-form-item>
      If your information is wrong, please contact the admin
    </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Person",
  data(){
    return {
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },

}

</script>

<style scoped>

</style>