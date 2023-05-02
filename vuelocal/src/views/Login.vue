<template>
  <div class="wrapper">
    <div
        style="margin: 200px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="authentication">
         <el-input size="medium" style="margin: 10px 0"
                    v-model="user.authentication"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="info" size="small" autocomplete="off" @click="auth">Send auth code to email</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import md5 from 'js-md5';

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: 'no username', trigger: 'blur'},

        ],
        password: [
          {required: true, message: 'no password', trigger: 'blur'},

        ],
      }
    }
  },
  methods: {
    auth(){
      let passwd = this.user.password
      let encryptedPasswd = md5(this.user.password)
      this.user.password = encryptedPasswd
      this.request.post("http://"+this.$server+":9090/user/email-auth", this.user).then(res => {
        if (res.code === 'CODE_200') {
           this.$message.success(res.msg+" code "+ res.code)
        } else {
          this.$message.error(res.msg +" code: "+ res.code)
        }
      })
      this.user.password = passwd
      passwd = null
    },
    login() {
      let passwd = this.user.password
      let encryptedPasswd = md5(this.user.password)
      this.user.password = encryptedPasswd

      this.request.post("http://"+this.$server+":9090/user/login", this.user).then(res => {
        if (res.code === 'CODE_200') {
          localStorage.setItem("user",JSON.stringify(res.data))
          localStorage.setItem("id",JSON.stringify(res.id))
          localStorage.setItem("username",JSON.stringify(res.data.username))
          localStorage.setItem("menus",JSON.stringify(res.data.menus))
          localStorage.setItem("login","true")
          this.$router.push("/home")
          this.$message.success("login success")
          // window.location.replace(":9090/home")
        } else {
          this.user.password = passwd
          passwd = null
         this.$message.error(res.msg +" code: "+ res.code)
        }
      })
      this.user.password = null

    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom, red,white,blue,white,red);
  overflow: hidden;
}
</style>