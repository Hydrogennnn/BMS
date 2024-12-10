<template>
  <div>
    <el-card style="width: 80%; margin: 40px auto;">
      <h2 style="padding: 30px">最新公告</h2>
      <el-table :data="noticeList" style="width: 100%">
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


    <!-- 显示公告详情 -->
    <el-card v-if="selectedNotice" style="width: 80%; margin: 40px auto;">
      <h3 class="detail-title">{{ selectedNotice.title }}</h3>
      <p class="detail-time">{{ formatDate(selectedNotice.time) }}</p>
      <p class="detail-content">{{ selectedNotice.content }}</p>
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
import {useRouter} from "vue-router/dist/vue-router";

export default {
  name: "LatestNotice",
  setup() {
    const noticeList = ref([]);
    const selectedNotice = ref(null);
    const router = useRouter(); // 使用 useRouter 获取路由实例

    // 获取最新公告
    const fetchNotice = async () => {
      try {
        const response = await request.get('/notice');
        noticeList.value = response.data.records;
      } catch (error) {
        console.error('获取最新公告失败:', error);
        ElMessage.error('获取最新公告失败，请稍后重试。');
      }
    };

    // 显示公告详情
    const showDetail = (notice) => {
      router.push({ name: 'NoticeDetails' , params: {id: notice.id}});
    };

    // 关闭公告详情
    const closeDetail = () => {
      selectedNotice.value = null;
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

    onMounted(fetchNotice);

    return {
      noticeList,
      selectedNotice,
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

}

.button-container {
  text-align: center;
  margin-top: 20px;
}
</style>
