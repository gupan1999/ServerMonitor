<template>
  <div class="app-container">
    <!-- <el-button type="primary" @click="handleCreate">
      <i class="el-icon-plus" />
      新建端点
    </el-button> -->

    <el-button type="info" @click="getList">
      <i class="el-icon-refresh" />
      同步
    </el-button>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top:30px;"
    >
      <el-table-column label="ID" prop="alertId" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.alertId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="250px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.alertName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Level" align="center" width="150px">
        <template slot-scope="{row}">
          <span>{{ row.alertLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Timestamp" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.alertTimestamp | parseTime() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Description" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.alertDescription }}</span>
        </template>
      </el-table-column>
           <el-table-column label="EndPointId" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endpointId }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="pageArgs.page"
      :limit.sync="pageArgs.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>

import { fetchAlertList } from '@/api/alert'

import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable2',
  components: { Pagination },
  filters: {
    parseTime: parseTime

  },
  data() {
    return {
      alert: {
        alertId: '',
        alertName: '',
        alertLevel: '',
        alertTimestamp: new Date(),
        alertDescription: '',
        endpointId: 0
      },
      total: 0,
      list: [],
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      tableKey: 0,
      pageArgs: {
        page: 1,
        limit: 20
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建端点'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleDate() {
      var startAt = new Date(this.task.start) * 1000 / 1000
      if (startAt < Date.now()) {
        this.task.start = new Date()
      }
    },
    getList() {
      this.listLoading = true
      fetchAlertList(this.pageArgs).then(response => {
        if (response.data.data === null) {
          this.list = []
        } else {
          this.list = response.data.data.alerts.reverse
          this.total = response.data.data.total
        }
        this.listLoading = false
      })
    },
    resetAlert() {
      this.alert = {
        alertId: '',
        alertName: '',
        alertLevel: '',
        alertTimestamp: new Date(),
        alertDescription: '',
        endpointId: 0
      }
    },
    handleCreate() {
      this.resetAlert()
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    handleUpdate(row) {
      this.task = Object.assign({}, row) // copy obj
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    }
    // handle(row) {
    //   var tmp = Object.assign({}, row)
    //   if (row.enabled === true) {
    //     tmp.enabled = false
    //     updateTask(tmp).then(() => {
    //       // row.enabled = false
    //            this.$notify({
    //             title: '完成',
    //             message: '成功终止任务',
    //             type: 'success',
    //             duration: 2000
    //           })
    //       this.getList()
    //     })
    //   } if (row.enabled === false) {
    //     tmp.enabled = true
    //     updateTask(tmp).then(() => {
    //            this.$notify({
    //             title: '完成',
    //             message: '成功重启任务',
    //             type: 'success',
    //             duration: 2000
    //           })
    //       // row.enabled = false
    //       this.getList()
    //     })
    //   }
    // },
    //  getCondition() {
    //   if (this.task.taskCondition === '大于') { 
    //     this.task.taskCondition = this.task.metric+'|>'+this.task.threshold
    //   }
    //   if (this.task.taskCondition === '小于') {
    //     this.task.taskCondition = this.task.metric+'|<'+this.task.threshold
    //   }
    //   return this.task.taskCondition
    // },
    // getCondition() {
    //   if (this.task.taskCondition === '大于') {
    //     this.task.taskCondition = this.task.metric+'&host='+this.task.host+'|>'+this.task.threshold
    //   }
    //   if (this.task.taskCondition === '小于') {
    //     this.task.taskCondition = this.task.metric+'&host='+this.task.host+'|<'+this.task.threshold
    //   }
    //   return this.task.taskCondition
    // },
    // createData() {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //       this.task.enabled = true
    //       // this.task.taskId = parseInt(Math.random() * 100) + 1024 // mock a id
    //       // addTask({"taskName":this.task.taskName, "taskLevel":this.task.taskLevel,"startTime":this.task.startTime,
    //       //   "enabled":true,"taskInterval":this.task.taskInterval,"taskCondition":this.ta,"taskTimestamp":this.task.taskTimestamp,"taskDesc":this.task.taskDesc}).then(() => {
    //       addTask(this.task).then(() => {
    //         this.list.push(this.task)
    //         this.dialogFormVisible = false
    //         this.$notify({
    //           title: 'Success',
    //           message: 'Created Successfully',
    //           type: 'success',
    //           duration: 2000
    //         })
    //       })
    //     }
    //   })
    // },
    // updateData() {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //         // updateTask({ 'taskId':taskData.taskId, 'taskName':taskData.taskName,'taskLevel':taskData.taskLevel, 'startTime':taskData.startTime,
    //         //   'enabled':taskData.enabled, 'taskInterval':taskData.taskInterval, 'taskCondition':this.getCondition(), 'taskTimestamp':taskData.timestamp,
    //         //   'taskDesc':taskData.taskDesc }).then(() => {
    //           const tempData = Object.assign({}, this.task)
    //           tempData.taskTimestamp = +new Date(tempData.taskTimestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
    //           updateTask(tempData)
    //           const index = this.list.findIndex(v => v.id === this.task.id)
    //           this.list.splice(index, 1, this.task)
    //           this.dialogFormVisible = false
    //           this.$notify({
    //             title: '完成',
    //             message: '修改成功',
    //             type: 'success',
    //             duration: 2000
    //           })
    //       }
    //     })
    //   },
    // handleDelete(row, index) {
    //   deleteTask(row.taskId).then(() => {
    //     this.$notify({
    //     title: '完成',
    //     message: '删除成功',
    //     type: 'success',
    //     duration: 2000
    //   })
    //   this.list.splice(index, 1)
    //   })
    // }

    // formatJson(filterVal) {
    //   return this.list.map(v => filterVal.map(j => {
    //     if (j === 'timestamp') {
    //       return parseTime(v[j])
    //     } else {
    //       return v[j]
    //     }
    //   }))
    // }

  }
}
</script>
