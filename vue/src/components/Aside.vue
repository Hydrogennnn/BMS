<template>
<div>


  <el-menu
      style="width: 200px; min-height: calc(100vh - 50px);"
      :default-active="path"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      router
      background-color="#30333c" text-color="#fff"
  >
    <el-menu-item index="/dashboard" >
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icondashboard "></use>
      </svg>
      <span>展示板</span>
    </el-menu-item>
    <el-sub-menu index="2" text-color="#fff">
      <template #title>
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mingpian "></use>
        </svg>
        <span>个人信息</span>
      </template>
      <el-menu-item index="/person" style="font-color: white">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-a-bianji1 "></use>
        </svg>
        <span>修改个人信息</span>
      </el-menu-item>
      <el-menu-item index="/password">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mima "></use>
        </svg>
        <span>修改密码</span>
      </el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/user" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconreader "></use>
      </svg>
      <span>读者管理</span>
    </el-menu-item>
    <el-menu-item index="/bookManage" v-if="user.role == 1" >
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>书籍管理</span>
    </el-menu-item>
    <el-menu-item index="/bookinstance" v-if="user.role == 1" >
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>书本管理</span>
    </el-menu-item>
    <el-menu-item index="/book" v-if="user.role == 2">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>图书查询</span>
    </el-menu-item>
    <el-menu-item index="/lendrecord">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>借阅信息</span>
    </el-menu-item>

    <el-menu-item index="/news" v-if="user.role == 2">
      <el-icon><Smoking /></el-icon>
      <span>资源动态</span>
    </el-menu-item>
    <el-menu-item index="/notice" v-if="user.role == 2">
      <el-icon><Calendar /></el-icon>
      <span>最新公告</span>
    </el-menu-item>
    <el-menu-item index="/newsmanage" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>新闻管理</span>
    </el-menu-item>
    <el-menu-item index="/noticemanage" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>公告管理</span>
    </el-menu-item>
    <el-menu-item index="/commentmanage" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>评论管理</span>
    </el-menu-item>

<!--    <el-menu-item index="/f" v-if="user.role == 2">-->
<!--      <svg class="icon" aria-hidden="true">-->
<!--        <use xlink:href="#iconlend-record"></use>-->
<!--      </svg>-->
<!--      <span>借阅信息</span>-->
<!--    </el-menu-item>-->
<!--    <el-menu-item index="/bookwithuser" >-->
<!--      <el-icon><grid /></el-icon>-->
<!--      <span>借阅状态</span>-->
<!--    </el-menu-item>-->
  </el-menu>

</div>
</template>

<script>




export default {
  name: "Aside",
  components:{},
  created(){
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
  },
  data(){
    return {
      user:{},
      path: this.$route.path
    }
  }
}
</script>

<style scoped>
.icon {
  width: 30px;
  height: 30px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>
