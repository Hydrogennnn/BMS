<template>
  <div class="showcase-container">
    <!-- 左边半部分 -->
    <div class="left-panel">
      <el-row :gutter="20">
        <el-col :span="5" v-for="item in cards" :key="item.title">
          <el-card class="box-card">
            <div slot="header" class="clearfix">{{ item.title }}</div>
            <div class="text item">
              <svg class="icon" aria-hidden="true">
                <use :xlink:href="item.icon" style="width: 100px"></use>
              </svg>
              <span class="text">{{ item.data }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div id="myTimer" style="margin-left: 15px;font-weight: 550;margin-top: 30px"></div>
      <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
      <div id="main" style="width: 700px; height: 350px; margin-left: 5px;"></div>
    </div>
    <!-- 右边半部分 -->
    <div class="right-panel">
      <!-- 上半部分 -->
      <div class="top-section">
        <h2>公告</h2>
        <el-divider />

        <el-scrollbar height="250px">

          <div v-for="item in noticelist" :key="item.title" class="notice-item">
            <el-button text class="button" @click="viewNotice(item)">
              {{ item.title }}
            </el-button>
            <span class="notice-time" style="float: right; margin-left: 10px;">{{ item.time }}</span>
            <el-divider class="notice-divider"/>
          </div>

        </el-scrollbar>

      </div>

      <!-- 下半部分 -->
      <div class="bottom-section">
        <h2>新闻</h2>
        <el-divider />

        <el-scrollbar height="350px">
          <div v-for="item in news" :key="item.id" class="news-item">
            <el-link :href="getNewsUrl(item.id)" type="primary" class="news-title">{{ item.title }}</el-link>
            <div class="news-time" style="text-align: right;">{{ item.time }}</div>
            <el-divider class="news-divider" />
          </div>
        </el-scrollbar>
      </div>

    </div>

    <!-- 抽屉组件 -->
    <el-drawer
        v-model="table"
        title="公告详情"
        direction="rtl"
        size="50%"
    >
      <div class="notice">
        <h3 class="title">{{currentNotice.title}}</h3>
        <p class="content">{{currentNotice.content}}</p>
        <p class="time">{{currentNotice.time}}</p>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {ElMessage} from "element-plus";
import request from "../utils/request";

export default {
  data() {
    return {
      table:false,
      cards: [
        { title: '已借阅', data: 10, icon: '#iconlend-record-pro' },
        { title: '总访问', data: 10, icon: '#iconvisit'   },
        { title: '图书数', data: 10, icon: '#iconbook-pro' },
        { title: '用户数', data: 100, icon: '#iconpopulation' }
      ],
      data:{},
      noticelist:[ ],
      news:[
        {title:'新闻标题1,2024年度知识竞赛成功举办',content:'这是第1条新闻的内容',time:'2024-11-22'},
        {title:'新闻标题2',content:'这是第2条新闻的内容',time:'2024-11-23'},
        {title:'新闻标题3',content:'这是第3条新闻的内容',time:'2024-11-23'},
        {title:'新闻标题4',content:'这是第4条新闻的内容',time:'2024-11-23'},
        {title:'新闻标题5',content:'这是第5条新闻的内容',time:'2024-11-23'},
        {title:'新闻标题6',content:'这是第6条新闻的内容',time:'2024-11-23'},
        {title:'新闻标题7',content:'这是第7条新闻的内容',time:'2024-11-23'}
      ],
    }
  },
  created() {
    request.get("/news",{
      params:{
        pageNum: 1,
        pageSize: 5,
      }
    }).then(res=>{
      if(res.code == 0){
        this.news = res.data.records;
        this.news.forEach(item=>{
          const date = new Date(item.time);
          item.time = date.toISOString().split('T')[0]; // 获取 ISO 格式的日期部分 yyyy-MM-dd
        })
      }
      else {
        ElMessage.error(res.msg);
      }
    })
    request.get("/notice",{
      params:{
        pageNum: 1,
        pageSize: 5,
      }
    }).then(res=>{
      if(res.code == 0){
        this.noticelist = res.data.records;
        this.noticelist.forEach(
            item=>{
              const date = new Date(item.time);
              item.time = date.toISOString().split('T')[0]; // 获取 ISO 格式的日期部分 yyyy-MM-dd
            }
        )
      }
      else{
        ElMessage.error(res.msg);
      }
    })
  },
  mounted() {
    this.circleTimer()

    request.get("/dashboard").then(res=>{
      if(res.code == 0)
      {

        this.cards[0].data = res.data.lendRecordCount
        this.cards[1].data = res.data.visitCount
        this.cards[2].data = res.data.bookCount
        this.cards[3].data = res.data.userCount

      }
      else
      {
        ElMessage.error(res.msg)
      }


      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('main'))
      console.log(this.cards[0].data)
      // 绘制图表
      myChart.setOption({
        title: {
          text: '统计'
        },
        tooltip: {
          trigger: 'axis'
          // axisPointer: {
          //   type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          // }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.cards.map(item => item.title),
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            type: 'bar',
            label: { show: true },
            barWidth: '25%',
            data: [
              {
                value: this.cards[0].data,
                itemStyle: { color: '#5470c6' }
              },
              {
                value: this.cards[1].data,
                itemStyle: { color: '#91cc75' }
              },
              {
                value: this.cards[2].data,
                itemStyle: { color: '#fac858' }
              },
              {
                value: this.cards[3].data,
                itemStyle: { color: '#ee6666' }
              }
            ]
          }
        ]
      })
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    })
  },
  methods: {
    getNewsUrl(newsId) {
      // 根据新闻ID生成对应的URL，这里以假设的路由为例
      return `/newsDetails/${newsId}`;
    },

    viewNotice(notice) {
      this.currentNotice = notice; // 将当前选中的图书数据赋值给currentNotice
      this.table = true; // 显示抽屉
    },
    circleTimer() {
      this.getTimer()
      setInterval(() => {
        this.getTimer()
      }, 1000)
    },
    getTimer() {
      var d = new Date()
      var t = d.toLocaleString()
      document.getElementById('myTimer').innerHTML = t
    }
  }
}
</script>

