<template>
  <div class="page-container">
    <!-- 第一张卡片：书籍详细信息 -->
    <el-card class="book-card">
      <h2 style="padding: 10px">图书详情</h2>
      <div class="book-content">
        <!-- 书籍封面 -->
        <img :src="book.imgHref" alt="书籍封面" class="book-image" />

        <!-- 书籍信息 -->
        <div class="book-text">
          <h3 class="book-name">{{ book.name }}</h3>
          <p class="book-author">{{ book.author }}</p>
          <p>简介：<span v-html="book.intro"></span></p>
          <p>出版年份：{{ new Date(book.publishYear).getFullYear() }}</p>
          <p>页数：{{ book.pages }}</p>
        </div>
      </div>
    </el-card>

    <!-- 第二张卡片：馆藏信息 -->
    <el-card class="book-card" style="margin-top: 40px;">
      <h2 style="padding: 10px">馆藏信息</h2>

      <div v-for="instance in bookInstances" :key="instance.barcode" class="book-content bookinstance-borrow">

          <div class="book-text">
            <p><strong>馆藏地点：</strong>{{ instance.location }}</p>
            <p><strong>馆藏编号：</strong>{{ instance.barcode }}</p>
          </div>
          <!-- 借阅按钮 -->
          <el-button v-if="instance.status == 1" type="primary" @click="borrowBook(instance)" class="borrow-btn">借阅</el-button>
        <el-button  v-else type="primary" @click="borrowBook(instance)" class="borrow-btn" :disabled="true">已借出</el-button>
      </div>
    </el-card>

    <!-- 第三张卡片：用户评论 -->
    <el-card class="book-card" style="margin-top: 40px;position: relative;">

      <h2 style="padding: 10px">用户评论</h2>
      <!-- 右上角按钮 -->
      <el-button
          type="primary"
          @click="addcomment"
          style="position: absolute; top: 10px; right: 10px;">
        添加评论
      </el-button>

      <div v-for="commentItem in comments" :key="commentItem.readerId" class="book-content">
        <div class="book-text comment-box">
          <div class="comment-content">
            <p><strong></strong>{{ commentItem.content }}</p>
          </div>
          <div class="comment-meta">
            <p><strong>读者ID：</strong>{{ commentItem.readerId }}</p>
            <p><strong>评论时间：</strong>{{ commentItem.time }}</p>
          </div>
        </div>

      </div>
    </el-card>
    <el-dialog v-model="dialogVisible" title="新闻发布" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="发表你的看法:">
          <el-input model-value="请输入你想说的话...." type="textarea" style="width: 80%" v-model="form.content" :autosize="{ minRows: 1, maxRows: 10 }"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="pub_comment">确 定</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex"; // 如果使用 Vuex 进行状态管理
import request from "@/utils/request";
import {resolveComponent} from "vue";
import {ElMessage} from "element-plus"; // 如果你使用 axios 来发起请求

export default {
  name: "LatestBookDetail",
  data() {
    return {
      book: {
        ISBN: "",
        name: "",
        author: "",
        publisher: "",
        intro: "",
        imgHref: "",
        publishYear: "",
        pages: "",
      },
      userId: "",
      bookInstances: [],
      comments: [],
      dialogVisible: false ,
      form: {},
      bookId: ""
    };
  },
  methods: {
    async fetchBookDetail(bookId) {
      console.log("加载书籍");
      try {
        const response = await request.get(`/book/${bookId}`);
        if (response.data) {
          this.book = response.data[0];
        }
      } catch (error) {
        console.error("获取图书详情失败:", error);
      }
    },
    async pub_comment(){
      try{
        console.log(this.form.content)
        const response = await request.post("/comment/pub",null,{
          params: {
            userId: this.userId,
            content: this.form.content,
            bookId: this.bookId
          }
        })
        ElMessage.success("发表成功!!!");
      }catch (error){
        console.log(error);
      }
      this.dialogVisible = false;

    },
    async fetchBookInstances(bookId) {
      try {
        const response = await request.get("/bookinstance", {
          params: { search1 : bookId },
        });
        if (response.data) {
          this.bookInstances = response.data.records;
        }
      } catch (error) {
        console.error("获取图书馆藏实例失败:", error);
      }
    },
    async fetchComments(bookId) {
      try {
        const response = await request.get("/comment/abook", {
          params: { bookId },
        });
        if (response.data) {
          this.comments = response.data.records;
        }
      } catch (error) {
        console.error("获取用户评论失败:", error);
      }
    },
    async addcomment(){
      this.dialogVisible = true;
      this.form = {}
    },
    async borrowBook(instance) {
      try {
        const response = await request.post("/LendRecord/borrow", instance, {
          params:{
            userId: this.userId,
          }
        });
        if (response.data.success) {
          alert(`成功借阅《${this.book.name}》`);
        } else {
          alert("借阅失败，请稍后再试。");
        }
      } catch (error) {
        console.error("借阅图书失败:", error);
        alert("借阅失败，请稍后再试。");
      }
    },
  },
  mounted() {
    const bookId = this.$route.params.id; // Vue 2 使用 this.$route 访问路由参数
    this.bookId = bookId;
    const userData = sessionStorage.getItem("user");
    const user = JSON.parse(userData);
    this.userId = user.id;

    this.fetchBookDetail(bookId);
    this.fetchBookInstances(bookId);
    this.fetchComments(bookId);
  },
};
</script>

<style scoped>
/* 同 Vue 3 的样式 */
.page-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  margin: 0;
  padding-top: 40px;
}

.book-card {
  width: 80%;
  max-width: 1200px;
  box-sizing: border-box;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
}


.book-content {
  display: flex;
  gap: 20px;
  line-height: 1.5;
}



.book-image {
  width: 150px;
  height: 220px;
  object-fit: cover;
  border-radius: 8px;
}
.borrow-btn{
  margin-top: 7px;
}
.book-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;  /* 为了定位内部元素 */
  gap: 10px;  /* 控制子元素之间的间距 */
}
.comment-box{
  padding-bottom: 20px;
  border-bottom: 1px solid #3a8ee6;
}

.comment-content {
  flex: 1;
}

.comment-meta {
  position: absolute;
  right: 0;
  bottom: 0;
  text-align: right;
  font-size: 12px;
  color: #888;
}

.bookinstance-borrow{
  margin-bottom: 5px;
}
.bookinstance-borrow:hover{
  background-color: lightskyblue;
  border-color: dodgerblue;
}
.book-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
}

.book-author {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
}

.book-intro {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.el-button {
  align-self: flex-start;
}
</style>
