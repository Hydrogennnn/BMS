<template>

  <div class="news-detail">
    <el-card v-if="newsItem" style="width: 80%; margin: 40px auto;">
      <h3 class="detail-title">{{ newsItem.title }}</h3>
      <p class="detail-time">{{ formatDate(newsItem.time) }}</p>
      <div v-html="newsItem.content" class="detail-content"></div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "newDetails",
  data() {
    return {
      newsItem:{title:"这是一条新闻的标题",content:"这是一条新闻的内容。",time:"2024-11-23"},
      // newsItems:null, // 用于存储新闻详情的数据
    };
  },
  created() {
    this.fetchNewsDetail();
  },
  methods: {
    formatDate(date){
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      }).format(new Date(date));
    },
    fetchNewsDetail() {
      try {
        // 假设你有一个API端点可以返回新闻详情
        const newsId = this.$route.params.id;
        console.log(newsId);
        request.get("/notice/"+newsId).then(res=>{
          console.log(res)
          this.newsItem = res.data[0];
        })
      } catch (error) {
        console.error('Failed to fetch news detail:', error);
        // 可以显示一个错误消息给用户
      }
    },
  },
};
</script>


<style scoped>

.news-detail{
  padding: 10px;
  overflow-y: auto; /* 允许垂直方向滚动 */
  height: 800px;
}
.title {
  text-align: center; /* 使标题居中 */
  margin-bottom: 20px; /* 添加标题与内容之间的间距 */
}

.content {
  text-align: left; /* 使内容左对齐 */
  text-indent: 2em; /* 为首段添加缩进，模拟首行空格效果 */
  margin-bottom: 20px; /* 添加内容与时间戳之间的间距 */
}

.time {
  text-align: right; /* 使时间戳右对齐 */
}
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