<style scoped>
.notice-item {
  /* 为每个通知项设置适当的布局和间距 */
  position: relative; /* 为了能够使用绝对定位来放置时间 */
  padding-bottom: 10px; /* 为分隔符上方的空间留出一些余地 */
  margin-bottom: 0px; /* 为每个通知项之间添加一些垂直间距 */
}

.button {
  /* 调整按钮的样式 */
  margin-bottom: 10px; /* 为按钮下方留出一些空间，以便时间与按钮不紧贴 */
}

.notice-time {
  /* 调整时间的样式和位置 */
  position: absolute; /* 使用绝对定位 */
  right: 0; /* 将其放置在父容器的右侧 */
  margin-bottom: 15px;
  margin-left: 10px; /* 保持与时间左侧的间距 */
}
.news-time {

  right: 0; /* 将其放置在父容器的右侧 */
  margin-bottom: 20px;
  margin-left: 10px; /* 保持与时间左侧的间距 */
}
.notice-divider {
  /* 调整分隔符的样式 */
  margin-top:0px; /* 移除分隔符上方的默认间距 */
}
.news-divider{
  margin-top: 0px; /* 减少分隔符与上面内容的间距 */
}
.news-title
{
  margin-bottom: -3px;
  font-size: 18px;
  color: #443535;
}
.news-item{
  font-size: 15px; /* 根据需要调整字体大小 */
  padding: -5px -5px; /* 根据需要调整内边距 */
}

.button{
  font-size: 18px; /* 根据需要调整字体大小 */
  padding: 10px 20px; /* 根据需要调整内边距 */
}
.notice{
  padding: 20px;
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
  margin-bottom: 15px;
}
.showcase-container {
  display: flex; /* 使用 flex 布局 */
  height: 100vh; /* 使容器高度占满视口高度 */
}

.left-panel {
  height: 800px;
  flex: 1; /* 左边部分占据一半的空间（或者根据需要调整比例） */
  background-color: #ffffff; /* 背景颜色，可以根据需要更改 */
  padding: 20px; /* 内边距，可以根据需要调整 */
  box-sizing: border-box; /* 盒模型为 border-box */
}

.right-panel {
  height: 800px;
  flex: 1; /* 右边部分也占据一半的空间 */
  display: flex; /* 右边部分再使用 flex 布局，以便分为上下两部分 */
  flex-direction: column; /* 垂直方向排列子元素 */
}

.top-section, .bottom-section {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  padding: 20px; /* 可选，增加内边距使阴影更明显 */
  margin-top: 20px; /* 可选，增加上边距使阴影与上方内容分隔 */
  background-color: #fff; /* 可选，设置背景色以更好地突出阴影 */
  flex: 1; /* 上半部分和下半部分各自占据一半的空间（或者根据需要调整比例） */
  padding: 20px; /* 内边距，可以根据需要调整 */
  box-sizing: border-box; /* 盒模型为 border-box */
}

.top-section {
  background-color: #ffffff; /* 上半部分背景颜色，可以根据需要更改 */
}

.bottom-section {

  background-color: #fff; /* 可选，设置背景色以更好地突出阴影 */
}

.box-card {
  width: 74%;
  height: 150px;
  margin-bottom: 20px;
  margin-left: 50px;
}

.clearfix {
  font-weight: 500;
  text-align: center;
  font-size: 17px;
}

.text {
  text-align: center;
  font-size: 25px;
  font-weight: 700;
  vertical-align: super;
}

#main {
  width: 100%;
  height: 500px;
  margin-top: 85px;
}

.icon {
  width: 70px;
  height: 50px;
  padding-top: 10px;
  padding-right: 10px;
}
</style>