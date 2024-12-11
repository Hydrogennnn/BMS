<template>
  <div class="home">

    <el-card style="width: 80%; margin: 40px auto;">
      <div style="margin: 10px 0;">
        <el-form inline="true" size="small">
          <el-form-item label="图书编号" >
            <el-input v-model="search1" placeholder="请输入图书编号"  clearable>
              <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
            </el-input>
          </el-form-item >
          <el-form-item label="图书名称" >
            <el-input v-model="search2" placeholder="请输入图书名称"  clearable>
              <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
            </el-input>
          </el-form-item >
          <el-form-item label="作者" >
            <el-input v-model="search3" placeholder="请输入作者"  clearable>
              <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
            </el-input>
          </el-form-item >
          <el-form-item>
            <el-button type="primary" style="margin-left: 1%" @click="fetchBook" size="mini" >
              <svg-icon iconClass="search"/>查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="mini"  type="danger" @click="clear">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <h2 style="padding: 30px">馆藏书籍</h2>
      <!-- 添加外层容器 -->
      <div class="book-grid">
        <el-card
            v-for="(book, index) in bookList"
            :key="index"
            class="book-item"
            @click="gotoDetail(book.id)"
            shadow="hover"
        >
          <div class="book-content">
            <!-- 书籍封面 -->
            <img :src="book.imgHref" alt="书籍封面" class="book-image" />
            <!-- 书籍信息 -->
            <div class="book-text">
              <h3 class="book-name">{{ book.name }}</h3>
              <p class="book-author">{{ book.author }}</p>
              <p class="book-intro" v-html="book.intro.length > 100 ? book.intro.slice(0, 100) + '...' : book.intro"></p>
            </div>
          </div>
        </el-card>
      </div>
    </el-card>
    <el-pagination
        v-model="currentPage"
        :page-sizes="[9, 12]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>
</template>

<script>
import request from '@/utils/request';
import { ElMessage } from 'element-plus';
import router from "@/router";

export default {
  name: "LatestBook",
  data() {
    return {
      currentPage: 1,
      pageSize: 9,
      total: 10,
      bookList: [],
      selectedBook: null,
      search1: '',
      search2: '',
      search3: '',
    };
  },
  methods: {
    // 获取图书数据
    async fetchBook() {
      try {
        const response = await request.get('/book', {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            search1: this.search1,
            search2: this.search2,
            search3: this.search3,
          }
        });
        this.bookList = response.data.records;
        this.total = response.data.total;
      } catch (error) {
        console.error('获取最新图书信息失败:', error);
        ElMessage.error('获取最新图书信息失败，请稍后重试。');
      }
    },
    // 处理页面大小变化
    handleSizeChange(PageSize) {
      this.pageSize = PageSize;
      this.fetchBook();
    },
    clear(){
      this.search1 = '';
      this.search2 = '';
      this.search3 = '';
    },
    // 处理当前页变化
    handleCurrentChange(PageNum) {
      this.currentPage = PageNum;
      this.fetchBook();
    },
    // 显示书籍详情
    showDetail(book) {
      this.selectedBook = book;
    },
    // 关闭书籍详情
    closeDetail() {
      this.selectedBook = null;
    },
    // 格式化日期
    formatDate(date) {
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      }).format(new Date(date));
    },
    // 跳转到书籍详情页面
    gotoDetail(id) {
      router.push({ name: 'BookDetail', params: { id: id } }); // 使用查询参数传递 bookId
    }
  },
  mounted() {
    this.fetchBook(); // 页面加载时获取书籍列表
  }
};
</script>

<style scoped>
.book-grid {
  display: flex;
  flex-wrap: wrap; /* 多行排列 */
  gap: 16px; /* 每个卡片之间的间距 */
  justify-content: space-between; /* 保证卡片均匀分布 */
}

.book-item {
  flex: 1 1 calc(33.333% - 16px); /* 每行显示三条信息 */
  box-sizing: border-box; /* 包括边框和内边距 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.book-name {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 8px;
}

.book-createTime {
  font-size: 14px;
  color: #999;
  margin: 0 0 16px;
}

.book-author {
  font-size: 14px;
  color: #333;
  margin: 0 0 8px;
}

.book-intro {
  font-size: 14px;
  color: #666;
  margin: 0 0 16px;
}

.book-content {
  display: flex;
  gap: 16px; /* 图片与文字之间的间距 */
}

.book-image {
  width: 80px; /* 固定图片宽度 */
  height: 120px; /* 固定图片高度 */
  object-fit: cover; /* 保证图片不失真 */
  border-radius: 8px;
}

.book-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
