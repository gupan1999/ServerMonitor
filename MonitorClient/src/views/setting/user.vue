<template>
  <div class="app-container">
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
      <el-table-column label="ID" prop="userId" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.userId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Org" align="center" width="150px">
        <template slot-scope="{row}">
          <span>{{ row.userOrg }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Links" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.serverNames }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Actions" align="center" width="300px" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="startBind(row)">
            绑定
          </el-button>

          <el-button type="primary" size="mini" @click="startUnbind(row)">
            解绑
          </el-button>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap1[dialogStatus1]" :visible.sync="dialogFormVisible1">
      <el-form ref="bindForm" :rules="bindRule" label-width="150px" label-position="left">
        <el-form-item label="serverName" prop="serverName">
          <!-- <el-input v-model.number="link.taskId" placeholder="任务id" /> -->
          <el-select v-if="dialogStatus1==='unbind'" v-model="link.serverName" placeholder="服务器">
            <el-option
              v-for="server in endpoint.serverNames"
              :key="server"
              :label="server"
              :value="server"
            />
          </el-select>
          <el-select v-else-if="dialogStatus1==='bind'" v-model="link.serverName" placeholder="服务器">
            <el-option
              v-for="server in serverNames"
              :key="server"
              :label="server"
              :value="server"
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
      @pagination="getList"
    />
  </div>
</template>
<script>
import { fetchServerNames } from '@/api/server'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { listDiff } from '@/utils/index'
import { fetchLink, bind, unbind } from '@/api/user'

export default {
  name: 'ComplexTable2',
  components: { Pagination },
  filters: {
    parseTime: parseTime

  },
  data() {
    return {
      endpoint: {
        userId: '',
        username: '',
        userOrg: '',
        serverNames: []
      },
      link: {
        userId: '',
        serverName: ''
      },
      pageArgs: {
        page: 1,
        limit: 20
      },
      total: 0,

      // 全部行数据
      list: [],
      listLoading: true,
      dialogVisible: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      tableKey: 0,
      bindRule: {
        userId: [{ required: true, message: 'endpointId is required', trigger: 'blur' }],
        serverName: [{ required: true, message: 'taskId is required', trigger: 'blur' }]
      },
      dialogFormVisible: false,
      dialogFormVisible1: false,
      dialogStatus: '',
      dialogStatus1: '',
      textMap1: {
        bind: '绑定',
        unbind: '解绑'
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
      fetchLink(this.pageArgs).then(response => {
        if (response.data.data === null) {
          this.list = []
        } else {
          this.list = response.data.data.links
          this.total = response.data.data.total
        }
        this.listLoading = false
      })
    },

    resetEndpoint() {
      this.endpoint = {
        id: '',
        name: '',
        org: '',
        serviceNames: []
      }
    },
    resetLink() {
      this.link = {
        userId: '',
        serverName: ''
      }
    },
    itemChange(val, index, item) {
      console.log('获取数据值，重新赋值', val, index, item)
      this.link.serverName = val
    },
    // handleCreate() {
    //   this.resetEndpoint()
    //   this.dialogStatus = 'create'
    //   this.dialogFormVisible = true
    //   this.$nextTick(() => {
    //     this.$refs['dataForm'].clearValidate()
    //   })
    // },
    // startEdit(row) {
    //   this.endpoint = Object.assign({}, row) // copy obj
    //   // this.task.start = new Date(this.task.start)
    //   this.dialogStatus = 'update'
    //   this.dialogFormVisible = true
    //   this.$nextTick(() => {
    //     this.$refs['dataForm'].clearValidate()
    //   })
    // },
    startBind(row) {
      this.resetLink()
      this.endpoint = Object.assign({}, row) // copy obj
      this.dialogStatus1 = 'bind'
      this.dialogFormVisible1 = true
      this.link.userId = row.userId
      this.serverNames = listDiff(this.serverNames, this.endpoint.serverNames)
      fetchServerNames().then((response) => {
        this.serverNames = response.data.data.serverNames
      })
      // fetchServerNames().then((response) => {
      //   this.serverNames = response.data.data.serverNames
      //   // const temp = []
      //   // for (const server of this.serverNames) {
      //   //   temp.push(server)
      //   // }
      //   // console.log(this.resultList)
      // })
      this.$nextTick(() => {
        this.$refs['bindForm'].clearValidate()
      })
    },

    handleBind() {
      bind(this.link).then(() => {
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
      this.link.userId = row.userId
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
    }
  }
}
</script>
