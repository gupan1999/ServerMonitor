<template>
  <div class="app-container">
    <el-button type="primary" @click="handleCreate">
      <i class="el-icon-plus" />
      新建端点
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
      <el-table-column label="ID" prop="endPointId" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.endpointId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endpointName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Limit" align="center" width="150px">
        <template slot-scope="{row}">
          <span>{{ row.levelLimit }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Destination" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.destination }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Parameter" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.parameter }}</span>
        </template>
      </el-table-column>

      <el-table-column label="TaskIds" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskIds }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" width="300px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="primary" @click="startEdit(row)">
            编辑
          </el-button>
          <el-button type="success" size="mini" @click="startBind(row)">
            绑定
          </el-button>

          <el-button type="primary" size="mini" @click="startUnbind(row)">
            解绑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap1[dialogStatus1]" :visible.sync="dialogFormVisible1">
      <el-form ref="bindForm" :rules="bindRule" label-width="100px" label-position="left">
        <el-form-item label="taskId" prop="taskId">
          <!-- <el-input v-model.number="link.taskId" placeholder="任务id" /> -->
          <el-select v-if="dialogStatus1==='unbind'" v-model="link.taskId" placeholder="任务id">
            <el-option
              v-for="taskId in endpoint.taskIds"
              :key="taskId"
              :label="taskId"
              :value="taskId"
            />
          </el-select>
          <el-select v-else-if="dialogStatus1==='bind'" v-model="link.taskId" placeholder="任务id">
            <el-option
              v-for="taskId in resultList"
              :key="taskId"
              :label="taskId"
              :value="taskId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="primary" @click="dialogFormVisible1 = false">
          取消
        </el-button>
        <el-button type="danger" @click="dialogStatus1==='bind'?handleBind():handleUnbind()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="pageArgs.page"
      :limit.sync="pageArgs.limit"
      @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="endpoint" :rules="rules" label-width="100px" label-position="left">
        <el-form-item label="Limit" prop="levelLimit">
          <el-select v-model="endpoint.levelLimit" placeholder="限制">
            <el-option label="Error" value="Error" />
            <el-option label="Warn" value="Warn" />
            <el-option label="Info" value="Info" />
          </el-select>
        </el-form-item>
        <el-form-item label="Destination" width="80px" prop="destination">
          <el-input v-model="endpoint.destination" placeholder="目标" />
        </el-form-item>
        <el-form-item label="Name" prop="endpointName">
          <el-input v-model="endpoint.endpointName" placeholder="端点名" />
        </el-form-item>
        <el-form-item label="Parameter" prop="parameter">
          <el-input v-model="endpoint.parameter" placeholder="参数" />
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
  </div>
</template>
<script>
import { fetchTaskIds } from '@/api/task'
import { fetchEndpointList, updateEndpoint, addEndpoint, bind, unbind, deleteEndpoint } from '@/api/endpoint'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { listDiff } from '@/utils/index'

