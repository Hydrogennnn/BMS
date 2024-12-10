<template>
  <div>
    <el-card style="width: 80%; margin: 40px auto;">
      <h2 style="padding: 30px">资源动态</h2>
      <el-table :data="newsList" style="width: 100%">
        <el-table-column prop="title" label="标题" width="800">
          <template #default="{ row }">
            <el-link @click="showDetail(row)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="时间" width="340">
          <template #default="{ row }">
            {{ formatDate(row.time) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 显示详情 -->
    <el-card v-if="selectedNews" style="width: 80%; margin: 40px auto;">
      <h3 class="detail-title">{{ selectedNews.title }}</h3>
      <p class="detail-time">{{ formatDate(selectedNews.time) }}</p>
      <div v-html="selectedNews.content"></div>
      <div class="button-container">
        <el-button @click="closeDetail">关闭</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';  // 导入 useRouter 钩子

export default {
  name: "LatestNews",
  setup() {
    const newsList = ref([]);
    const selectedNews = ref(null);
    const router = useRouter(); // 使用 useRouter 获取路由实例

    // 获取最新新闻
    const fetchNews = async () => {
      try {
        const response = await request.get('/news');
        newsList.value = response.data.records;
      } catch (error) {
        console.error('获取最新资讯失败:', error);
        ElMessage.error('获取最新资讯失败，请稍后重试。');
      }
    };

    // 显示详情
    const showDetail = (news) => {
      // 使用 router.push 跳转到新闻详情页面
      router.push({ name: 'NewsDetails' , params: {id: news.id}});
    };

    // 关闭详情
    const closeDetail = () => {
      selectedNews.value = null;
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

    onMounted(fetchNews);

    return {
      newsList,
      selectedNews,
      formatDate,
      showDetail,
      closeDetail
    };
  }
}
</script>

<style scoped>
.detail-title {
  text-align: center;
  margin: 0;
}

.detail-time {
  text-align: center;
  margin: 10px 0;
}

.detail-content {
  margin-top: 20px;
  text-indent: 2em;
  line-height: 2;
}

.button-container {
  text-align: center;
  margin-top: 20px;
}
</style>
