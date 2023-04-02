<template>
  <div class="app-container">
    <el-button type="primary" @click="handleCreate">
      <i class="el-icon-plus" />
      新建任务
    </el-button>

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
      <el-table-column label="ID" prop="taskId" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.taskId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Timestamp" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskTimestamp | parseTime() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="TaskName" align="center" width="100px">
        <template slot-scope="{row}">
          <span>{{ row.taskName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ServerName" align="center" width="150px">
        <template slot-scope="{row}">
          <span>{{ row.serverName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Condition" min-width="300px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskCondition }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Level" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Desc" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Interval" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskInterval }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" width="230px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.enabled!=false" type="primary" size="mini" @click="handle(row)">
            停止
          </el-button>

          <el-button v-if="row.enabled!=true" type="success" size="mini" @click="handle(row)">
            重启
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :model="task" :rules="rules" label-width="120px" label-position="left" >
        <el-form-item label="Name" prop="taskName">
          <el-input v-model="task.taskName" placeholder="任务名" />
        </el-form-item>
        <el-form-item label="Level" prop="taskLevel" >
          <el-select v-model="task.taskLevel" placeholder="级别" >
            <el-option label="Error" value="Error" />
            <el-option label="Warn" value="Warn" />
            <el-option label="Info" value="Info" />
          </el-select>
        </el-form-item>
        <el-form-item v-model="task.serverName" label="ServerName" prop="serverName" >
          <el-select v-model="task.serverName" placeholder="主机名">
            <el-option
              v-for=" name in serverList"
              :key="name"
              :label="name"
              :value="name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Interval" prop="taskInterval">
          <el-select v-model="task.taskInterval" placeholder="间隔">
            <el-option label="10s" value="10s" />
            <el-option label="30s" value="30s" />
            <el-option label="1m" value="1m" />
            <el-option label="5m" value="5m" />
            <el-option label="15m" value="15m" />
            <el-option label="1h" value="1h" />
            <el-option label="6h" value="6h" />
            <el-option label="12h" value="12h" />
            <el-option label="24h" value="24h" />
            <el-option label="2d" value="2d" />
            <el-option label="7d" value="7d" />
          </el-select>
        </el-form-item>
        <el-form-item label="Condition" width="80px" prop="taskCondition">
          <el-input v-model="task.taskCondition" placeholder="表达式" />
        </el-form-item>
        <el-form-item label="StartTime" prop="startTime">
          <el-date-picker v-model="task.startTime" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="开始时间" @change="handleDate" />
        </el-form-item>
        <el-form-item label="EndTime">
          <el-date-picker v-model="task.stopTime" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="结束时间" @change="handleDate1" />
          <!--  <el-select v-model="task.interval" placeholder="持续时间">
            <el-option label="10s" value="10s" />
            <el-option label="30s" value="30s" />
            <el-option label="1m" value="1m" />
            <el-option label="5m" value="5m" />
            <el-option label="15m" value="15m" />
            <el-option label="1h" value="1h" />
            <el-option label="6h" value="6h" />
            <el-option label="12h" value="12h" />
            <el-option label="24h" value="24h" />
            <el-option label="2d" value="2d" />
            <el-option label="7d" value="7d" />
          </el-select> -->
        </el-form-item> 
        <el-form-item label="Description">
          <el-input
            v-model="task.taskDesc"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="详情描述"
          />

        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="primary" @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="danger" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
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
import { fetchTaskList, updateTask, addTask, deleteTask } from '@/api/task'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { fetchAvailServerNames } from '@/api/server'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  filters: {
    // statusFilter(status) {
    //   const statusMap = {
    //     published: 'success',
    //     draft: 'info',
    //     deleted: 'danger'
    //   }
    //   return statusMap[status]
    // },
    parseTime: parseTime

  },
  data() {
    return {
      task: {
        taskId: null,
        taskName: '',
        taskInterval: '',
        taskCondition: '',
        taskLevel: '',
        taskDesc: '',
        enabled: false,
        taskTimestamp: new Date(),
        startTime: new Date(),
        stopTime: null,
        userId: null,
        serverName: ''
      },
      total: 0,
      pageArgs: {
        page: 1,
        limit: 20
      },
      dialogVisible: false,
      dialogType: 'new',
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      tableKey: 0,
      list: [],
      serverList: [],
      listLoading: true,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建任务'
      },
      rules: {
        taskName: [{ required: true, message: 'name is required', trigger: 'blur' }],
        startTime: [{ required: true, message: 'start is required', trigger: 'change' }],
        taskInterval: [{ required: true, message: 'interval is required', trigger: 'change' }],
        taskLevel: [{ required: true, message: 'level is required', trigger: 'change' }],
        taskCondition: [{ required: true, message: 'condition is required', trigger: 'blur' }],
        serverName: [{ required: true, message: 'serverName is required', trigger: 'change' }]
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
    handleDate1() {
      if (this.task.stopTime < this.task.startTime) {
        this.task.stopTime = null
      }
    }, 
    itemChange(val, index, item) {
      console.log('获取数据值，重新赋值', val, index, item)
      this.task.serverName = val
    },
    getList() {
      this.listLoading = true
      fetchTaskList(this.pageArgs).then(response => {
        if (response.data.data === null) {
          this.list = []
        } else {
          this.list = response.data.data.tasks
          this.total = response.data.data.total
        }
        this.listLoading = false
      })
    },
    resetTask() {
      this.task = {
        taskId: null,
        taskName: '',
        taskInterval: '',
        taskCondition: '',
        taskLevel: '',
        taskDesc: '',
        enabled: false,
        taskTimestamp: new Date(),
        startTime: new Date(),
        stopTime: null,
        userId: null,
        serverName: ''
      }
    },
    handleCreate() {
      this.resetTask()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      fetchAvailServerNames().then((response) => {
        this.serverList = response.data.data.serverNames
      })
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    handleUpdate(row) {
      this.task = Object.assign({}, row) // copy obj

      // this.task.start = new Date(this.task.start)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      fetchAvailServerNames().then((response) => {
        this.serverList = response.data.data.serverNames
      })
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handle(row) {
      var tmp = Object.assign({}, row)
      if (row.enabled === true) {
        tmp.enabled = false
        updateTask(tmp).then(() => {
          this.$notify({
            title: '完成',
            message: '成功终止任务',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      } if (row.enabled === false) {
        tmp.enabled = true
        updateTask(tmp).then(() => {
          this.$notify({
            title: '完成',
            message: '成功重启任务',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.task.enabled = true
          this.task.startTime = +new Date(this.task.startTime)
          if (this.task.stopTime) {
            this.task.stopTime = +new Date(this.task.stopTime)
          }
          this.task.taskTimestamp = +new Date(this.task.taskTimestamp)
          // this.task.startTime = +new Date(this.task.startTime)
          // if (this.task.stopTime) {
          //   this.task.stopTime = +new Date(this.task.stopTime)
          // }
          // this.task.taskTimestamp = +new Date(this.task.taskTimestamp)
          // this.task.taskId = parseInt(Math.random() * 100) + 1024 // mock a id
          // addTask({"taskName":this.task.taskName, "taskLevel":this.task.taskLevel,"startTime":this.task.startTime,
          //   "enabled":true,"taskInterval":this.task.taskInterval,"taskCondition":this.ta,"taskTimestamp":this.task.taskTimestamp,"taskDesc":this.task.taskDesc}).then(() => {
          addTask(this.task).then(() => {
            // this.list.push(this.task)
            this.dialogFormVisible = false
            this.$notify({
              title: '完成',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.task.startTime = +new Date(this.task.startTime)
          if (this.task.stopTime) {
            this.task.stopTime = +new Date(this.task.stopTime)
          }
          this.task.taskTimestamp = +new Date(this.task.taskTimestamp)
          const tempData = Object.assign({}, this.task)
          console.log(tempData)
          // tempData.startTime = +new Date(tempData.startTime)
          // if (this.task.stopTime) {
          //   this.task.stopTime = +new Date(this.task.stopTime)
          // }
          // tempData.taskTimestamp = +new Date(tempData.taskTimestamp)
          // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          //   const temp = { 'taskName':temp.taskName, 'taskLevel':temp.taskLevel,'startTime':temp.startTime,
          //  'enabled':temp.enabled,'taskInterval':temp.taskInterval,'taskCondition':temp.taskCondition,'taskTimestamp':temp.taskTimestamp,'taskDesc':temp.task.taskDesc}
          updateTask(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.task.id)
            this.list.splice(index, 1, this.task)
            this.dialogFormVisible = false
            this.$notify({
              title: '完成',
              message: '修改成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleDelete(row, index) {
      deleteTask(row.taskId).then(() => {
        this.$notify({
          title: '完成',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.list.splice(index, 1)
      })
    }
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
