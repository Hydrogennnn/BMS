<template>
  <div class="page-container">
    <!-- 第一张卡片：书籍详细信息 -->
    <el-card class="book-card">
      <h2 style="padding: 30px">图书详情</h2>
      <div class="book-content">
        <!-- 书籍封面 -->
        <img :src="book.value.imgHref" alt="书籍封面" class="book-image" />

        <!-- 书籍信息 -->
        <div class="book-text">
          <h3 class="book-name">{{ book.value.name }}</h3>
          <p class="book-author">{{ book.value.author }}</p>
          <p>简介：<span v-html="book.value.intro"></span></p>
          <p>出版年份： {{ new Date(book.value.publishYear).getFullYear() }}</p>
          <p>页数： {{ book.value.pages }}</p>
        </div>
      </div>
    </el-card>

    <!-- 第二张卡片：馆藏信息 -->
    <el-card class="book-card" style="margin-top: 40px;">
      <h2 style="padding: 30px">馆藏信息</h2>
      <div v-for="instance in bookInstances" :key="instance.barcode" class="book-content">
        <div class="book-text">
          <p><strong>馆藏地点：</strong>{{ instance.location }}</p>
          <p><strong>馆藏编号：</strong>{{ instance.barcode }}</p>
        </div>
        <!-- 借阅按钮 -->
        <el-button type="primary" @click="borrowBook(instance)">借阅</el-button>
      </div>
    </el-card>

    <!-- 第三张卡片：用户评论 -->
    <el-card class="book-card" style="margin-top: 40px;">
      <h2 style="padding: 30px">用户评论</h2>
      <div v-for="commentItem in comments" :key="commentItem.readerId" class="book-content">
        <div class="book-text">
          <p><strong>读者ID：</strong>{{ commentItem.readerId }}</p>
          <p><strong>评论时间：</strong>{{ commentItem.time }}</p>
          <p><strong>评论内容：</strong>{{ commentItem.content }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';  // 如果你使用 axios 来发起请求

export default {
  name: "LatestBookDetail",
  setup() {
    // 获取当前路由对象
    const route = useRoute();
    const bookId = route.params.id;  // 从路由参数中获取书籍的ID

    // 存储图书的详细信息
    const book = ref({
      ISBN: "",
      name: "",
      author: "",
      publisher: "",
      intro: "",
      imgHref: "",
      publishYear: "",
      pages: "",
    });
    // 存储多个馆藏实例信息
    const bookInstances = ref([]);

    // 存储多个用户评论信息
    const comments = ref([]);


    // 借阅图书的功能
    const borrowBook = async (instance) => {
      try {
        // 获取 user_id
        const userData = sessionStorage.getItem("user");
        const user = JSON.parse(userData);
        const user_id = user.id;

        // 调用借阅接口
        const response = await axios.post('/LendRecord/borrow', instance,user_id);

        if (response.data.success) {
          alert(`成功借阅《${book.value.name}》`);
        } else {
          alert("借阅失败，请稍后再试。");
        }
      } catch (error) {
        console.error("借阅图书失败:", error);
        alert("借阅失败，请稍后再试。");
      }
    };
    const fetchBookDetail = async (bookId) => {
      try {
        const response = await axios.get(`/book/detail/${bookId}`);  // 使用实际的API路径
        if (response.data) {
          // 更新book的数据
          book.value = response.data;
        }
      } catch (error) {
        console.error("获取图书详情失败:", error);
      }
    };

    // 获取图书馆藏实例信息
    const fetchBookInstances = async (bookId) => {
      try {
        const response = await axios.get(`/bookinstance`, {
          params: { bookId }  // 传递图书ID获取对应的馆藏信息
        });
        if (response.data) {
          bookInstances.value = response.data.records;
        }
      } catch (error) {
        console.error("获取图书馆藏实例失败:", error);
      }
    };

    // 获取图书的用户评论
    const fetchComments = async (bookId) => {
      try {
        const response = await axios.get(`/comment/abook`, {
          params: { bookId }  // 传递图书ID获取对应的评论
        });
        if (response.data) {
          comments.value = response.data.records; // 假设返回的数据包含用户评论
        }
      } catch (error) {
        console.error("获取用户评论失败:", error);
      }
    };

    // 在组件挂载时，调用接口获取图书详细信息
    onMounted(() => {
      fetchBookDetail(bookId);  // 根据bookId获取图书信息
      fetchBookInstances(bookId);  // 获取馆藏信息
      fetchComments(bookId);  // 获取用户评论
    });

    return {
      book,
      comments,
      borrowBook,
      bookInstances,
      fetchBookDetail
    };
  }
};
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;  /* 将卡片放在页面上方 */
  align-items: center;          /* 水平居中 */
  height: 100vh;                /* 高度为视口高度 */
  margin: 0;
  padding-top: 40px;            /* 给顶部留出一些空间 */
}

.book-card {
  width: 80%;                  /* 卡片宽度为 80% */
  max-width: 1200px;           /* 最大宽度 1200px，避免卡片过大 */
  box-sizing: border-box;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.book-content {
  display: flex;
  gap: 20px;  /* 书籍封面和信息之间的间距 */
}

.book-image {
  width: 150px;        /* 图片宽度 */
  height: 220px;       /* 图片高度 */
  object-fit: cover;   /* 保持图片的比例 */
  border-radius: 8px;
}

.book-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
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

.book-recommendation {
  font-size: 14px;
  color: #555;
}

/* 第二张卡片：馆藏信息 */
.book-card + .book-card {
  margin-top: 40px;
}

.el-button {
  align-self: flex-start; /* 借阅按钮左对齐 */
}

/* 第三张卡片：用户评论 */
.book-card + .book-card + .book-card {
  margin-top: 40px;
}

.book-text p {
  margin-bottom: 10px;
}
</style>
