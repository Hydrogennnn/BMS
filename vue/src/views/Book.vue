 <template>
    <div>
      <el-card style="width: 80%; margin: 40px auto;">
        <h2 style="padding: 30px">馆藏书籍</h2>
        <!-- 添加外层容器 -->
        <div class="book-grid">
          <el-card
              v-for="(book, index) in bookList"
              :key="index"
              class="book-item"
              @click="gotoDetail(book.id)"
          >
            <div class="book-content">
            <!-- 书籍封面 -->
            <img :src="book.imgHref" alt="书籍封面" class="book-image" />
            <!-- 书籍信息 -->
            <div class="book-text">
            <h3 class="book-name">{{ book.name }}</h3>
<!--            <p class="book-createTime">{{ formatDate(book.createTime) }}</p>-->
            <p class="book-author">{{ book.author }}</p>
<!--            <p class="book-intro">{{ book.intro }}</p>-->
            <!-- 判断并限制 intro 字符数 -->
              <p class="book-intro" v-html="book.intro.length > 100 ? book.intro.slice(0, 100) + '...' : book.intro"></p>

              <!--            <el-link @click="showDetail(book)">查看详情</el-link>-->
<!--              <el-link :to="{ name: 'bookDetail', params: { id: book.id } }">查看详情</el-link>-->
              <!-- 使用 router-link 进行跳转，并传递书籍的 id -->
<!--              <router-link :to="{ path: '/bookDetail', query: { id: book.id } }">-->
<!--                查看详情-->
<!--              </router-link>-->
            </div>
        </div>
          </el-card>
        </div>
      </el-card>

<!--      &lt;!&ndash; 显示详情 &ndash;&gt;-->
<!--      <el-card v-if="selectedNews" style="width: 80%; margin: 40px auto;">-->
<!--        <h3 class="detail-title">{{ selectedNews.title }}</h3>-->
<!--        <p class="detail-time">{{ formatDate(selectedNews.time) }}</p>-->
<!--        <p class="detail-content">{{ selectedNews.content }}</p>-->
<!--        <div class="button-container">-->
<!--          <el-button @click="closeDetail">关闭</el-button>-->
<!--        </div>-->
<!--      </el-card>-->
    </div>
  </template>


<script>
import { ref, onMounted } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';
import router from "@/router";

export default {
  name: "LatestBook",
  setup() {
    const bookList = ref([]);
    const selectedBook = ref(null);

    // 获取最新新闻
    const fetchBook = async () => {
      try {
        const response = await request.get('/book');
        bookList.value = response.data.records;
        console.log(bookList.value);
      } catch (error) {
        console.error('获取最新图书信息失败:', error);
        ElMessage.error('获取最新图书信息失败，请稍后重试。');
      }
    };

    // 显示详情
    const showDetail = (book) => {
      selectedBook.value = book;
    };

    // 关闭详情
    const closeDetail = () => {
      selectedBook.value = null;
    };

    // 格式化日期
    const formatDate = (date) => {
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      }).format(new Date(date));
    };

    const gotoDetail=(bookId)=> {
      router.push({ path: '/bookDetail', query: { id: bookId } }); // 使用查询参数传递 bookId
    };

    onMounted(fetchBook);
    return {
      bookList,
      selectedBook,
      formatDate,
      showDetail,
      closeDetail,
      gotoDetail
    };
  }
}
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

 .book-grid {
   display: flex;
   flex-wrap: wrap; /* 多行排列 */
   gap: 16px; /* 每个卡片之间的间距 */
   justify-content: space-between; /* 保证卡片均匀分布 */
 }

.book-item {
  width: 280px; /* 固定每个信息组件的宽度 */
  height: auto; /* 高度自适应 */
  box-sizing: border-box; /* 包括边框和内边距 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
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