export default {
  name: 'ComplexTable2',
  components: { Pagination },
  filters: {
    parseTime: parseTime

  },
  data() {
    return {
      endpoint: {
        endpointId: '',
        endpointName: '',
        levelLimit: '',
        destination: '',
        parameter: '',
        taskIds: [],
        userId: null
      },
      link: {
        endpointId: '',
        taskId: ''
      },
      pageArgs: {
        page: 1,
        limit: 20
      },
      total: 0,
      taskList: [],
      resultList: [],
      list: [],
      listLoading: true,
      dialogVisible: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      tableKey: 0,
      rules: {
        levelLimit: [{ required: true, message: 'limit is required', trigger: 'change' }],
        destination: [{ required: true, message: 'destination is required', trigger: 'blur' }],
        endpointName: [{ required: true, message: 'name is required', trigger: 'blur' }]
      },
      bindRule: {
        endpointId: [{ required: true, message: 'endpointId is required', trigger: 'blur' }],
        taskId: [{ required: true, message: 'taskId is required', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      dialogFormVisible1: false,
      dialogStatus: '',
      dialogStatus1: '',
      textMap: {
        update: '编辑',
        create: '新建端点'
      },
      textMap1: {
        bind: '绑定',
        unbind: '解绑'
      }
    }
  },
  created() {
    this.getList()
    // console.log(parseTime(1652516128000))
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
      fetchEndpointList(this.pageArgs).then(response => {
        if (response.data.data === null) {
          this.list = []
        } else {
          this.list = response.data.data.endpoints
          this.total = response.data.data.total
        }
        this.listLoading = false
      })
    },
    // parseItems() {
    //   var tmp
    //   for(let i in this.list){
    //     tmp = i.taskCondition
    //     tmp.s
    //   }
    // },

    resetEndpoint() {
      this.endpoint = {
        endpointId: '',
        endpointName: '',
        levelLimit: '',
        destination: '',
        parameter: '',
        taskIds: [],
        userId: null
      }
    },
    resetLink() {
      this.link = {
        endpointId: '',
        taskId: ''
      }
    },
    itemChange(val, index, item) {
      console.log('获取数据值，重新赋值', val, index,item);
      this.link.taskId = val
      // this.$set(this.offerBatchModifyData.InquiryItemList, index, item);
      console.log(this.offerBatchModifyData)
    },
    handleCreate() {
      this.resetEndpoint()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    startEdit(row) {
      this.endpoint = Object.assign({}, row) // copy obj
      // this.task.start = new Date(this.task.start)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    startBind(row) {
      this.resetLink()
      this.endpoint = Object.assign({}, row) // copy obj
      this.dialogStatus1 = 'bind'
      this.dialogFormVisible1 = true
      this.link.endpointId = row.endpointId
      fetchTaskIds().then((response) => {
        this.taskList = response.data.data.taskIds
        const temp = []
        for (const task of this.taskList) {
          temp.push(task)
        }
        console.log(temp)
        console.log(JSON.parse(JSON.stringify(this.endpoint.taskIds)))
        this.resultList = JSON.parse(JSON.stringify(listDiff(temp, JSON.parse(JSON.stringify(this.endpoint.taskIds)))))
        console.log(this.resultList)
      })
      this.$nextTick(() => {
        this.$refs['bindForm'].clearValidate()
      })
    },

    handleBind() {
      bind(this.link).then(() => {
        // this.endpoint.taskIds.push(this.link.taskId)
        this.$notify({
          title: '成功',
          message: '绑定成功',
          type: 'success',
          duration: 2000
        })
        this.dialogFormVisible1 = false
        this.getList()
      })
    },
    startUnbind(row) {
      this.resetLink()
      this.endpoint = Object.assign({}, row) // copy obj
      this.dialogStatus1 = 'unbind'
      this.dialogFormVisible1 = true
      this.link.endpointId = row.endpointId
      this.$nextTick(() => {
        this.$refs['bindForm'].clearValidate()
      })
    },
    handleUnbind() {
      unbind(this.link).then(() => {
        this.$notify({
          title: '成功',
          message: '解绑成功',
          type: 'success',
          duration: 2000
        })
        this.dialogFormVisible1 = false
        this.getList()
      })
    },
    handleUpdate(row) {
      this.endpoint = Object.assign({}, row) // copy obj
      // this.task.start = new Date(this.task.start)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.endpoint)
          addEndpoint({ 'endpointName': tempData.endpointName, 'levelLimit': tempData.levelLimit,
            'destination': tempData.destination, 'parameter': tempData.parameter }).then(() => {
            this.list.push(this.endpoint)
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
          const tempData = Object.assign({}, this.endpoint)
          updateEndpoint({ 'endpointId': tempData.endpointId, 'endpointName': tempData.endpointName, 'levelLimit': tempData.levelLimit,
            'destination': tempData.destination, 'parameter': tempData.parameter , 'userId': tempData.userId}).then(() => {
            const index = this.list.findIndex(v => v.id === this.endpoint.id)
            this.list.splice(index, 1, this.endpoint)
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
      deleteEndpoint(row.endpointId).then(() => {
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
