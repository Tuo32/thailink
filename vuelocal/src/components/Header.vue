<template>
  <div style="font-size: 18px; text-align: right; font-size: 12px;  line-height: 60px; display:flex ">
    <div style="font-size: 24px; color: RGB(48,65,85);">
     <b> {{ currentPath() }}</b>
    </div>


    <div style="flex: 2; ">

      <!--    <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>-->

    </div>
  <el-dropdown style=" cursor:pointer">
    <span style="border-style: solid; border-width: 1px; padding: 5px;" >Hello {{user.name}}  <i class="el-icon-arrow-down" ></i></span>

    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item>
        <span style="text-decoration: none " @click="goToUser"> 个人信息（user information）</span>
      </el-dropdown-item>
      <el-dropdown-item>
        <span style="text-decoration: none " @click="logout">退出（logout）</span>
      </el-dropdown-item>

    </el-dropdown-menu>


  </el-dropdown>
    </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass: String,
    collapse: Boolean
  },
  data(){
    return {
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      storeMenus: localStorage.getItem("menus"),

    }
  },
  methods:{
    currentPath() {
      let menus = JSON.parse(this.storeMenus)
      console.log("menus"+menus)
      const path = this.$route.path;
      const modifiedPath = path.slice(0, 1) + path.charAt(1).toUpperCase() + path.slice(2) + " Management";
      console.log(modifiedPath);
      return modifiedPath.substring(1);
    },

    logout(){
      localStorage.removeItem("user")
      localStorage.setItem("login","false")
      this.$message.success("user log out")
      localStorage.clear();
      this.$router.push("/")
    },
    goToUser(){
      this.$router.push("/person")
    },

  }

}
</script>

<style scoped>

</style>