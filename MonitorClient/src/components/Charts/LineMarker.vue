<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import resize from './mixins/resize'

var echarts = require('echarts')

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    },
    seriesData: {
      type: Array,
      default: () => [
        {
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
          data: this.seriesData
        }
      ]
    },
    xAxisData: {
      type: Array,
      default: () => []
    },
    titleData: {
      type: String,
      default: ''
    },
    field: {
      type: String,
      default: ''
    },
    legendData: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      chart: null
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },

  mounted() {
    this.initChart()
  },

  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        backgroundColor: '#fff',
        title: {
          text: this.titleData,
          x: '20',
          top: '12',
          textStyle: {
            color: '#fff',
            fontSize: '16'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            textStyle: {
              color: '#fff'
            }
          }
        },
        grid: {
          left: '4%',
          right: '4%',
          borderWidth: 0,
          top: 48,
          bottom: 95,
          textStyle: {
            color: '#fff'
          }
        },
        // legend: {
        //   top: 10,
        //   icon: 'rect',
        //   itemWidth: 15,
        //   itemHeight: 8,
        //   itemGap: 12,
        //   right: '4%',
        //   textStyle: {
        //     fontSize: 15,
        //     color: '#F1F1F3'
        //   }
        // },
        calculable: true,
        xAxis: [{
          type: 'category',
          axisLine: {
            lineStyle: {
              color: '#90979c'
            }
          },
          data: this.xAxisData
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        dataZoom: [{
          show: true,
          height: 20,
          bottom: 50,
          start: 0,
          end: 100,
          handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
          textStyle: {
            color: '#fff'
          }
        }, {
          type: 'inside',
          show: true,
          height: 15,
          start: 1,
          end: 35
        }],
        series: this.seriesData
      })
    }
    // getChart() {
    // getChart().then(response => {
    //   console.log(response)
    //   this.xAxisData = response.data.data.times
    //   this.itemData = response.data.data.values
    //   this.legendData = response.data.data.field
    //   this.titleData = response.data.data.metric + ' : ' + response.data.data.host
    //   this.initChart()
    // })
  }
  // }
}

</script>
