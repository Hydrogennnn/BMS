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
      noticelist:[
        { title: '2024年度知识竞赛成功举办', content: '2024年度图书馆举办的知识竞赛活动于11月20日下午2点拉开帷幕，各位同学积极参与！！！！', time: '2024-12-05' },
        { title: '寒假开放时间调整', content: '寒假期间图书馆开放时间将调整为每日9:00-17:00，请合理安排借阅时间。', time: '2024-12-01' },
        { title: '图书馆资源维护通知', content: '由于系统维护，本馆所有在线数据库将在2024年12月10日至2024年12月12日无法访问，请提前做好相关准备。', time: '2024-12-03' },
      ],
      news:[
        {title:'2024年度知识竞赛成功举办',content:'2024年度图书馆举办的知识竞赛活动于11月20日下午2点拉开帷幕，各位同学积极参与！！！！',time:'2024-11-22'},
        {title:'数据库上新——“中国音网”数据库',content:'中国音网是由华韵文化科技有限公司与中央音乐学院共同建设的公共文化有声数据资源库，内容依托于华韵档案馆自身收藏的近10万张老唱片，整合了海内外音响档案馆、独立藏家和艺术家的私人资源，介质类型包括蜡筒、唱片、开盘带等等，体裁包括了戏曲、曲艺、器乐、歌舞等多个门类，其中不少为华韵独藏的善本、珍本、孤本。此次我馆采购了其中两个子库——红色音乐文化基因库、人类口头和非物质文化遗产基因库。\n' +
              '\n' +
              '红色音乐文化基因库收录了1万余首中华民族之红色记忆的象征性曲目，描述和再现历史事件、英烈人物、红色历程等，包括《百年峥嵘岁月-中国红歌汇》《歌剧—洪湖赤卫队》《让我们荡起双桨》等专辑。',time:'2024-11-23'},
        {title:'第十五届全国知识组织与知识服务学术研讨会会议通知',content:'我们诚挚邀请您参加第十五届全国知识组织与知识服务学术研讨会。本次会议将汇聚国内外专家学者，共同探讨知识组织与知识服务领域的最新研究成果与发展趋势。会议内容包括主题演讲、学术论文交流及圆桌讨论，期待您的参与与分享。\n' +
              '\n' +
              '请关注我们的后续通知，获取更多会议细节与注册信息。',time:'2024-11-23'},
      ],
    }
  },
  created() {

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
      return `/news/${newsId}`;
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
  top: 50%; /* 垂直居中，但需要进一步调整 */
  transform: translateY(-50%); /* 通过平移来真正实现垂直居中 */
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
}
.showcase-container {
  display: flex; /* 使用 flex 布局 */
  height: 100vh; /* 使容器高度占满视口高度 */
}

.left-panel {
  flex: 1; /* 左边部分占据一半的空间（或者根据需要调整比例） */
  background-color: #ffffff; /* 背景颜色，可以根据需要更改 */
  padding: 20px; /* 内边距，可以根据需要调整 */
  box-sizing: border-box; /* 盒模型为 border-box */
}

.right-panel {
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