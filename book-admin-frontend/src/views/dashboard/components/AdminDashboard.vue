<template>
  <el-row :gutter="20">
    <!-- 左侧内容 -->
    <el-col :span="16">
      <!-- 系统概况 -->
      <el-card header="系统概况">
        <el-row :gutter="16">
          <el-col :span="6">
            <el-card shadow="hover" class="stat-item">
              <el-icon :size="30" class="stat-icon"><User /></el-icon>
              <div class="stat-title">总用户数</div>
              <div class="stat-value" id="userCount"></div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card shadow="hover" class="stat-item">
              <el-icon :size="30" class="stat-icon"><Collection /></el-icon>
              <div class="stat-title">总图书数</div>
              <div class="stat-value" id="bookCount"></div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card shadow="hover" class="stat-item">
              <el-icon :size="30" class="stat-icon"><Reading /></el-icon>
              <div class="stat-title">总借阅量</div>
              <div class="stat-value" id="borrowCount"></div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card shadow="hover" class="stat-item">
              <el-icon :size="30" class="stat-icon"><Star /></el-icon>
              <div class="stat-title">当前活动</div>
              <div class="stat-value" id="eventCount"></div>
            </el-card>
          </el-col>
        </el-row>
      </el-card>

      <!-- 借阅趋势统计图 -->
      <el-card header="借阅趋势" style="margin-top: 20px">
        <div ref="borrowTrendChart" style="width: 100%; height: 400px"></div>
      </el-card>

      <!-- 活跃用户统计图 -->
      <el-card header="活跃用户" style="margin-top: 20px">
        <div ref="activeUserChart" style="width: 100%; height: 400px"></div>
      </el-card>
    </el-col>

    <!-- 右侧内容 -->
    <el-col :span="8">
      <!-- 新用户注册统计图 -->
      <el-card header="新用户注册" style="margin-bottom: 20px">
        <div ref="newUserChart" style="width: 100%; height: 400px"></div>
      </el-card>

      <!-- 图书分类分布饼图 -->
      <el-card header="图书分类分布" style="margin-top: 20px">
        <div ref="bookCategoryChart" style="width: 100%; height: 400px"></div>
      </el-card>
    </el-col>
  </el-row>
</template>
<script lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { User, Collection, Reading, Star } from '@element-plus/icons-vue'
import { CountUp } from 'countup.js'

interface Stats {
  totalUsers: number
  totalBooks: number
  totalBorrows: number
  activeEvents: number
}
interface CategoryDistribution {
  name: string
  value: number
}

export default {
  name: 'AdminHomePage',
  setup() {
    const stats = ref<Stats>({
      totalUsers: 5000,
      totalBooks: 15000,
      totalBorrows: 35000,
      activeEvents: 3,
    })
    const animateStats = () => {
      nextTick(() => {
        new CountUp('userCount', stats.value.totalUsers).start()
        new CountUp('bookCount', stats.value.totalBooks).start()
        new CountUp('borrowCount', stats.value.totalBorrows).start()
        new CountUp('eventCount', stats.value.activeEvents).start()
      })
    }
    onMounted(() => {
      animateStats()
    })
    const borrowTrendChart = ref<HTMLDivElement | null>(null)
    const activeUserChart = ref<HTMLDivElement | null>(null)
    const newUserChart = ref<HTMLDivElement | null>(null)
    const bookCategoryChart = ref<HTMLDivElement | null>(null)

    const borrowTrendData = ref([120, 132, 101, 134, 90, 230, 210])
    const activeUserChartData = ref([
      { date: '2025-05-01', value: 10 },
      { date: '2025-05-02', value: 15 },
      { date: '2025-05-03', value: 12 },
      { date: '2025-05-04', value: 18 },
    ])
    const newUserRegistrationData = ref([
      { date: '2025-05-01', value: 20 },
      { date: '2025-05-02', value: 30 },
      { date: '2025-05-03', value: 25 },
      { date: '2025-05-04', value: 35 },
    ])
    const bookCategoryDistribution = ref<CategoryDistribution[]>([
      { name: '文学', value: 45 },
      { name: '科技', value: 30 },
      { name: '历史', value: 15 },
      { name: '艺术', value: 10 },
    ])

    const initChart = (el: HTMLElement | null, option: any) => {
      if (el) {
        const chart = echarts.init(el)
        chart.setOption(option)
      } else {
        console.warn('DOM element for chart not found.')
      }
    }

    onMounted(() => {
      nextTick(() => {
        initChart(borrowTrendChart.value, {
          xAxis: {
            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: borrowTrendData.value,
              type: 'line',
            },
          ],
        })

        initChart(activeUserChart.value, {
          xAxis: {
            type: 'category',
            data: activeUserChartData.value.map((i) => i.date),
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: activeUserChartData.value.map((i) => i.value),
              type: 'bar',
            },
          ],
        })

        initChart(newUserChart.value, {
          xAxis: {
            type: 'category',
            data: newUserRegistrationData.value.map((i) => i.date),
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: newUserRegistrationData.value.map((i) => i.value),
              type: 'bar',
            },
          ],
        })

        initChart(bookCategoryChart.value, {
          series: [
            {
              type: 'pie',
              radius: '50%',
              data: bookCategoryDistribution.value,
            },
          ],
        })
      })
    })

    return {
      stats,
      borrowTrendChart,
      activeUserChart,
      newUserChart,
      bookCategoryChart,
      User,
      Collection,
      Reading,
      Star,
    }
  },
}
</script>

<style scoped>
.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 120px;
}

.stat-title {
  font-size: 16px;
  color: gray;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}
</style>
