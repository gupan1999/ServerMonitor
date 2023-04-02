<template>
  <div class="chart-container">
    <el-cascader
      :key="isResouceShow"
      ref="selector"
      :props="{multiple:true}"
      :options="options"
      style="width:350px"
      collapse-tags
      :clearable="true"
      @change="handle"
    />
    <!-- <el-select v-model="query.host" placeholder="服务器">
            <el-option
              v-for="server in serverList"
              :key="server"
              :label="server"
              :value="server"
            />
          </el-select> -->
    <el-date-picker
      v-model="date"
      type="datetimerange"
      value-format="yyyy-MM-dd HH:mm:ss"
      :default-time="['00:00:00', '23:59:59']"

      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      />
    <el-button type="info" @click="fetchData">
      <i class="el-icon-refresh" />
      查询
    </el-button>
    <chart ref="chart" :series-data="seriesData" :x-axis-data="timeSeries" :title-data="titleData" height="100%" width="100%" />
  </div>

</template>

<script>
import Chart from '@/components/Charts/LineMarker'
import { fetchAvailServerNames } from '@/api/server'
import { getChart } from '@/api/chart'

export default {
  name: 'LineChart',
  components: { Chart },
  data() {
    return {
      date: [],
      startTime: '',
      stopTime: '',
      series: [],
      isResouceShow: 0,
      serverList: [],
      seriesData: [
      ],
      cpus: [
        { label: 'cpu_total', value: 1 },
        { label: 'cpu_0', value: 2 },
        { label: 'cpu_1', value: 3 },
        { label: 'cpu_2', value: 4 },
        { label: 'cpu_3', value: 5 },
        { label: 'cpu_4', value: 6 }
      ],
      cpuField: [
        { label: 'usage_iowait', value: 4, children: this.serverList },
        { label: 'usage_irq', value: 5, children: this.serverList },
        { label: 'usage_nice', value: 6, children: this.serverList },
        { label: 'usage_softirq', value: 7, children: this.serverList },
        { label: 'usage_steal', value: 8, children: this.serverList }
      ],
      systemField: [
        { label: 'load1', value: 1 },
        { label: 'load15', value: 2 },
        { label: 'load5', value: 3 },
        { label: 'n_cpus', value: 4 },
        { label: 'n_users', value: 5 },
        { label: 'uptime', value: 6 },
        { label: 'uptime_format', value: 7 }
      ],
      diskField: [
        { label: 'free', value: 1 },
        { label: 'inodes_free', value: 2 },
        { label: 'inodes_total', value: 3 },
        { label: 'inodes_used', value: 4 },
        { label: 'total', value: 5 },
        { label: 'used', value: 6 }
      ],
      diskioField: [
        { label: 'io_time', value: 1 },
        { label: 'iops_in_progress', value: 2 },
        { label: 'merged_reads', value: 3 },
        { label: 'merged_writes', value: 4 },
        { label: 'read_bytes', value: 5 },
        { label: 'read_time', value: 6 },
        { label: 'reads', value: 7 },
        { label: 'weight_io_time', value: 8 },
        { label: 'write_bytes', value: 9 },
        { label: 'write_time', value: 10 },
        { label: 'writes', value: 11 }
      ],
      memField: [
        { label: 'active', value: 1, children: this.serverList },
        { label: 'available', value: 2, children: this.serverList },
        { label: 'available_percent', value: 3, children: this.serverList },
        { label: 'free', value: 4, children: this.serverList },
        { label: 'inactive', value: 5, children: this.serverList },
        { label: 'mapped', value: 6, children: this.serverList },
        { label: 'cached', value: 7, children: this.serverList },
        { label: 'buffered', value: 8, children: this.serverList },
        { label: 'shared', value: 9, children: this.serverList },
        { label: 'swap_cached', value: 10, children: this.serverList },
        { label: 'swap_free', value: 11, children: this.serverList },
        { label: 'swap_total', value: 12, children: this.serverList },
        { label: 'total', value: 13, children: this.serverList }
      ],
      netField: [
        { label: 'bytes_recv', value: 1 },
        { label: 'bytes_sent', value: 2 },
        { label: 'drop_in', value: 3 },
        { label: 'drop_out', value: 5 },
        { label: 'err_in', value: 6 },
        { label: 'err_out', value: 7 },
        { label: 'packets_recv', value: 8 },
        { label: 'packets_sent', value: 9 }
      ],
      processesField: [
        { label: 'blocked', value: 1, children: this.serverList },
        { label: 'dead', value: 2, children: this.serverList },
        { label: 'idle', value: 3, children: this.serverList },
        { label: 'paging', value: 5, children: this.serverList },
        { label: 'total', value: 6, children: this.serverList },
        { label: 'threads', value: 8, children: this.serverList },
        { label: 'unknown', value: 9, children: this.serverList },
        { label: 'zombies', value: 10, children: this.serverList }
      ],
      swapField: [
        { label: 'free', value: 1, children: this.serverList },
        { label: 'in', value: 2, children: this.serverList },
        { label: 'out', value: 3, children: this.serverList },
        { label: 'total', value: 5, children: this.serverList },
        { label: 'used', value: 6, children: this.serverList },
        { label: 'used_percent', value: 8, children: this.serverList }
      ],
      default: {
        name: '',
        type: 'line',
        symbol: 'circle',
        symbolSize: 1,
        showSymbol: false,
        lineStyle: {
          normal: {
            width: 1
          }
        },
        itemStyle: {
          normal: {
            color: 'rgba(252,230,48,1)',
            barBorderRadius: 0,
            label: {
              show: true,
              position: 'top',
              formatter(p) {
                return p.value > 0 ? p.value : ''
              }
            }
          }
        },
        data: []
      },
      timeSeries: [],
      serverName: '',
      querys: [],
      titleData: '',
      legendData: '',
      query: {
        start: '-6h',
        stop: '0s',
        measurement: '',
        field: '',
        host: '',
        params: ''
      },
      options: [
        { label: 'system', children: this.systemField, value: 1 },
        { label: 'cpu', children: this.cpuField, value: 2 },
        { label: 'disk', children: this.diskField, value: 3 },
        { label: 'diskio', children: this.diskioField, value: 4 },
        { label: 'mem', children: this.memField, value: 5 },
        { label: 'net', children: this.netField, value: 6 },
        // { label: 'netstat', children: this.netstatField, value: 7 },
        { label: 'processes', children: this.processesField, value: 8 },
        // { label: 'procstat', children: this.procstatField, value: 9 },
        // { label: 'procstat_lookup', children: this.procstat_lookupField, value: 10 },
        { label: 'swap', children: this.swapField, value: 11 }
      ]
    }
  },
  watch: {
    date(value) {
      this.startTime = value[0]
      console.log(value[0])
      this.stopTime = value[1]
      console.log(value[1])
    }
  },
  created() {
    fetchAvailServerNames().then((response) => {
      var id = 0
      for (const server of response.data.data.serverNames) {
        this.serverList.push({ label: server, value: id++ })
      }
      for (const cpu of this.cpus) {
        cpu.children = this.serverList
      }
      this.options[0].children = this.systemField
      for (const t of this.options[0].children) {
        t.children = this.serverList
      }
      this.options[1].children = this.cpuField
      this.options[2].children = this.diskField
      this.options[3].children = this.diskioField
      this.options[4].children = this.memField
      for (const t of this.options[4].children) {
        t.children = this.serverList
      }
      this.options[5].children = this.netField
      // this.options[6].children = this.netstatField
      this.options[6].children = this.processesField
      for (const t of this.options[6].children) {
        t.children = this.serverList
      }
      // this.options[8].children = this.procstatField
      // this.options[9].children = this.procstat_lookupField
      this.options[7].children = this.swapField
      for (const t of this.options[7].children) {
        t.children = this.serverList
      }
      // console.log(this.options)
    })
  },
  methods: {
    handleDate() {
      console.log(this.value1)
    },
    handle() {
      this.querys = []
      const checkedNode = this.$refs['selector'].getCheckedNodes()
      var cnt = 0
      for (const node0 of checkedNode) {
        cnt = cnt + node0.pathLabels.length
      }
      cnt = cnt / checkedNode.length
      for (const node1 of checkedNode) {
        if (node1.pathLabels.length < cnt) checkedNode.splice(node1, 1)
      }
      for (const node of checkedNode) {
        const pathLabels = node.pathLabels
        // console.log(pathLabels)
        const query = { start: '-3h', stop: '0s', measurement: '', field: '', host: '', params: '' }
        for (var i = 0; i < pathLabels.length; i++) {
          if (i === 0) query.measurement = pathLabels.at(0)
          else if (i === 1) query.field = pathLabels.at(1)
          else if (i === pathLabels.length - 1) query.host = pathLabels.at(pathLabels.length - 1)
          else query.params = pathLabels.at(i)
          // else this.query.params = this.query.params.concat(','.concat(pathLabels.at(i)))
        }
  
        console.log(query.start)
        console.log(query.stop)
        const temp = Object.assign({}, query)
        this.querys.push(temp)
      }
      // console.log(this.querys)
    },

    randomRgbaColor() {
      var r = Math.floor(Math.random() * 256)
      var g = Math.floor(Math.random() * 256)
      var b = Math.floor(Math.random() * 256)
      var alpha = Math.random()
      return `rgba(${r},${g},${b},${alpha})`
    },

    fetchData() {
      this.seriesData = []
      // console.log(this.default)
      for (var i = 0; i < this.querys.length; i++) {
        // console.log(this.seriesData[i])
        this.querys[i].start = this.startTime
        this.querys[i].stop = this.stopTime
        const sendData = JSON.parse(JSON.stringify(this.querys.at(i)))
        getChart(sendData).then().then((response) => {
          // console.log(this.seriesData[i].data)
          this.seriesData.push(
            {
              name: response.data.data.host + ' ' + response.data.data.field,
              type: 'line',
              symbol: 'circle',
              symbolSize: 1,
              showSymbol: false,
              lineStyle: {
                normal: {
                  width: 2
                }
              },
              itemStyle: {
                normal: {
                  barBorderRadius: 0,
                  label: {
                    show: true,
                    position: 'top',
                    formatter(p) {
                      return p.value > 0 ? p.value : ''
                    }
                  }
                }
              },
              data: response.data.data.values
            }
          )
          // this.series = response.data.data.values
          this.timeSeries = response.data.data.times

          // this.titleData = response.data.data.metric
          // this.legendData = response.data.data.field
          // console.log(response.data)
           this.$refs['chart'].setOptions()
        })
        this.seriesData = []
       
      }
      // for (const temp of this.querys) {
    }
    // refresh() {
    //   fetchAvailServerNames().then((response) => {
    //     var id = 0
    //     for (const server of response.data.data.serverNames) {
    //       this.serverList.push({ label: server, value: id++ })
    //     }

    //     for (const cpu of this.cpus) {
    //       cpu.children = this.serverList
    //     }
    //     console.log(this.options)
    //   })
    // }
  }
}

</script>

<style scoped>
.chart-container{
  position: relative;
  width: 100%;
  height: calc(100vh - 50px);
}
#my-class.el-cascader .el-input .el-input__inner:focus, .el-cascader .el-input.is-focus .el-input__inner{
height: 50px;
}
#my-class.el-cascader__tags{
display: inline-flex;
margin-right: 50px;
flex-wrap: nowrap;
}
</style>

