<template>
  <el-menu :default-openeds="[]" style="

        min-height:100%;  overflow-x:hidden"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse= false
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center; ">
      <img src="../assets/logo.png" style="width: 20px; position: relative; top:5px; margin-right: 5px"/>
      <b style="color:white" v-show=true>Thailink Management System</b>
    </div>
    <el-menu-item :index="'/home'">
        <i :class="'el-icon-s-home'"></i>
      <span slot="title">Home</span>
    </el-menu-item>
<!--    the navigation menus are dynamically retrieved from database-->
      <div v-for="item in menus" :key="item.menuID">
        <div v-if="item.position == 'aside'">
          <el-menu-item :index="item.path">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.menuName}}</span>
          </el-menu-item>
          </div>
        </div>

  </el-menu>

</template>

<script>
export default {
  name: "Aside",

  data(){
    return{
      menus: localStorage.getItem("menus")?JSON.parse(localStorage.getItem("menus")) : [],

      isCollapse : false,
    }
  },
  created() {
    console.log(this.menus)
  },
  methods:{
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
    }
  }
}
</script>

<style scoped>




</style>